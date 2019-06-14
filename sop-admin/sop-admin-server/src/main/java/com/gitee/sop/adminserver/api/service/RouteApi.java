package com.gitee.sop.adminserver.api.service;

import com.alibaba.fastjson.JSON;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;
import com.gitee.easyopen.util.CopyUtil;
import com.gitee.fastmybatis.core.query.Query;
import com.gitee.sop.adminserver.api.isv.result.RoleVO;
import com.gitee.sop.adminserver.api.service.param.RouteAddParam;
import com.gitee.sop.adminserver.api.service.param.RouteDeleteParam;
import com.gitee.sop.adminserver.api.service.param.RoutePermissionParam;
import com.gitee.sop.adminserver.api.service.param.RouteSearchParam;
import com.gitee.sop.adminserver.api.service.param.RouteUpdateParam;
import com.gitee.sop.adminserver.api.service.result.RouteVO;
import com.gitee.sop.adminserver.bean.GatewayRouteDefinition;
import com.gitee.sop.adminserver.bean.RouteConfigDto;
import com.gitee.sop.adminserver.bean.ZookeeperContext;
import com.gitee.sop.adminserver.common.BizException;
import com.gitee.sop.adminserver.common.ZookeeperPathExistException;
import com.gitee.sop.adminserver.common.ZookeeperPathNotExistException;
import com.gitee.sop.adminserver.entity.ConfigRouteBase;
import com.gitee.sop.adminserver.entity.PermRole;
import com.gitee.sop.adminserver.entity.PermRolePermission;
import com.gitee.sop.adminserver.mapper.ConfigRouteBaseMapper;
import com.gitee.sop.adminserver.mapper.PermRoleMapper;
import com.gitee.sop.adminserver.mapper.PermRolePermissionMapper;
import com.gitee.sop.adminserver.service.RouteConfigService;
import com.gitee.sop.adminserver.service.RoutePermissionService;
import com.gitee.sop.adminserver.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@ApiService
@ApiDoc("服务管理-路由管理")
@Slf4j
public class RouteApi {

    @Autowired
    PermRolePermissionMapper permRolePermissionMapper;

    @Autowired
    PermRoleMapper permRoleMapper;

    @Autowired
    ConfigRouteBaseMapper configRouteBaseMapper;

    @Autowired
    RoutePermissionService routePermissionService;

    @Autowired
    RouteConfigService routeConfigService;

    @Autowired
    RouteService routeService;

    @Api(name = "route.list")
    @ApiDocMethod(description = "路由列表")
    List<RouteVO> listRoute(RouteSearchParam param) throws Exception {
        List<RouteVO> routeDefinitionList = routeService.getRouteDefinitionList(param)
                .stream()
                .map(gatewayRouteDefinition -> {
                    RouteVO vo = new RouteVO();
                    BeanUtils.copyProperties(gatewayRouteDefinition, vo);
                    vo.setRoles(this.getRouteRole(gatewayRouteDefinition.getId()));
                    return vo;
                })
                .collect(Collectors.toList());

        return routeDefinitionList;
    }

