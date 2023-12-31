package com.gitee.sop.websiteserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.websiteserver.bean.EurekaApplication;
import com.gitee.sop.websiteserver.bean.EurekaApps;
import com.gitee.sop.websiteserver.bean.EurekaInstance;
import com.gitee.sop.websiteserver.bean.EurekaUri;
import com.gitee.sop.websiteserver.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tanghc
 */
@Slf4j
public class EurekaServerService implements ServerService  {

    private OkHttpClient client = new OkHttpClient();

    @Value("${eureka.client.serviceUrl.defaultZone:}")
    private String eurekaUrls;

    @Override
    public List<String> listServerHost(String serviceId) {
        if (StringUtils.isBlank(eurekaUrls)) {
            throw new IllegalArgumentException("未指定eureka.client.serviceUrl.defaultZone参数");
        }
        Objects.requireNonNull(serviceId, "serviceId can not be null");
        String json = null;
        try {
            json = this.requestEurekaServer(EurekaUri.QUERY_APPS);
        } catch (IOException e) {
            return Collections.emptyList();
        }
        EurekaApps eurekaApps = JSON.parseObject(json, EurekaApps.class);

        List<EurekaApplication> applicationList = eurekaApps.getApplications().getApplication();
        for (EurekaApplication eurekaApplication : applicationList) {
            if (!serviceId.equalsIgnoreCase(eurekaApplication.getName())) {
                continue;
            }
            List<EurekaInstance> instanceList = eurekaApplication.getInstance();
            return instanceList.stream()
                    .map(eurekaInstance -> eurekaInstance.getIpAddr() + ":" + eurekaInstance.getPort())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private String requestEurekaServer(EurekaUri eurekaUri, String... args) throws IOException {
        Request request = eurekaUri.getRequest(this.getFirstDefaultZoneServiceUrl(), args);
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            log.error("操作失败，url:{}, msg:{}, code:{}", eurekaUri.getUri(args), response.message(), response.code());
            throw new RuntimeException("操作失败");
        }
    }

    private List<String> getDefaultZoneServiceUrls() {
        if (!StringUtils.isEmpty(eurekaUrls)) {
            return Stream.of(org.springframework.util.StringUtils.commaDelimitedListToStringArray(eurekaUrls))
                    .map(url -> !url.endsWith("/") ? (url += "/").trim() : url.trim())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private String getFirstDefaultZoneServiceUrl() {
        List<String> serviceUrls = getDefaultZoneServiceUrls();
        if (CollectionUtils.isEmpty(serviceUrls)) {
            throw new IllegalArgumentException("未指定eureka.client.serviceUrl.defaultZone参数");
        }
        return serviceUrls.get(0);
    }
}
