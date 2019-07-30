/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;

/**
 * 描述
 * @author linkun
 * @created 2018年8月8日 下午5:02:41
 */
public class SysRoleUserRespBo extends SysRoleUser {

	private String userName;
	private String roleName;
	private String account;
	private Long roleUserId;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Long getRoleUserId() {
		return roleUserId;
	}
	public void setRoleUserId(Long roleUserId) {
		this.roleUserId = roleUserId;
	}


}
