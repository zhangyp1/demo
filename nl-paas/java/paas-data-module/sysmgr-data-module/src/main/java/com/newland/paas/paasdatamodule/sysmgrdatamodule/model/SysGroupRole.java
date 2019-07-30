package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;

public class SysGroupRole implements Serializable {
    private static final long serialVersionUID = 97516149491863370L;

    @NotNull(message = "工组角色Id不能为空",groups= {ValidateUpdate.class})
    private Long groupRoleId;

    @NotBlank(message = "工组角色名称不能为空",groups= {ValidateAdd.class})
    private String groupRoleName;

    private String groupRoleDesc;

    @NotNull(message = "工组Id不能为空",groups= {ValidateAdd.class})
    private Long groupId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Long delFlag;

    public Long getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName == null ? null : groupRoleName.trim();
    }

    public String getGroupRoleDesc() {
        return groupRoleDesc;
    }

    public void setGroupRoleDesc(String groupRoleDesc) {
        this.groupRoleDesc = groupRoleDesc == null ? null : groupRoleDesc.trim();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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