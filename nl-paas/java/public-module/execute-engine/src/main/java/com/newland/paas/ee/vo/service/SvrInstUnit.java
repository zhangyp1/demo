package com.newland.paas.ee.vo.service;

import java.io.Serializable;
import java.util.Date;

/**
 * ����Ԫʵ�� SVR_INST_UNIT 
 */
public class SvrInstUnit implements Serializable {
    private static final long serialVersionUID = 42204593803789477L;

    private Long svrInstUnitId;

    private Long svrInstId;

    private Long unitId;

    private String unitName;

    private String unitVersion;

    private String unitDesc;

    private String bearTargetType;

	private Long bearTargetId;

    private Long logClusterId;

    private Long runInsNum;

    private Long minInsNum;

    private Long maxInsNum;

    private String unitStatus;

    private Float quotaCpu;

    private Float quotaMemory;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

	private String bearTargetName;

    public Long getSvrInstUnitId() {
        return svrInstUnitId;
    }

    public void setSvrInstUnitId(Long svrInstUnitId) {
        this.svrInstUnitId = svrInstUnitId;
    }

    public Long getSvrInstId() {
        return svrInstId;
    }

    public void setSvrInstId(Long svrInstId) {
        this.svrInstId = svrInstId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitVersion() {
        return unitVersion;
    }

    public void setUnitVersion(String unitVersion) {
        this.unitVersion = unitVersion;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getBearTargetType() {
        return bearTargetType;
    }

    public void setBearTargetType(String bearTargetType) {
        this.bearTargetType = bearTargetType;
    }

    public Long getBearTargetId() {
        return bearTargetId;
    }

    public void setBearTargetId(Long bearTargetId) {
        this.bearTargetId = bearTargetId;
    }

    public Long getLogClusterId() {
        return logClusterId;
    }

    public void setLogClusterId(Long logClusterId) {
        this.logClusterId = logClusterId;
    }

    public Long getRunInsNum() {
        return runInsNum;
    }

    public void setRunInsNum(Long runInsNum) {
        this.runInsNum = runInsNum;
    }

    public Long getMinInsNum() {
        return minInsNum;
    }

    public void setMinInsNum(Long minInsNum) {
        this.minInsNum = minInsNum;
    }

    public Long getMaxInsNum() {
        return maxInsNum;
    }

    public void setMaxInsNum(Long maxInsNum) {
        this.maxInsNum = maxInsNum;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public Float getQuotaCpu() {
        return quotaCpu;
    }

    public void setQuotaCpu(Float quotaCpu) {
        this.quotaCpu = quotaCpu;
    }

    public Float getQuotaMemory() {
        return quotaMemory;
    }

    public void setQuotaMemory(Float quotaMemory) {
        this.quotaMemory = quotaMemory;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getBearTargetName() {
        return bearTargetName;
    }

    public void setBearTargetName(String bearTargetName) {
        this.bearTargetName = bearTargetName;
    }
}