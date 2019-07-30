package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产小应用承载
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstAppletDeployRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2730018862527404611L;

    // 小应用承载标识
    private Long appletDeployId;
    // 目标资产名称
    private String targetAssetName;
    // 目标资产版本范围
    private String targetAssetVersionRange;
    // 承载接口方法名称
    private String intfMethodName;
    // 单元id
    private Long unitId;
    // 目标资产单元名称
    private String unitName;
    // 承载描述
    private String deployDesc;

    public Long getAppletDeployId() {
        return appletDeployId;
    }

    public void setAppletDeployId(Long appletDeployId) {
        this.appletDeployId = appletDeployId;
    }

    public String getTargetAssetName() {
        return targetAssetName;
    }

    public void setTargetAssetName(String targetAssetName) {
        this.targetAssetName = targetAssetName;
    }

    public String getTargetAssetVersionRange() {
        return targetAssetVersionRange;
    }

    public void setTargetAssetVersionRange(String targetAssetVersionRange) {
        this.targetAssetVersionRange = targetAssetVersionRange;
    }

    public String getIntfMethodName() {
        return intfMethodName;
    }

    public void setIntfMethodName(String intfMethodName) {
        this.intfMethodName = intfMethodName;
    }

    public String getDeployDesc() {
        return deployDesc;
    }

    public void setDeployDesc(String deployDesc) {
        this.deployDesc = deployDesc;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
