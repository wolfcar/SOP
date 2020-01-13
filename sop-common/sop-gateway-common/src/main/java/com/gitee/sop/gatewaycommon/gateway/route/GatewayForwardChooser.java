package com.gitee.sop.gatewaycommon.gateway.route;

import com.gitee.sop.gatewaycommon.gateway.ServerWebExchangeUtil;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.route.BaseForwardChooser;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author tanghc
 */
public class GatewayForwardChooser extends BaseForwardChooser<ServerWebExchange> {
    @Override
    protected ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }
}
