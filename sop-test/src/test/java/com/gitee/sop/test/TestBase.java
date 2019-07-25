package com.gitee.sop.test;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author tanghc
 */
public class TestBase extends TestCase {

    static HttpTool httpTool = new HttpTool();

    /**
     * 发送POST请求
     *
     * @param url
     * @return JSON或者字符串
     */
    public static String post(String url, Map<String, String> params) {
        try {
            return httpTool.requestJson(url, JSON.toJSONString(params), Collections.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("网络请求异常", e);
        }
    }

    /**
     * 发送get请求
     *
     * @param url
     * @return JSON或者字符串
     */
    public static String get(String url, Map<String, String> params) {
        try {
            return httpTool.request(url, params, Collections.emptyMap(), HttpTool.HTTPMethod.GET);
        } catch (IOException e) {
            throw new RuntimeException("网络请求异常", e);
        }
    }

    protected static String buildParamQuery(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString().substring(1);
    }

}