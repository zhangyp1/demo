package com.newland.paas.ee.vo.service;

import com.newland.paas.ee.vo.IngressInfo;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 9:18
 */
public class SvrInstLoadBalanceConfig {


	/**
	 * 服务实例负载均衡配置iid
	 */
	private Long svrInstLoadBalanceId;

	private String loadBalanceName;
	/**
	 * 服务实例id
	 */
	private Long svrInstId;
	/**
	 * 接入点名称
	 */
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


	public Long getSvrInstLoadBalanceId() {
		return svrInstLoadBalanceId;
	}

	public void setSvrInstLoadBalanceId(Long svrInstLoadBalanceId) {
		this.svrInstLoadBalanceId = svrInstLoadBalanceId;
	}

	public Long getSvrInstId() {
		return svrInstId;
	}

	public void setSvrInstId(Long svrInstId) {
		this.svrInstId = svrInstId;
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
