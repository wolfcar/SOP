package com.gitee.sop.storyweb.controller;

import com.gitee.sop.servercommon.annotation.BizCode;
import com.gitee.sop.servercommon.annotation.Open;
import com.gitee.sop.storyweb.controller.param.CategoryParam;
import com.gitee.sop.storyweb.controller.param.LargeTextParam;
import com.gitee.sop.storyweb.controller.param.StoryParam;
import com.gitee.sop.storyweb.controller.result.CategoryResult;
import com.gitee.sop.storyweb.controller.result.StoryResult;
import com.gitee.sop.storyweb.controller.result.TreeResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 签名验证通过后，到达这里进行具体的业务处理。
 *
 * @author tanghc
 */
@RestController
@RequestMapping("story")
@Slf4j
@Api(tags = "故事接口")
public class Example1001_BaseController {

    // http://localhost:2222/stroy/get
    // 原生的接口，可正常调用
    @RequestMapping("/get")
    public StoryResult get() {
        StoryResult result = new StoryResult();
        result.setId(1L);
        result.setName("海底小纵队(原生)");
        return result;
    }

    // 基础用法
    @ApiOperation(value = "获取故事信息（首位）", notes = "获取故事信息的详细信息", position = -100/* position默认0，值越小越靠前 */)
    @Open(value = "story.get", bizCode = {
            // 定义业务错误码，用于文档显示
            @BizCode(code = "100001", msg = "姓名错误", solution = "填写正确的姓名"),
            @BizCode(code = "100002", msg = "备注错误", solution = "填写正确备注"),
    })
    @RequestMapping("/get/v1")
    public StoryResult get_v1(StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName("海底小纵队(story.get1.0), " + "param:" + param);
        return story;
    }

    // 指定版本号
    @ApiOperation(value = "获取故事信息", notes = "获取故事信息的详细信息")
    @Open(value = "story.get", version = "2.0", bizCode = {
            // 定义业务错误码，用于文档显示
            @BizCode(code = "100001", msg = "姓名错误", solution = "填写正确的姓名"),
            @BizCode(code = "100002", msg = "备注错误", solution = "填写正确备注"),
    })
    @RequestMapping("/get/v2")
    public StoryResult get_v2(StoryParam param) {
        StoryResult story = new StoryResult();
        story.setId(1L);
        story.setName("海底小纵队(story.get2.0), " + "param:" + param);
        return story;
    }

    // 参数绑定
    @Open(value = "story.param.bind", mergeResult = false)
    @RequestMapping("/get/param/v1")
    public StoryResult param(int id, String name) {
        StoryResult result = new StoryResult();
        result.setName("参数绑定：id:" + id + ", name:" + name);
        return result;
    }

    // 忽略验证
    @ApiOperation(value = "忽略签名验证", notes = "忽略签名验证")
    @Open(value = "story.get.ignore", ignoreValidate = true)
    @RequestMapping("/get/ignore/v1")
    public StoryResult getStory21(StoryParam story) {
        StoryResult result = new StoryResult();
        result.setName(story.getName() + ", ignoreValidate = true");
        return result;
    }

    @Open(value = "story.get.large")
    @RequestMapping("/get/large/v1")
    public StoryResult getStoryLarge(LargeTextParam param) {
        StoryResult result = new StoryResult();
        int length = param.getContent().getBytes(StandardCharsets.UTF_8).length;
        result.setName("length:" + length);
        return result;
    }

    // 返回数组结果
    @ApiOperation(value = "返回数组结果（第二）", notes = "返回数组结果", position = -99)
    @Open("story.list")
    @RequestMapping("/list/v1")
    public List<StoryResult> getStory3(StoryParam story) {
        int index = 0;
        StoryResult storyVO = new StoryResult();
        storyVO.setId(1L);
        storyVO.setName("白雪公主, index:" + index++);
        storyVO.setGmt_create(new Date());

        StoryResult storyVO2 = new StoryResult();
        storyVO2.setId(1L);
        storyVO2.setName("白雪公主, index:" + index++);
        storyVO2.setGmt_create(new Date());

        return Arrays.asList(storyVO, storyVO2);
    }

    // 演示文档表格树
    @ApiOperation(value = "获取分类信息", notes = "演示表格树")
    @Open("category.get")
    @PostMapping("/category/get/v1")
    public CategoryResult getCategory(CategoryParam param) {
        System.out.println(param);
        StoryResult result = new StoryResult();
        result.setId(1L);
        result.setName("白雪公主");
        result.setGmt_create(new Date());
        CategoryResult categoryResult = new CategoryResult();
        categoryResult.setCategoryName("娱乐");
        categoryResult.setStoryResult(result);
        return categoryResult;
    }

    // 演示文档页树状返回
    @ApiOperation(value = "树状返回", notes = "树状返回")
    @Open("story.tree.get")
    @PostMapping("/tree/v1")
    public TreeResult tree(StoryParam param) {
        int id = 0;
        TreeResult parent = new TreeResult();
        parent.setId(++id);
        parent.setName("父节点");
        parent.setPid(0);

        TreeResult child1 = new TreeResult();
        child1.setId(++id);
        child1.setName("子节点1");
        child1.setPid(1);

        TreeResult child2 = new TreeResult();
        child2.setId(++id);
        child2.setName("子节点2");
        child2.setPid(1);

        parent.setChildren(Arrays.asList(child1, child2));

        return parent;
    }

}
