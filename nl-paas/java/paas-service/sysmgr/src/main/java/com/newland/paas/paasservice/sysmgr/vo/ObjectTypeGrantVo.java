package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象类型授权Vo
 *
 * @author zhongqingjiang
 */
public class ObjectTypeGrantVo implements Serializable {

    public ObjectTypeGrantVo() {
        sysCategoryGrantList = new ArrayList<>();
    }

    private String objTypeCode;
    private String objTypeName;
    List<SysCategoryGrantVo> sysCategoryGrantList;

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

    public List<SysCategoryGrantVo> getSysCategoryGrantList() {
        return sysCategoryGrantList;
    }

    public void setSysCategoryGrantList(List<SysCategoryGrantVo> sysCategoryGrantList) {
        this.sysCategoryGrantList = sysCategoryGrantList;
    }
}
