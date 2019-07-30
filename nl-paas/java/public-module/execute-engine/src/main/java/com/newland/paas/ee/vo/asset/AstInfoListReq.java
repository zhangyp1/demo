package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:AstInfoListReq
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * 
 * @author kangweixiang
 * @version
 * @Date 2018年6月22日 下午4:59:21
 * 
 * @History: // 历史修改记录  <author>  // 作者 <time>    // 修改时间 <version> // 版本 <desc>  // 描述
 */
public class AstInfoListReq implements Serializable {
    private static final long serialVersionUID = 8776906632571317387L;

    private Long assetId;

    private Long tenantId;

    private String assetName;

    private String assetStatus;

    private Short assetType;

    private List<Long> assetTypes;

    private List<Long> ownerId;

    private String providerName;

    private String categoryCode;

    private String categoryName;

    private String startTime;

    private String endTime;

    private List<AstLabelReq> assetLabel;

    public Long getAssetId() {

        return assetId;
    }

    public void setAssetId(Long assetId) {

        this.assetId = assetId;
    }

    public Long getTenantId() {

        return tenantId;
    }

    public void setTenantId(Long tenantId) {

        this.tenantId = tenantId;
    }

    public String getAssetName() {

        return assetName;
    }

    public void setAssetName(String assetName) {

        this.assetName = assetName;
    }

    public List<Long> getOwnerId() {

        return ownerId;
    }

    public void setOwnerId(List<Long> ownerId) {

        this.ownerId = ownerId;
    }

    public String getProviderName() {

        return providerName;
    }

    public void setProviderName(String providerName) {

        this.providerName = providerName;
    }

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String startTime) {

        this.startTime = startTime;
    }

    public String getEndTime() {

        return endTime;
    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;
    }

    public List<AstLabelReq> getAssetLabel() {

        return assetLabel;
    }

    public void setAssetLabel(List<AstLabelReq> assetLabel) {

        this.assetLabel = assetLabel;
    }

    public List<Long> getAssetTypes() {

        return assetTypes;
    }

    public void setAssetTypes(List<Long> assetTypes) {

        this.assetTypes = assetTypes;
    }

    public String getAssetStatus() {

        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {

        this.assetStatus = assetStatus;
    }

    public Short getAssetType() {

        return assetType;
    }

    public void setAssetType(Short assetType) {

        this.assetType = assetType;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "AstInfoListReq [assetId=" + assetId + ", tenantId=" + tenantId + ", assetName=" + assetName
            + ", assetStatus=" + assetStatus + ", ownerId=" + ownerId + ", providerName=" + providerName
            + ", categoryCode=" + categoryCode + ", categoryName=" + categoryName + ", startTime=" + startTime
            + ", endTime=" + endTime + ", assetLabel=" + assetLabel + "]";
    }
}
