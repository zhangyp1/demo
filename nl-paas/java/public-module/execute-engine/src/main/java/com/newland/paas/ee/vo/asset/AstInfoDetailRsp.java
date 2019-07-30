package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产明细
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstInfoDetailRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8978704867778260729L;

    // 资产ID
    private Long assetId;
    // 资产名称
    private String assetName;
    // 当前选择版本
    private String selectedVersion;
    // 资产状态
    private String assetStatus;
    // 资产类别
    private String assetCatalog;
    // 资产类型
    private String assetType;
    // 发布类型
    private String publishType;
    // 销售状态
    private String saleStatus;
    // 提供方名称
    private String providerName;
    // 所属工组
    private Long belongGroup;
    // 创建者ID
    private Long creatorId;
    // 租户ID
    private Long tenantId;
    // 创建时间
    private String createDate;
    // 资产标签
    private List<String> assetLabel;
    // 资产介绍（资产概要描述）
    private String summaryDesc;
    // 变更说明（资产详细描述）
    private String detailDesc;
    // 资产图标文件全路径名称
    private String logoPathName;
    // 资产包地址
    private String assetServerAddr;
    // 资产镜像地址
    private String imageRegistryAddr;
    // 是否跨集群
    private Long crossCluster;
    // 配置清单
    private List<AstConfigListRsp> configList = Collections.emptyList();
    // 文档列表
    private List<AstUseDocRsp> docList = Collections.emptyList();
    // 指标信息
    private List<AstMetricListRsp> metricList = Collections.emptyList();
    // 实例属性
    private List<AstInstancePropRsp> instanceProperties = Collections.emptyList();
    // 单元列表
    private List<AstUnitRsp> units = Collections.emptyList();
    // 应用模块
    private List<AstModuleRsp> appModules = Collections.emptyList();
    // 控制接口
    private List<AstExternalCtlIntfRsp> ctlInterfaces = Collections.emptyList();
    // 服务接口
    private List<AstExternalSvrIntfRsp> svrInterfaces = Collections.emptyList();
    // 外部依赖关系
    private List<AstExternalDependRsp> extDepends = Collections.emptyList();

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getSelectedVersion() {
        return selectedVersion;
    }

    public void setSelectedVersion(String selectedVersion) {
        this.selectedVersion = selectedVersion;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getAssetCatalog() {
        return assetCatalog;
    }

    public void setAssetCatalog(String assetCatalog) {
        this.assetCatalog = assetCatalog;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Long getBelongGroup() {
        return belongGroup;
    }

    public void setBelongGroup(Long belongGroup) {
        this.belongGroup = belongGroup;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<String> getAssetLabel() {
        return assetLabel;
    }

    public void setAssetLabel(List<String> assetLabel) {
        this.assetLabel = assetLabel;
    }

    public String getSummaryDesc() {
        return summaryDesc;
    }

    public void setSummaryDesc(String summaryDesc) {
        this.summaryDesc = summaryDesc;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getLogoPathName() {
        return logoPathName;
    }

    public void setLogoPathName(String logoPathName) {
        this.logoPathName = logoPathName;
    }

    public String getAssetServerAddr() {
        return assetServerAddr;
    }

    public void setAssetServerAddr(String assetServerAddr) {
        this.assetServerAddr = assetServerAddr;
    }

    public String getImageRegistryAddr() {
        return imageRegistryAddr;
    }

    public void setImageRegistryAddr(String imageRegistryAddr) {
        this.imageRegistryAddr = imageRegistryAddr;
    }

    public Long getCrossCluster() {
        return crossCluster;
    }

    public void setCrossCluster(Long crossCluster) {
        this.crossCluster = crossCluster;
    }

    public List<AstConfigListRsp> getConfigList() {
        return configList;
    }

    public void setConfigList(List<AstConfigListRsp> configList) {
        this.configList = configList;
    }

    public List<AstUseDocRsp> getDocList() {
        return docList;
    }

    public void setDocList(List<AstUseDocRsp> docList) {
        this.docList = docList;
    }

    public List<AstMetricListRsp> getMetricList() {
        return metricList;
    }

    public void setMetricList(List<AstMetricListRsp> metricList) {
        this.metricList = metricList;
    }

    public List<AstInstancePropRsp> getInstanceProperties() {
        return instanceProperties;
    }

    public void setInstanceProperties(List<AstInstancePropRsp> instanceProperties) {
        this.instanceProperties = instanceProperties;
    }

    public List<AstUnitRsp> getUnits() {
        return units;
    }

    public void setUnits(List<AstUnitRsp> units) {
        this.units = units;
    }

    public List<AstModuleRsp> getAppModules() {
        return appModules;
    }

    public void setAppModules(List<AstModuleRsp> appModules) {
        this.appModules = appModules;
    }

    public List<AstExternalCtlIntfRsp> getCtlInterfaces() {
        return ctlInterfaces;
    }

    public void setCtlInterfaces(List<AstExternalCtlIntfRsp> ctlInterfaces) {
        this.ctlInterfaces = ctlInterfaces;
    }

    public List<AstExternalSvrIntfRsp> getSvrInterfaces() {
        return svrInterfaces;
    }

    public void setSvrInterfaces(List<AstExternalSvrIntfRsp> svrInterfaces) {
        this.svrInterfaces = svrInterfaces;
    }

    public List<AstExternalDependRsp> getExtDepends() {
        return extDepends;
    }

    public void setExtDepends(List<AstExternalDependRsp> extDepends) {
        this.extDepends = extDepends;
    }
}
