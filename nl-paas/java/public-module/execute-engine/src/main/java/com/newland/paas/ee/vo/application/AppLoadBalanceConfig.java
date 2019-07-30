package com.newland.paas.ee.vo.application;

import com.newland.paas.ee.vo.IngressInfo;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 11:09
 */
public class AppLoadBalanceConfig {


	private Long appLoadBalanceId;

	private String loadBalanceName;

	private Long appInfoId;

	private Long endpointId;

	private String endpointName;

	private Long loadBalanceId;

	private String connectionType;

	private Long hostPort;

	private Long servicePort;

	private Long tenantId;

	private Date createDate;

	private Date changeDate;

	private Long creatorId;

	private IngressInfo ingressInfo;


	public Long getAppLoadBalanceId() {
		return appLoadBalanceId;
	}

	public void setAppLoadBalanceId(Long appLoadBalanceId) {
		this.appLoadBalanceId = appLoadBalanceId;
	}

	public Long getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(Long appInfoId) {
		this.appInfoId = appInfoId;
	}

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public Long getLoadBalanceId() {
		return loadBalanceId;
	}

	public void setLoadBalanceId(Long loadBalanceId) {
		this.loadBalanceId = loadBalanceId;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public Long getHostPort() {
		return hostPort;
	}

	public void setHostPort(Long hostPort) {
		this.hostPort = hostPort;
	}

	public Long getServicePort() {
		return servicePort;
	}

	public void setServicePort(Long servicePort) {
		this.servicePort = servicePort;
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

	public IngressInfo getIngressInfo() {
		return ingressInfo;
	}

	public void setIngressInfo(IngressInfo ingressInfo) {
		this.ingressInfo = ingressInfo;
	}

	public String getLoadBalanceName() {
		return loadBalanceName;
	}

	public void setLoadBalanceName(String loadBalanceName) {
		this.loadBalanceName = loadBalanceName;
	}
}
