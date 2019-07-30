package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产单元配置
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstUnitConfigListRsp implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2901724243261506598L;

    private Long unitId;

    private String name;

    private String configKey;

    private String configName;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

}
