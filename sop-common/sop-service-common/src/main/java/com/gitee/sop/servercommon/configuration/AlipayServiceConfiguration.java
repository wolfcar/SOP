package com.gitee.sop.servercommon.configuration;

import com.gitee.sop.servercommon.bean.ServiceConfig;
import com.gitee.sop.servercommon.param.SopHandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 具备支付宝开放平台服务提供能力
 * @author tanghc
 */
public class AlipayServiceConfiguration extends BaseServiceConfiguration {

    static {
        // 默认版本号为1.0
        ServiceConfig.getInstance().setDefaultVersion("1.0");
    }


    @Override
    protected void doAfter() {
        super.doAfter();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = getApplicationContext().getBean(RequestMappingHandlerAdapter.class);
        SopHandlerMethodArgumentResolver sopHandlerMethodArgumentResolver = ServiceConfig.getInstance().getMethodArgumentResolver();
        sopHandlerMethodArgumentResolver.setRequestMappingHandlerAdapter(requestMappingHandlerAdapter);
    }
}
