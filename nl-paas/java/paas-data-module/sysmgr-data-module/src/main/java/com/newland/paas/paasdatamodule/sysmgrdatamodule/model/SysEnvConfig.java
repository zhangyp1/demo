package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysEnvConfig implements Serializable {
    private static final long serialVersionUID = 660219947786939006L;

    private Long envConfigId;

    private String envConfigKey;

    private String envConfigValue;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Long delFlag;

    public Long getEnvConfigId() {
        return envConfigId;
    }

    public void setEnvConfigId(Long envConfigId) {
        this.envConfigId = envConfigId;
    }

    public String getEnvConfigKey() {
        return envConfigKey;
    }

    public void setEnvConfigKey(String envConfigKey) {
        this.envConfigKey = envConfigKey == null ? null : envConfigKey.trim();
    }

    public String getEnvConfigValue() {
        return envConfigValue;
    }

    public void setEnvConfigValue(String envConfigValue) {
        this.envConfigValue = envConfigValue == null ? null : envConfigValue.trim();
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

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}