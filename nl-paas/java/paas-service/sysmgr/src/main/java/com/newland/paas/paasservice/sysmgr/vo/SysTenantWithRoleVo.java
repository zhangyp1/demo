package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantBo;

import java.util.ArrayList;
import java.util.List;

/**
 * 带平台角色的租户信息Vo
 *
 * @author zhongqingjiang
 */
public class SysTenantWithRoleVo extends SysTenantBo {

    public SysTenantWithRoleVo() {
        this.sysRoleList = new ArrayList<>();
    }

    private List<SysRole> sysRoleList;

    public List<SysRole> getSysRoleList() {
        return sysRoleList;
    }

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }
}
