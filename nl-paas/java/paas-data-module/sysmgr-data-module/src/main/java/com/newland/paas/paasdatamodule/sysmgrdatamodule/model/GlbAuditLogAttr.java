package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:PanYang
 * Date:Created in 下午2:58 2018/7/30
 * Modified By:
 */
public class GlbAuditLogAttr implements Serializable
{
	private static final long serialVersionUID = 512745119446932980L;
	//审计日志ID
	private Long auditLogId;
	//审计日志键
	private String attrKey;
	//审计日志值
	private String attrValue;
	
	public Long getAuditLogId()
	{
		return auditLogId;
	}
	
	public void setAuditLogId(Long auditLogId)
	{
		this.auditLogId = auditLogId;
	}
	
	public String getAttrKey()
	{
		return attrKey;
	}
	
	public void setAttrKey(String attrKey)
	{
		this.attrKey = attrKey;
	}
	
	public String getAttrValue()
	{
		return attrValue;
	}
	
	public void setAttrValue(String attrValue)
	{
		this.attrValue = attrValue;
	}
}
   