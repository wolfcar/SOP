package com.gitee.sop.gatewaycommon.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@Controller
public class FilterPrintController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("sop/listGlobalFilters")
    public Mono<ResponseEntity<String>> listGlobalFilters(ServerWebExchange exchange) {
        Map<String, GlobalFilter> filterMap = applicationContext.getBeansOfType(GlobalFilter.class);
        List<String> filters = filterMap.values()
                .stream()
                .sorted(new Comparator<GlobalFilter>() {
                    @Override
                    public int compare(GlobalFilter o1, GlobalFilter o2) {
                        if (o1 instanceof Ordered && o2 instanceof Ordered) {
                            Ordered order1 = (Ordered) o1;
                            Ordered order2 = (Ordered) o2;
                            return Integer.compare(order1.getOrder(), order2.getOrder());
                        }
                        return 0;
                    }
                })
                .map(globalFilter -> {
                    int order = 0;
                    if (globalFilter instanceof Ordered) {
                        Ordered ordered = (Ordered) globalFilter;
                        order = ordered.getOrder();
                    }
                    return order + ", " + globalFilter.getClass().getSimpleName();
                })
                .collect(Collectors.toList());

        String result = String.join("<br>", filters);
        return Mono.just(ResponseEntity.ok(result));
    }


}
