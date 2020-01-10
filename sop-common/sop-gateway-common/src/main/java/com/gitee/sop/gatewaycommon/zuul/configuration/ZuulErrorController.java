package com.gitee.sop.gatewaycommon.zuul.configuration;

import com.gitee.sop.gatewaycommon.bean.ApiContext;
import com.gitee.sop.gatewaycommon.result.ResultExecutor;
import com.gitee.sop.gatewaycommon.zuul.ZuulContext;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理网关自身异常
 *
 * @author tanghc
 */
@ControllerAdvice
@Slf4j
public class ZuulErrorController {

    /**
     * 错误最终会到这里来
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getResponse() == null) {
            ctx.setResponse(response);
        }
        Throwable throwable = ctx.getThrowable();
        log.error("zuul网关报错，URL:{}, status:{}, params:{}",
                request.getRequestURL().toString()
                , response.getStatus()
                , ZuulContext.getApiParam()
                , throwable);
        return this.buildResult(throwable);
    }

    protected Object buildResult(Throwable throwable) {
        ResultExecutor<RequestContext, String> resultExecutor = ApiContext.getApiConfig().getZuulResultExecutor();
        return resultExecutor.buildErrorResult(RequestContext.getCurrentContext(), throwable);
    }

}
