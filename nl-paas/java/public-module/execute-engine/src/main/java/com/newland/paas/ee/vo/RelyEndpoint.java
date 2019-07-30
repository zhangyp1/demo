package com.newland.paas.ee.vo;

import com.newland.paas.ee.vo.application.AppEndpointProp;

import java.util.Date;
import java.util.List;

/**
 * @author laiCheng
 * @date 2018/8/25 15:05
 */
public class RelyEndpoint {

    private Long relyEndpointId;

    private Long instId;

    private Long relyId;

    private Long endpointId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private List<AppEndpointProp> relyEndpointProps;

    public List<AppEndpointProp> getRelyEndpointProps() {
        return relyEndpointProps;
    }

    public void setRelyEndpointProps(List<AppEndpointProp> relyEndpointProps) {
        this.relyEndpointProps = relyEndpointProps;
    }

    public Long getRelyEndpointId() {
        return relyEndpointId;
    }

    public void setRelyEndpointId(Long relyEndpointId) {
        this.relyEndpointId = relyEndpointId;
    }

    public Long getInstId() {
        return instId;
    }

    public void setInstId(Long instId) {
        this.instId = instId;
    }

    public Long getRelyId() {
        return relyId;
    }

    public void setRelyId(Long relyId) {
        this.relyId = relyId;
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
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
