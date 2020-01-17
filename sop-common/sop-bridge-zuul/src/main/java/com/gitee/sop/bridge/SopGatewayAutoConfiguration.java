package com.gitee.sop.bridge;

import com.gitee.sop.gatewaycommon.config.BaseGatewayAutoConfiguration;
import com.gitee.sop.gatewaycommon.zuul.configuration.AlipayZuulConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * https://blog.csdn.net/seashouwang/article/details/80299571
 * @author tanghc
 */
@Configuration
@EnableZuulProxy
@Import(AlipayZuulConfiguration.class)
// 在ErrorMvcAutoConfiguration之前加载
// 如果不加会出现basicErrorController和zuulErrorController冲突
// zuulErrorController是SOP中的，提前加载后basicErrorController就不会加载
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
public class SopGatewayAutoConfiguration extends BaseGatewayAutoConfiguration {
}

