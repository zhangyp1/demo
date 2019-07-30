/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * 描述
 * @author linkun
 * @created 2018年7月5日 下午4:21:23
 */
public class UserWorkInfoBO {
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 工单类型
     */
    private String workbillType;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 审批状态
     */
    private String approvalStatus;
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @return 
     */
    public Long getTenantId() {
        return tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @param tenantId
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @return 
     */
    public String getTenantName() {
        return tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @param tenantName
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @return 
     */
    public String getWorkbillType() {
        return workbillType;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @param workbillType
     */
    public void setWorkbillType(String workbillType) {
        this.workbillType = workbillType;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @return 
     */
    public String getCreateDate() {
        return createDate;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @param createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @return 
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:23:15
     * @param approvalStatus
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    
}

