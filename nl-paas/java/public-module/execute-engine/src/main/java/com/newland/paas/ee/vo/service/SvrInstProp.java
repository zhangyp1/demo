package com.newland.paas.ee.vo.service;

import java.io.Serializable;
import java.util.Date;

/**
 * ����ʵ������ SVR_INST_PROP 
 */
public class SvrInstProp implements Serializable {
    private static final long serialVersionUID = 336728473475559444L;

    /**
     * ����ʵ������ID 
     */
    private Long svrInstPropId;

    /**
     * ����ʵ��ID 
     */
    private Long svrInstId;

    /**
     * ��Ԫid ���� �ʲ���Ԫ��
�������������Ӧ�����Ϊ��
     */
    private Long unitId;

    /**
     * ����key 
     */
    private String propKey;

    /**
     * �������� 
     */
    private String propDesc;

    /**
     * ����val 
     */
    private String propVal;

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


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSvrInstPropId() {
        return svrInstPropId;
    }

    public void setSvrInstPropId(Long svrInstPropId) {
        this.svrInstPropId = svrInstPropId;
    }

    public Long getSvrInstId() {
        return svrInstId;
    }

    public void setSvrInstId(Long svrInstId) {
        this.svrInstId = svrInstId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPropDesc() {
        return propDesc;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }

    public String getPropVal() {
        return propVal;
    }

    public void setPropVal(String propVal) {
        this.propVal = propVal;
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
}