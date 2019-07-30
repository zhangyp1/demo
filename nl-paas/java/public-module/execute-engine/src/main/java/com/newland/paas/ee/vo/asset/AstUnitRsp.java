package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产单元
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstUnitRsp implements Serializable {

    /**
     * 唯一标志
     */
    private static final long serialVersionUID = -6381908608712753702L;

    // 单元标识
    private Long unitId;
    // 单元类型
    private String unitType;
    // 单元名称
    private String unitName;
    // 单元版本
    private String unitVersion;
    // 承载目标类型 字典：k8s k8s集群,metalMachine 物理机集群,asset 资产
    private String deployTargetType;
    // 单元包内容路径
    private String unitPath;
    // 文件地址(服务器地址)
    private String fileUrl;
    // 单元描述
    private String unitDesc;
    // 单元属性
    private String unitAttribute;
    // 重启策略
    private String restartPolicy;
    // 是否允许弹性
    private Long elastic;
    // 依赖框架
    private String framework;
    // 框架版本范围（支持:1.0 [1.0, 3.5] >4.0）
    private String frameworkVersionRange;
    // 单元包含的模块
    private List<AstUnitModuleRsp> unitModules = Collections.emptyList();
    // 单元包含的配置
    private List<AstUnitConfigListRsp> unitConfigs = Collections.emptyList();
    // 单元承载关系
    private List<AstAppletDeployRsp> unitDeploys = Collections.emptyList();

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public String getDeployTargetType() {
        return deployTargetType;
    }

    public void setDeployTargetType(String deployTargetType) {
        this.deployTargetType = deployTargetType;
    }

    public String getUnitPath() {
        return unitPath;
    }

    public void setUnitPath(String unitPath) {
        this.unitPath = unitPath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getUnitAttribute() {
        return unitAttribute;
    }

    public void setUnitAttribute(String unitAttribute) {
        this.unitAttribute = unitAttribute;
    }

    public String getRestartPolicy() {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public Long getElastic() {
        return elastic;
    }

    public void setElastic(Long elastic) {
        this.elastic = elastic;
    }

    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    public String getFrameworkVersionRange() {
        return frameworkVersionRange;
    }

    public void setFrameworkVersionRange(String frameworkVersionRange) {
        this.frameworkVersionRange = frameworkVersionRange;
    }

    public List<AstUnitModuleRsp> getUnitModules() {
        return unitModules;
    }

    public void setUnitModules(List<AstUnitModuleRsp> unitModules) {
        this.unitModules = unitModules;
    }

    public List<AstUnitConfigListRsp> getUnitConfigs() {
        return unitConfigs;
    }

    public void setUnitConfigs(List<AstUnitConfigListRsp> unitConfigs) {
        this.unitConfigs = unitConfigs;
    }

    public List<AstAppletDeployRsp> getUnitDeploys() {
        return unitDeploys;
    }

    public void setUnitDeploys(List<AstAppletDeployRsp> unitDeploys) {
        this.unitDeploys = unitDeploys;
    }

}
