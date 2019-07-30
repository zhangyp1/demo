package com.newland.paas.ee.vo.service;

import java.util.Date;

/**
 * @author laiCheng
 * @date 2018/8/7 9:19
 */
public class SvrInstUnitLogConfig {


	private Long instUnitLogCId;

	private Long svrInstUnitId;

	private Long svrInstId;

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


	public Long getInstUnitLogCId() {
		return instUnitLogCId;
	}

	public void setInstUnitLogCId(Long instUnitLogCId) {
		this.instUnitLogCId = instUnitLogCId;
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
