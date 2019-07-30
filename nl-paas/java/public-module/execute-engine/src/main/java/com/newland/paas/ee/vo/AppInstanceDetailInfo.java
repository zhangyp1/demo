package com.newland.paas.ee.vo;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.newland.paas.ee.vo.application.AppConfig;
import com.newland.paas.ee.vo.application.AppEndpoint;
import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.application.AppProp;
import com.newland.paas.ee.vo.application.AppRely;
import com.newland.paas.ee.vo.application.AppUnit;
import com.newland.paas.ee.vo.application.AppUnitAlarmConfig;
import com.newland.paas.ee.vo.application.AppUnitLogConfig;

/**
 * 应用实例详细信息
 *
 * @author ai
 */
public class AppInstanceDetailInfo {

    /**
     * 应用id
     */
    private Long appInfoId;

    private String appCode;

    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用状态
     */
    private String appStatus;
    /**
     * 安装凭证
     */
    private String installIdent;
    /**
     * 系统条目id
     */
    private Long sysCategoryId;
    /**
     * 资产id
     */
    private Long astId;
    /**
     * 资产版本
     */
    private String astVer;

    /**
     * 资产名称
     */
    private String astName;
    /**
     * 应用logo
     */
    private String logoUrl;
    /**
     * 重要程度
     */
    private String important;
    /**
     * 租 户id
     */
    private Long tenantId;
    /**
     * 拥有者id
     */
    private Long ownerId;
    /**
     * 拥有者类型
     */
    private Short ownerType;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date changeDate;
    /**
     * 创建人id
     */
    private Long creatorId;

    private List<AppConfig> appConfigs = Collections.emptyList();

    private List<AppProp> appProps = Collections.emptyList();

    private List<AppUnit> appUnits = Collections.emptyList();

    private List<AppEndpoint> appEndpoints = Collections.emptyList();

    private List<AppRely> appRelies = Collections.emptyList();

    private List<AppLoadBalanceConfig> appLoadBalanceConfigs = Collections.emptyList();

    private List<AppUnitAlarmConfig> appUnitAlarmConfigLIst = Collections.emptyList();

    private List<AppUnitLogConfig> appUnitLogConfigs = Collections.emptyList();

    // 依赖类型
    private String relyType;

    public String getRelyType() {
        return relyType;
    }

    public void setRelyType(String relyType) {
        this.relyType = relyType;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(Long appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getInstallIdent() {
        return installIdent;
    }

    public void setInstallIdent(String installIdent) {
        this.installIdent = installIdent;
    }

    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
    }

    public Long getAstId() {
        return astId;
    }

    public void setAstId(Long astId) {
        this.astId = astId;
    }

    public String getAstVer() {
        return astVer;
    }

    public void setAstVer(String astVer) {
        this.astVer = astVer;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Short getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Short ownerType) {
        this.ownerType = ownerType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public List<AppConfig> getAppConfigs() {
        return appConfigs;
    }

    public void setAppConfigs(List<AppConfig> appConfigs) {
        this.appConfigs = appConfigs;
    }

    public List<AppProp> getAppProps() {
        return appProps;
    }

    public void setAppProps(List<AppProp> appProps) {
        this.appProps = appProps;
    }

    public List<AppUnit> getAppUnits() {
        return appUnits;
    }

    public void setAppUnits(List<AppUnit> appUnits) {
        this.appUnits = appUnits;
    }

    public List<AppEndpoint> getAppEndpoints() {
        return appEndpoints;
    }

    public void setAppEndpoints(List<AppEndpoint> appEndpoints) {
        this.appEndpoints = appEndpoints;
    }

    public List<AppRely> getAppRelies() {
        return appRelies;
    }

    public void setAppRelies(List<AppRely> appRelies) {
        this.appRelies = appRelies;
    }

    public List<AppLoadBalanceConfig> getAppLoadBalanceConfigs() {
        return appLoadBalanceConfigs;
    }

    public void setAppLoadBalanceConfigs(List<AppLoadBalanceConfig> appLoadBalanceConfigs) {
        this.appLoadBalanceConfigs = appLoadBalanceConfigs;
    }

    public List<AppUnitAlarmConfig> getAppUnitAlarmConfigLIst() {
        return appUnitAlarmConfigLIst;
    }

    public void setAppUnitAlarmConfigLIst(List<AppUnitAlarmConfig> appUnitAlarmConfigLIst) {
        this.appUnitAlarmConfigLIst = appUnitAlarmConfigLIst;
    }

    public List<AppUnitLogConfig> getAppUnitLogConfigs() {
        return appUnitLogConfigs;
    }

    public void setAppUnitLogConfigs(List<AppUnitLogConfig> appUnitLogConfigs) {
        this.appUnitLogConfigs = appUnitLogConfigs;
    }

    public String getAstName() {
        return astName;
    }

    public void setAstName(String astName) {
        this.astName = astName;
    }
}
