package com.newland.paas.ee.vo.application;

import com.newland.paas.sbcommon.annotation.Doc;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 应用单元信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:09
 */
public class AppUnit {
	/**
	 * 应用单元id
	 */
	private Long appUnitId;
	/**
	 * 应用id
	 */
	private Long appInfoId;
	/**
	 * 资产单元id
	 */
	private Long unitId;
	/**
	 * 单元名称
	 */
	private String unitName;
	/**
	 * 单元版本
	 */
	private String unitVersion;
	/**
	 * 单元描述
	 */
	private String unitDesc;
	/**
	 * 集群id
	 */
	private Long clusterId;
	private String bearTargetType;
	@Doc("日志集群id")
	private Long logClusterId;
	/**
	 * 运行实例数
	 */
	private Long runInsNum;
	/**
	 * 最小实例数
	 */
	private Long minInsNum;
	/**
	 * 最大实例数
	 */
	private Long maxInsNum;
	/**
	 * 单元状态
	 */
	private String unitStatus;
	/**
	 * cpu占用数
	 */
	private Float quotaCpu;
	/**
	 * 内存占用数
	 */
	private Float quotaMemory;
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

	/**
	 * 应用单元属性
	 */
	private List<AppUnitProp> appUnitProps = Collections.emptyList();


	public Long getAppUnitId() {
		return appUnitId;
	}

	public void setAppUnitId(Long appUnitId) {
		this.appUnitId = appUnitId;
	}

	public Long getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(Long appInfoId) {
		this.appInfoId = appInfoId;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitVersion() {
		return unitVersion;
	}

	public void setUnitVersion(String unitVersion) {
		this.unitVersion = unitVersion;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public Long getClusterId() {
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	public String getBearTargetType() {
		return bearTargetType;
	}

	public void setBearTargetType(String bearTargetType) {
		this.bearTargetType = bearTargetType;
	}

	public Long getLogClusterId() {
		return logClusterId;
	}

	public void setLogClusterId(Long logClusterId) {
		this.logClusterId = logClusterId;
	}

	public Long getRunInsNum() {
		return runInsNum;
	}

	public void setRunInsNum(Long runInsNum) {
		this.runInsNum = runInsNum;
	}

	public Long getMinInsNum() {
		return minInsNum;
	}

	public void setMinInsNum(Long minInsNum) {
		this.minInsNum = minInsNum;
	}

	public Long getMaxInsNum() {
		return maxInsNum;
	}

	public void setMaxInsNum(Long maxInsNum) {
		this.maxInsNum = maxInsNum;
	}

	public String getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(String unitStatus) {
		this.unitStatus = unitStatus;
	}

	public Float getQuotaCpu() {
		return quotaCpu;
	}

	public void setQuotaCpu(Float quotaCpu) {
		this.quotaCpu = quotaCpu;
	}

	public Float getQuotaMemory() {
		return quotaMemory;
	}

	public void setQuotaMemory(Float quotaMemory) {
		this.quotaMemory = quotaMemory;
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

	public List<AppUnitProp> getAppUnitProps() {
		return appUnitProps;
	}

	public void setAppUnitProps(List<AppUnitProp> appUnitProps) {
		this.appUnitProps = appUnitProps;
	}
}
