/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;

/**
 * 描述
 * @author linkun
 * @created 2018年7月31日 下午2:45:06
 */
public class SysUserGroupRoleRespBo extends SysUserGroupRole {
	
	private String groupRoleName;
	private String userName;
	private String userDept;
	private String userPhone;
	public String getGroupRoleName() {
		return groupRoleName;
	}
	public void setGroupRoleName(String groupRoleName) {
		this.groupRoleName = groupRoleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDept() {
		return userDept;
	}
	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	
}
