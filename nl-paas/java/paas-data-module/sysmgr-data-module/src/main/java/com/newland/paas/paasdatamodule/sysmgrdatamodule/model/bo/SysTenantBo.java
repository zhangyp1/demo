/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * 描述
 * @author linkun
 * @created 2018年7月5日 下午2:44:10
 */
public class SysTenantBo {
    private static final Log logger = LogFactory.getLogger(SysTenantBo.class);
    
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 审批状态
     */
    private String status;
    /**
     * 租户管理员
     */
    private String tenantAdmins;
    /**
     * 描述
     */
    private String description;
    /**
     * 资产仓库地址
     */
    private String astAddress;
    /**
     * 镜像仓库地址
     */
    private String imageProject;

    /**
     * 租户标识
     */
    private String tenantCode;

    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public Long getTenantId() {
        return tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param tenantId
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public String getTenantName() {
        return tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param tenantName
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public String getCreateDate() {
        return createDate;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public String getStatus() {
        return status;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public String getTenantAdmins() {
        return tenantAdmins;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param tenantAdmins
     */
    public void setTenantAdmins(String tenantAdmins) {
        this.tenantAdmins = tenantAdmins;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @return 
     */
    public String getDescription() {
        return description;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午2:45:45
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAstAddress() {
        return astAddress;
    }

    public void setAstAddress(String astAddress) {
        this.astAddress = astAddress;
    }

    public String getImageProject() {
        return imageProject;
    }

    public void setImageProject(String imageProject) {
        this.imageProject = imageProject;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}

