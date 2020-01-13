package com.gitee.sop.gatewaycommon.route;

import com.gitee.sop.gatewaycommon.bean.RouteDefinition;
import com.gitee.sop.gatewaycommon.bean.TargetRoute;
import com.gitee.sop.gatewaycommon.manager.EnvGrayManager;
import com.gitee.sop.gatewaycommon.manager.RouteRepositoryContext;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tanghc
 */
public abstract class BaseForwardChooser<T> implements ForwardChooser<T> {

    @Autowired
    private EnvGrayManager envGrayManager;

    protected abstract ApiParam getApiParam(T t);

    @Override
    public ForwardInfo getForwardInfo(T t) {
        ApiParam apiParam = getApiParam(t);
        ForwardInfo forwardInfo = new ForwardInfo();
        String nameVersion = apiParam.fetchNameVersion();
        TargetRoute targetRoute = RouteRepositoryContext.getRouteRepository().get(nameVersion);
        RouteDefinition routeDefinitionOrig = targetRoute.getRouteDefinition();
        String path = routeDefinitionOrig.getPath();
        String versionInHead = apiParam.fetchVersion();
        String serviceId = targetRoute.getServiceRouteInfo().fetchServiceIdLowerCase();
        // 如果服务在灰度阶段，返回一个灰度版本号
        String grayVersion = envGrayManager.getVersion(serviceId, nameVersion);
        // 如果是灰度环境
        if (grayVersion != null && envGrayManager.containsKey(serviceId, apiParam.fetchAppKey())) {
            String newNameVersion = apiParam.fetchName() + grayVersion;
            TargetRoute targetRouteDest = RouteRepositoryContext.getRouteRepository().get(newNameVersion);
            if (targetRouteDest != null) {
                if (BooleanUtils.toBoolean(routeDefinitionOrig.getCompatibleMode())) {
                    versionInHead = grayVersion;
                } else {
                    // 获取灰度接口
                    RouteDefinition routeDefinition = targetRouteDest.getRouteDefinition();
                    path = routeDefinition.getPath();
                }
            }
        }

        forwardInfo.setPath(path);
        forwardInfo.setVersion(versionInHead);

        return forwardInfo;
    }

}
