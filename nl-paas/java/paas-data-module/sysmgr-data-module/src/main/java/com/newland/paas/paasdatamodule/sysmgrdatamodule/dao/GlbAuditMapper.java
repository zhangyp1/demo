package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAudit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAuditLogAttr;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;

import java.util.List;

/**
 * Author:PanYang
 * Date:Created in 下午3:24 2018/7/30
 * Modified By:
 */
public interface GlbAuditMapper
{
	List<GlbAudit> listGlbAudit(GlbAudit glbAudit);
	
	List<GlbAuditLogAttr> listGlbAuditObjOperate(GlbAuditLogAttr glbAuditLogAttr);
	
	//保存审计日志
	Long  saveAudit(GlbAudit glbAudit);
	//保存审计日志扩展表
	void saveAuditAttr(GlbAuditLogAttr glbAuditLogAttr);
	
	List<SysUser> getUserByName(SysUser record);
	
	
}
   