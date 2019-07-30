package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 运行实例
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstInstancePropRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1349225276261286003L;

    // 实例属性标识
    private Long instancePropId;
    // 属性名称
    private String propName;
    // 属性数据类型
    private String propDataType;
    // 属性描述
    private String propDesc;

    public Long getInstancePropId() {
        return instancePropId;
    }

    public void setInstancePropId(Long instancePropId) {
        this.instancePropId = instancePropId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropDataType() {
        return propDataType;
    }

    public void setPropDataType(String propDataType) {
        this.propDataType = propDataType;
    }

    public String getPropDesc() {
        return propDesc;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }
}
