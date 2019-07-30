package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产外部服务接口参数
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstExternalSvrIntfAttrRsp implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4723530714727554668L;
    // 键
    private String attrKey;
    // 值
    private String attrValue;

    public String getAttrKey() {
        return attrKey;
    }

    public void setAttrKey(String attrKey) {
        this.attrKey = attrKey;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
