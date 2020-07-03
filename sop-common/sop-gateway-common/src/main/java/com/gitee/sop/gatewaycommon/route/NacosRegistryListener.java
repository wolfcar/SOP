package com.gitee.sop.gatewaycommon.route;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.gitee.sop.gatewaycommon.bean.InstanceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 加载服务路由，nacos实现
 *
 * @author tanghc
 */
@Slf4j
public class NacosRegistryListener extends BaseRegistryListener {

    private static final String METADATA_KEY_TIME_STARTUP = "time.startup";

    private volatile Set<NacosServiceHolder> cacheServices = new HashSet<>();

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired(required = false)
    private List<RegistryEvent> registryEventList;

    @Override
    public synchronized void onEvent(ApplicationEvent applicationEvent) {
        List<NacosServiceHolder> serviceList = this.getServiceList();
        final Set<NacosServiceHolder> currentServices = new HashSet<>(serviceList);

        // 删除现有的，剩下的就是新服务
        currentServices.removeAll(cacheServices);
        // 如果有新的服务注册进来
        if (currentServices.size() > 0) {
            currentServices.forEach(nacosServiceHolder -> {
                Instance instance = nacosServiceHolder.getInstance();
                InstanceDefinition instanceDefinition = new InstanceDefinition();
                instanceDefinition.setInstanceId(instance.getInstanceId());
                instanceDefinition.setServiceId(nacosServiceHolder.getServiceId());
                instanceDefinition.setIp(instance.getIp());
                instanceDefinition.setPort(instance.getPort());
                instanceDefinition.setMetadata(instance.getMetadata());
                pullRoutes(instanceDefinition);
                if (registryEventList != null) {
                    registryEventList.forEach(registryEvent -> registryEvent.onRegistry(instanceDefinition));
                }
            });
        }

        // 如果有服务下线
        Set<String> removedServiceIdList = getRemovedServiceId(serviceList);
        // 移除
        this.doRemove(removedServiceIdList);

        cacheServices = new HashSet<>(serviceList);
    }

    /**
     * 获取建康的服务实例
     * @return 没有返回空的list
     */
    private List<NacosServiceHolder> getServiceList() {
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        ListView<String> servicesOfServer = null;
        try {
            servicesOfServer = namingService.getServicesOfServer(1, Integer.MAX_VALUE);
        } catch (NacosException e) {
            log.error("namingService.getServicesOfServer()错误", e);
        }
        if (servicesOfServer == null || CollectionUtils.isEmpty(servicesOfServer.getData())) {
            return Collections.emptyList();
        }
        return servicesOfServer
                .getData()
                .stream()
                .filter(this::canOperator)
                .map(serviceName -> {
                    try {
                        // 获取服务实例
                        List<Instance> allInstances = namingService.getAllInstances(serviceName);
                        if (CollectionUtils.isEmpty(allInstances)) {
                            return null;
                        }
                        Instance instance = allInstances.stream()
                                // 只获取建康实例
                                .filter(Instance::isHealthy)
                                // 根据启动时间倒叙，找到最新的服务器
                                .max(Comparator.comparing(ins -> {
                                    String startupTime = ins.getMetadata().getOrDefault(METADATA_KEY_TIME_STARTUP, "0");
                                    return Long.valueOf(startupTime);
                                }))
                                .orElse(null);
                        if (instance == null) {
                            return null;
                        } else {
                            String startupTime = instance.getMetadata().getOrDefault("time.startup", "0");
                            long time = Long.parseLong(startupTime);
                            return new NacosServiceHolder(serviceName, time, instance);
                        }
                    } catch (NacosException e) {
                        log.error("namingService.getAllInstances(serviceName)错误，serviceName：{}", serviceName, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取已经下线的serviceId
     *
     * @param serviceList 最新的serviceId集合
     * @return 返回已下线的serviceId
     */
    private Set<String> getRemovedServiceId(List<NacosServiceHolder> serviceList) {
        Set<String> cache = cacheServices.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        Set<String> newList = serviceList.stream()
                .map(NacosServiceHolder::getServiceId)
                .collect(Collectors.toSet());

        cache.removeAll(newList);
        return cache;
    }

    private void doRemove(Set<String> deletedServices) {
        if (deletedServices == null) {
            return;
        }
        deletedServices.forEach(serviceId -> {
            this.removeRoutes(serviceId);
            if (registryEventList != null) {
                registryEventList.forEach(registryEvent -> registryEvent.onRemove(serviceId));
            }
        });
    }

}
