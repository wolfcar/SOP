package com.gitee.sop.gatewaycommon.zuul.route;

import com.gitee.sop.gatewaycommon.bean.AbstractTargetRoute;
import lombok.Getter;
import org.springframework.cloud.netflix.zuul.filters.Route;

/**
 * @author tanghc
 */
@Getter
public class ZuulTargetRoute extends AbstractTargetRoute<ZuulServiceRouteInfo, ZuulRouteDefinition, Route> {

    public ZuulTargetRoute(ZuulServiceRouteInfo baseServiceRouteInfo, ZuulRouteDefinition baseRouteDefinition, Route targetRoute) {
        super(baseServiceRouteInfo, baseRouteDefinition, targetRoute);
    }
}
