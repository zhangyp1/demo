/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUrlRole;

/**
 * 描述
 * @author linkun
 * @created 2018年8月6日 下午2:54:19
 */
public class SysUrlRoleReqBo extends SysUrlRole {
	
	private String[] urls;

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	
	
}
