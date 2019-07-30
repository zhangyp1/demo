package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysMenuOperRole implements Serializable {
    private static final long serialVersionUID = 795315500067535861L;

    private Long id;

    private Long roleId;

    private Long menuOperId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Short delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuOperId() {
        return menuOperId;
    }

    public void setMenuOperId(Long menuOperId) {
        this.menuOperId = menuOperId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }
}