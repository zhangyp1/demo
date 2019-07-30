package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysTenantLimit implements Serializable {
    private static final long serialVersionUID = 81103073269562388L;

    private Long id;
    // 租户id
    private Long tenantId;
    // 配额项
    private String quotaItem;
    // 配额值
    private Long quotaValue;
    // 配额计算类型 1--百分比 2--数值
    private Long quotaCalcType;

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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getQuotaItem() {
        return quotaItem;
    }

    public void setQuotaItem(String quotaItem) {
        this.quotaItem = quotaItem == null ? null : quotaItem.trim();
    }

    public Long getQuotaValue() {
        return quotaValue;
    }

    public void setQuotaValue(Long quotaValue) {
        this.quotaValue = quotaValue;
    }

    public Long getQuotaCalcType() {
        return quotaCalcType;
    }

    public void setQuotaCalcType(Long quotaCalcType) {
        this.quotaCalcType = quotaCalcType;
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

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}