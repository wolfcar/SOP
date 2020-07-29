package com.gitee.sop.bridge;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.gitee.sop.bridge.route.NacosRegistryListener;
import com.gitee.sop.gatewaycommon.route.RegistryListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;

import java.util.Map;

/**
 * @author tanghc
 */
@Configuration
public class SopRegisterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NacosWatch nacosWatch(NacosDiscoveryProperties nacosDiscoveryProperties, ObjectProvider<TaskScheduler> taskScheduler, Environment environment) {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String contextPath = environment.getProperty("server.servlet.context-path");
        // 将context-path信息加入到metadata中
        if (contextPath != null) {
            metadata.put("context-path", contextPath);
        }
        // 在元数据中新增启动时间，不能修改这个值，不然网关拉取接口会有问题
        metadata.put("time.startup", String.valueOf(System.currentTimeMillis()));
        return new NacosWatch(nacosDiscoveryProperties, taskScheduler);
    }

    /**
     * 微服务路由加载
     */
    @Bean
    RegistryListener registryListenerNacos() {
        return new NacosRegistryListener();
    }
}
