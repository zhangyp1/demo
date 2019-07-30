package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 审计日志
 * Author:PanYang
 * Date:Created in 下午2:46 2018/7/30
 * Modified By:
 */
public class GlbAudit implements Serializable
{
	private static final long serialVersionUID = 512745119446932980L;
	//主键id
	private Long auditLogId;
	//ip地址
	private String ip;
	//平台模块 字典：PF_MODULE 系统 sysmgr；公共 common ；集群 clumgr；资源 resmgr；应用 appmgr；服务 svrmgr；资产 astmgr
	private String auditModule;
	//版本
	private String auditVersion;
	//审计类别 字典：AUDIT_CATEGORY object_operate对象操作
	private String auditCategory;
	//描述
	private String auditDesc;
	//创建时间
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	//修改时间
	private Date changeTime;
	
	private String operate_account;
	
	private String operate_name;
	
	private String object_type;
	
	private String operate_code;

	private String object_name;

	private String myDate;

	private String startTime;

	private String endTime;

	public Long getAuditLogId()
	{
		return auditLogId;
	}

	public void setAuditLogId(Long auditLogId)
	{
		this.auditLogId = auditLogId;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getAuditModule()
	{
		return auditModule;
	}

	public void setAuditModule(String auditModule)
	{
		this.auditModule = auditModule;
	}

	public String getAuditVersion()
	{
		return auditVersion;
	}

	public void setAuditVersion(String auditVersion)
	{
		this.auditVersion = auditVersion;
	}

	public String getAuditCategory()
	{
		return auditCategory;
	}

	public void setAuditCategory(String auditCategory)
	{
		this.auditCategory = auditCategory;
	}

	public String getAuditDesc()
	{
		return auditDesc;
	}

	public void setAuditDesc(String auditDesc)
	{
		this.auditDesc = auditDesc;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getChangeTime()
	{
		return changeTime;
	}

	public void setsetCreateTimeChangeTime(Date changeTime)
	{
		this.changeTime = changeTime;
	}

	public String getOperate_account()
	{
		return operate_account;
	}

	public void setOperate_account(String operate_account)
	{
		this.operate_account = operate_account;
	}

	public String getOperate_name()
	{
		return operate_name;
	}

	public void setOperate_name(String operate_name)
	{
		this.operate_name = operate_name;
	}

	public String getObject_type()
	{
		return object_type;
	}

	public void setObject_type(String object_type)
	{
		this.object_type = object_type;
	}

	public String getOperate_code()
	{
		return operate_code;
	}

	public void setOperate_code(String operate_code)
	{
		this.operate_code = operate_code;
	}

	public String getMyDate()
	{
		return myDate;
	}

	public void setMyDate(String myDate)
	{
		this.myDate = myDate;
	}

	public String getObject_name()
	{
		return object_name;
	}

	public void setObject_name(String object_name)
	{
		this.object_name = object_name;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
