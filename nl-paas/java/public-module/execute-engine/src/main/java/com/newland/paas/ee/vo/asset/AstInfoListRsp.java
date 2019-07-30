package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:AstInfoListRsp
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * 
 * @author kangweixiang
 * @version
 * @Date 2018年6月25日 上午9:07:10
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class AstInfoListRsp implements Serializable{
    private static final long serialVersionUID = 1L;

    private String logoPathName;

    private String assetName;

    private List<AstVersionRsp> assetVersions;

    private String assetType;

    private String assetStatus;
    
    private Long saleStatus;

    private Long creatorName;

    private Date createDate;

    private String providerName;

    private String summaryDesc;
    
    private String selectedVersion;// 当前选择版本
    
    private Long tenantId;

    

    public String getLogoPathName() {
    
        return logoPathName;
    }

    public void setLogoPathName(String logoPathName) {
    
        this.logoPathName = logoPathName;
    }

    public String getAssetName() {
    
        return assetName;
    }

    public void setAssetName(String assetName) {
    
        this.assetName = assetName;
    }

    public List<AstVersionRsp> getAssetVersions() {
    
        return assetVersions;
    }

    public void setAssetVersions(List<AstVersionRsp> assetVersions) {
    
        this.assetVersions = assetVersions;
    }

    public String getAssetType() {
    
        return assetType;
    }

    public void setAssetType(String assetType) {
    
        this.assetType = assetType;
    }

    public String getAssetStatus() {
    
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
    
        this.assetStatus = assetStatus;
    }

    public Long getSaleStatus() {
    
        return saleStatus;
    }

    public void setSaleStatus(Long saleStatus) {
    
        this.saleStatus = saleStatus;
    }

    public Long getCreatorName() {
    
        return creatorName;
    }

    public void setCreatorName(Long creatorName) {
    
        this.creatorName = creatorName;
    }

    public Date getCreateDate() {
    
        return createDate;
    }

    public void setCreateDate(Date createDate) {
    
        this.createDate = createDate;
    }

    public String getProviderName() {
    
        return providerName;
    }

    public void setProviderName(String providerName) {
    
        this.providerName = providerName;
    }

    public String getSummaryDesc() {
    
        return summaryDesc;
    }

    public void setSummaryDesc(String summaryDesc) {
    
        this.summaryDesc = summaryDesc;
    }

    public String getSelectedVersion() {
    
        return selectedVersion;
    }

    public void setSelectedVersion(String selectedVersion) {
    
        this.selectedVersion = selectedVersion;
    }

    public Long getTenantId() {
    
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
    
        this.tenantId = tenantId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assetName == null) ? 0 : assetName.hashCode());
        result = prime * result
                + ((assetStatus == null) ? 0 : assetStatus.hashCode());
        result = prime * result
                + ((assetType == null) ? 0 : assetType.hashCode());
        result = prime * result
                + ((creatorName == null) ? 0 : creatorName.hashCode());
        result = prime * result
                + ((logoPathName == null) ? 0 : logoPathName.hashCode());
        result = prime * result
                + ((providerName == null) ? 0 : providerName.hashCode());
        result = prime * result
                + ((saleStatus == null) ? 0 : saleStatus.hashCode());
        result = prime * result
                + ((summaryDesc == null) ? 0 : summaryDesc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AstInfoListRsp other = (AstInfoListRsp)obj;
        if (assetName == null) {
            if (other.assetName != null)
                return false;
        } else if (!assetName.equals(other.assetName))
            return false;
        if (assetStatus == null) {
            if (other.assetStatus != null)
                return false;
        } else if (!assetStatus.equals(other.assetStatus))
            return false;
        if (assetType == null) {
            if (other.assetType != null)
                return false;
        } else if (!assetType.equals(other.assetType))
            return false;
        if (creatorName == null) {
            if (other.creatorName != null)
                return false;
        } else if (!creatorName.equals(other.creatorName))
            return false;
        if (logoPathName == null) {
            if (other.logoPathName != null)
                return false;
        } else if (!logoPathName.equals(other.logoPathName))
            return false;
        if (providerName == null) {
            if (other.providerName != null)
                return false;
        } else if (!providerName.equals(other.providerName))
            return false;
        if (saleStatus == null) {
            if (other.saleStatus != null)
                return false;
        } else if (!saleStatus.equals(other.saleStatus))
            return false;
        if (summaryDesc == null) {
            if (other.summaryDesc != null)
                return false;
        } else if (!summaryDesc.equals(other.summaryDesc))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AstInfoListRsp [logoPathName=" + logoPathName + ", assetName="
                + assetName + ", assetVersions=" + assetVersions
                + ", assetType=" + assetType + ", assetStatus=" + assetStatus
                + ", creatorName=" + creatorName + ", createDate=" + createDate
                + ", providerName=" + providerName + ", summaryDesc="
                + summaryDesc + "]";
    }

}
