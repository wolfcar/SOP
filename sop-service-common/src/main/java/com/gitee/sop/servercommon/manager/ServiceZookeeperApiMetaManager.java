package com.gitee.sop.servercommon.manager;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.servercommon.bean.ServiceApiInfo;
import com.gitee.sop.servercommon.route.GatewayPredicateDefinition;
import com.gitee.sop.servercommon.route.GatewayRouteDefinition;
import com.gitee.sop.servercommon.route.ServiceRouteInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 上传路由到zookeeper
 * @author tanghc
 */
@Slf4j
@Getter
@Setter
public class ServiceZookeeperApiMetaManager implements ApiMetaManager {

    /**
     * zookeeper存放接口路由信息的根目录
     */
    public static final String SOP_SERVICE_ROUTE_PATH = "/sop-service-route";
    public static final String PATH_START_CHAR = "/";

    /**
     * NameVersion=alipay.story.get1.0
     * see com.gitee.sop.gatewaycommon.routeDefinition.NameVersionRoutePredicateFactory
     */
    private static String QUERY_PREDICATE_DEFINITION_TPL = "NameVersion=%s";

    private static ServiceApiInfo.ApiMeta FIRST_API_META = new ServiceApiInfo.ApiMeta("_first.route_", "/", "v_000");

    private Environment environment;

