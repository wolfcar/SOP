package com.gitee.sop.gateway.controller;

import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.zuul.ValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 传统web开发入口
 * @author tanghc
 */
@Slf4j
@WebServlet(urlPatterns = "/rest/*")
public class RestServlet extends HttpServlet {

    private static final String EMPTY_VERSION = "";

    @Value("${zuul.servlet-path:/zuul}")
    private String path;

    @Value("${sop.restful.path:/rest}")
    private String restPath;

    @Autowired
    private ValidateService validateService;

    /**
     * 验证回调，可自定义实现接口
     */
    private ValidateService.ValidateCallback callback = (currentContext -> {
        try {
            currentContext.getRequest().getRequestDispatcher(path).forward(currentContext.getRequest(), currentContext.getResponse());
        } catch (Exception e) {
            log.error("请求转发异常", e);
        }
    });

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURL().toString();
        int index = url.indexOf(restPath);
        // 取/rest的后面部分
        String path = url.substring(index + restPath.length());
        request.setAttribute(SopConstants.REDIRECT_METHOD_KEY, path);
        request.setAttribute(SopConstants.REDIRECT_VERSION_KEY, EMPTY_VERSION);
        validateService.validate(request, response, callback);
    }

}