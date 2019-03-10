package com.gitee.sop.gatewaycommon.manager;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.gatewaycommon.bean.BaseRouteDefinition;
import com.gitee.sop.gatewaycommon.bean.BaseServiceRouteInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.gitee.sop.gatewaycommon.bean.SopConstants.SOP_SERVICE_ROUTE_PATH;


/**
 * 路由管理
 *
 * @param <R> 路由根对象，可以理解为最外面的大json：{....,routeDefinitionList:[]}
 * @param <E> 路由Item对象，对应大json里面的具体路由信息，routeDefinitionList:[]
 * @param <T> 目标路由对象，返回最终的路由对象
 * @author tanghc
 */
@Slf4j
public abstract class BaseRouteManager<R extends BaseServiceRouteInfo<E>, E extends BaseRouteDefinition, T> implements RouteManager {

    protected String sopServiceApiPath = SOP_SERVICE_ROUTE_PATH;

    protected Environment environment;

    protected RouteRepository<R, T> routeRepository;

    protected abstract Class<R> getServiceRouteInfoClass();

    protected abstract T buildRouteDefinition(R serviceRouteInfo, E routeDefinition);

    public BaseRouteManager(Environment environment, RouteRepository<R, T> routeRepository) {
        this.environment = environment;
        this.routeRepository = routeRepository;
    }

    @Override
    public void refresh() {
        log.info("刷新本地接口信息");
        try {
            String zookeeperServerAddr = environment.getProperty("spring.cloud.zookeeper.connect-string");
            if (StringUtils.isEmpty(zookeeperServerAddr)) {
                throw new RuntimeException("未指定spring.cloud.zookeeper.connect-string参数");
            }
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperServerAddr)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();

            client.start();

            client.create()
                    // 如果节点存在则Curator将会使用给出的数据设置这个节点的值
                    .orSetData()
                    // 如果指定节点的父节点不存在，则Curator将会自动级联创建父节点
                    .creatingParentContainersIfNeeded()
                    .forPath(sopServiceApiPath, "".getBytes());

            this.watchChildren(client, sopServiceApiPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void watchChildren(CuratorFramework client, String sopServiceApiPath) throws Exception {
        // 为子节点添加watcher
        // PathChildrenCache: 监听数据节点的增删改，可以设置触发的事件
        final PathChildrenCache childrenCache = new PathChildrenCache(client, sopServiceApiPath, true);

        /**
         * StartMode: 初始化方式
         * POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件
         * NORMAL：异步初始化
         * BUILD_INITIAL_CACHE：同步初始化
         */
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        // 列出子节点数据列表，需要使用BUILD_INITIAL_CACHE同步初始化模式才能获得，异步是获取不到的
        List<ChildData> childDataList = childrenCache.getCurrentData();
        log.info("微服务API详细数据列表：");
        for (ChildData childData : childDataList) {
            String nodeData = new String(childData.getData());
            log.info("\t* 子节点路径：" + childData.getPath() + "，该节点的数据为：" + nodeData);
            R serviceRouteInfo = JSON.parseObject(nodeData, getServiceRouteInfoClass());
            saveRouteDefinitionList(serviceRouteInfo);
        }
        // 添加事件监听器
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                PathChildrenCacheEvent.Type type = event.getType();
                synchronized (type) {
                    // 通过判断event type的方式来实现不同事件的触发
                    if (PathChildrenCacheEvent.Type.CHILD_ADDED.equals(type)) {
                        String nodeData = new String(event.getData().getData());
                        R serviceRouteInfo = JSON.parseObject(nodeData, getServiceRouteInfoClass());
                        // 添加子节点时触发
                        log.info("子节点：{}添加，数据为:{}", event.getData().getPath(), nodeData);
                        saveRouteDefinitionList(serviceRouteInfo);
                    } else if (PathChildrenCacheEvent.Type.CHILD_UPDATED.equals(type)) {
                        String nodeData = new String(event.getData().getData());
                        R serviceRouteInfo = JSON.parseObject(nodeData, getServiceRouteInfoClass());
                        // 修改子节点数据时触发
                        log.info("子节点：{}修改，数据为:{}", event.getData().getPath(), nodeData);
                        // 删除下面所有节点
                        routeRepository.deleteAll(serviceRouteInfo);
                        // 添加新节点
                        saveRouteDefinitionList(serviceRouteInfo);
                    } else if (PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(type)) {
                        String nodeData = new String(event.getData().getData());
                        log.info("子节点：{}删除，数据为:{}", event.getData().getPath(), nodeData);
                        R serviceRouteInfo = JSON.parseObject(nodeData, getServiceRouteInfoClass());
                        deleteRouteDefinitionList(serviceRouteInfo);
                    }
                }
            }
        });
    }

    protected void saveRouteDefinitionList(R serviceRouteInfo) {
        for (E routeDefinitionItem : serviceRouteInfo.getRouteDefinitionList()) {
            RouteDefinitionItemContext.add(routeDefinitionItem);
            T routeDefinition = buildRouteDefinition(serviceRouteInfo, routeDefinitionItem);
            routeRepository.add(serviceRouteInfo, routeDefinition);
        }
    }

    protected void deleteRouteDefinitionList(R serviceRouteInfo) {
        for (E routeDefinitionItem : serviceRouteInfo.getRouteDefinitionList()) {
            RouteDefinitionItemContext.delete(routeDefinitionItem);
        }
        routeRepository.deleteAll(serviceRouteInfo);
    }

}
