package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysAliasObj extends SysAliasObjKey implements Serializable {
    private static final long serialVersionUID = 51390719657384637L;

    private String objCodeP;

    private String aliasObjName;

    private String objName;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creator;

    private Long delFlag;

    public String getObjCodeP() {
        return objCodeP;
    }

    public void setObjCodeP(String objCodeP) {
        this.objCodeP = objCodeP == null ? null : objCodeP.trim();
    }

    public String getAliasObjName() {
        return aliasObjName;
    }

    public void setAliasObjName(String aliasObjName) {
        this.aliasObjName = aliasObjName == null ? null : aliasObjName.trim();
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName == null ? null : objName.trim();
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

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}