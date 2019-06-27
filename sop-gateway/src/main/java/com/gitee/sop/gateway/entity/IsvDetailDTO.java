package com.gitee.sop.gateway.entity;

import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class IsvDetailDTO {

    /** appKey, 数据库字段：app_key */
    private String appKey;

    /** 0启用，1禁用, 数据库字段：status */
    private Byte status;

    // keys

    /** secret, 数据库字段：secret */
    private String secret;

    /** 开发者生成的公钥, 数据库字段：public_key_isv */
    private String publicKeyIsv;

    /** 平台生成的私钥, 数据库字段：private_key_platform */
    private String privateKeyPlatform;

    private Byte signType;
}
