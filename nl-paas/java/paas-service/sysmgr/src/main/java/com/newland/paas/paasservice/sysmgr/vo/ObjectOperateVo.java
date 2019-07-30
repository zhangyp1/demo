package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 对象操作
 *
 * @author zhongqingjiang
 */
public class ObjectOperateVo implements Serializable {

    public ObjectOperateVo() {

    }

    public ObjectOperateVo(String operateCode, String operateName) {
        this.operateCode = operateCode;
        this.operateName = operateName;
    }

    public String operateCode;
    public String operateName;

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }
}
