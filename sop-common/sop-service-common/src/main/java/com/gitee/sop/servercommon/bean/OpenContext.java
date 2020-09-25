package com.gitee.sop.servercommon.bean;

import java.util.Date;
import java.util.Map;

/**
 * 获取开放平台请求参数。
 *
 * @author tanghc
 */
public interface OpenContext {

    /**
     * 获取某个参数值
     * @param name 参数名称
     * @return 没有返回null
     */
    String getParameter(String name);

    /**
     * 返回所有的请求参数
     * @return 返回所有的请求参数
     */
    Map<String, Object> getParameterMap();

    /**
     * 返回appid
     *
     * @return 返回appid
     */
    String getAppId();

    /**
     * 返回业务参数json字符串
     *
     * @return 返回字符串业务参数
     */
    String getBizContent();

    /**
     * 返回业务对象
     *
     * @return 业务对象
     */
    Object getBizObject();

    /**
     * 返回字符编码
     *
     * @return 如UTF-8
     */
    String getCharset();

    /**
     * 返回接口名
     *
     * @return 如：alipay.goods.get
     */
    String getMethod();

    /**
     * 返回版本号
     *
     * @return 如：1.0
     */
    String getVersion();

    /**
     * 返回参数格式化
     *
     * @return 如：json
     */
    String getFormat();

    /**
     * 返回签名类型
     *
     * @return 如：RSA2
     */
    String getSignType();

    /**
     * 返回时间戳
     *
     * @return
     */
    Date getTimestamp();


    /**
     * 返回token
     *
     * @return 返回token
     */
     String getAppAuthToken();

    /**
     * 返回回调地址
     *
     * @return 返回回调地址
     */
    String getNotifyUrl();

}
