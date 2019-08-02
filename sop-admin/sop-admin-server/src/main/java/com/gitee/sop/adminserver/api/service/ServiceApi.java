package com.gitee.sop.adminserver.api.service;

import com.alibaba.fastjson.JSON;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;
import com.gitee.sop.adminserver.api.service.param.ServiceAddParam;
import com.gitee.sop.adminserver.api.service.param.ServiceInstanceGrayParam;
import com.gitee.sop.adminserver.api.service.param.ServiceInstanceParam;
import com.gitee.sop.adminserver.api.service.param.ServiceSearchParam;
import com.gitee.sop.adminserver.api.service.result.RouteServiceInfo;
import com.gitee.sop.adminserver.api.service.result.ServiceInfoVo;
import com.gitee.sop.adminserver.api.service.result.ServiceInstanceVO;
import com.gitee.sop.adminserver.bean.ChannelMsg;
import com.gitee.sop.adminserver.bean.MetadataEnum;
import com.gitee.sop.adminserver.bean.ServiceRouteInfo;
import com.gitee.sop.adminserver.bean.UserKeyDefinition;
import com.gitee.sop.adminserver.bean.ZookeeperContext;
import com.gitee.sop.adminserver.common.BizException;
import com.gitee.sop.adminserver.common.ChannelOperation;
import com.gitee.sop.adminserver.common.ZookeeperPathExistException;
import com.gitee.sop.adminserver.common.ZookeeperPathNotExistException;
import com.gitee.sop.adminserver.entity.ConfigGrayUserkey;
import com.gitee.sop.adminserver.mapper.ConfigGrayUserkeyMapper;
import com.gitee.sop.registryapi.bean.ServiceInfo;
import com.gitee.sop.registryapi.bean.ServiceInstance;
import com.gitee.sop.registryapi.service.RegistryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@ApiService
@ApiDoc("服务管理-服务列表")
@Slf4j
public class ServiceApi {

    @Autowired
    private RegistryService registryService;

    @Autowired
    private ConfigGrayUserkeyMapper configGrayUserkeyMapper;

    @Api(name = "zookeeper.service.list")
    @ApiDocMethod(description = "zk中的服务列表", elementClass = RouteServiceInfo.class)
    List<RouteServiceInfo> listServiceInfo(ServiceSearchParam param) {
        String routeRootPath = ZookeeperContext.getSopRouteRootPath();
        List<ChildData> childDataList = ZookeeperContext.getChildrenData(routeRootPath);
        List<RouteServiceInfo> serviceInfoList = childDataList.stream()
                .filter(childData -> childData.getData() != null && childData.getData().length > 0)
                .map(childData -> {
                    String serviceNodeData = new String(childData.getData());
                    RouteServiceInfo serviceInfo = JSON.parseObject(serviceNodeData, RouteServiceInfo.class);
                    return serviceInfo;
                })
                .filter(serviceInfo -> {
                    if (StringUtils.isBlank(param.getServiceId())) {
                        return true;
                    } else {
                        return serviceInfo.getServiceId().contains(param.getServiceId());
                    }
                })
                .collect(Collectors.toList());

        return serviceInfoList;
    }

    @Api(name = "service.custom.add")
    @ApiDocMethod(description = "添加服务")
    void addService(ServiceAddParam param) {
        String serviceId = param.getServiceId();
        String servicePath = ZookeeperContext.buildServiceIdPath(serviceId);
        ServiceRouteInfo serviceRouteInfo = new ServiceRouteInfo();
        Date now = new Date();
        serviceRouteInfo.setServiceId(serviceId);
        serviceRouteInfo.setDescription("自定义服务");
        serviceRouteInfo.setCreateTime(now);
        serviceRouteInfo.setUpdateTime(now);
        serviceRouteInfo.setCustom(BooleanUtils.toInteger(true));
        String serviceData = JSON.toJSONString(serviceRouteInfo);
        try {
            ZookeeperContext.addPath(servicePath, serviceData);
        } catch (ZookeeperPathExistException e) {
            throw new BizException("服务已存在");
        }
    }

    @Api(name = "service.custom.del")
    @ApiDocMethod(description = "删除自定义服务")
    void delService(ServiceSearchParam param) {
        String serviceId = param.getServiceId();
        String servicePath = ZookeeperContext.buildServiceIdPath(serviceId);
        String data = null;
        try {
            data = ZookeeperContext.getData(servicePath);
        } catch (ZookeeperPathNotExistException e) {
            throw new BizException("服务不存在");
        }
        if (StringUtils.isBlank(data)) {
            throw new BizException("非自定义服务，无法删除");
        }
        ServiceRouteInfo serviceRouteInfo = JSON.parseObject(data, ServiceRouteInfo.class);
        int custom = serviceRouteInfo.getCustom();
        if (!BooleanUtils.toBoolean(custom)) {
            throw new BizException("非自定义服务，无法删除");
        }
        ZookeeperContext.deletePathDeep(servicePath);
    }

