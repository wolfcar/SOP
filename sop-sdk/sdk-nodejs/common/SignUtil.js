const {KJUR, hextob64} = require('jsrsasign')

const HashMap = {
    SHA256withRSA: 'SHA256withRSA',
    SHA1withRSA: 'SHA1withRSA'
}

const PEM_BEGIN = '-----BEGIN PRIVATE KEY-----\n'
const PEM_END = '\n-----END PRIVATE KEY-----'

/**
 * rsa签名参考：https://www.jianshu.com/p/145eab95322c
 */
exports.SignUtil = {
    /**
     * 创建签名
     * @param params 请求参数
     * @param privateKey 私钥，PKCS8
     * @param signType 签名类型，RSA,RSA2
     * @returns 返回签名内容
     */
    createSign(params, privateKey, signType) {
        const content = this.getSignContent(params)
        return this.sign(content, privateKey, signType)
    },
    sign: function (content, privateKey, signType) {
        if (signType.toUpperCase() === 'RSA') {
            return this.rsaSign(content, privateKey, HashMap.SHA1withRSA)
        } else if (signType.toUpperCase() === 'RSA2') {
            return this.rsaSign(content, privateKey, HashMap.SHA256withRSA)
        } else {
            throw 'signType错误'
        }
    },
    /**
     * rsa签名
     * @param content 签名内容
     * @param privateKey 私钥
     * @param hash hash算法，SHA256withRSA，SHA1withRSA
     * @returns 返回签名字符串，base64
     */
    rsaSign: function (content, privateKey, hash) {
        privateKey = this._formatKey(privateKey)
        // 创建 Signature 对象
        const signature = new KJUR.crypto.Signature({
            alg: hash,
            //!这里指定 私钥 pem!
            prvkeypem: privateKey
        })
        signature.updateString(content)
        const signData = signature.sign()
        // 将内容转成base64
        return hextob64(signData)
    },
    _formatKey: function (key) {
        if (!key.startsWith(PEM_BEGIN)) {
            key = PEM_BEGIN + key
        }
        if (!key.endsWith(PEM_END)) {
            key = key + PEM_END
        }
        return key
    },
    /**
     * 获取签名内容
     * @param params 请求参数
     * @returns {string}
     */
    getSignContent: function (params) {
        const paramNames = []
        // 获取对象中的Key
        paramNames.push(...Object.keys(params || {})
        // 过滤无效的KeyValue
        .filter(paramName => {
            // 参数名不为undefined且参数值不为undefined
            return !(typeof paramName === undefined || typeof params[paramName] === undefined)
        }))

        paramNames.sort()

        // 合成签名字符串
        const paramNameValue = paramNames.map(paramName => {
            const val = params[paramName];
            return `${paramName}=${val}`
        })

        return paramNameValue.join('&')
    }
}
