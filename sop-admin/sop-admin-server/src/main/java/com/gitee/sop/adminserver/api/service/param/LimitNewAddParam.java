package com.gitee.sop.adminserver.api.service.param;

import com.gitee.easyopen.doc.annotation.ApiDocField;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 限流
 * @author tanghc
 */
@Data
public class LimitNewAddParam {
    /** 路由id, 数据库字段：route_id */
    @ApiDocField(description = "routeId")
    private String routeId;

    /**  数据库字段：app_key */
    @ApiDocField(description = "appKey")
    private String appKey;

    /** 限流ip，多个用英文逗号隔开, 数据库字段：limit_ip */
    @ApiDocField(description = "limitIp")
    private String limitIp;

    @ApiDocField(description = "serviceId")
    @NotBlank(message = "serviceId can not null")
    private String serviceId;

    /** 限流策略，1：漏桶策略，2：令牌桶策略, 数据库字段：limit_type */
    @ApiDocField(description = "限流策略，1：漏桶策略，2：令牌桶策略")
    @NotNull(message = "limitType不能为空")
    private Byte limitType;

    /** 每秒可处理请求数, 数据库字段：exec_count_per_second */
    @ApiDocField(description = "每秒可处理请求数")
    private Integer execCountPerSecond;

    /** 返回的错误码, 数据库字段：limit_code */
    @ApiDocField(description = "返回的错误码")
    private String limitCode;

    /** 返回的错误信息, 数据库字段：limit_msg */
    @ApiDocField(description = "返回的错误信息")
    private String limitMsg;

    /** 令牌桶容量, 数据库字段：token_bucket_count */
    @ApiDocField(description = "令牌桶容量")
    private Integer tokenBucketCount;

    /** 1:开启，0关闭, 数据库字段：limit_status */
    @ApiDocField(description = "1:开启，0关闭")
    @NotNull(message = "limitStatus不能为空")
    private Byte limitStatus;

    @ApiDocField(description = "排序字段")
    @NotNull(message = "orderIndex不能为空")
    @Min(value = 0, message = "orderIndex必须大于等于0")
    private Integer orderIndex;
}
