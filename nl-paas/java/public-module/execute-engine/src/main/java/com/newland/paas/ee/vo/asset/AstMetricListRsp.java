package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产指标清单
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstMetricListRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1556556671572398858L;
    // 指标标识
    private Long metricId;
    // 指标分类
    private String metricCategory;
    // 指标名称
    private String metricName;
    // 指标数据类型
    private String metricDataType;
    // 指标单位
    private String metricUnit;
    // 指标级别
    private String metricLevel;
    // 指标说明
    private String metricDesc;

    public Long getMetricId() {
        return metricId;
    }

    public void setMetricId(Long metricId) {
        this.metricId = metricId;
    }

    public String getMetricCategory() {
        return metricCategory;
    }

    public void setMetricCategory(String metricCategory) {
        this.metricCategory = metricCategory;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricDataType() {
        return metricDataType;
    }

    public void setMetricDataType(String metricDataType) {
        this.metricDataType = metricDataType;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public void setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
    }

    public String getMetricLevel() {
        return metricLevel;
    }

    public void setMetricLevel(String metricLevel) {
        this.metricLevel = metricLevel;
    }

    public String getMetricDesc() {
        return metricDesc;
    }

    public void setMetricDesc(String metricDesc) {
        this.metricDesc = metricDesc;
    }
}
