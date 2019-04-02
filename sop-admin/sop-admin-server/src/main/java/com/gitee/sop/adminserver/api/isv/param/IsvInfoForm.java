package com.gitee.sop.adminserver.api.isv.param;

import com.gitee.easyopen.doc.annotation.ApiDocField;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

/**
 * @author tanghc
 */
@Data
public class IsvInfoForm {

    private int signType = 1;

    /** secret, 数据库字段：secret */
    @ApiDocField(description = "secret")
    @Length(max = 100,message = "secret长度不能超过100")
    private String secret = "";

    /** 公钥, 数据库字段：pub_key */
    @ApiDocField(description = "pubKey", required = true)
    @NotBlank(message = "pubKey不能为空")
    private String pubKey;

    /** 私钥, 数据库字段：pri_key */
    @ApiDocField(description = "priKey", required = true)
    @NotBlank(message = "priKey不能为空")
    private String priKey;

    /** 0启用，1禁用, 数据库字段：status */
    @ApiDocField(description = "状态：0：启用，1：禁用")
    private Byte status = 0;

    @ApiDocField(description = "roleCode数组", elementClass = String.class)
    private List<String> roleCode = Collections.emptyList();
}