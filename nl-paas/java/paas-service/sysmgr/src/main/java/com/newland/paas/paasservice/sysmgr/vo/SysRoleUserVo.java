package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;

import java.util.List;

/**
 * Created by Lenovo on 2018/7/27.
 */
public class SysRoleUserVo {

    private Long roleId;

    private List<SysUser> sysUserList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<SysUser> getSysUserList() {
        return sysUserList;
    }

    public void setSysUserList(List<SysUser> sysUserList) {
        this.sysUserList = sysUserList;
    }
}
