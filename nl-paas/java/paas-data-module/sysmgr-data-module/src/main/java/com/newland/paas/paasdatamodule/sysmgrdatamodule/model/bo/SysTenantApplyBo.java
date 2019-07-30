package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;

/**
 * 描述
 * @author zhoufl
 * @created 2018年8月17日
 */
public class SysTenantApplyBo extends SysTenant {
	private String applyReason;

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
}
