package com.gitee.sop.storyweb.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import com.gitee.sop.servercommon.util.UploadUtil;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author tanghc
 */
@RestController
public class DemoOrderController {

    @Value("${server.port}")
    private String port;

    @ApiMapping("member.info.get")
    public Object member(MemberInfoGetParam param, HttpServletRequest request) {
        if ("tom".equals(param.name)) {
            throw new IllegalArgumentException("name参数错误");
        }
        Collection<MultipartFile> uploadFiles = UploadUtil.getUploadFiles(request);
        for (MultipartFile uploadFile : uploadFiles) {
            try {
                System.out.println("文件名称：" + uploadFile.getOriginalFilename()
                        + " 表单名称：" + uploadFile.getName()
                        + " 文件内容：" +
                        IOUtils.toString(uploadFile.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(param);
        return JSON.parseObject("{\n" +
                "    \"id\": 123,\n" +
                "    \"name\": \"jim\",\n" +
                "    \"member_info\": {\n" +
                "        \"is_vip\": 1,\n" +
                "        \"vip_endtime\": \"2020-11-11 11:11:11\"\n" +
                "    }\n" +
                "}", MemberInfoGetResult.class);
    }


    @Data
    public static class MemberInfoGetParam {
        private String name;
        private Integer age;
        private String address;
    }

    @Data
    public static class MemberInfoGetResult {
        private Integer id;
        private String name;
        private MemberInfoGetResultMemberInfo member_info;
    }

    @Data
    public static class MemberInfoGetResultMemberInfo {
        private Byte is_vip;
        private String vip_endtime;
    }

}
