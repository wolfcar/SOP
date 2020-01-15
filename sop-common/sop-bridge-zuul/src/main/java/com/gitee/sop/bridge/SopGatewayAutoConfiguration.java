package com.gitee.sop.bridge;

import com.gitee.sop.gatewaycommon.zuul.configuration.AlipayZuulConfiguration;
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
public class SopGatewayAutoConfiguration {
}

