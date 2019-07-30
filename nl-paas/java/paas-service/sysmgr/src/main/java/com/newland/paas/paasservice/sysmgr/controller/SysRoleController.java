package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasservice.sysmgr.service.SysRoleService;
import com.newland.paas.paasservice.sysmgr.vo.InquireSysRolePageListVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色
 *
 * @author zhongqingjiang
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/roleMgr")
@Validated
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping(value = "/roleList")
    public ResultPageData<SysRole> getAllSysRoleForTenant(
            @Validated @RequestBody BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException {
        return sysRoleService.getAllSysRoleForTenant(input);
    }

    @PostMapping("/roleListForAdmin")
    public ResultPageData<SysRole> getAllSysRoleForAdmin(
            @Validated @RequestBody BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException {
        return sysRoleService.getAllSysRoleForAdmin(input);
    }

    /********** 工组角色模板 **********/

    @GetMapping("/group-role-templates")
    public List<SysRole> listGroupRoleTemplate() throws ApplicationException {
        return sysRoleService.listGroupRoleTemplate();
    }

    /********** 工组角色 **********/

    @PostMapping("/group-roles")
    public void insertGroupRole(@RequestBody SysRole sysRole) throws ApplicationException {
        sysRoleService.insertGroupRole(sysRole);
    }

    @PutMapping("/group-roles/{id}")
    public void updateGroupRole(@PathVariable Long id, @RequestBody SysRole sysRole) throws ApplicationException {
        sysRole.setRoleId(id);
        sysRoleService.updateGroupRole(sysRole);
    }

    @DeleteMapping("/group-roles/{id}")
    public void deleteGroupRole(@PathVariable Long id) throws ApplicationException {
        sysRoleService.deleteGroupRole(id);
    }

    @GetMapping("/group-roles/{id}")
    public SysRole getGroupRole(@PathVariable Long id) throws ApplicationException {
        return sysRoleService.getGroupRole(id);
    }

}