    public ServiceZookeeperApiMetaManager(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void uploadApi(ServiceApiInfo serviceApiInfo) {
        ServiceRouteInfo serviceRouteInfo = this.buildServiceGatewayInfo(serviceApiInfo);
        this.uploadServiceRouteInfoToZookeeper(serviceRouteInfo);
    }

    /**
     * 构建接口信息，符合spring cloud gateway的格式
     *
     * @param serviceApiInfo
     * @return
     */
    protected ServiceRouteInfo buildServiceGatewayInfo(ServiceApiInfo serviceApiInfo) {
        List<ServiceApiInfo.ApiMeta> apis = serviceApiInfo.getApis();
        List<GatewayRouteDefinition> routeDefinitionList = new ArrayList<>(apis.size() + 1);
        routeDefinitionList.add(this.buildReadBodyRouteDefinition(serviceApiInfo));
        for (ServiceApiInfo.ApiMeta apiMeta : apis) {
            GatewayRouteDefinition gatewayRouteDefinition = this.buildGatewayRouteDefinition(serviceApiInfo, apiMeta);
            routeDefinitionList.add(gatewayRouteDefinition);
        }
        ServiceRouteInfo serviceRouteInfo = new ServiceRouteInfo();
        serviceRouteInfo.setServiceId(serviceApiInfo.getServiceId());
        serviceRouteInfo.setRouteDefinitionList(routeDefinitionList);
        return serviceRouteInfo;
    }

    /**
     * 添加com.gitee.sop.gatewaycommon.routeDefinition.ReadBodyRoutePredicateFactory,解决form表单获取不到问题
     *
     * @return
     */
    protected GatewayRouteDefinition buildReadBodyRouteDefinition(ServiceApiInfo serviceApiInfo) {
        GatewayRouteDefinition readBodyRouteDefinition = this.buildGatewayRouteDefinition(serviceApiInfo, FIRST_API_META);
        readBodyRouteDefinition.setOrder(Integer.MIN_VALUE);

        GatewayPredicateDefinition gatewayPredicateDefinition = new GatewayPredicateDefinition();
        gatewayPredicateDefinition.setName("ReadBody");
        GatewayPredicateDefinition readerBodyPredicateDefinition = this.buildNameVersionPredicateDefinition(FIRST_API_META);
        List<GatewayPredicateDefinition> predicates = Arrays.asList(gatewayPredicateDefinition, readerBodyPredicateDefinition);
        readBodyRouteDefinition.setPredicates(predicates);

        return readBodyRouteDefinition;
    }

    protected GatewayRouteDefinition buildGatewayRouteDefinition(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        GatewayRouteDefinition gatewayRouteDefinition = new GatewayRouteDefinition();
        // 唯一id规则：接口名 + 版本号
        gatewayRouteDefinition.setId(apiMeta.fetchNameVersion());
        gatewayRouteDefinition.setFilters(Collections.emptyList());
        gatewayRouteDefinition.setOrder(0);
        List<GatewayPredicateDefinition> predicates = Arrays.asList(this.buildNameVersionPredicateDefinition(apiMeta));
        gatewayRouteDefinition.setPredicates(predicates);
        // lb://story-service#/alipay.story.get/
        String uri = this.buildUri(serviceApiInfo, apiMeta);
        gatewayRouteDefinition.setUri(uri);
        gatewayRouteDefinition.setIgnoreValidate(apiMeta.isIgnoreValidate());
        return gatewayRouteDefinition;
    }

    protected String buildUri(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        String servletPath = getServletPath(serviceApiInfo, apiMeta);
        if (servletPath == null) {
            servletPath = PATH_START_CHAR;
        }
        if (!servletPath.startsWith(PATH_START_CHAR)) {
            servletPath = PATH_START_CHAR + servletPath;
        }
        return "lb://" + serviceApiInfo.getServiceId() + "#" + servletPath;
    }

    protected String getServletPath(ServiceApiInfo serviceApiInfo, ServiceApiInfo.ApiMeta apiMeta) {
        return apiMeta.getPath();
    }

    protected GatewayPredicateDefinition buildNameVersionPredicateDefinition(ServiceApiInfo.ApiMeta apiMeta) {
        return new GatewayPredicateDefinition(String.format(QUERY_PREDICATE_DEFINITION_TPL, apiMeta.fetchNameVersion()));
    }

    /**
     * 上传接口信息到zookeeper
     *
     * @param serviceRouteInfo
     */
    protected void uploadServiceRouteInfoToZookeeper(ServiceRouteInfo serviceRouteInfo) {
        String zookeeperServerAddr = environment.getProperty("spring.cloud.zookeeper.connect-string");
        if (StringUtils.isEmpty(zookeeperServerAddr)) {
            throw new RuntimeException("未指定spring.cloud.zookeeper.connect-string参数");
        }
        CuratorFramework client = null;
        try {
            // 保存路径
            String savePath = SOP_SERVICE_ROUTE_PATH + "/" + serviceRouteInfo.getServiceId();

            client = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperServerAddr)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();

            client.start();

            log.info("上传接口信息到zookeeper，path:{}, serviceId：{}, 接口数量：{}",
                    savePath,
                    serviceRouteInfo.getServiceId(),
                    serviceRouteInfo.getRouteDefinitionList().size());

            String parentPath = this.uploadFolder(client, serviceRouteInfo);
            this.uploadRouteItems(client, serviceRouteInfo, parentPath);
        } catch (Exception e) {
            log.error("更新接口信息到zookeeper失败, serviceId:{}", serviceRouteInfo.getServiceId(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * 上传文件夹内容
     * @param client
     * @param serviceRouteInfo
     * @return 返回文件夹路径
     */
    protected String uploadFolder(CuratorFramework client, ServiceRouteInfo serviceRouteInfo) throws Exception {
        // 保存路径
        String savePath = SOP_SERVICE_ROUTE_PATH + "/" + serviceRouteInfo.getServiceId();
        String serviceRouteInfoJson = JSON.toJSONString(serviceRouteInfo);
        this.saveNode(client, savePath, serviceRouteInfoJson.getBytes());
        return savePath;
    }

    /**
     * 上传路由信息
     * @param client
     * @param serviceRouteInfo
     * @throws Exception
     */
    protected void uploadRouteItems(CuratorFramework client, ServiceRouteInfo serviceRouteInfo, String parentPath) throws Exception {
        List<GatewayRouteDefinition> routeDefinitionList = serviceRouteInfo.getRouteDefinitionList();
        for (GatewayRouteDefinition routeDefinition : routeDefinitionList) {
            // 父目录/子目录
            String savePath = parentPath + PATH_START_CHAR + routeDefinition.getId();
            String routeDefinitionJson = JSON.toJSONString(routeDefinition);
            this.saveNode(client, savePath, routeDefinitionJson.getBytes());
        }
    }

    protected void saveNode(CuratorFramework client, String path, byte[] data) throws Exception {
        client.create()
                // 如果节点存在则Curator将会使用给出的数据设置这个节点的值
                .orSetData()
                // 如果指定节点的父节点不存在，则Curator将会自动级联创建父节点
                .creatingParentContainersIfNeeded()
                .forPath(path, data);
    }

}
