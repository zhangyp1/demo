package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysTenantMember implements Serializable {
    private static final long serialVersionUID = 371806936422235888L;

    private Long id;

    private Long tenantId;

    private Long userId;

    private Short isAdmin;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Short delFlag;
    
    private String workType;
    
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Short getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Short isAdmin) {
        this.isAdmin = isAdmin;
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

    public Short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午7:28:52
     * @return 
     */
    public String getWorkType() {
        return workType;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午7:28:52
     * @param workType
     */
    public void setWorkType(String workType) {
        this.workType = workType;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午7:28:52
     * @return 
     */
    public String getStatus() {
        return status;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午7:28:52
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}