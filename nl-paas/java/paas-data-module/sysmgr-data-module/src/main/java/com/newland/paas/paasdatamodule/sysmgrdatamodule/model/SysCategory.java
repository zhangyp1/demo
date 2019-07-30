package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 系统分组
 */
public class SysCategory implements Serializable {
    private static final long serialVersionUID = 180958498917855694L;
    // id
    private Long sysCategoryId;
    // 名称
    private String sysCategoryName;
    // 父系统分组id
    private Long sysCategoryPid;
    // 租户id
    private Long tenantId;
    // 创建日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    // 修改日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeDate;
    // 创建人
    private Long creatorId;
    // 删除标识
    private Long delFlag;
    // 工组id
    private Long groupId;
    // 系统分组Code
    private String sysCategoryCode;

    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
    }

    public String getSysCategoryName() {
        return sysCategoryName;
    }

    public void setSysCategoryName(String sysCategoryName) {
        this.sysCategoryName = sysCategoryName == null ? null : sysCategoryName.trim();
    }

    public Long getSysCategoryPid() {
        return sysCategoryPid;
    }

    public void setSysCategoryPid(Long sysCategoryPid) {
        this.sysCategoryPid = sysCategoryPid;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getSysCategoryCode() {
        return sysCategoryCode;
    }

    public void setSysCategoryCode(String sysCategoryCode) {
        this.sysCategoryCode = sysCategoryCode;
    }
}