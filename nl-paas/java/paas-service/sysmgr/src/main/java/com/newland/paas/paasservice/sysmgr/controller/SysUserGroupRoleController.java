package com.newland.paas.paasservice.sysmgr.controller;

import java.util.List;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;
import com.newland.paas.paasservice.sysmgr.service.SysUserGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月27日 下午6:48:19
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/userGroupRoleMgr")
@Validated
@AuditObject("用户工组角色管理")
public class SysUserGroupRoleController {

    @Autowired
    private SysUserGroupRoleService sysUserGroupRoleService;

    /**
     * 角色授权给多个用户
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午3:29:49
     */
    @PostMapping("/addUsers")
    @AuditOperate(value = "addUsers", name = "角色授权给多个用户")
    public int addUsers(@RequestBody BasicRequestContentVo<SysUserGroupRoleReqBo> reqInfo) throws ApplicationException {
        return sysUserGroupRoleService.removeAllAndAddUsers(reqInfo.getParams());
    }

    /**
     * 根据工组角色获取用户列表
     * 描述
     *
     * @param groupRoleId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:25:00
     */
    @GetMapping("/getUsersByGroupRole/{groupRoleId}")
    public List<SysUserGroupRoleRespBo> getUsersByGroupRole(@PathVariable("groupRoleId") Long groupRoleId) throws ApplicationException {
        return sysUserGroupRoleService.getUsersByGroupRole(groupRoleId);
    }

    /**
     * 根据用户id获取工组角色列表
     * 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:25:19
     */
    @GetMapping("/getGroupRolesByUser/{userId}/{groupId}")
    public List<SysUserGroupRoleRespBo> getGroupRolesByUser(
            @PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) throws ApplicationException {
        return sysUserGroupRoleService.getGroupRolesByUser(userId, groupId);
    }

    /**
     * 获取所有工组下的用户
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:24:44
     */
    @PostMapping("/getAllUsersByTenant")
    public List<SysUserGroupRoleRespBo> getAllUsersByGroup(
            @RequestBody BasicRequestContentVo<SysUserGroupRoleReqBo> reqInfo) throws ApplicationException {
        return sysUserGroupRoleService.getAllUsersByGroup(reqInfo.getParams());
    }

}

