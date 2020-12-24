package com.gitee.sop.test;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.test.alipay.AlipaySignature;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模仿支付宝客户端请求接口
 */
public class AlipayClientPostTest extends TestBase {

    String url = "http://localhost:8081";
    String appId = "20201224791621120804519936";
    // 平台提供的私钥
    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDU+FpS0kHO0xPb\n" +
            "3pSn45fnduCsvleVwjcXzIAJSN/8W9SSIEOtNNPviq6shCUlei9otZDMkpTo/SpX\n" +
            "aEcg157CnO5XI3pILna018aboqPvfxykjkYe9/FBZPo/7WQmKXKCOyENerqBJeYx\n" +
            "T59cQSq9w/fOZPK5STttrfzj/AAIMawpj4Cgprxdtq2v3inCGU9kX3wkEycdZ3tA\n" +
            "ts4THN9cErBnG0BQVA2urv6RbAcBhn1Qrf+2GOX1cCA6x+uyuDPD2YlprykqmF1H\n" +
            "AneTr95Grm+5F+bGP9qLKoIB5a6LzFJ3TLx4d1kxI/cqZ4kShvcQ0+wnreceIOP0\n" +
            "HAQ4O3m/AgMBAAECggEAajG4oEm1hNMduOohCCJlsYZGe/yhocxpOlW/lmGfRq/n\n" +
            "rEGoXWrVL0Hg3ac6+pgVocm/fTKuTAtJHLKjnQ3HXVVxR0QqimmYyY46u4p64kZo\n" +
            "2ukSsAiEZU5btprB8IGEVsnzLkx/STzDrtz8Ir1f+aYJIAKYQanxlzxFuM0EmxB4\n" +
            "4tjoKhZj+d3JYPXJIU6imhX4qH0Sec8A24Zup8pwyOZIZecKioPsfnobfJ0oI88f\n" +
            "tH62svfIxov68JMimFPjNiUnq+myVQuRdLFfcyWJOktJKIbiQIvT9CLZ0pudJPDM\n" +
            "yvmX3JVnzPaueoPFRxXbgKoAg69Iy2egbzB4DccFeQKBgQDpgn61PFBivtrdihaN\n" +
            "UF/IzMKs9GLdU/7Lsw26hlrvNpVl7eWD3h2QUHCbzXmrvfEJgWUCjhQxMOwJ4ILd\n" +
            "DBFCmIIZTTR/N8UvmJWzxPhWLwl94HpFSK9CsDjWdCQds8iQqkHHKCzDBiUNYMjU\n" +
            "zQY8Pzi/UagGMr+dl3dJO70RBQKBgQDpe2zdYtw2kflULhcPgYr+a3z7kaDtLUEm\n" +
            "u3hZTtCtPkpmflJpf2y/EyD0X79mU1fh+KTormswienUHxvFmu/UxYfqLzj1xhBB\n" +
            "JW/brEWcIesPsb6EzMF+26KKne/xliwWYEyTQ8NXXJZuQVVxv4lPnATn/sifXmxs\n" +
            "b3wGMkuq8wKBgHXnaRkTujcRzSsdZWO3GJYoJYf04dKFbdrmruDmpBWzBt5vr3rU\n" +
            "9TKAG0vgBTZdcs5s4lbW7IUmAZi/HvSD+lcY8F/cJsyxmUP+FGCv7QB/QZiodrRz\n" +
            "DeNrXVeTTwUcWQIKpanstCVI/f5yRxWD7EkRVxLrJR70EuJ8r0NwAXgNAoGAWcYi\n" +
            "atFrWPQxySNbrJOjzQWpbdVl6iw/Y9QrhzNd228bDmO97mCXgLSrNCtoncCoBvQi\n" +
            "/HCsOGKe7Pf1JrrXQpmdE/eKUD4S2+m4I+AOd+U2y7cNSWStdcifq+rkaqDSXNw0\n" +
            "NV+VyY6JLOWkI/OAqYKVBFsb6uNUjzEioANQwu8CgYEApE2ozFSIP8vrKj6avhfK\n" +
            "qp4FXgXMYP6rv/XkpcH4YsQLHw5iOhJPSYwKcCB8zqNmVxQprOJuRTTAAHPm6DZH\n" +
            "CN+izw4/s6XbwOQ7E4PfqrBj3srD8pOe1WekGQ05LrLm+oEoQlPYmHB/hyrZA2uY\n" +
            "prI1pe7GDnajEzPDv4xq+Nk=";

    /**
    参数	            类型	    是否必填	    最大长度	    描述	            示例值
    app_id	        String	是	        32	    支付宝分配给开发者的应用ID	2014072300007148
    method	        String	是	        128	    接口名称	alipay.trade.fastpay.refund.query
    format	        String	否	        40	    仅支持JSON	JSON
    charset	        String	是	        10	    请求使用的编码格式，如utf-8,gbk,gb2312等	utf-8
    sign_type	    String	是	        10	    商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2	RSA2
    sign	        String	是	        344	    商户请求参数的签名串，详见签名	详见示例
    timestamp	    String	是	        19	    发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"	2014-07-24 03:07:50
    version	        String	是	        3	    调用的接口版本，固定为：1.0	1.0
    app_auth_token	String	否	        40	    详见应用授权概述
    biz_content	    String	是		请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    // 这个请求会路由到story服务
    @Test
    public void testGet() throws Exception {

        // 公共请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_id", appId);
        params.put("method", "story.get");
        params.put("format", "json");
        params.put("charset", "utf-8");
        params.put("sign_type", "RSA2");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("version", "1.0");

        // 业务参数
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("id", "1");
        bizContent.put("name", "葫芦娃");

        params.put("biz_content", JSON.toJSONString(bizContent));
        String content = AlipaySignature.getSignContent(params);
        String sign = AlipaySignature.rsa256Sign(content, privateKey, "utf-8");
        params.put("sign", sign);

        System.out.println("----------- 请求信息 -----------");
        System.out.println("请求参数：" + buildParamQuery(params));
        System.out.println("商户秘钥：" + privateKey);
        System.out.println("待签名内容：" + content);
        System.out.println("签名(sign)：" + sign);
        System.out.println("URL参数：" + buildUrlQuery(params));

        System.out.println("----------- 返回结果 -----------");
        String responseData = get(url, params);// 发送请求
        System.out.println(responseData);
    }


}
