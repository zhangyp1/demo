package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysGroupLimit implements Serializable {
    private static final long serialVersionUID = 20353133625744100L;

    private Long id;

    private Long groupId;

    private Short quotaItem;

    private Long quotaValue;

    private Short quotaCalcType;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Short getQuotaItem() {
        return quotaItem;
    }

    public void setQuotaItem(Short quotaItem) {
        this.quotaItem = quotaItem;
    }

    public Long getQuotaValue() {
        return quotaValue;
    }

    public void setQuotaValue(Long quotaValue) {
        this.quotaValue = quotaValue;
    }

    public Short getQuotaCalcType() {
        return quotaCalcType;
    }

    public void setQuotaCalcType(Short quotaCalcType) {
        this.quotaCalcType = quotaCalcType;
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