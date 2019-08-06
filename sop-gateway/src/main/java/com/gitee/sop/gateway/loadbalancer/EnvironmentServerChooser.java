package com.gitee.sop.gateway.loadbalancer;

import com.gitee.sop.gateway.manager.DbEnvGrayManager;
import com.gitee.sop.gatewaycommon.bean.SpringContext;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.param.Param;
import com.gitee.sop.gatewaycommon.zuul.ZuulContext;
import com.gitee.sop.gatewaycommon.zuul.loadbalancer.BaseServerChooser;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 预发布、灰度环境选择，参考自：https://segmentfault.com/a/1190000017412946
 *
 * @author tanghc
 */
public class EnvironmentServerChooser extends BaseServerChooser {

    private static final String MEDATA_KEY_ENV = "env";
    private static final String ENV_PRE_VALUE = "pre";
    private static final String ENV_GRAY_VALUE = "gray";

    /**
     * 预发布机器域名
     */
    private static final String PRE_DOMAIN = "localhost";

    @Override
    protected boolean isPreServer(Server server) {
        String env = getEnvValue(server);
        return ENV_PRE_VALUE.equals(env);
    }

    @Override
    protected boolean isGrayServer(Server server) {
        String env = getEnvValue(server);
        return ENV_GRAY_VALUE.equals(env);
    }

    private String getEnvValue(Server server) {
        // eureka存储的metadata
        Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
        return metadata.get(MEDATA_KEY_ENV);
    }


    /**
     * 通过判断hostname来确定是否是预发布请求，可修改此方法实现自己想要的
     *
     * @param request request
     * @return 返回true：可以进入到预发环境
     */
    @Override
    protected boolean canVisitPre(Server server, HttpServletRequest request) {
        String serverName = request.getServerName();
        return PRE_DOMAIN.equals(serverName);
    }

    /**
     * 能否进入灰度环境，可修改此方法实现自己想要的
     *
     * @param request request
     * @return 返回true：可以进入到预发环境
     */
    protected boolean canVisitGray(Server server, HttpServletRequest request) {
        ApiParam apiParam = ZuulContext.getApiParam();
        DbEnvGrayManager userKeyManager = SpringContext.getBean(DbEnvGrayManager.class);
        boolean canVisit = false;
        /*if (this.isGrayUser(apiParam, userKeyManager, server, request)) {
            // 指定灰度版本号
            String instanceId = server.getMetaInfo().getInstanceId();
            String newVersion = userKeyManager.getVersion(instanceId, apiParam.fetchNameVersion());
            if (newVersion != null) {
                // 在header中设置新的版本号，然后微服务端先获取这个新版本号
                RequestContext.getCurrentContext().addZuulRequestHeader(ParamNames.HEADER_VERSION_NAME, newVersion);
                canVisit = true;
            }
        }*/
        return this.isGrayUser(apiParam, userKeyManager, server, request);
    }


    /**
     * 是否是灰度用户
     *
     * @param param          接口参数
     * @param userKeyManager userKey管理
     * @param server         服务器实例
     * @param request        request
     * @return true：是
     */
    protected boolean isGrayUser(Param param, DbEnvGrayManager userKeyManager, Server server, HttpServletRequest request) {
        String instanceId = server.getMetaInfo().getInstanceId();
        // 这里的灰度用户为appKey，包含此appKey则为灰度用户，允许访问
        String appKey = param.fetchAppKey();
        return userKeyManager.containsKey(instanceId, appKey);
    }
}
