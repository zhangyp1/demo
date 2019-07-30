package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象类型
 *
 * @author zhongqingjiang
 */
public class ObjectTypeVo implements Serializable {

    public ObjectTypeVo() {

    }

    public ObjectTypeVo(String objTypeCode, String objTypeName) {
        this.objTypeCode = objTypeCode;
        this.objTypeName = objTypeName;
        this.operates = new ArrayList<>();
    }

    private String objTypeCode;

    private String objTypeName;

    List<GlbDict> operates;

    public String getObjTypeCode() {
        return objTypeCode;
    }

    public void setObjTypeCode(String objTypeCode) {
        this.objTypeCode = objTypeCode;
    }

    public String getObjTypeName() {
        return objTypeName;
    }

    public void setObjTypeName(String objTypeName) {
        this.objTypeName = objTypeName;
    }

    public List<GlbDict> getOperates() {
        return operates;
    }

    public void setOperates(List<GlbDict> operates) {
        this.operates = operates;
    }
}
