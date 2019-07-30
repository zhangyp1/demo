package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.util.List;

/**
 * Author:PanYang
 * Date:Created in 上午10:03 2018/8/4
 * Modified By:
 */
public class AuditLog
{
	private GlbAudit glbAudit;
	
	private List<GlbAuditLogAttr> glbAuditLogAttr;
	
	public GlbAudit getGlbAudit()
	{
		return glbAudit;
	}
	
	public void setGlbAudit(GlbAudit glbAudit)
	{
		this.glbAudit = glbAudit;
	}
	
	public List<GlbAuditLogAttr> getGlbAuditLogAttr()
	{
		return glbAuditLogAttr;
	}
	
	public void setGlbAuditLogAttr(List<GlbAuditLogAttr> glbAuditLogAttr)
	{
		this.glbAuditLogAttr = glbAuditLogAttr;
	}
	
	@Override public String toString()
	{
		return "AuditLog{" + "glbAudit=" + glbAudit + ", glbAuditLogAttr=" + glbAuditLogAttr + '}';
	}
}
   