package com.gitee.sop.servercommon.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

import static com.gitee.sop.servercommon.bean.ParamNames.API_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.APP_AUTH_TOKEN_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.APP_KEY_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.BIZ_CONTENT_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.CHARSET_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.FORMAT_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.NOTIFY_URL_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.SIGN_TYPE_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.TIMESTAMP_NAME;
import static com.gitee.sop.servercommon.bean.ParamNames.TIMESTAMP_PATTERN;
import static com.gitee.sop.servercommon.bean.ParamNames.VERSION_NAME;

/**
 * @author tanghc
 */
public class OpenContextImpl implements OpenContext {
    private JSONObject requestParams;
    private Object bizObject;

    public OpenContextImpl(JSONObject requestParams) {
        this.requestParams = requestParams;
    }

    public void setBizObject(Object bizObject) {
        this.bizObject = bizObject;
    }

    @Override
    public String getAppId() {
        return requestParams.getString(APP_KEY_NAME);
    }

    @Override
    public Object getBizObject() {
        return bizObject;
    }

    @Override
    public String getBizContent() {
        return requestParams.getString(BIZ_CONTENT_NAME);
    }

    @Override
    public String getCharset() {
        return requestParams.getString(CHARSET_NAME);
    }

    @Override
    public String getMethod() {
        return requestParams.getString(API_NAME);
    }

    @Override
    public String getVersion() {
        return requestParams.getString(VERSION_NAME);
    }

    @Override
    public String getFormat() {
        return requestParams.getString(FORMAT_NAME);
    }

    @Override
    public String getSignType() {
        return requestParams.getString(SIGN_TYPE_NAME);
    }

    @Override
    public Date getTimestamp() {
        String timestampStr = requestParams.getString(TIMESTAMP_NAME);
        try {
            return DateUtils.parseDate(timestampStr, TIMESTAMP_PATTERN);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String getAppAuthToken() {
        return requestParams.getString(APP_AUTH_TOKEN_NAME);
    }

    @Override
    public String getNotifyUrl() {
        return requestParams.getString(NOTIFY_URL_NAME);
    }


    @Override
    public String toString() {
        return requestParams.toString();
    }
}
