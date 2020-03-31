package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.ApiAbility;
import com.gitee.sop.storyweb.controller.param.StoryParam;
import com.gitee.sop.storyweb.controller.result.StoryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tanghc
 */
@RestController
@Slf4j
// 注解放在这里，表示类中的方法都具备接口开放能力
@ApiAbility
public class TaobaoController {

    @RequestMapping("/order/get")
    public StoryResult get(@RequestParam String data) {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName(data);
        return story;
    }

    @RequestMapping("/order/save")
    public StoryResult save(StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId((long) param.getId());
        story.setName(param.getName());
        return story;
    }

    @RequestMapping("/order/json")
    public StoryResult json(@RequestBody StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId((long) param.getId());
        story.setName(param.getName());
        return story;
    }
}
