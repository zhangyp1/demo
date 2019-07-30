package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产配置信息
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstConfigListRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6979927138073792038L;

    // 配置项标识
    private Long cfgItemId;
    // 配置分组名称（英文）
    private String groupName;
    // 配置分组显示名称
    private String groupShowName;
    // 配置项名称
    private String cfgItemName;
    // 配置数据类型（枚举类型时，每个枚举项返回一条完整记录，把枚举项值存放到取值范围字段）
    private String cfgDataType;
    // 默认值
    private String defaultValue;
    // 配置项生效方式
    private String effectType;
    // 配置项描述(说明)
    private String cfgDesc;
    // 枚举值或值域区间
    private String enumOrRangeValue;
    // 校验正则表达式
    private String verifyRegex;

    public Long getCfgItemId() {
        return cfgItemId;
    }

    public void setCfgItemId(Long cfgItemId) {
        this.cfgItemId = cfgItemId;
    }

    public String getCfgItemName() {
        return cfgItemName;
    }

    public void setCfgItemName(String cfgItemName) {
        this.cfgItemName = cfgItemName;
    }

    public String getCfgDataType() {
        return cfgDataType;
    }

    public void setCfgDataType(String cfgDataType) {
        this.cfgDataType = cfgDataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCfgDesc() {
        return cfgDesc;
    }

    public void setCfgDesc(String cfgDesc) {
        this.cfgDesc = cfgDesc;
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

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupShowName() {
        return groupShowName;
    }

    public void setGroupShowName(String groupShowName) {
        this.groupShowName = groupShowName;
    }
}
