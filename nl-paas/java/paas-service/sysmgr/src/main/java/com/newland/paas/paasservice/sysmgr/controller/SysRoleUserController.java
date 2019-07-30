/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysRoleUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018-08-09 08:53:46
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/sysRoleUserMgr")
@Validated
@AuditObject("角色用户管理")
public class SysRoleUserController {

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * @param params
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    @GetMapping
    public ResultPageData<SysRoleUserRespBo> pagedQuery(SysRoleUserReqBo params, PageInfo pageInfo)
            throws ApplicationException {
        return sysRoleUserService.page(params, pageInfo);
    }

    /**
     * @param params
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/list")
    public List<SysRoleUserRespBo> list(SysRoleUserReqBo params) throws ApplicationException {
        return sysRoleUserService.list(params);
    }

    /**
     * 获取租户所有的成员（包括角色）
     * 描述
     *
     * @param tenantId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月9日 上午11:58:41
     */
    @GetMapping("/getRoleUserByTenant/{tenantId}")
    public List<SysRoleUserRespBo> getRoleUsersByTenant(@PathVariable("tenantId") Long tenantId) {
        return sysRoleUserService.getRoleUsersByTenant(tenantId);
    }

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/listSysRoleByUserId/{userId}")
    public List<SysRole> listSysRoleByUserId(@PathVariable("userId") Long userId) {
        return sysRoleService.listSysRoleByUserId(userId);
    }

    /**
     * 根据用户ID和租户ID获取角色列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/listSysRole/{tenantId}/{userId}")
    public List<SysRole> listSysRole(@PathVariable("tenantId") Long tenantId, @PathVariable("userId") Long userId) {
        return sysRoleService.listSysRole(tenantId, userId);
    }

}

