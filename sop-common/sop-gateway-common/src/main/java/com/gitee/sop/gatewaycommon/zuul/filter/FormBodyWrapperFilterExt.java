package com.gitee.sop.gatewaycommon.zuul.filter;

import com.gitee.sop.gatewaycommon.util.RequestUtil;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tanghc
 */
public class FormBodyWrapperFilterExt extends FormBodyWrapperFilter {


    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 如果是文件上传请求，不需要包装
        if (RequestUtil.isMultipart(request)) {
            return false;
        } else {
            return super.shouldFilter();
        }
    }
}
