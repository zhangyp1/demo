package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;

public class SysMenuOper implements Serializable {
    private static final long serialVersionUID = 165546897070393239L;

    @NotNull(message = "id不能为空",groups= {ValidateUpdate.class})
    private Long id;

    @NotBlank(message = "名称不能为空",groups= {ValidateAdd.class})
    private String name;

    @NotNull(message = "类型不能为空",groups= {ValidateAdd.class})
    private Short type;

    private String url;

    private String description;

    @NotNull(message = "父节点不能为空",groups= {ValidateAdd.class})
    private Long parentId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Short delFlag;

    private String icon;

    private String code;
    
    @NotBlank(message = "排序不能为空",groups= {ValidateAdd.class})
    @Range(max=99,min=0,groups= {ValidateAdd.class,ValidateUpdate.class})
    private String orderNumber;
    
    private String hidden;
    
    private String addition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
    /**
     * @author linkun
     * @created 2018年7月19日 下午6:46:52
     * @return 
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @author linkun
     * @created 2018年7月19日 下午6:46:52
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午6:28:43
     * @return 
     */
    public String getHidden() {
        return hidden;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午6:28:43
     * @param hidden
     */
    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
    
    private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}
    
    
}