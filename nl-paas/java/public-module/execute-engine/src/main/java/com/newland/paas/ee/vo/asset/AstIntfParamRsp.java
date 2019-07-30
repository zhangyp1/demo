package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产外部控制接口参数
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstIntfParamRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6839465491620405138L;
    // 参数标识
    private Long paramId;
    // 参数名称
    private String paramName;
    // 参数数据类型
    private String paramDataType;
    // 默认值
    private String defaultValue;
    // 枚举值或值域区间
    private String enumOrRangeValue;
    // 校验正则表达式
    private String verifyRegex;
    // 选项类型(是否必填)
    private String optionalType;
    // 参数描述(说明)
    private String paramDesc;

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(String paramDataType) {
        this.paramDataType = paramDataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEnumOrRangeValue() {
        return enumOrRangeValue;
    }

    public void setEnumOrRangeValue(String enumOrRangeValue) {
        this.enumOrRangeValue = enumOrRangeValue;
    }

    public String getVerifyRegex() {
        return verifyRegex;
    }

    public void setVerifyRegex(String verifyRegex) {
        this.verifyRegex = verifyRegex;
    }

    public String getOptionalType() {
        return optionalType;
    }

    public void setOptionalType(String optionalType) {
        this.optionalType = optionalType;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

}