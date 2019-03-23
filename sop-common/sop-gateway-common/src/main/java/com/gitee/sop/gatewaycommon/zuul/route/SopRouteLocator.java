package com.gitee.sop.gatewaycommon.zuul.route;

import com.gitee.sop.gatewaycommon.message.ErrorEnum;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.zuul.ZuulContext;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.core.Ordered;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由定位
 * @author tanghc
 */
public class SopRouteLocator implements RouteLocator, Ordered {

    private ZuulRouteRepository zuulRouteRepository;

    public SopRouteLocator(ZuulRouteRepository zuulRouteRepository) {
        this.zuulRouteRepository = zuulRouteRepository;
    }

    @Override
    public Collection<String> getIgnoredPaths() {
        return Collections.emptyList();
    }

    @Override
    public List<Route> getRoutes() {
        return zuulRouteRepository.listAll()
                .parallelStream()
                .map(zuulTargetRoute -> zuulTargetRoute.getTargetRouteDefinition())
                .collect(Collectors.toList());
    }

    /**
     * 这里决定使用哪个路由
     * @param path
     * @return
     */
    @Override
    public Route getMatchingRoute(String path) {
        ApiParam param = ZuulContext.getApiParam();
        String nameVersion = param.fetchNameVersion();
        ZuulTargetRoute zuulTargetRoute = zuulRouteRepository.get(nameVersion);
        if (zuulTargetRoute == null) {
            return null;
        }
        // 路由是否启用
        if (!zuulTargetRoute.getRouteDefinition().enable()) {
            throw ErrorEnum.ISV_INVALID_METHOD.getErrorMeta().getException();
        }
        return zuulTargetRoute.getTargetRouteDefinition();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}