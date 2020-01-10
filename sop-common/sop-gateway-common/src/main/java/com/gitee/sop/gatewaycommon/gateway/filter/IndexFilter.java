package com.gitee.sop.gatewaycommon.gateway.filter;

import com.gitee.sop.gatewaycommon.bean.SopConstants;
import com.gitee.sop.gatewaycommon.exception.ApiException;
import com.gitee.sop.gatewaycommon.gateway.ServerWebExchangeUtil;
import com.gitee.sop.gatewaycommon.manager.EnvironmentContext;
import com.gitee.sop.gatewaycommon.manager.EnvironmentKeys;
import com.gitee.sop.gatewaycommon.param.ApiParam;
import com.gitee.sop.gatewaycommon.validate.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 入口
 *
 * @author tanghc
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IndexFilter implements WebFilter {

    private static final String INDEX_PATH = "/";
    private static final String REST_PATH_PREFIX = "/rest";
    private static final String SOP_PATH_PREFIX = "/sop";

    @Autowired
    private Validator validator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // SOP路径，直接放行
        if (path.startsWith(SOP_PATH_PREFIX)) {
            return chain.filter(exchange);
        }
        // 如果是restful请求，直接转发
        if (path.startsWith(REST_PATH_PREFIX)) {
            String sopRestfulEnableValue = EnvironmentKeys.SOP_RESTFUL_ENABLE.getValue();
            if (!Objects.equals("true", sopRestfulEnableValue)) {
                log.error("尝试调用restful请求，但sop.restful.enable未开启");
                return ServerWebExchangeUtil.forwardUnknown(exchange, chain);
            }
            ServerWebExchange newExchange = ServerWebExchangeUtil.getRestfulExchange(exchange, path);
            return chain.filter(newExchange);
        }
        if (Objects.equals(path, INDEX_PATH)) {
            if (request.getMethod() == HttpMethod.POST) {
                ServerRequest serverRequest = ServerWebExchangeUtil.createReadBodyRequest(exchange);
                // 读取请求体中的内容
                Mono<?> modifiedBody = serverRequest.bodyToMono(String.class)
                        .flatMap(body -> {
                            // 构建ApiParam
                            ApiParam apiParam = ServerWebExchangeUtil.getApiParam(exchange, body);
                            // 签名验证
                            doValidate(apiParam);
                            return Mono.just(body);
                        });
                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, (Class) String.class);
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());

                // the new content type will be computed by bodyInserter
                // and then set in the request decorator
                headers.remove(HttpHeaders.CONTENT_LENGTH);

                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                        exchange, headers);
                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                            ServerWebExchange newExchange = exchange.mutate().request(decorator).build();
                            return chain.filter(newExchange);
                        }));

            } else {
                URI uri = exchange.getRequest().getURI();
                // 原始参数
                String originalQuery = uri.getRawQuery();
                // 构建ApiParam
                ApiParam apiParam = ServerWebExchangeUtil.getApiParam(exchange, originalQuery);
                // 签名验证
                doValidate(apiParam);
                ServerWebExchange newExchange = ServerWebExchangeUtil.getForwardExchange(exchange, apiParam);
                return chain.filter(newExchange);
            }
        } else {
            return ServerWebExchangeUtil.forwardUnknown(exchange, chain);
        }
    }

    private void doValidate(ApiParam apiParam) {
        try {
            validator.validate(apiParam);
        } catch (ApiException e) {
            log.error("验证失败，ip:{}, params:{}, errorMsg:{}", apiParam.fetchIp(), apiParam.toJSONString(), e.getMessage());
            apiParam.setThrowable(e);
        }
    }

    private ServerHttpRequestDecorator decorate(
            ServerWebExchange exchange
            , HttpHeaders headers
            , CachedBodyOutputMessage outputMessage
    ) {
        ApiParam apiParam = ServerWebExchangeUtil.getApiParam(exchange);
        ServerHttpRequest newRequest = ServerWebExchangeUtil.getForwardRequest(exchange.getRequest(), apiParam);
        return new ServerHttpRequestDecorator(newRequest) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                httpHeaders.set(SopConstants.REDIRECT_VERSION_KEY, apiParam.fetchVersion());
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}