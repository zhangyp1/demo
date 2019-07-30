package com.newland.paas.advice.audit;

import java.util.ArrayList;
import java.util.List;

/**
 * 审计日志
 *
 * @author WRP
 * @since 2018/11/8
 */
public class AuditVo {
    private GlbAuditVo glbAudit;

    private List<GlbAuditLogAttrVo> glbAuditLogAttr = new ArrayList<>();

    public GlbAuditVo getGlbAudit() {
        return glbAudit;
    }

    public void setGlbAudit(GlbAuditVo glbAudit) {
        this.glbAudit = glbAudit;
    }

    public List<GlbAuditLogAttrVo> getGlbAuditLogAttr() {
        return glbAuditLogAttr;
    }

    public void setGlbAuditLogAttr(List<GlbAuditLogAttrVo> glbAuditLogAttr) {
        this.glbAuditLogAttr = glbAuditLogAttr;
    }

    @Override
    public String toString() {
        return "AuditLog{" + "glbAudit=" + glbAudit + ", glbAuditLogAttr=" + glbAuditLogAttr + '}';
    }
}
