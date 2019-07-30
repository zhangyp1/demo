package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * GlbLabelValue
 */
public class GlbLabelValue implements Serializable {
    private static final long serialVersionUID = 93488642645769496L;

    private Long glbLabelValueId;

    private Long glbLabelId;

    private String labelValue;

    private Long tenantId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeDate;

    private Long creatorId;

    private Long delFlag;

    public Long getGlbLabelValueId() {
        return glbLabelValueId;
    }

    public void setGlbLabelValueId(Long glbLabelValueId) {
        this.glbLabelValueId = glbLabelValueId;
    }

    public Long getGlbLabelId() {
        return glbLabelId;
    }

    public void setGlbLabelId(Long glbLabelId) {
        this.glbLabelId = glbLabelId;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue == null ? null : labelValue.trim();
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
