package com.newland.paas.advice.audit;

import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * 审计日志
 *
 * @author WRP
 * @since 2018/11/8
 */
public class GlbAuditLogAttrVo implements Serializable {
    private static final long serialVersionUID = 512745119446932980L;
    /**
     * 审计日志ID
     */
    private Long auditLogId;
    /**
     * 审计日志键
     */
    @Null
    private String attrKey;
    /**
     * 审计日志值
     */
    @Null
    private String attrValue;

    public Long getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(Long auditLogId) {
        this.auditLogId = auditLogId;
    }

    public String getAttrKey() {
        return attrKey;
    }

    public void setAttrKey(String attrKey) {
        this.attrKey = attrKey;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
   