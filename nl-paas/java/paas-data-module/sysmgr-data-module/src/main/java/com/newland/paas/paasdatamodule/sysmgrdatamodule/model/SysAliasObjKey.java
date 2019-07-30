package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;

public class SysAliasObjKey implements Serializable {
    private static final long serialVersionUID = 279368277158034294L;

    private String aliasObjCode;

    private String aliasObjType;

    private String objCode;

    public String getAliasObjCode() {
        return aliasObjCode;
    }

    public void setAliasObjCode(String aliasObjCode) {
        this.aliasObjCode = aliasObjCode == null ? null : aliasObjCode.trim();
    }

    public String getAliasObjType() {
        return aliasObjType;
    }

    public void setAliasObjType(String aliasObjType) {
        this.aliasObjType = aliasObjType == null ? null : aliasObjType.trim();
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode == null ? null : objCode.trim();
    }
}