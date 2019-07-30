package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;

import java.util.List;

/**
 * @program: paas-all
 * @description: 用户所有信息
 * @author: Frown
 * @create: 2018-07-23 11:33
 **/
public class SysUserAllInfoBO extends SysUser {
    // 租户
    private List<SysTenantRoleBo> sysTenantList;
    // 工组
    private List<SysGroupRoleEx> sysGroupList;
    // 角色
    private List<SysRole> sysRoleList;

    public List<SysTenantRoleBo> getSysTenantList() {
        return sysTenantList;
    }

    public void setSysTenantList(List<SysTenantRoleBo> sysTenantList) {
        this.sysTenantList = sysTenantList;
    }

    public List<SysGroupRoleEx> getSysGroupList() {
        return sysGroupList;
    }

    public void setSysGroupList(List<SysGroupRoleEx> sysGroupList) {
        this.sysGroupList = sysGroupList;
    }

    public List<SysRole> getSysRoleList() {
        return sysRoleList;
    }

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }
}
