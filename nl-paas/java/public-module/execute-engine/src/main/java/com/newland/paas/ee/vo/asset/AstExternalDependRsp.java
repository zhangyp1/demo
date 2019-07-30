package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产外部依赖
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstExternalDependRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -447821662031625530L;

    // 外部依赖标识
    private Long extDependId;
    // 依赖名称（界面不显示）
    private String dependName;
    // 使用单元ID
    private Long unitId;
    // 使用单元名称
    private String unitName;
    // 可选必选开关
    private String required;
    // 依赖场景描述
    private String sceneDesc;
    // 接口可见性 public：公有 private：私有 指依赖外部资产接口还是内部单元接口
    private String scope;
    // 依赖限制 字典：AST_DEPEND_RESTRICT
    private String restrict;
    // 资产外部依赖项
    private List<AstDependOptionRsp> dependOptionList = Collections.emptyList();

    public Long getExtDependId() {
        return extDependId;
    }

    public void setExtDependId(Long extDependId) {
        this.extDependId = extDependId;
    }

    public String getDependName() {
        return dependName;
    }

    public void setDependName(String dependName) {
        this.dependName = dependName;
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

    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRestrict() {
        return restrict;
    }

    public void setRestrict(String restrict) {
        this.restrict = restrict;
    }

    public List<AstDependOptionRsp> getDependOptionList() {
        return dependOptionList;
    }

    public void setDependOptionList(List<AstDependOptionRsp> dependOptionList) {
        this.dependOptionList = dependOptionList;
    }

}
