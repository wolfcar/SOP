package com.gitee.sop.bookweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import com.gitee.sop.story.api.domain.Story;
import com.gitee.sop.story.api.param.DemoParam;
import com.gitee.sop.story.api.result.DemoResult;
import com.gitee.sop.story.api.service.DemoService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调用dubbo服务，provider是story，见DefaultDemoService.java
 * dubbo配置方式参见：https://github.com/apache/dubbo-spring-boot-project/blob/0.2.x/README_CN.md
 * <p>
 * 对比SpringCloud提供的Feign，dubbo会方便很多。
 * <p>
 * Feign的使用方式参见：com.gitee.sop.bookweb.controller.AlipayBookController#getBook2()
 */
@RestController
public class DubboConsumerController {

    @Reference(version = "${demo.service.version}",
            application = "${dubbo.application.id}",
            url = "dubbo://localhost:12345")
    private DemoService demoService;

    // 作为开放接口
    @ApiMapping(value = "dubbo.story.get")
    public Story openApi(DemoParam demoParam) {
        // 通过dubbo调用story提供的服务
        DemoResult dubboStory = demoService.getStory(demoParam);
        Story story = new Story();
        story.setId(dubboStory.getId());
        story.setName(dubboStory.getName());
        return story;
    }


}