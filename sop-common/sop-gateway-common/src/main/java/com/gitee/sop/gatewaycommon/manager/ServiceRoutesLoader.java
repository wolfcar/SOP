package com.gitee.sop.gatewaycommon.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.gitee.sop.gatewaycommon.bean.NacosConfigs;
import com.gitee.sop.gatewaycommon.bean.ServiceRouteInfo;
import com.gitee.sop.gatewaycommon.bean.TargetRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 发现新服务，更新路由信息
 *
 * @author tanghc
 */
@Slf4j
public class ServiceRoutesLoader<T extends TargetRoute> {

    private static final String SECRET = "a3d9sf!1@odl90zd>fkASwq";

    private static final int FIVE_SECONDS = 1000 * 5;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @NacosInjected
    private ConfigService configService;

    @Autowired
    private BaseRouteCache<T> baseRouteCache;

    private RestTemplate restTemplate = new RestTemplate();

    private Map<String, Long> updateTimeMap = new HashMap<>(16);

    public ServiceRoutesLoader() {
        // 解决statusCode不等于200，就抛异常问题
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return statusCode == null;
            }
        });
    }

    public synchronized void load(ApplicationEvent event) {
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        List<ServiceInfo> subscribes = null;
        try {
            subscribes = namingService.getSubscribeServices();
        } catch (NacosException e) {
            log.error("namingService.getSubscribeServices()错误", e);
        }
        if (CollectionUtils.isEmpty(subscribes)) {
            return;
        }
        // subscribe
        String thisServiceId = nacosDiscoveryProperties.getService();
//        ConfigService configService = nacosConfigProperties.configServiceInstance();
        for (ServiceInfo serviceInfo : subscribes) {
            String serviceName = serviceInfo.getName();
            // 如果是本机服务，跳过
            if (Objects.equals(thisServiceId, serviceName)) {
                continue;
            }
            // nacos会不停的触发事件，这里做了一层拦截
            // 同一个serviceId5秒内允许访问一次
            Long lastUpdateTime = updateTimeMap.getOrDefault(serviceName, 0L);
            long now = System.currentTimeMillis();
            if (now - lastUpdateTime < FIVE_SECONDS) {
                continue;
            }
            updateTimeMap.put(serviceName, now);
            try {
                String dataId = NacosConfigs.getRouteDataId(serviceName);
                String groupId = NacosConfigs.GROUP_ROUTE;
                List<Instance> allInstances = namingService.getAllInstances(serviceName);
                if (CollectionUtils.isEmpty(allInstances)) {
                    // 如果没有服务列表，则删除所有路由信息
                    baseRouteCache.remove(serviceName);
                    configService.removeConfig(dataId, groupId);
                } else {
                    for (Instance instance : allInstances) {
                        String url = getRouteRequestUrl(instance);
                        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                        if (responseEntity.getStatusCode() == HttpStatus.OK) {
                            String body = responseEntity.getBody();
                            ServiceRouteInfo serviceRouteInfo = JSON.parseObject(body, ServiceRouteInfo.class);
                            baseRouteCache.load(serviceRouteInfo, callback -> {
                                try {
                                    log.info("加载服务路由，serviceId:{}, instance:{}",serviceName, instance);
                                    configService.publishConfig(dataId, groupId, body);
                                } catch (NacosException e) {
                                    log.error("nacos推送失败，serviceId:{}, instance:{}",serviceName, instance);
                                }
                            });
                        }
                    }
                }
            } catch (NacosException e) {
                log.error("选择服务实例失败，serviceName:{}", serviceName, e);
            }
        }
    }

    private static String getRouteRequestUrl(Instance instance) {
        String query = buildQuery(SECRET);
        return "http://" + instance.getIp() + ":" + instance.getPort() + "/sop/routes" + query;
    }

    private static String buildQuery(String secret) {
        String time = String.valueOf(System.currentTimeMillis());
        String source = secret + time + secret;
        String sign = DigestUtils.md5DigestAsHex(source.getBytes());
        return "?time=" + time + "&sign=" + sign;
    }

}