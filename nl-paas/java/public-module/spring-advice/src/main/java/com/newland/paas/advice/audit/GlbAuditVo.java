package com.newland.paas.advice.audit;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 审计日志
 *
 * @author WRP
 * @since 2018/11/8
 */
public class GlbAuditVo implements Serializable {
    private static final long serialVersionUID = 512745119446932980L;
    /**
     * 主键id
     */
    private Long auditLogId;
    /**
     * ip地址
     */
    @Null
    private String ip;
    /**
     * 平台模块 字典：PF_MODULE 系统 sysmgr；公共 common ；集群 clumgr；资源 resmgr；应用 appmgr；服务 svrmgr；资产 astmgr
     */
    @Null
    private String module;
    /**
     * 版本
     */
    @Null
    private String version;
    /**
     * 审计类别 字典：AUDIT_CATEGORY object_operate对象操作，默认[system_operate]
     */
    @Null
    private String category;
    /**
     * 描述
     */
    @Null
    private String desc;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeTime;

    private String date;


    public Long getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(Long auditLogId) {
        this.auditLogId = auditLogId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
