package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 系统分组对象请求Vo
 *
 * @author zhongqingjiang
 */
public class SysCategoryObjReqVo implements Serializable {

    private Long sysCategoryId;
    private Boolean bGrant;
    private String likeObjName;
    private String objType;

    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
    }

    public Boolean getbGrant() {
        return bGrant;
    }

    public void setbGrant(Boolean bGrant) {
        this.bGrant = bGrant;
    }

    public String getLikeObjName() {
        return likeObjName;
    }

    public void setLikeObjName(String likeObjName) {
        this.likeObjName = likeObjName;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }
}
