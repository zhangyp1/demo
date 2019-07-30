package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 11:10
 */
public class AppUnitAlarmConfig {

	private Long unitAlarmCId;

	private Long appUnitId;

	private Long appInfoId;

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

	public Long getUnitAlarmCId() {
		return unitAlarmCId;
	}

	public void setUnitAlarmCId(Long unitAlarmCId) {
		this.unitAlarmCId = unitAlarmCId;
	}

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
}
