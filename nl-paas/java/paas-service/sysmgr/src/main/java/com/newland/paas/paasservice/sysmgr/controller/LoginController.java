/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.LoginInfoVO;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SongDi
 * @date 2018/08/14
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/login")
public class LoginController {
    @Autowired
    private SysTenantMemberService sysTenantMemberService;
    @Autowired
    private SysGroupUserService sysGroupUserService;
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysTenantService sysTenantService;

    /**
     * 查询用户在租户下的 工组、角色
     */
    @GetMapping(value = "/refresh/{tenantId}/{userId}")
    public LoginInfoVO refresh(@PathVariable("tenantId") Long tenantId, @PathVariable("userId") Long userId) throws ApplicationException {
        LoginInfoVO userinfo = new LoginInfoVO();
        Boolean result = sysTenantMemberService.isTenantAdmin(tenantId, userId);
        userinfo.setTenantAdmin(result);
        // 查询租户信息
        SysTenant sysTenant = sysTenantService.selectByPrimaryKey(tenantId);
        userinfo.setTenantCode(sysTenant.getTenantCode());
        // 根据租户和用户ID查询直接工组
        List<SysGroupUserRespBo> groupList = sysGroupUserService.getGroupsByTenantAndUser(tenantId, userId);
        List<Long> groupIdList = groupList.stream().map(SysGroupUserRespBo::getGroupId).collect(Collectors.toList());
        // 根据直接工组，查询子工组
        List<SysGroup> subGroupList = sysGroupService.getSubGroupById(groupIdList);
        List<Long> subGroupIdList =
                subGroupList.stream().map(SysGroup::getGroupId).collect(Collectors.toList());
        userinfo.setGroupIdList(groupIdList);
        userinfo.setSubGroupIdList(subGroupIdList);
        SysRoleUserReqBo sysRoleUserReqBo = new SysRoleUserReqBo();
        sysRoleUserReqBo.setUserId(userId);
        sysRoleUserReqBo.setTenantId(tenantId);
        // 根据租户和用户ID查询角色列表
        List<SysRoleUserRespBo> sysRoleList = sysRoleUserService.list(sysRoleUserReqBo);
        List<Long> roleIdList =
                sysRoleList.stream().map(SysRoleUserRespBo::getRoleId).distinct().collect(Collectors.toList());
        userinfo.setRoleIdList(roleIdList);
        return userinfo;
    }
}
