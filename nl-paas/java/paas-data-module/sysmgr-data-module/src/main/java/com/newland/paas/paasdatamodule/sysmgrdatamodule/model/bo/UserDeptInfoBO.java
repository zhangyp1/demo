package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
/**
 * 用户所属部门的信息列表（根据表结构的设计考虑后续复用的情况）
 * @author caifeitong
 */
public class UserDeptInfoBO {
	/**
	 * 部门ID
	 */
	private String deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	
	public String getDeptId() {
		return deptId;
	}
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
