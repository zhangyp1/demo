package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.io.Serializable;
import java.util.Date;

public class SysEnvConfigBo implements Serializable {

    public SysEnvConfigBo() {
    }

    public SysEnvConfigBo(String envConfigKey, String envConfigValue) {
        this.envConfigKey = envConfigKey;
        this.envConfigValue = envConfigValue;
    }

    private String envConfigKey;

    private String envConfigValue;

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
}