const axios = require('axios');
const formData = require('form-data');
const moment = require('moment');
const qs = require('qs');

const {RequestType} = require('./RequestType');
const {SignUtil} = require('./SignUtil');
const {BaseRequest} = require('../request/BaseRequest');

const HEADERS = {'Accept-Encoding': 'identity'};

const getHeaders = (headers = {}) => {
    return Object.assign({}, headers, HEADERS);
};

const parseResponse = (error, response, request) => {
    if (!error && response.status === 200) {
        return request.parseResponse(response.data);
    } else {
        return { // 重新封装请求异常回调，以防中断
            msg: '请求异常',
            code: '502',
            sub_msg: `${error}`,
            sub_code: 'isv.invalid-server'
        };
    }
};

const buildParams = (instance, request, token) => {
    const {appId, privateKey} = instance;
    const allParams = {
        'app_id': appId,
        'method': request.getMethod(),
        'charset': 'UTF-8',
        'sign_type': 'RSA2',
        'timestamp': moment().format('YYYY-MM-DD HH:mm:ss'),
        'version': request.getVersion(),
        'biz_content': JSON.stringify(request.bizModel)
    };

    if (token) {
        allParams['app_auth_token'] = token;
    }
    // 创建签名
    allParams.sign = SignUtil.createSign(allParams, privateKey, 'RSA2');
    return allParams;
};

const executeRequest = async (instance = {}, request, token, callback, {headers}) => {
    const params = buildParams(instance, request, token);
    const {url} = instance;
    const options = {
        url,
        method: 'POST',
        params: undefined,
        data: undefined
    };
    headers = getHeaders(headers);
    const requestType = request.getRequestType();
    switch (requestType) {
        case RequestType.GET: {
            options.method = 'GET';
            options.params = params;
        }
            break;
        case RequestType.POST_FORM: {
            headers = Object.assign(headers, {
                'Content-Type': 'application/x-www-form-urlencoded'
            });
            options.data = qs.stringify(params);
        }
            break;
        case RequestType.POST_JSON: {
            options.data = params;
        }
            break;
        case RequestType.POST_FILE: {
            const formData = new formData();
            (request.files || []).forEach(({name, path}) => {
                formData.append(name, path, {
                    contentType: 'application/octet-stream'
                });
            });
            Object.keys(params).forEach(key => {
                const value = params[key];
                if (!(typeof key === 'undefined' || typeof value === 'undefined')) {
                    formData.append(key, params[key]);
                }
            });
            options.data = formData;
        }
            break;
        default: {
            callback(parseResponse(new Error('request.getRequestType()类型不正确'), undefined, request));
            return;
        }
    }
    try {
        options['headers'] = headers;
        const response = await axios.request(options);
        callback(parseResponse(undefined, response, request));
    } catch (error) {
        callback(parseResponse(error, undefined, request));
    }
};

module.exports = class OpenClient {
    /**
     * 初始化客户端
     * @param appId 应用ID
     * @param privateKey 应用私钥，2048位，PKCS8
     * @param url 请求url
     */
    constructor(appId, privateKey, url) {
        this.appId = appId;
        this.privateKey = privateKey;
        this.url = url;
    }

    /**
     * 发送请求
     * @param request 请求类
     * @param callback 回调函数，参数json（undefined则使用executeSync）
     * @param options 自定义参数，如headers
     */
    execute(request, callback, options) {
        if (typeof callback == 'function') {
            return this.executeToken(request, null, callback, options);
        } else {
            return this.executeSync(request, options);
        }
    }

    /**
     * 发送同步请求
     * @param request 请求类
     * @param options 自定义参数，如headers
     * */
    executeSync(request, options) {
        return new Promise((resolve) => {
            const _ = this.execute(request, res => {
                resolve(res);
            }, options);
        });
    }

    /**
     * 发送请求
     * @param request 请求类
     * @param token token
     * @param callback 回调函数，参数json（undefined则使用executeTokenSync）
     * @param options 自定义参数，如headers
     */
    async executeToken(request, token, callback, options) {
        if (!(request instanceof BaseRequest)) {
            throw 'request类未继承BaseRequest';
        }
        if (typeof callback == 'function') {
            const files = request.files;
            if (files && files.length > 0) {
                request.setForceRequestType(RequestType.POST_FILE);
            }
            return await executeRequest(this, request, token, callback, options);
        } else {
            return this.executeTokenSync(request, token);
        }
    }

    /**
     * 发送同步请求
     * @param request 请求类
     * @param token token
     * @param options 自定义参数，如headers
     */
    executeTokenSync(request, token, options) {
        return new Promise((resolve) => {
            const _ = this.executeToken(request, token, res => {
                resolve(res);
            }, options);
        });
    }

};