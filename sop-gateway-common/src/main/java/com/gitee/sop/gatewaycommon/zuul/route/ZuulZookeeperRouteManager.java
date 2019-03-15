package com.gitee.sop.gatewaycommon.zuul.route;

import com.gitee.sop.gatewaycommon.manager.BaseRouteManager;
import com.gitee.sop.gatewaycommon.manager.RouteRepository;
import com.gitee.sop.gatewaycommon.util.RoutePathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.core.env.Environment;

/**
 * 路由内容管理，新增活修改路由
 *
 * @author tanghc
 */
@Slf4j
public class ZuulZookeeperRouteManager extends BaseRouteManager<ZuulServiceRouteInfo, ZuulRouteDefinition, ZuulTargetRoute> {

    public ZuulZookeeperRouteManager(Environment environment, RouteRepository<ZuulTargetRoute> routeRepository) {
        super(environment, routeRepository);
    }

    @Override
    protected Class<ZuulServiceRouteInfo> getServiceRouteInfoClass() {
        return ZuulServiceRouteInfo.class;
    }

    @Override
    protected Class<ZuulRouteDefinition> getRouteDefinitionClass() {
        return ZuulRouteDefinition.class;
    }

    @Override
    protected ZuulTargetRoute buildRouteDefinition(ZuulServiceRouteInfo serviceRouteInfo, ZuulRouteDefinition routeDefinition) {
        Route route = new Route(routeDefinition.getId(), RoutePathUtil.findPath(routeDefinition.getUri()), serviceRouteInfo.getServiceId(), null, false, null);
        return new ZuulTargetRoute(serviceRouteInfo, routeDefinition, route);
    }
}
