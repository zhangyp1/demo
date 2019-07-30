package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

/**
 * @Description: 分页成员列表查询接口请求参数BO
 * @Author: SongYJ
 * @Date: 2018/8/9
 */
public class SysTenantMemberReqBO {
	private Long tenantId;
	private String userNameLike;

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(String userNameLike) {
		this.userNameLike = userNameLike;
	}

}
