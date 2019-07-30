package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRoleSysRole;

/**
 * 工组角色与系统角色关系
 *
 * @author zhongqingjiang
 */
public interface SysGroupRoleSysRoleService {

    void add(SysGroupRoleSysRole sysGroupRoleSysRole);

    void update(SysGroupRoleSysRole sysGroupRoleSysRole);

    void delete(Long groupRoleSysRoleId);

    SysGroupRoleSysRole getByRoleId(Long roleId);

    SysGroupRoleSysRole getByGroupRoleId(Long groupRoleId);

}
