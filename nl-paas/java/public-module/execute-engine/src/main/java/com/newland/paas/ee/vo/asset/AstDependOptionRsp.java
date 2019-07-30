package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产外部依赖项
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstDependOptionRsp implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2670019962070149929L;

    // 外部依赖ID
    private Long extDependId;
    // 目标资产名称
    private String targetAssetName;
    // 目标资产版本范围用英文逗号分割
    private String targetAssetVersionRange;
    // 依赖目标资产的接口方法名称
    private String intfMethodNames;
    // 依赖接口名称
    private String intfKey;

    public Long getExtDependId() {
        return extDependId;
    }

    public void setExtDependId(Long extDependId) {
        this.extDependId = extDependId;
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

    public String getIntfMethodNames() {
        return intfMethodNames;
    }

    public void setIntfMethodNames(String intfMethodNames) {
        this.intfMethodNames = intfMethodNames;
    }

    public String getIntfKey() {
        return intfKey;
    }

    public void setIntfKey(String intfKey) {
        this.intfKey = intfKey;
    }

}
