package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class MsgType implements Serializable {
    private static final long serialVersionUID = 36939799817648803L;

    private Long msgTypeId;

    private String typeName;

    private String typeDesc;

    private Long manualRelease;

    private Long creatorId;

    private Date createDate;

    private Date changeDate;

    private Long delFlag;

    public Long getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Long msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc == null ? null : typeDesc.trim();
    }

    public Long getManualRelease() {
        return manualRelease;
    }

    public void setManualRelease(Long manualRelease) {
        this.manualRelease = manualRelease;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}