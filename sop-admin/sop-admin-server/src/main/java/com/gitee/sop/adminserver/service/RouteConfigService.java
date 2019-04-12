package com.gitee.sop.adminserver.service;

import com.alibaba.fastjson.JSON;
import com.gitee.sop.adminserver.bean.ChannelMsg;
import com.gitee.sop.adminserver.bean.RouteConfigDto;
import com.gitee.sop.adminserver.bean.ZookeeperContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tanghc
 */
@Service
@Slf4j
public class RouteConfigService {

    /**
     * 发送路由配置消息
     * @param routeConfigDto
     * @throws Exception
     */
    public void sendRouteConfigMsg(RouteConfigDto routeConfigDto) throws Exception {
        String configData = JSON.toJSONString(routeConfigDto);
        ChannelMsg channelMsg = new ChannelMsg("update", configData);
        String jsonData = JSON.toJSONString(channelMsg);
        String path = ZookeeperContext.getRouteConfigChannelPath();
        log.info("消息推送--路由配置(update), path:{}, data:{}", path, jsonData);
        ZookeeperContext.createOrUpdateData(path, jsonData);
    }
}