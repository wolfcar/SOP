package com.gitee.sop.gatewaycommon.route;

import com.alibaba.nacos.api.naming.pojo.Instance;

/**
 * @author tanghc
 */
public class NacosServiceHolder extends ServiceHolder {

    private Instance instance;

    public NacosServiceHolder(String serviceId, long lastUpdatedTimestamp, Instance instance) {
        super(serviceId, lastUpdatedTimestamp);
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }
}
