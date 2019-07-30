package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * 应用接入点信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:13
 */
public class AppEndpoint {
	private Long endpointId;

	private Long appInfoId;

	private String endpointName;

	private String endpointIdent;

	private String endpointValue;

	private Long appUnitId;

	private Long tenantId;

	private Date createDate;

	private Date changeDate;

	private Long creatorId;

	private String intfKey;

	private Long interfaceId;

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	public Long getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(Long appInfoId) {
		this.appInfoId = appInfoId;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public String getEndpointValue() {
		return endpointValue;
	}

	public void setEndpointValue(String endpointValue) {
		this.endpointValue = endpointValue;
	}

	public Long getAppUnitId() {
		return appUnitId;
	}

	public void setAppUnitId(Long appUnitId) {
		this.appUnitId = appUnitId;
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

	public String getIntfKey() {
		return intfKey;
	}

	public void setIntfKey(String intfKey) {
		this.intfKey = intfKey;
	}

	public Long getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Long interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getEndpointIdent() {
		return endpointIdent;
	}

	public void setEndpointIdent(String endpointIdent) {
		this.endpointIdent = endpointIdent;
	}
}
