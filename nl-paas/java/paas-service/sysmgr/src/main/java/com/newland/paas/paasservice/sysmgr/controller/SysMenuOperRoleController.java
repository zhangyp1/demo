package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperRoleService;
import com.newland.paas.paasservice.sysmgr.vo.SysMenuOperRoleVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色菜单分配
 *
 * @author caifeitong
 * @date 2018/7/31 10:44
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/menuOperRole")
@Validated
@AuditObject("角色菜单管理")
public class SysMenuOperRoleController {

    @Autowired
    private SysMenuOperRoleService sysMenuOperRoleService;

    /**
     * 添加角色菜单
     * @param vo
     * @throws ApplicationException
     */
    @PutMapping(value = "/menuAssign")
    @AuditOperate(value = "assignAndCorrectMenuPrivileges", name = "添加角色菜单")
    public void assignMenuPrivilegesToSysRole(@RequestBody BasicRequestContentVo<SysMenuOperRoleVo> vo)
            throws ApplicationException {
        sysMenuOperRoleService.assignAndCorrectMenuPrivileges(vo.getParams().getRoleId(),
                vo.getParams().getMenuIdList());
    }

    /**
     *
     * @param roleId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/menuIdList")
    public List<Long> getSelectMenuIdList(Long roleId) throws ApplicationException {
        return sysMenuOperRoleService.getSelectMenuIdList(roleId);
    }
}
