package com.newland.paas.ee.vo.service;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务实例配置
 */
public class SvrInstConfig implements Serializable {
    private static final long serialVersionUID = 602438473832972265L;

	/**
	 * 服务实例配置id
	 */
	private Long svrInstConfigId;
	/**
	 * 服务实例id
	 */
	private Long svrInstId;
	/**
	 * 资产配置id
	 */
	private Long configId;
	/**
	 * 配置组名
	 */
	private String groupName;
	/**
	 * 配置组显示名
	 */
	private String groupShowName;
	/**
	 * 配置名
	 */
	private String configName;
	/**
	 * 配置描述
	 */
	private String configDesc;
	/**
	 * 配置值
	 */
	private String configValue;
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


	public Long getSvrInstConfigId() {
		return svrInstConfigId;
	}

	public void setSvrInstConfigId(Long svrInstConfigId) {
		this.svrInstConfigId = svrInstConfigId;
	}

	public Long getSvrInstId() {
		return svrInstId;
	}

	public void setSvrInstId(Long svrInstId) {
		this.svrInstId = svrInstId;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
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