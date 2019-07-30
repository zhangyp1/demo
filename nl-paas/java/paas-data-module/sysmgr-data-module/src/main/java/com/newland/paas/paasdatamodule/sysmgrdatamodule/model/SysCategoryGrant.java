package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统分组授权
 *
 * @author zhongqingjiang
 */
public class SysCategoryGrant implements Serializable {
    private static final long serialVersionUID = 324809281898260830L;

    private Long categoryGrantId;

    @NotNull(message = "系统分组ID不能为空", groups= {ValidateAdd.class})
    private Long categoryId;

    @NotBlank(message = "对象类型不能为空", groups= {ValidateAdd.class})
    private String objType;

    @NotNull(message = "操作集合不能为空", groups= {ValidateAdd.class})
    private String operates;

    @NotNull(message = "工组ID不能为空", groups= {ValidateAdd.class})
    private Long groupId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    @JsonIgnore
    private Short delFlag;

    public Long getCategoryGrantId() {
        return categoryGrantId;
    }

    public void setCategoryGrantId(Long categoryGrantId) {
        this.categoryGrantId = categoryGrantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getOperates() {
        return operates;
    }

    public void setOperates(String operates) {
        this.operates = operates;
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

    public Short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }
}