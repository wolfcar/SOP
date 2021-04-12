const needle = require('needle');
const moment = require('moment');

const {Class} = require('./Class');
const {RequestType} = require('./RequestType');
const {SignUtil} = require('./SignUtil');
const {BaseRequest} = require('../request/BaseRequest');

const HEADERS = {'Accept-Encoding': 'identity'};

const getHeaders = function (headers) {
    if (!headers) {
        return HEADERS;
    }
    for (const key in HEADERS) {
        headers[key] = HEADERS[key];
    }
    return headers;
};

const OpenClient = Class.create({
    /**
     * 初始化客户端
     * @param appId 应用ID
     * @param privateKey 应用私钥，2048位，PKCS8
     * @param url 请求url
     */
    init: function (appId, privateKey, url) {
        this.appId = appId;
        this.privateKey = privateKey;
        this.url = url;
    },
    /**
     * 发送请求
     * @param request 请求类
     * @param callback 回调函数，参数json（undefined则使用executeSync）
     */
    execute: function (request, callback) {
        if (typeof callback == 'function') {
            this.executeToken(request, null, callback);
        } else {
            return this.executeSync(request);
        }
    },
    /**
     * 发送同步请求
     * @param request 请求类
     * */
    executeSync: function (request) {
        return new Promise((resolve) => {
            this.execute(request, res => {
                resolve(res);
            });
        });
    },
    /**
     * 发送请求
     * @param request 请求类
     * @param token token
     * @param callback 回调函数，参数json（undefined则使用executeTokenSync）
     */
    executeToken: function (request, token, callback) {
        if (!(request instanceof BaseRequest)) {
            throw 'request类未继承BaseRequest';
        }
        if (typeof callback == 'function') {
            const requestType = request.getRequestType();
            if (request.files) {
                this._postFile(request, callback);
            } else {
                switch (requestType) {
                    case RequestType.GET:
                        this._get(request, callback);
                        break;
                    case RequestType.POST_FORM:
                        this._postForm(request, callback);
                        break;
                    case RequestType.POST_JSON:
                        this._postJson(request, callback);
                        break;
                    case RequestType.POST_FILE:
                        this._postFile(request, callback);
                        break;
                    default : {
                        throw 'request.getRequestType()类型不正确';
                    }
                }
            }
        } else {
            return this.executeTokenSync(request, token);
        }
    },
    /**
     * 发送同步请求
     * @param request 请求类
     * @param token token
     */
    executeTokenSync: function (request, token) {
        return new Promise((resolve) => {
            this.executeToken(request, token, res => {
                resolve(res);
            });
        });
    },
    _get: function (request, callback) {
        const allParams = this._buildParams(request);
        const that = this;
        // needle.request(method, url, data[, options][, callback])
        needle.request('GET', this.url, allParams, {
            headers: getHeaders()
        }, function (error, response) {
            callback(that._parseResponse(error, response, request));
        });
    },
    _postForm: function (request, callback) {
        const allParams = this._buildParams(request);
        const that = this;
        needle.request('POST', this.url, allParams, {
            headers: getHeaders({
                'Content-Type': 'application/x-www-form-urlencoded'
            })
        }, function (error, response) {
            callback(that._parseResponse(error, response, request));
        });
    },
    _postJson: function (request, callback) {
        const allParams = this._buildParams(request);
        const that = this;
        needle.request('POST', this.url, allParams, {
            headers: getHeaders(), json: true
        }, function (error, response) {
            callback(that._parseResponse(error, response, request));
        });
    },
    _postFile: function (request, callback) {
        const allParams = this._buildParams(request);
        const files = request.files;
        files.forEach(row => {
            // 设置成{ file: row.path, content_type: 'application/octet-stream' }格式
            // needle会认为是上传文件
            allParams[row.name] = {file: row.path, content_type: 'application/octet-stream'};
        });
        const that = this;
        needle.request('POST', this.url, allParams, {
            headers: getHeaders(), multipart: true
        }, function (error, response) {
            callback(that._parseResponse(error, response, request));
        });
    },
    _parseResponse: function (error, response, request) {
        if (!error && response.statusCode === 200) {
            return request.parseResponse(response.body);
        } else {
            // throw '请求异常：' + error
            return { // 重新封装请求异常回调，以防中断
                msg: '请求异常',
                code: '502',
                sub_msg: `${error}`,
                sub_code: 'isv.invalid-server'
            };
        }
    },
    _buildParams: function (request, token) {
        const allParams = {
            'app_id': this.appId,
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
        const sign = SignUtil.createSign(allParams, this.privateKey, 'RSA2');
        allParams.sign = sign;
        return allParams;
    }
});

module.exports = OpenClient;
