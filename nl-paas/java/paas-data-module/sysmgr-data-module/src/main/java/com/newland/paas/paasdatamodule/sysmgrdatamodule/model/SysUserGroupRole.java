package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;

public class SysUserGroupRole implements Serializable {
    private static final long serialVersionUID = 900418873475220860L;

    @NotNull(message = "Id不能为空",groups= {ValidateUpdate.class})
    private Long userGroupRoleId;

    private Long userId;

    private Long groupRoleId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Long delFlag;
    
    public Long getUserGroupRoleId() {
        return userGroupRoleId;
    }

    
    public void setUserGroupRoleId(Long userGroupRoleId) {
        this.userGroupRoleId = userGroupRoleId;
    }

    
    public Long getUserId() {
        return userId;
    }

    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
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
}