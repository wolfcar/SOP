package com.gitee.sop.gatewaycommon.validate.taobao;


import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.message.ErrorEnum;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.validate.AbstractSigner;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 淘宝开放平台签名验证实现，http://open.taobao.com/doc.htm?docId=101617&docType=1
 *
 * @author tanghc
 */
@Slf4j
public class TaobaoSigner extends AbstractSigner {

    @Override
    public String buildServerSign(ApiParam param, String secret) {
        String signMethod = param.fetchSignMethod();
        if (signMethod == null) {
            signMethod = SopConstants.DEFAULT_SIGN_METHOD;
        }
        String signContent = TaobaoSignature.getSignContent(param);
        try {
            return TaobaoSignature.doSign(signContent, secret, signMethod);
        } catch (IOException e) {
            log.error("淘宝签名失败, param:{}", param, e);
            throw ErrorEnum.ISP_UNKNOWN_ERROR.getErrorMeta().getException();
        }
    }
}
