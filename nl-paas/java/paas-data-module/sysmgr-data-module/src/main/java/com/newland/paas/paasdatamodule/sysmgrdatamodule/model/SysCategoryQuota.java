package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

/**
 * SysCategoryQuota
 */
public class SysCategoryQuota implements Serializable {
    private static final long serialVersionUID = 90545513505787112L;

    private Long id;

    private Long sysCategoryId;

    private Float cpuQuota;

    private Float memoryQuota;

    private Float storageQuota;

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

    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
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
