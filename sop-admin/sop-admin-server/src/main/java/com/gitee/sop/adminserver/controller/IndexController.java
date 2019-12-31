package com.gitee.sop.adminserver.controller;

import com.gitee.easyopen.ApiConfig;
import com.gitee.easyopen.support.ApiController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * api入口
 */
@Controller
@RequestMapping("api")
public class IndexController extends ApiController {

    @Value("${easyopen.show-doc:false}")
    private String showDoc;

    @Override
    protected void initApiConfig(ApiConfig apiConfig) {
        apiConfig.setShowDoc(Boolean.valueOf(showDoc));
        apiConfig.setIgnoreValidate(true);
    }
}
