/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;

/**
 * 描述
 * @author linkun
 * @created 2018年8月8日 下午5:02:22
 */
public class SysRoleUserReqBo extends SysRoleUser {
	
	private RoleUserReqBo[] roleUsers;
	
	//设置管理员使用
	private Long roleUserId;

	public RoleUserReqBo[] getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(RoleUserReqBo[] roleUsers) {
		this.roleUsers = roleUsers;
	}

	public Long getRoleUserId() {
		return roleUserId;
	}

	public void setRoleUserId(Long roleUserId) {
		this.roleUserId = roleUserId;
	}

	
}

