package com.gitee.sop.test;

import com.gitee.sop.test.taobao.TaobaoSignature;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模仿淘宝客户端请求接口
 */
public class TaobaoClientPostTest extends TestBase {


    String url = "http://localhost:8081";
    String appId = "20190331562013861008375808";
    // 淘宝私钥
    String secret = "29864b93427447f5ac6c44df746f84ef";

    @Test
    public void testGet() throws Exception {

        // 公共请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key", appId);
        params.put("method", "order.get");
        params.put("format", "json");
        params.put("sign_method", "md5");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("v", "");

        // 业务参数
        params.put("data", "get11111");

        System.out.println("----------- 请求信息 -----------");
        System.out.println("请求参数：" + buildParamQuery(params));
        System.out.println("商户秘钥：" + secret);
        String content = TaobaoSignature.getSignContent(params);
        System.out.println("待签名内容：" + content);
        String sign = TaobaoSignature.doSign(content, secret, "md5");
        System.out.println("签名(sign)：" + sign);

        params.put("sign", sign);

        System.out.println("----------- 返回结果 -----------");
        String responseData = get(url, params);// 发送请求
        System.out.println(responseData);
    }


    @Test
    public void testPost() throws Exception {

        // 公共请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key", appId);
        params.put("method", "order.save");
        params.put("format", "json");
        params.put("sign_method", "md5");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("v", "");

        // 业务参数
        // 参数接收见：com.gitee.sop.storyweb.controller.AlipayController2
        params.put("id", "1");
        params.put("name", "葫芦娃post");

        System.out.println("----------- 请求信息 -----------");
        System.out.println("请求参数：" + buildParamQuery(params));
        System.out.println("商户秘钥：" + secret);
        String content = TaobaoSignature.getSignContent(params);
        System.out.println("待签名内容：" + content);
        String sign = TaobaoSignature.doSign(content, secret, "md5");
        System.out.println("签名(sign)：" + sign);

        params.put("sign", sign);

        System.out.println("----------- 返回结果 -----------");
        String responseData = post(url, params);// 发送请求
        System.out.println(responseData);
    }

    @Test
    public void testJson() throws Exception {

        // 公共请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key", appId);
        params.put("method", "order.json");
        params.put("format", "json");
        params.put("sign_method", "md5");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("v", "");

        // 业务参数
        // 参数接收见：com.gitee.sop.storyweb.controller.AlipayController2
        params.put("id", "1");
        params.put("name", "葫芦娃json..");

        System.out.println("----------- 请求信息 -----------");
        System.out.println("请求参数：" + buildParamQuery(params));
        System.out.println("商户秘钥：" + secret);
        String content = TaobaoSignature.getSignContent(params);
        System.out.println("待签名内容：" + content);
        String sign = TaobaoSignature.doSign(content, secret, "md5");
        System.out.println("签名(sign)：" + sign);

        params.put("sign", sign);

        System.out.println("----------- 返回结果 -----------");
        String responseData = postJson(url, params);// 发送请求
        System.out.println(responseData);
    }



}
