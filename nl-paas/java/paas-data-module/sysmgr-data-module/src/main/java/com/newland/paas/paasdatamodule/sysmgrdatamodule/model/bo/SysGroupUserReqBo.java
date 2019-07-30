/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;

/**
 * 描述
 * @author linkun
 * @created 2018-08-01 13:57:24
 */
public class SysGroupUserReqBo extends SysGroupUser {
	
	private Long[] groupIds;
	private Long[] userIds;
	//设置管理员使用
	private Long groupUserId;
	public Long[] getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(Long[] groupIds) {
		this.groupIds = groupIds;
	}
	public Long[] getUserIds() {
		return userIds;
	}
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	public Long getGroupUserId() {
		return groupUserId;
	}
	public void setGroupUserId(Long groupUserId) {
		this.groupUserId = groupUserId;
	}
	
	private String userNameLike;
	public String getUserNameLike() {
		return userNameLike;
	}
	public void setUserNameLike(String userNameLike) {
		this.userNameLike = userNameLike;
	}
	
	private Long[] notInUserIds;
	public Long[] getNotInUserIds() {
		return notInUserIds;
	}
	public void setNotInUserIds(Long[] notInUserIds) {
		this.notInUserIds = notInUserIds;
	}
	
	
	
	
}
