package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产单元模块
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstUnitModuleRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5349074556432571116L;

    // 单元模块标识
    private Long unitModuleId;
    // 模块名称
    private String moduleName;
    // 模块版本
    private String moduleVersion;
    // 模块类型
    private String moduleType;
    // 模块描述
    private String moduleDesc;
    // 模块属性
    private String moduleAttribute;

    public Long getUnitModuleId() {
        return unitModuleId;
    }

    public void setUnitModuleId(Long unitModuleId) {
        this.unitModuleId = unitModuleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getModuleAttribute() {
        return moduleAttribute;
    }

    public void setModuleAttribute(String moduleAttribute) {
        this.moduleAttribute = moduleAttribute;
    }
}
