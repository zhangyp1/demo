package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 11:10
 */
public class AppUnitLogConfig {


	private Long unitLogCId;

	private Long appUnitId;

	private Long appInfoId;

	private String logCategory;

	private Short isOpen;

	private String resolver;

	private String fieldSeparator;

	private String kvSeparator;

	private String fieldSet;

	private String regex;

	private Long tenantId;

	private Date createDate;

	private Date changeDate;

	private Long creatorId;

	public Long getUnitLogCId() {
		return unitLogCId;
	}

	public void setUnitLogCId(Long unitLogCId) {
		this.unitLogCId = unitLogCId;
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

	public String getLogCategory() {
		return logCategory;
	}

	public void setLogCategory(String logCategory) {
		this.logCategory = logCategory;
	}

	public Short getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Short isOpen) {
		this.isOpen = isOpen;
	}

	public String getResolver() {
		return resolver;
	}

	public void setResolver(String resolver) {
		this.resolver = resolver;
	}

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public String getKvSeparator() {
		return kvSeparator;
	}

	public void setKvSeparator(String kvSeparator) {
		this.kvSeparator = kvSeparator;
	}

	public String getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(String fieldSet) {
		this.fieldSet = fieldSet;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
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
