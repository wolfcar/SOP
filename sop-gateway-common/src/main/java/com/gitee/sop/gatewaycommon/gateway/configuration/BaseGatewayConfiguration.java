package com.gitee.sop.gatewaycommon.gateway.configuration;

import com.gitee.sop.gatewaycommon.bean.ApiContext;
import com.gitee.sop.gatewaycommon.gateway.filter.GatewayModifyResponseGatewayFilter;
import com.gitee.sop.gatewaycommon.gateway.filter.LoadBalancerClientExtFilter;
import com.gitee.sop.gatewaycommon.gateway.filter.ValidateFilter;
import com.gitee.sop.gatewaycommon.gateway.handler.GatewayExceptionHandler;
import com.gitee.sop.gatewaycommon.gateway.route.GatewayRouteRepository;
import com.gitee.sop.gatewaycommon.gateway.route.NameVersionRoutePredicateFactory;
import com.gitee.sop.gatewaycommon.gateway.route.ReadBodyRoutePredicateFactory;
import com.gitee.sop.gatewaycommon.gateway.route.GatewayZookeeperRouteManager;
import com.gitee.sop.gatewaycommon.manager.RouteRepositoryContext;
import com.gitee.sop.gatewaycommon.message.ErrorFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;


/**
 * @author tanghc
 */
public class BaseGatewayConfiguration {

    @Autowired
    protected Environment environment;

    @Autowired
    protected GatewayZookeeperRouteManager gatewayZookeeperApiMetaManager;

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     *
     * @param viewResolversProvider
     * @param serverCodecConfigurer
     * @return
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {

        GatewayExceptionHandler jsonExceptionHandler = new GatewayExceptionHandler();
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return jsonExceptionHandler;
    }

    /**
     * 处理返回结果
     * @return
     */
    @Bean
    GatewayModifyResponseGatewayFilter gatewayModifyResponseGatewayFilter() {
        return new GatewayModifyResponseGatewayFilter();
    }

    /**
     * 读取post请求参数
     * @return
     */
    @Bean
    ReadBodyRoutePredicateFactory readBodyRoutePredicateFactory() {
        return new ReadBodyRoutePredicateFactory();
    }

    @Bean
    NameVersionRoutePredicateFactory paramRoutePredicateFactory() {
        return new NameVersionRoutePredicateFactory();
    }

    @Bean
    ValidateFilter validateFilter() {
        return new ValidateFilter();
    }

    @Bean
    LoadBalancerClientExtFilter loadBalancerClientExtFilter() {
        return new LoadBalancerClientExtFilter();
    }

    @Bean
    GatewayZookeeperRouteManager gatewayZookeeperRouteManager(Environment environment, GatewayRouteRepository gatewayRouteManager) {
        return new GatewayZookeeperRouteManager(environment, gatewayRouteManager);
    }

    @Bean
    GatewayRouteRepository gatewayRouteRepository() {
        GatewayRouteRepository gatewayRouteRepository = new GatewayRouteRepository();
        RouteRepositoryContext.setRouteRepository(gatewayRouteRepository);
        return gatewayRouteRepository;
    }


    @PostConstruct
    public void after() {
        doAfter();
    }

    protected void doAfter() {
        initMessage();
        gatewayZookeeperApiMetaManager.refresh();
    }

    protected void initMessage() {
        ErrorFactory.initMessageSource(ApiContext.getApiConfig().getI18nModules());
    }

}
