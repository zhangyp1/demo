package com.newland.paas.ee.vo.application;

import java.util.Date;

/**
 * 应用单元成员信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:09
 */
public class AppUnitMember {
	/**
	 * 单元成员id
	 */
	private Long unitMemberId;
	/**
	 * 应用单元id
	 */
	private Long appUnitId;
	/**
	 * 成员地址
	 */
	private String address;
	/**
	 * 成员状态
	 */
	private String memberStatus;
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

	public Long getUnitMemberId() {
		return unitMemberId;
	}

	public void setUnitMemberId(Long unitMemberId) {
		this.unitMemberId = unitMemberId;
	}

	public Long getAppUnitId() {
		return appUnitId;
	}

	public void setAppUnitId(Long appUnitId) {
		this.appUnitId = appUnitId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
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
