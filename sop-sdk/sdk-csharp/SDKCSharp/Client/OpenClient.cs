﻿using System;
using System.Web;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using System.Threading.Tasks;

using SDKCSharp.Common;
using SDKCSharp.Request;
using SDKCSharp.Response;
using SDKCSharp.Utility;
using System.IO;
using Newtonsoft.Json.Linq;

namespace SDKCSharp.Client
{
    /// <summary>
    /// 客户端
    /// </summary>
    public class OpenClient
    {

        private static OpenConfig DEFAULT_CONFIG = new OpenConfig();

        private Dictionary<string, string> header = new Dictionary<string, string>();


        private string url;
        private string appId;
        private string privateKey;
        private string publicKeyPlatform;

        private OpenConfig openConfig;
        private OpenRequest openRequest;
        private DataNameBuilder dataNameBuilder;


        public OpenClient(string url, string appId, string privateKey) 
            : this(url, appId, privateKey,false, DEFAULT_CONFIG)
        {
            
        }

        public OpenClient(string url, string appId, string privateKey, string publicKeyPlatform)
            : this(url, appId, privateKey)
        {
            this.publicKeyPlatform = publicKeyPlatform;
        }

        public OpenClient(string url, string appId, string privateKey, bool priKeyFromFile) 
            : this(url, appId, privateKey, priKeyFromFile, DEFAULT_CONFIG)
        {

        }

        public OpenClient(string url, string appId, string privateKey, bool priKeyFromFile, string publicKeyPlatform)
            : this(url, appId, privateKey, priKeyFromFile)
        {
            this.publicKeyPlatform = publicKeyPlatform;
        }

        public OpenClient(string url, string appId, string privateKey,bool priKeyFromFile, OpenConfig openConfig)
        {
            this.url = url;
            this.appId = appId;
            this.privateKey = privateKey;
            this.openConfig = openConfig;
            this.openRequest = new OpenRequest(openConfig);
            // 如果是从文件中加载私钥
            if (priKeyFromFile)
            {
                this.privateKey = LoadCertificateFile(privateKey);
            }
            this.dataNameBuilder = openConfig.DataNameBuilder;
        }

        public OpenClient(string url, string appId, string privateKey, bool priKeyFromFile, string publicKeyPlatform, OpenConfig openConfig)
            : this(url, appId, privateKey, priKeyFromFile, openConfig)
        {
            this.publicKeyPlatform = publicKeyPlatform;
        }

        /// <summary>
        /// 加载秘钥文件
        /// </summary>
        /// <returns>返回私钥内容.</returns>
        /// <param name="filename">文件路径.</param>
        private static string LoadCertificateFile(string filename)
        {
            if(!File.Exists(filename))
            {
                throw new SopException("文件不存在," + filename);
            }
            using (FileStream fs = File.OpenRead(filename))
            {
                byte[] data = new byte[fs.Length];
                fs.Read(data, 0, data.Length);
                return Encoding.UTF8.GetString(data);
            }
        }

        /// <summary>
        /// 发送请求
        /// </summary>
        /// <typeparam name="T">返回的Response类</typeparam>
        /// <param name="request">请求对象</param>
        /// <returns>返回Response类</returns>
        public virtual T Execute<T>(BaseRequest<T> request) where T : BaseResponse
        {
            return this.Execute<T>(request, null);
        }

        /// <summary>
        /// 发送请求
        /// </summary>
        /// <typeparam name="T">返回的Response类</typeparam>
        /// <param name="request">请求对象</param>
        /// <param name="accessToken">accessToken</param>
        /// <returns>返回Response类</returns>
        public virtual T Execute<T>(BaseRequest<T> request, string accessToken) where T : BaseResponse
        {
            RequestForm requestForm = request.CreateRequestForm(this.openConfig);
            Dictionary<string, string> form = requestForm.Form;
            if (!string.IsNullOrEmpty(accessToken))
            {
                form[this.openConfig.AccessTokenName] = accessToken;
            }
            form[this.openConfig.AppKeyName] = this.appId;
            string sign = SignUtil.CreateSign(form, privateKey, openConfig.Charset, openConfig.SignType);
            form[this.openConfig.SignName] = sign;

            string resp = this.DoExecute(url, requestForm, header);

            return this.ParseResponse<T>(resp, request);
        }

