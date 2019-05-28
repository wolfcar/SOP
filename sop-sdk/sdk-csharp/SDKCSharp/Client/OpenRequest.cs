﻿using System;
using System.Web;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using SDKCSharp.Common;
using SDKCSharp.Utility;
using SDKCSharp.Response;

namespace SDKCSharp.Client
{
    public class OpenRequest
    {
       
        private const string HTTP_ERROR_CODE = "-400";

        private OpenConfig openConfig;
        private OpenHttp openHttp;

        public OpenRequest(OpenConfig openConfig)
        {
            this.openConfig = openConfig;
            this.openHttp = new OpenHttp(openConfig);
        }

        /// <summary>
        /// 请求服务器
        /// </summary>
        /// <param name="url">url</param>
        /// <param name="requestForm">请求表单信息</param>
        /// <param name="header">请求头</param>
        /// <returns></returns>
        public string Request(string url, RequestForm requestForm, Dictionary<string, string> header)
        {
            Dictionary<string, string> form = requestForm.Form;
            List<UploadFile> files = requestForm.Files;
            if (files != null && files.Count > 0)
            {
                return this.openHttp.PostFile(url, form, header, files);
            }
            else
            {
                RequestMethod requestMethod = requestForm.RequestMethod;
                if (requestMethod == RequestMethod.GET)
                {
                    string query = this.BuildGetQueryString(form, requestForm.Charset);
                    if (!string.IsNullOrEmpty(query))
                    {
                        url = url + "?" + query;
                    }
                    return openHttp.Get(url, header);
                }
                return this.openHttp.RequestFormBody(url, form, header);
            }
        }

        public string BuildGetQueryString(Dictionary<string, string> form, Encoding charset)
        {
            StringBuilder queryString = new StringBuilder();
            Dictionary<string, string>.KeyCollection keys = form.Keys;
            int i = 0;
            foreach (string keyName in keys)
            {
                if (i++ > 0)
                {
                    queryString.Append("&");
                }
                queryString.Append(keyName).Append("=")
                        .Append(HttpUtility.UrlEncode(form[keyName], charset));
            }
            return queryString.ToString();
        }



        protected string CauseException(Exception e)
        {
            ErrorResponse result = new ErrorResponse();
            result.SubCode = HTTP_ERROR_CODE;
            result.SubMsg = e.Message;
            result.Code = HTTP_ERROR_CODE;
            result.Msg = e.Message;
            return JsonUtil.ToJSONString(result);
        }

    }

    class ErrorResponse : BaseResponse
    {
    }
}
