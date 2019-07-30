package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysTenant implements Serializable {
    private static final long serialVersionUID = 248877581960729352L;

    private Long id;

    private String tenantCode;

    private String tenantName;

    private String tenantDesc;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    private Short delFlag;
    
    private String status;

    private String astAddress;

    private String astUsername;

    private String astPassword;

    private String imageProject;

    private String imageUsername;

    private String imagePassword;

    private String supplierStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode == null ? null : tenantCode.trim();
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName == null ? null : tenantName.trim();
    }

    public String getTenantDesc() {
        return tenantDesc;
    }

    public void setTenantDesc(String tenantDesc) {
        this.tenantDesc = tenantDesc == null ? null : tenantDesc.trim();
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
     * @created 2018年7月4日 下午7:08:55
     * @return 
     */
    public String getStatus() {
        return status;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 下午7:08:55
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午5:07:24
     * @return 
     */
    public String getAstAddress() {
        return astAddress;
    }

    /**
     * @author linkun
     * @created 2018年7月9日 下午5:07:24
     * @param astAddress
     */
    public void setAstAddress(String astAddress) {
        this.astAddress = astAddress;
    }

    public String getAstUsername() {
        return astUsername;
    }

    public void setAstUsername(String astUsername) {
        this.astUsername = astUsername;
    }

    public String getAstPassword() {
        return astPassword;
    }

    public void setAstPassword(String astPassword) {
        this.astPassword = astPassword;
    }

    public String getImageProject() {
        return imageProject;
    }

    public void setImageProject(String imageProject) {
        this.imageProject = imageProject;
    }

    public String getImageUsername() {
        return imageUsername;
    }

    public void setImageUsername(String imageUsername) {
        this.imageUsername = imageUsername;
    }

    public String getImagePassword() {
        return imagePassword;
    }

    public void setImagePassword(String imagePassword) {
        this.imagePassword = imagePassword;
    }

    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }
}