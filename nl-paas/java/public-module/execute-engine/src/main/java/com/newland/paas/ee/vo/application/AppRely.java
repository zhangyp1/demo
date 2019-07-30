package com.newland.paas.ee.vo.application;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.newland.paas.ee.vo.RelyEndpoint;

/**
 * 应用依赖信息
 *
 * @author laiCheng
 * @date 2018/6/27 15:14
 */
public class AppRely {
    private Long appRelyId;

    private Long appInfoId;

    private Long extDependId;

    private Long appUnitId;

    private String relyTargetType;

    private Long relyTargetId;

    private String relyName;

    private String relyTargetName;

    private String bindToken;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private List<RelyEndpoint> relyEndpoints = Collections.emptyList();

    /**
     * 依赖限制
     */
    private String restrict;

    public String getRestrict() {
        return restrict;
    }

    public void setRestrict(String restrict) {
        this.restrict = restrict;
    }

    public Long getAppRelyId() {
        return appRelyId;
    }

    public void setAppRelyId(Long appRelyId) {
        this.appRelyId = appRelyId;
    }

    public Long getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(Long appInfoId) {
        this.appInfoId = appInfoId;
    }

    public Long getExtDependId() {
        return extDependId;
    }

    public void setExtDependId(Long extDependId) {
        this.extDependId = extDependId;
    }

    public Long getAppUnitId() {
        return appUnitId;
    }

    public void setAppUnitId(Long appUnitId) {
        this.appUnitId = appUnitId;
    }

    public String getRelyTargetType() {
        return relyTargetType;
    }

    public void setRelyTargetType(String relyTargetType) {
        this.relyTargetType = relyTargetType;
    }

    public Long getRelyTargetId() {
        return relyTargetId;
    }

    public void setRelyTargetId(Long relyTargetId) {
        this.relyTargetId = relyTargetId;
    }

    public String getRelyName() {
        return relyName;
    }

    public void setRelyName(String relyName) {
        this.relyName = relyName;
    }

    public String getRelyTargetName() {
        return relyTargetName;
    }

    public void setRelyTargetName(String relyTargetName) {
        this.relyTargetName = relyTargetName;
    }

    public String getBindToken() {
        return bindToken;
    }

    public void setBindToken(String bindToken) {
        this.bindToken = bindToken;
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

    public List<RelyEndpoint> getRelyEndpoints() {
        return relyEndpoints;
    }

    public void setRelyEndpoints(List<RelyEndpoint> relyEndpoints) {
        this.relyEndpoints = relyEndpoints;
    }
}
