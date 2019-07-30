package com.newland.paas.ee.vo.service;

import java.io.Serializable;
import java.util.Date;

/**
 * ����ʵ������� SVR_INST_ENDPOINT 
 */
public class SvrInstEndpoint implements Serializable {
    private static final long serialVersionUID = 141944817485201924L;

    private Long svrInstEndpointId;

    private Long svrInstId;

    private String endpointName;

	private String endpointIdent;

    private String endpointValue;

	private String intfKey;

	private Long svrInstUnitId;

    private Long tenantId;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

	private Long interfaceId;


    public Long getSvrInstEndpointId() {
        return svrInstEndpointId;
    }

    public void setSvrInstEndpointId(Long svrInstEndpointId) {
        this.svrInstEndpointId = svrInstEndpointId;
    }

    public Long getSvrInstId() {
        return svrInstId;
    }

    public void setSvrInstId(Long svrInstId) {
        this.svrInstId = svrInstId;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
	    this.endpointName = endpointName;
    }

    public String getEndpointValue() {
        return endpointValue;
    }

    public void setEndpointValue(String endpointValue) {
	    this.endpointValue = endpointValue;
    }

	public String getIntfKey() {
		return intfKey;
	}

	public void setIntfKey(String intfKey) {
		this.intfKey = intfKey;
	}

	public Long getSvrInstUnitId() {
		return svrInstUnitId;
	}

	public void setSvrInstUnitId(Long svrInstUnitId) {
		this.svrInstUnitId = svrInstUnitId;
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

	public Long getInterfaceId() {
		return interfaceId;
    }

	public void setInterfaceId(Long interfaceId) {
		this.interfaceId = interfaceId;
    }

	public String getEndpointIdent() {
		return endpointIdent;
	}

	public void setEndpointIdent(String endpointIdent) {
		this.endpointIdent = endpointIdent;
	}
}