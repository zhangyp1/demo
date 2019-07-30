
package com.newland.paas.ee.vo.cluster;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.newland.paas.ee.vo.TenantInstanceDetailInfo;


/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ZoneDetailVo
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 *
 */
public class ZoneDetailVo {

	/**
	 * 集群分区ID
	 */
	private Long clusterZoneId;
	
	 /**
     * 租户ID
     */
	@NotNull(message = "集群分区租户ID不能为空")
    private Long tenantId;

    /**
     * CPU配额
     */
	@NotNull(message = "集群分区CPU配额不能为空")
    private Float cpuQuota;

    /**
     * 内存配额
     */
	@NotNull(message = "集群分区内存配额不能为空")
    private Float memoryQuota;

	  /**
     * 集群分区名称
     */
    private String zoneName;
    
    /**
     * 分区来源
     */
    private String source;

    private Date createDate;

    private Date changeDate;


    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 系统组ID
     */
    private Long sysCategoryId;

    private Long creatorId;

    /**
     * 0 否，1 是
     */
    private Short delFlag;
    // -1 删除记录标记 0 修改记录标记 1 新增记录标记
    private Short extendsOper;
    private TenantInstanceDetailInfo tenantInfo;

	public Short getExtendsOper() {
		return extendsOper;
	}

	public void setExtendsOper(Short extendsOper) {
		this.extendsOper = extendsOper;
	}

	public Long getTenantId() {
	
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
	
		this.tenantId = tenantId;
	}

	public Float getCpuQuota() {
	
		return cpuQuota;
	}

	public void setCpuQuota(Float cpuQuota) {
	
		this.cpuQuota = cpuQuota;
	}

	public Float getMemoryQuota() {
	
		return memoryQuota;
	}

	public void setMemoryQuota(Float memoryQuota) {
	
		this.memoryQuota = memoryQuota;
	}

	public String getSource() {
	
		return source;
	}

	public void setSource(String source) {
	
		this.source = source;
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

	public String getZoneName() {
	
		return zoneName;
	}

	public void setZoneName(String zoneName) {
	
		this.zoneName = zoneName;
	}

	public Long getClusterZoneId() {
	
		return clusterZoneId;
	}

	public void setClusterZoneId(Long clusterZoneId) {
	
		this.clusterZoneId = clusterZoneId;
	}

	public Long getClusterId() {
	
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
	
		this.clusterId = clusterId;
	}

	public Long getSysCategoryId() {
	
		return sysCategoryId;
	}

	public void setSysCategoryId(Long sysCategoryId) {
	
		this.sysCategoryId = sysCategoryId;
	}

	public Long getCreatorId() {
	
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
	
		this.creatorId = creatorId;
	}

	public TenantInstanceDetailInfo getTenantInfo() {
	
		return tenantInfo;
	}

	public void setTenantInfo(TenantInstanceDetailInfo tenantInfo) {
	
		this.tenantInfo = tenantInfo;
	}

	public Short getDelFlag() {
	
		return delFlag;
	}

	public void setDelFlag(Short delFlag) {
	
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ZoneDetailVo [clusterZoneId=");
		builder.append(clusterZoneId);
		builder.append(", tenantId=");
		builder.append(tenantId);
		builder.append(", cpuQuota=");
		builder.append(cpuQuota);
		builder.append(", memoryQuota=");
		builder.append(memoryQuota);
		builder.append(", zoneName=");
		builder.append(zoneName);
		builder.append(", source=");
		builder.append(source);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", changeDate=");
		builder.append(changeDate);
		builder.append(", clusterId=");
		builder.append(clusterId);
		builder.append(", sysCategoryId=");
		builder.append(sysCategoryId);
		builder.append(", creatorId=");
		builder.append(creatorId);
		builder.append(", tenantInfo=");
		builder.append(tenantInfo);
		builder.append(", delFlag=");
		builder.append(delFlag);
		builder.append("]");
		return builder.toString();
	}
    
}

