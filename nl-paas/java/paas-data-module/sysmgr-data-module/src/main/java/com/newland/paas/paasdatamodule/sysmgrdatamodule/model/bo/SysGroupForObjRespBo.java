package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

import java.util.List;

public class SysGroupForObjRespBo extends SysGroup {

    private Boolean isAdmin;

    private List<GlbDict> operates;

    private List<GlbDict> selectOperates;

    private List<SysGroupRoleBO> groupRoles;

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<GlbDict> getOperates() {
        return operates;
    }

    public List<GlbDict> getSelectOperates() {
        return selectOperates;
    }

    public void setSelectOperates(List<GlbDict> selectOperates) {
        this.selectOperates = selectOperates;
    }

    public void setOperates(List<GlbDict> operates) {
        this.operates = operates;
    }

    public List<SysGroupRoleBO> getGroupRoles() {
        return groupRoles;
    }

    public void setGroupRoles(List<SysGroupRoleBO> groupRoles) {
        this.groupRoles = groupRoles;
    }
}
