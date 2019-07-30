package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 工组角色与系统角色关系
 *
 * @author zhongqingjiang
 */
public class SysGroupRoleSysRole implements Serializable {
    private static final long serialVersionUID = 324809898020358630L;

    @NotNull(message = "ID不能为空", groups= {ValidateUpdate.class})
    private Long groupRoleSysRoleId;

    @NotNull(message = "工组角色ID不能为空", groups= {ValidateAdd.class})
    private Long groupRoleId;

    @NotNull(message = "系统角色ID不能为空", groups= {ValidateAdd.class})
    private Long roleId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    @JsonIgnore
    private Short delFlag;

    public Long getGroupRoleSysRoleId() {
        return groupRoleSysRoleId;
    }

    public void setGroupRoleSysRoleId(Long groupRoleSysRoleId) {
        this.groupRoleSysRoleId = groupRoleSysRoleId;
    }

    public Long getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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