package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysGroupObj implements Serializable {
    private static final long serialVersionUID = 765906841240676636L;

    private Long groupId;

    private Long objId;

    private String objName;

    private String objType;

    private String isSright;

    private Date createDate;

    private Integer status;

    private Date changeDate;

    private Long creatorId;

    private Integer delFlag;

    public SysGroupObj() {
    }

    public SysGroupObj(Long groupId, Long objId, String objName, String objType, String isSright, Integer status, Long creatorId) {
        this.groupId = groupId;
        this.objId = objId;
        this.objName = objName;
        this.objType = objType;
        this.isSright = isSright;
        this.status = status;
        this.creatorId = creatorId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
        this.objName = objName;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType == null ? null : objType.trim();
    }

    public String getIsSright() {
        return isSright;
    }

    public void setIsSright(String isSright) {
        this.isSright = isSright;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}