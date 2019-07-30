package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * 应用单元属性信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:10
 */
public class AppUnitProp {
	/**
	 * 单元属性id
	 */
	private Long unitPropId;
	/**
	 * 应用单元id
	 */
	private Long appUnitId;
	/**
	 * 属性key
	 */
	private String propKey;
	/**
	 * 属性描述
	 */
	private String propDesc;
	/**
	 * 属性值
	 */
	private String propVal;
	/**
	 * 租户id
	 */
	private Long tenantId;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date changeDate;
	/**
	 * 创建者id
	 */
	private Long creatorId;

	public Long getUnitPropId() {
		return unitPropId;
	}

	public void setUnitPropId(Long unitPropId) {
		this.unitPropId = unitPropId;
	}

	public Long getAppUnitId() {
		return appUnitId;
	}

	public void setAppUnitId(Long appUnitId) {
		this.appUnitId = appUnitId;
	}

	public String getPropKey() {
		return propKey;
	}

	public void setPropKey(String propKey) {
		this.propKey = propKey;
	}

	public String getPropDesc() {
		return propDesc;
	}

	public void setPropDesc(String propDesc) {
		this.propDesc = propDesc;
	}

	public String getPropVal() {
		return propVal;
	}

	public void setPropVal(String propVal) {
		this.propVal = propVal;
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
}