        /// <summary>
        /// 执行请求
        /// </summary>
        /// <param name="url">请求url</param>
        /// <param name="requestForm">请求内容</param>
        /// <param name="header">请求header</param>
        /// <returns>返回服务器响应内容</returns>
        protected virtual string DoExecute(string url, RequestForm requestForm, Dictionary<string, string> header)
        {
            return openRequest.Request(this.url, requestForm, header);
        }

        /// <summary>
        /// 解析返回结果
        /// </summary>
        /// <typeparam name="T">返回的Response</typeparam>
        /// <param name="resp">服务器响应内容</param>
        /// <param name="request">请求Request</param>
        /// <returns>返回Response</returns>
        protected virtual T ParseResponse<T>(string resp, BaseRequest<T> request) where T: BaseResponse
        {
            string method = request.Method;
            string rootNodeName = this.dataNameBuilder.Build(method);
            string errorRootNode = openConfig.ErrorResponseName;
            Dictionary<string, object> responseData = JsonUtil.ParseToDictionary(resp);
            bool errorResponse = responseData.ContainsKey(errorRootNode);
            if (errorResponse)
            {
                rootNodeName = errorRootNode;
            }
            object data = responseData[rootNodeName];
            responseData.TryGetValue(openConfig.SignName, out object sign);
            if (sign != null && !string.IsNullOrEmpty(publicKeyPlatform))
            {
                string signContent = BuildBizJson(rootNodeName, resp);
                if (!CheckResponseSign(signContent, sign.ToString(), publicKeyPlatform))
                {
                    ErrorResponse checkSignErrorResponse = SopSdkErrors.CHECK_RESPONSE_SIGN_ERROR;
                    data = JsonUtil.ToJSONString(checkSignErrorResponse);
                }
            }
            string jsonData = data == null ? "{}" : data.ToString();
            T t = JsonUtil.ParseObject<T>(jsonData);
            t.Body = jsonData;
            return t;
        }

        /// <summary>
        /// 验证服务端返回的sign
        /// </summary>
        /// <returns><c>true</c>, if response sign was checked, <c>false</c> otherwise.</returns>
        /// <param name="signContent">Response data.</param>
        /// <param name="sign">sign data.</param>
        /// <param name="publicKeyPlatform">Public key platform.</param>
        protected virtual bool CheckResponseSign(string signContent, string sign, string publicKeyPlatform)
        {
            try
            {
                Encoding charset = openConfig.Charset;
                SignType signType = openConfig.SignType;
                return SignUtil.RsaCheck(signContent, sign, publicKeyPlatform, charset, signType);
            }
            catch (Exception)
            {
                return false;
            }
        }

        protected virtual string BuildBizJson(string rootNodeName, string body)
        {
            int indexOfRootNode = body.IndexOf(rootNodeName);
            if (indexOfRootNode < 0)
            {
                rootNodeName = openConfig.ErrorResponseName;
                indexOfRootNode = body.IndexOf(rootNodeName);
            }
            string result = null;
            if (indexOfRootNode > 0)
            {
                result = BuildJsonNodeData(body, rootNodeName, indexOfRootNode);
            }
            return result;
        }

        protected virtual string BuildJsonNodeData(string body, string rootNode, int indexOfRootNode)
        {
            int signDataStartIndex = indexOfRootNode + rootNode.Length + 2;
            int indexOfSign = body.IndexOf("\"" + openConfig.SignName  + "\"");
            if (indexOfSign < 0)
            {
                return null;
            }

            int signDataEndIndex = indexOfSign - 1;
            int length = signDataEndIndex - signDataStartIndex;

            return body.Substring(signDataStartIndex, length);
        }
    }
}
