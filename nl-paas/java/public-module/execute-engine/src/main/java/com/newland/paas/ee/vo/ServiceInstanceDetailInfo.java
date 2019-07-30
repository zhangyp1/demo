package com.newland.paas.ee.vo;

import com.newland.paas.ee.vo.service.SvrInstConfig;
import com.newland.paas.ee.vo.service.SvrInstEndpoint;
import com.newland.paas.ee.vo.service.SvrInstLoadBalanceConfig;
import com.newland.paas.ee.vo.service.SvrInstProp;
import com.newland.paas.ee.vo.service.SvrInstRely;
import com.newland.paas.ee.vo.service.SvrInstUnit;
import com.newland.paas.ee.vo.service.SvrInstUnitAlarmConfig;
import com.newland.paas.ee.vo.service.SvrInstUnitLogConfig;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 服务实例详细信息
 * @author ai
 *
 */
public class ServiceInstanceDetailInfo {
    /**
     * 服务实例id
     */
    private Long svrInstId;
    /**
     * 服务实例所属系统组
     */
    private Long sysCategoryId;
    /**
     * 服务实例所属服务id
     */
    private Long svrServicesId;
    /**
     * 服务实例来源模板
     */
    private Long svrTemplateId;
    /**
     * 服务实例名称
     */
    private String instName;
    /**
     * 服务实例状态
     */
    private String instStatus;
    /**
     * 服务实例绑定租户id
     */
    private Long instTenantId;
    /**
     * 服务实例资产id
     */
    private Long astId;
    /**
     * 资产版本
     */
    private String astVer;
    /**
     * 资产图标
     */
    private String logoUrl;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建者
     */
    private Long creatorId;


    private List<SvrInstConfig> svrInstConfigs = Collections.emptyList();

    private List<SvrInstProp> svrInstProps = Collections.emptyList();

    private List<SvrInstEndpoint> svrInstEndpoints = Collections.emptyList();

    private List<SvrInstRely> svrInstRelies = Collections.emptyList();

    private List<SvrInstUnit> svrInstUnits = Collections.emptyList();

    private List<SvrInstLoadBalanceConfig> svrInstLoadBalanceConfigs = Collections.EMPTY_LIST;

    private List<SvrInstUnitAlarmConfig> svrInstUnitAlarmConfigs = Collections.EMPTY_LIST;

    private List<SvrInstUnitLogConfig> svrInstUnitLogConfigs = Collections.EMPTY_LIST;


    public Long getSvrInstId() {
        return svrInstId;
    }

    public void setSvrInstId(Long svrInstId) {
        this.svrInstId = svrInstId;
    }

    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
    }

    public Long getSvrServicesId() {
        return svrServicesId;
    }

    public void setSvrServicesId(Long svrServicesId) {
        this.svrServicesId = svrServicesId;
    }

    public Long getSvrTemplateId() {
        return svrTemplateId;
    }

    public void setSvrTemplateId(Long svrTemplateId) {
        this.svrTemplateId = svrTemplateId;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getInstStatus() {
        return instStatus;
    }

    public void setInstStatus(String instStatus) {
        this.instStatus = instStatus;
    }

    public Long getInstTenantId() {
        return instTenantId;
    }

    public void setInstTenantId(Long instTenantId) {
        this.instTenantId = instTenantId;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public List<SvrInstConfig> getSvrInstConfigs() {
        return svrInstConfigs;
    }

    public void setSvrInstConfigs(List<SvrInstConfig> svrInstConfigs) {
        this.svrInstConfigs = svrInstConfigs;
    }

    public List<SvrInstProp> getSvrInstProps() {
        return svrInstProps;
    }

    public void setSvrInstProps(List<SvrInstProp> svrInstProps) {
        this.svrInstProps = svrInstProps;
    }

    public List<SvrInstEndpoint> getSvrInstEndpoints() {
        return svrInstEndpoints;
    }

    public void setSvrInstEndpoints(List<SvrInstEndpoint> svrInstEndpoints) {
        this.svrInstEndpoints = svrInstEndpoints;
    }

    public List<SvrInstRely> getSvrInstRelies() {
        return svrInstRelies;
    }

    public void setSvrInstRelies(List<SvrInstRely> svrInstRelies) {
        this.svrInstRelies = svrInstRelies;
    }

    public List<SvrInstUnit> getSvrInstUnits() {
        return svrInstUnits;
    }

    public void setSvrInstUnits(List<SvrInstUnit> svrInstUnits) {
        this.svrInstUnits = svrInstUnits;
    }

    public List<SvrInstLoadBalanceConfig> getSvrInstLoadBalanceConfigs() {
        return svrInstLoadBalanceConfigs;
    }

    public void setSvrInstLoadBalanceConfigs(List<SvrInstLoadBalanceConfig> svrInstLoadBalanceConfigs) {
        this.svrInstLoadBalanceConfigs = svrInstLoadBalanceConfigs;
    }

    public List<SvrInstUnitAlarmConfig> getSvrInstUnitAlarmConfigs() {
        return svrInstUnitAlarmConfigs;
    }

    public void setSvrInstUnitAlarmConfigs(List<SvrInstUnitAlarmConfig> svrInstUnitAlarmConfigs) {
        this.svrInstUnitAlarmConfigs = svrInstUnitAlarmConfigs;
    }

    public List<SvrInstUnitLogConfig> getSvrInstUnitLogConfigs() {
        return svrInstUnitLogConfigs;
    }

    public void setSvrInstUnitLogConfigs(List<SvrInstUnitLogConfig> svrInstUnitLogConfigs) {
        this.svrInstUnitLogConfigs = svrInstUnitLogConfigs;
    }
}
