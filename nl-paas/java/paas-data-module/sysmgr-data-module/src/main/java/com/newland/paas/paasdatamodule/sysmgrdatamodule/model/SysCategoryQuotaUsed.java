package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysCategoryQuotaUsed implements Serializable {
    private static final long serialVersionUID = 912701462737038243L;

    private Long id;

    private Long objId;

    private String objName;

    private Float cpuQuota;

    private Float memoryQuota;

    private Float storageQuota;

    private Long categoryId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Long delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName == null ? null : objName.trim();
    }

    public Float getCpuQuota() {
        return cpuQuota;
    }

    public void setCpuQuota(Float cpuQuota) {
        this.cpuQuota = cpuQuota;
    }

    public Float getMemoryQuota() {
        return memoryQuota;
    }

    public void setMemoryQuota(Float memoryQuota) {
        this.memoryQuota = memoryQuota;
    }

    public Float getStorageQuota() {
        return storageQuota;
    }

    public void setStorageQuota(Float storageQuota) {
        this.storageQuota = storageQuota;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateDate() {
        return createDate == null ? null : (Date)createDate.clone();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }

    public Date getChangeDate() {
        return changeDate == null ? null : (Date)changeDate.clone();
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate == null ? null : (Date)changeDate.clone();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}
