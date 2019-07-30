package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;

import java.util.ArrayList;
import java.util.List;

/**
 * 带工组角色列表的工组Vo
 *
 * @author zhongqingjiang
 */
public class SysGroupWithRoleVo extends SysGroupRespBo {
    public SysGroupWithRoleVo() {
        this.groupRoleList = new ArrayList<>();
    }

    private List<SysGroupRole> groupRoleList;

    public List<SysGroupRole> getGroupRoleList() {
        return groupRoleList;
    }

    public void setGroupRoleList(List<SysGroupRole> groupRoleList) {
        this.groupRoleList = groupRoleList;
    }
}