    @Api(name = "route.list", version = "1.2")
    @ApiDocMethod(description = "路由列表1.2")
    List<RouteVO> listRoute2(RouteSearchParam param) throws Exception {
        List<RouteVO> routeDefinitionList = routeService.getRouteDefinitionList(param)
                .stream()
                .map(gatewayRouteDefinition -> {
                    RouteVO vo = new RouteVO();
                    BeanUtils.copyProperties(gatewayRouteDefinition, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return routeDefinitionList;
    }

    @Api(name = "route.add")
    @ApiDocMethod(description = "新增路由")
    void addRoute(RouteAddParam param) {
        String id = param.getName() + param.getVersion();
        String routePath = ZookeeperContext.buildRoutePath(param.getServiceId(), id);
        GatewayRouteDefinition routeDefinition = new GatewayRouteDefinition();
        CopyUtil.copyPropertiesIgnoreNull(param, routeDefinition);
        routeDefinition.setId(id);
        routeDefinition.setCustom(1);
        try {
            ZookeeperContext.addPath(routePath, JSON.toJSONString(routeDefinition));
        } catch (ZookeeperPathExistException e) {
            throw new BizException("路由已存在");
        }
        this.updateRouteConfig(routeDefinition);
    }

    @Api(name = "route.update")
    @ApiDocMethod(description = "修改路由")
    void updateRoute(RouteUpdateParam param) {
        String routePath = ZookeeperContext.buildRoutePath(param.getServiceId(), param.getId());
        GatewayRouteDefinition routeDefinition = this.getGatewayRouteDefinition(routePath);
        CopyUtil.copyPropertiesIgnoreNull(param, routeDefinition);
        try {
            ZookeeperContext.updatePathData(routePath, JSON.toJSONString(routeDefinition));
        } catch (ZookeeperPathNotExistException e) {
            throw new BizException("路由不存在");
        }
        this.updateRouteConfig(routeDefinition);
    }

    @Api(name = "route.del")
    @ApiDocMethod(description = "删除路由")
    void delRoute(RouteDeleteParam param) {
        String routePath = ZookeeperContext.buildRoutePath(param.getServiceId(), param.getId());
        GatewayRouteDefinition routeDefinition = this.getGatewayRouteDefinition(routePath);
        if (!BooleanUtils.toBoolean(routeDefinition.getCustom())) {
            throw new BizException("非自定义路由，无法删除");
        }
        ZookeeperContext.deletePathDeep(routePath);
    }

    private GatewayRouteDefinition getGatewayRouteDefinition(String zookeeperRoutePath) {
        String data = null;
        try {
            data = ZookeeperContext.getData(zookeeperRoutePath);
        } catch (ZookeeperPathNotExistException e) {
            throw new BizException("路由不存在");
        }
        return JSON.parseObject(data, GatewayRouteDefinition.class);
    }

    private void updateRouteConfig(GatewayRouteDefinition routeDefinition) {
        try {
            String routeId = routeDefinition.getId();
            ConfigRouteBase configRouteBase = configRouteBaseMapper.getByColumn("route_id", routeId);
            boolean doSave = configRouteBase == null;
            if (doSave) {
                configRouteBase = new ConfigRouteBase();
                configRouteBase.setRouteId(routeId);
            }
            configRouteBase.setStatus((byte) routeDefinition.getStatus());

            int i = doSave ? configRouteBaseMapper.save(configRouteBase)
                    : configRouteBaseMapper.update(configRouteBase);

            if (i > 0) {
                this.sendMsg(routeDefinition);
            }
        } catch (Exception e) {
            log.error("发送msg失败", e);
        }
    }

    private void sendMsg(GatewayRouteDefinition routeDefinition) {
        RouteConfigDto routeConfigDto = new RouteConfigDto();
        routeConfigDto.setRouteId(routeDefinition.getId());
        routeConfigDto.setStatus(routeDefinition.getStatus());
        routeConfigService.sendRouteConfigMsg(routeConfigDto);
    }

    @Api(name = "route.role.get")
    @ApiDocMethod(description = "获取路由对应的角色", elementClass = RoleVO.class)
    List<RoleVO> getRouteRole(RouteSearchParam param) {
        if (StringUtils.isBlank(param.getId())) {
            throw new BizException("id不能为空");
        }
        return this.getRouteRole(param.getId());
    }

    private List<RoleVO> getRouteRole(String id) {
        return permRolePermissionMapper.listByColumn("route_id", id)
                .stream()
                .map(permRolePermission -> {
                    RoleVO vo = new RoleVO();
                    String roleCode = permRolePermission.getRoleCode();
                    PermRole permRole = permRoleMapper.getByColumn("role_code", roleCode);
                    BeanUtils.copyProperties(permRole, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Api(name = "route.role.update")
    @ApiDocMethod(description = "更新路由对应的角色")
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteRole(RoutePermissionParam param) {
        String routeId = param.getRouteId();
        // 删除所有数据
        Query delQuery = new Query();
        delQuery.eq("route_id", routeId);
        permRolePermissionMapper.deleteByQuery(delQuery);

        List<String> roleCodes = param.getRoleCode();
        if (CollectionUtils.isNotEmpty(roleCodes)) {
            List<PermRolePermission> tobeSave = new ArrayList<>(roleCodes.size());
            for (String roleCode : roleCodes) {
                PermRolePermission permRolePermission = new PermRolePermission();
                permRolePermission.setRoleCode(roleCode);
                permRolePermission.setRouteId(routeId);
                tobeSave.add(permRolePermission);
            }
            // 批量添加
            permRolePermissionMapper.saveBatch(tobeSave);
        }

        try {
            routePermissionService.sendRoutePermissionReloadMsg();
        } catch (Exception e) {
            log.info("消息推送--路由权限(reload)失败", e);
            throw new BizException("修改失败，请查看日志");
        }
    }

}
