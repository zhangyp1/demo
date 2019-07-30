package com.newland.paas.ee.vo.service;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 9:12
 */
public class SvrInstUnitAlarmConfig {

	/**
	 * id
	 */
	private Long instUnitAlarmCId;
	/**
	 * 服务实例单元id
	 */
	private Long svrInstUnitId;
	/**
	 * 服务实例id
	 */
	private Long svrInstId;
	/**
	 * 是否启用
	 */
	private Short isOpen;

	private String kpiKey;

	private String compareWay;

	private String valueStart;

	private String valueEnd;

	private String alarmLevel;

	private String alarmContent;

	private Long tenantId;

	private Date createDate;

	private Date changeDate;

	private Long creatorId;


	public Long getInstUnitAlarmCId() {
		return instUnitAlarmCId;
	}

	public void setInstUnitAlarmCId(Long instUnitAlarmCId) {
		this.instUnitAlarmCId = instUnitAlarmCId;
	}

	public Long getSvrInstUnitId() {
		return svrInstUnitId;
	}

	public void setSvrInstUnitId(Long svrInstUnitId) {
		this.svrInstUnitId = svrInstUnitId;
	}

	public Long getSvrInstId() {
		return svrInstId;
	}

	public void setSvrInstId(Long svrInstId) {
		this.svrInstId = svrInstId;
	}

	public Short getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Short isOpen) {
		this.isOpen = isOpen;
	}

	public String getKpiKey() {
		return kpiKey;
	}

	public void setKpiKey(String kpiKey) {
		this.kpiKey = kpiKey;
	}

	public String getCompareWay() {
		return compareWay;
	}

	public void setCompareWay(String compareWay) {
		this.compareWay = compareWay;
	}

	public String getValueStart() {
		return valueStart;
	}

	public void setValueStart(String valueStart) {
		this.valueStart = valueStart;
	}

	public String getValueEnd() {
		return valueEnd;
	}

	public void setValueEnd(String valueEnd) {
		this.valueEnd = valueEnd;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
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
