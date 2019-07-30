package com.newland.paas.ee.vo.application;

/**
 * @author wq <br>
 * @version 1.0
 * @date 2018/11/13 15:10 <br>
 */
public class AppEndpointProp {
    private Long endpointPropId;

    private Long endpointId;

    private Long appInfoId;

    private String propKey;

    private String propVal;

    public Long getEndpointPropId() {
        return endpointPropId;
    }

    public void setEndpointPropId(Long endpointPropId) {
        this.endpointPropId = endpointPropId;
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public Long getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(Long appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey == null ? null : propKey.trim();
    }

    public String getPropVal() {
        return propVal;
    }

    public void setPropVal(String propVal) {
        this.propVal = propVal == null ? null : propVal.trim();
    }

}
