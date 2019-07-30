package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class GlbLabel implements Serializable {
    private static final long serialVersionUID = 428949450958137283L;

    private Long glbLabelId;

    private String labelType;

    private String objType;

    private String labelCode;

    private String labelName;

    private Long reservedWord;

    private Long tenantId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeDate;

    private Long creatorId;

    private Long delFlag;

    public Long getGlbLabelId() {
        return glbLabelId;
    }

    public void setGlbLabelId(Long glbLabelId) {
        this.glbLabelId = glbLabelId;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType == null ? null : labelType.trim();
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType == null ? null : objType.trim();
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode == null ? null : labelCode.trim();
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName == null ? null : labelName.trim();
    }

    public Long getReservedWord() {
        return reservedWord;
    }

    public void setReservedWord(Long reservedWord) {
        this.reservedWord = reservedWord;
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

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlbLabel)) return false;
        GlbLabel glbLabel = (GlbLabel) o;
        return Objects.equals(getLabelCode(), glbLabel.getLabelCode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getLabelCode());
    }
}