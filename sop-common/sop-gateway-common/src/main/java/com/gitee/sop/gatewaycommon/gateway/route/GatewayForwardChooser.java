package com.gitee.sop.gatewaycommon.gateway.route;

import com.gitee.sop.gatewaycommon.gateway.ServerWebExchangeUtil;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.route.BaseForwardChooser;
import com.gitee.sop.gatewaycommon.route.ForwardInfo;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author tanghc
 */
public class GatewayForwardChooser extends BaseForwardChooser<ServerWebExchange> {

    private static final String VALIDATE_ERROR_PATH = "/sop/validateError";

    @Override
    public ApiParam getApiParam(ServerWebExchange exchange) {
        return ServerWebExchangeUtil.getApiParam(exchange);
    }

    @Override
    public ForwardInfo getForwardInfo(ServerWebExchange exchange) {
        // 如果有异常，直接跳转到异常处理
        if (ServerWebExchangeUtil.getThrowable(exchange) != null) {
            return new ForwardInfo(VALIDATE_ERROR_PATH, "");
        }
        return super.getForwardInfo(exchange);
    }
}
