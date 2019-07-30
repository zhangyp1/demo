/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantLimit;

import java.util.List;

/**
 * 描述
 * @author linkun
 * @created 2018年8月16日 上午10:27:02
 */
public class SysTenantReqBo extends SysTenant {
	private String description;
	private Long tenantId;
	/**
	 * 租户资源配额项
	 */
	private List<SysTenantLimit> tenantLimits;
	
	private RoleUserReqBo[] roleUsers;

	public RoleUserReqBo[] getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(RoleUserReqBo[] roleUsers) {
		this.roleUsers = roleUsers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<SysTenantLimit> getTenantLimits() {
		return tenantLimits;
	}

	public void setTenantLimits(List<SysTenantLimit> tenantLimits) {
		this.tenantLimits = tenantLimits;
	}
}
