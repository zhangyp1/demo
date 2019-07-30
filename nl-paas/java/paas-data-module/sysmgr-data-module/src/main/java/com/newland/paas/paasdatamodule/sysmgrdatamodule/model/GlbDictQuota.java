package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;

/**
 * 资源配额项
 * @author wrp
 * @Date 2018/6/26
 */
public class GlbDictQuota implements Serializable {
    private static final long serialVersionUID = 927214099955836056L;

    private String quotaCode;

    private String unit;

    private Integer min;

    private Integer max;

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode == null ? null : quotaCode.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}