package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * 应用配置信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:06
 */
public class AppConfig {

	private Long appConfigId;

	private Long configId;

	private Long appInfoId;

	private String groupName;

	private String groupShowName;

	private String configName;

	private String configDesc;

	private String configValue;

	private Long tenantId;

	private Date createDate;

	private Date changeDate;

	private Long creatorId;

	private Short glbPropPlaceholder;

	public Short getGlbPropPlaceholder() {
		return glbPropPlaceholder;
	}

	public void setGlbPropPlaceholder(Short glbPropPlaceholder) {
		this.glbPropPlaceholder = glbPropPlaceholder;
	}

	public Long getAppConfigId() {
		return appConfigId;
	}

	public void setAppConfigId(Long appConfigId) {
		this.appConfigId = appConfigId;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public Long getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(Long appInfoId) {
		this.appInfoId = appInfoId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupShowName() {
		return groupShowName;
	}

	public void setGroupShowName(String groupShowName) {
		this.groupShowName = groupShowName;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigDesc() {
		return configDesc;
	}

	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
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
