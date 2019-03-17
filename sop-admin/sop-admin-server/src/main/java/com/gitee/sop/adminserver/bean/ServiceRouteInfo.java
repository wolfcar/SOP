package com.gitee.sop.adminserver.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author thc
 */
@Data
public class ServiceRouteInfo {
    /** 服务名称，对应spring.application.name */
    private String serviceId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime = new Date();

    private String description;

    @JSONField(serialize = false)
    private List<GatewayRouteDefinition> routeDefinitionList;
}