    @Api(name = "service.instance.list")
    @ApiDocMethod(description = "获取注册中心的服务列表", elementClass = ServiceInfoVo.class)
    List<ServiceInstanceVO> listService(ServiceSearchParam param) {
        List<ServiceInfo> serviceInfos;
        try {
            serviceInfos = registryService.listAllService(1, 99999);
        } catch (Exception e) {
            log.error("获取服务实例失败", e);
            return Collections.emptyList();
        }
        List<ServiceInstanceVO> serviceInfoVoList = new ArrayList<>();
        AtomicInteger idGen = new AtomicInteger(1);
        serviceInfos.stream()
                .filter(serviceInfo -> {
                    if (StringUtils.isBlank(param.getServiceId())) {
                        return true;
                    }
                    return StringUtils.containsIgnoreCase(serviceInfo.getServiceId(), param.getServiceId());
                })
                .forEach(serviceInfo -> {
                    int pid = idGen.getAndIncrement();
                    String serviceId = serviceInfo.getServiceId();
                    ServiceInstanceVO parent = new ServiceInstanceVO();
                    parent.setId(pid);
                    parent.setServiceId(serviceId);
                    parent.setParentId(0);
                    serviceInfoVoList.add(parent);
                    List<ServiceInstance> instanceList = serviceInfo.getInstances();
                    for (ServiceInstance instance : instanceList) {
                        ServiceInstanceVO instanceVO = new ServiceInstanceVO();
                        BeanUtils.copyProperties(instance, instanceVO);
                        int id = idGen.getAndIncrement();
                        instanceVO.setId(id);
                        instanceVO.setParentId(pid);
                        if (instanceVO.getMetadata() == null) {
                            instanceVO.setMetadata(Collections.emptyMap());
                        }
                        serviceInfoVoList.add(instanceVO);
                    }
                });

        return serviceInfoVoList;
    }

    @Api(name = "service.instance.offline")
    @ApiDocMethod(description = "服务禁用")
    void serviceOffline(ServiceInstanceParam param) {
        try {
            registryService.offlineInstance(param.buildServiceInstance());
        } catch (Exception e) {
            log.error("服务禁用失败，param:{}", param, e);
            throw new BizException("服务禁用失败，请查看日志");
        }
    }

    @Api(name = "service.instance.online")
    @ApiDocMethod(description = "服务启用")
    void serviceOnline(ServiceInstanceParam param) throws IOException {
        try {
            registryService.onlineInstance(param.buildServiceInstance());
        } catch (Exception e) {
            log.error("服务启用失败，param:{}", param, e);
            throw new BizException("服务启用失败，请查看日志");
        }
    }

    @Api(name = "service.instance.env.pre")
    @ApiDocMethod(description = "预发布")
    void serviceEnvPre(ServiceInstanceParam param) throws IOException {
        try {
            MetadataEnum envPre = MetadataEnum.ENV_PRE;
            registryService.setMetadata(param.buildServiceInstance(), envPre.getKey(), envPre.getValue());
        } catch (Exception e) {
            log.error("预发布失败，param:{}", param, e);
            throw new BizException("预发布失败，请查看日志");
        }
    }

    @Api(name = "service.instance.env.gray")
    @ApiDocMethod(description = "灰度发布")
    void serviceEnvGray(ServiceInstanceGrayParam param) throws IOException {
        try {
            Boolean onlyUpdateGrayUserkey = param.getOnlyUpdateGrayUserkey();
            if (onlyUpdateGrayUserkey == null || !onlyUpdateGrayUserkey) {
                MetadataEnum envPre = MetadataEnum.ENV_GRAY;
                registryService.setMetadata(param.buildServiceInstance(), envPre.getKey(), envPre.getValue());
            }

            String instanceId = param.getInstanceId();
            String userKeyContent = param.getUserKeyContent();
            String nameVersionContent = param.getNameVersionContent();

            ConfigGrayUserkey configGrayUserkey = configGrayUserkeyMapper.getByColumn("instance_id", instanceId);
            if (configGrayUserkey == null) {
                configGrayUserkey = new ConfigGrayUserkey();
                configGrayUserkey.setInstanceId(instanceId);
                configGrayUserkey.setUserKeyContent(userKeyContent);
                configGrayUserkey.setNameVersionContent(nameVersionContent);
                configGrayUserkeyMapper.save(configGrayUserkey);
            } else {
                configGrayUserkey.setUserKeyContent(userKeyContent);
                configGrayUserkey.setNameVersionContent(nameVersionContent);
                configGrayUserkeyMapper.update(configGrayUserkey);
            }
            this.sendUserKeyMsg(instanceId, ChannelOperation.GRAY_USER_KEY_SET);
        } catch (Exception e) {
            log.error("灰度发布失败，param:{}", param, e);
            throw new BizException("灰度发布失败，请查看日志");
        }
    }

    @Api(name = "service.instance.env.online")
    @ApiDocMethod(description = "上线")
    void serviceEnvOnline(ServiceInstance param) throws IOException {
        try {
            MetadataEnum envPre = MetadataEnum.ENV_ONLINE;
            registryService.setMetadata(param, envPre.getKey(), envPre.getValue());
            this.sendUserKeyMsg(param.getServiceId(), ChannelOperation.GRAY_USER_KEY_CLEAR);
        } catch (Exception e) {
            log.error("上线失败，param:{}", param, e);
            throw new BizException("上线失败，请查看日志");
        }
    }

    @Api(name = "service.instance.gray.userkey.get")
    ConfigGrayUserkey getGrayUserkey(ServiceSearchParam param) {
        return configGrayUserkeyMapper.getByColumn("instance_id", param.getInstanceId());
    }

    private void sendUserKeyMsg(String serviceId, ChannelOperation channelOperation) {
        UserKeyDefinition userKeyDefinition = new UserKeyDefinition();
        userKeyDefinition.setServiceId(serviceId);
        ChannelMsg channelMsg = new ChannelMsg(channelOperation, userKeyDefinition);
        String jsonData = JSON.toJSONString(channelMsg);
        String path = ZookeeperContext.getUserKeyChannelPath();
        log.info("消息推送--灰度发布({}), path:{}, data:{}",channelOperation.getOperation(), path, jsonData);
        ZookeeperContext.createOrUpdateData(path, jsonData);
    }

}
