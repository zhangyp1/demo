package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.AuditLog;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAudit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAuditLogAttr;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * Author:PanYang
 * Date:Created in 下午3:06 2018/7/30
 * Modified By:
 */
public interface GlbAuditService {
    /**
     *分页查询审计日志
     * @param glbAudit
     * @param pageInfo
     * @return
     */
    ResultPageData getAllGlbAudit(GlbAudit glbAudit, PageInfo pageInfo);

    /**
     *查询审计日志关联对象
     * @param id
     * @return
     */
    List<GlbAuditLogAttr> getAllGlbAuditOperate(Long id);

    /**
     *审计日志入库
     * @param auditLogVo
     */
    void putAudit(AuditLog auditLogVo);

    /**
     * 审计日志写入xml
     * @param al
     * @param account
     * @param objectType
     * @param objectId
     * @param operateCode
     * @param operateName
     */
    void writeAuditDate(AuditLog al, String account, String objectType,
                        String objectId, String operateCode, String operateName);
}
