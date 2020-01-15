package com.gitee.sop.bridge;

import com.gitee.sop.gatewaycommon.gateway.configuration.AlipayGatewayConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * https://blog.csdn.net/seashouwang/article/details/80299571
 * @author tanghc
 */
@Configuration
@Import(AlipayGatewayConfiguration.class)
public class SopGatewayAutoConfiguration {
}
