/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;

/**
 * 描述
 * @author linkun
 * @created 2018年7月31日 下午2:44:44
 */
public class SysUserGroupRoleReqBo extends SysUserGroupRole {
	
	/**
	 * 用户id列表
	 */
	private Long[] userIds;
	private String userNameLike;

	public Long[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	
	
	
	public String getUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(String userNameLike) {
		this.userNameLike = userNameLike;
	}



	/**
	 * 群组角色id列表
	 */
	private Long[] groupRoleIds;

	public Long[] getGroupRoleIds() {
		return groupRoleIds;
	}

	public void setGroupRoleIds(Long[] groupRoleIds) {
		this.groupRoleIds = groupRoleIds;
	}
	
	private Long[] notInUserIds;

	public Long[] getNotInUserIds() {
		return notInUserIds;
	}

	public void setNotInUserIds(Long[] notInUserIds) {
		this.notInUserIds = notInUserIds;
	}
	
	
	
	
}
