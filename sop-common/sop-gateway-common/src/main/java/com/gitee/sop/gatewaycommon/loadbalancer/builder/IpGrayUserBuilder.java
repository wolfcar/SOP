package com.gitee.sop.gatewaycommon.loadbalancer.builder;

import com.gitee.sop.gatewaycommon.param.ApiParam;

/**
 * @author tanghc
 */
public class IpGrayUserBuilder implements GrayUserBuilder {
    
    @Override
    public String buildGrayUserKey(ApiParam apiParam) {
        return apiParam.fetchIp();
    }

    @Override
    public int order() {
        return 1;
    }
}
