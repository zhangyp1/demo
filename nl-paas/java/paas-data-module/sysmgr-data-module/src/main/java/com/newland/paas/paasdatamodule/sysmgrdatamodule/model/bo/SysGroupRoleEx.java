package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

/**
 * @program: paas-all
 * @description: 工组角色信息
 * @author: Frown
 * @create: 2018-08-24 10:01
 **/
public class SysGroupRoleEx extends SysGroup {
    // 工组角色名称
    private String groupRoleName;

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName;
    }
}
