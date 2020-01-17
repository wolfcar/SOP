package com.gitee.sop.gatewaycommon.zuul.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.exception.ApiException;
import com.gitee.sop.gatewaycommon.message.Error;
import com.gitee.sop.gatewaycommon.message.ErrorEnum;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.result.BaseExecutorAdapter;
import com.gitee.sop.gatewaycommon.result.ResultExecutorForZuul;
import com.gitee.sop.gatewaycommon.zuul.ZuulContext;
import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tanghc
 */
@Slf4j
public class ZuulResultExecutor extends BaseExecutorAdapter<RequestContext, String> implements ResultExecutorForZuul {

    @Override
    protected boolean isMergeResult(RequestContext request) {
        Object notMerge = request.getRequest().getAttribute(SopConstants.SOP_NOT_MERGE);
        if (notMerge != null) {
            return false;
        }
        return super.isMergeResult(request);
    }

    @Override
    public int getResponseStatus(RequestContext requestContext) {
        List<Pair<String, String>> bizHeaders = requestContext.getZuulResponseHeaders();

        return bizHeaders.stream()
                .filter(header -> SopConstants.X_SERVICE_ERROR_CODE.equals(header.first()))
                .map(header -> Integer.valueOf(header.second()))
                .findFirst()
                .orElse(requestContext.getResponseStatusCode());
    }

    @Override
    public String getResponseErrorMessage(RequestContext requestContext) {
        List<Pair<String, String>> bizHeaders = requestContext.getZuulResponseHeaders();
        int index = -1;
        String errorMsg = null;
        for (int i = 0; i < bizHeaders.size(); i++) {
            Pair<String, String> header = bizHeaders.get(i);
            if (SopConstants.X_SERVICE_ERROR_MESSAGE.equals(header.first())) {
                errorMsg = header.second();
                index = i;
                break;
            }
        }
        if (index > -1) {
            requestContext.getZuulResponseHeaders().remove(index);
        }
        return errorMsg;
    }

    @Override
    public ApiParam getApiParam(RequestContext requestContext) {
        return ZuulContext.getApiParam();
    }

    @Override
    public String buildErrorResult(RequestContext request, Throwable throwable) {
        Error error = getError(throwable);
        return isMergeResult(request) ? this.merge(request, (JSONObject) JSON.toJSON(error))
                : JSON.toJSONString(error);
    }

    public static Error getError(Throwable throwable) {
        Error error = null;
        if (throwable instanceof ZuulException) {
            ZuulException ex = (ZuulException) throwable;
            Throwable cause = ex.getCause();
            if (cause instanceof ApiException) {
                ApiException apiException = (ApiException) cause;
                error = apiException.getError();
            }
        } else if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            error = apiException.getError();
        }
        if (error == null) {
            error = ErrorEnum.ISP_UNKNOWN_ERROR.getErrorMeta().getError();
        }
        return error;
    }
}
