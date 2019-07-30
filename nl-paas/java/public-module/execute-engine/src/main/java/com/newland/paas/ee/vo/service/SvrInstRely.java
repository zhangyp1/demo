package com.newland.paas.ee.vo.service;

import com.newland.paas.ee.vo.RelyEndpoint;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ����ʵ������ SVR_INST_RELY 
 */
public class SvrInstRely implements Serializable {
    private static final long serialVersionUID = 973507795905813240L;

    /**
     * ��������ID 
     */
    private Long svrInstRelyId;

    /**
     * ����ģ��ID 
     */
    private Long svrInstId;

    /**
     * ����ģ�嵥Ԫid 
     */
    private Long svrInstUnitId;

    /**
     * �ⲿ����ID �����ʲ��ⲿ������
     */
    private Long extDependId;

    /**
     * 1-����ʵ�� 2-����ģ��
     */
    private String relyTargetType;

    /**
     * ����Ŀ��ID 
     */
    private Long relyTargetId;

    /**
     * ��������
     */
    private String relyName;

    /**
     * ����Ŀ������
     */
    private String relyTargetName;

    /**
     * �⻧id 
     */
    private Long tenantId;

    /**
     * ����ʱ�� 
     */
    private Date createDate;

    /**
     * �޸�ʱ�� 
     */
    private Date changeDate;

    /**
     * ������ 
     */
    private Long creatorId;

    private List<RelyEndpoint> relyEndpoints = Collections.EMPTY_LIST;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSvrInstRelyId() {
        return svrInstRelyId;
    }

    public void setSvrInstRelyId(Long svrInstRelyId) {
        this.svrInstRelyId = svrInstRelyId;
    }

    public Long getSvrInstId() {
        return svrInstId;
    }

    public void setSvrInstId(Long svrInstId) {
        this.svrInstId = svrInstId;
    }

    public Long getSvrInstUnitId() {
        return svrInstUnitId;
    }

    public void setSvrInstUnitId(Long svrInstUnitId) {
        this.svrInstUnitId = svrInstUnitId;
    }

    public Long getExtDependId() {
        return extDependId;
    }

    public void setExtDependId(Long extDependId) {
        this.extDependId = extDependId;
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