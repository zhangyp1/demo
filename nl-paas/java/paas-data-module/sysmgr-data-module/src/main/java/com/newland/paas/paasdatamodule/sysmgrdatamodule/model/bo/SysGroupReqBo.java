/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

/**
 * 描述
 * @author linkun
 * @created 2018年7月30日 下午6:24:52
 */
public class SysGroupReqBo extends SysGroup {
	
	/**
	 * 不等于id
	 */
	private Long neqId ;
	/**
	 * 模糊查询
	 */
	private String groupNameLike;

	public Long getNeqId() {
		return neqId;
	}

	public void setNeqId(Long neqId) {
		this.neqId = neqId;
	}

	public String getGroupNameLike() {
		return groupNameLike;
	}

	public void setGroupNameLike(String groupNameLike) {
		this.groupNameLike = groupNameLike;
	}
	
	private List<String> parentGroupNames;

	public List<String> getParentGroupNames() {
		return parentGroupNames;
	}

	public void setParentGroupNames(List<String> parentGroupNames) {
		this.parentGroupNames = parentGroupNames;
	}

	
	
}
