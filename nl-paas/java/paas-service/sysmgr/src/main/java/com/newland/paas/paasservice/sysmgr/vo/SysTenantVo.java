/*
 *
 */
package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;

/**
 * 
 * 租户响应vo
 * 描述
 * @author linkun
 * @created 2018年6月27日 下午3:07:47
 */
public class SysTenantVo {
    
    private Long tenantId;
    private String tenantName;
    private String description;
    private String astAddress;
    private String imageAddress;
    private String harborAddress;
    private List<TenantUserVO> adminUsers;
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @return 
     */
    public Long getTenantId() {
        return tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @param tenantId
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:55:33
     * @return 
     */
    public String getDescription() {
        return description;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:55:33
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @return 
     */
    public String getAstAddress() {
        return astAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @param astAddress
     */
    public void setAstAddress(String astAddress) {
        this.astAddress = astAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @return 
     */
    public String getImageAddress() {
        return imageAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @param imageAddress
     */
    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @return 
     */
    public String getHarborAddress() {
        return harborAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @param harborAddress
     */
    public void setHarborAddress(String harborAddress) {
        this.harborAddress = harborAddress;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @return 
     */
    public List<TenantUserVO> getAdminUsers() {
        return adminUsers;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:45:43
     * @param adminUsers
     */
    public void setAdminUsers(List<TenantUserVO> adminUsers) {
        this.adminUsers = adminUsers;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:54:46
     * @return 
     */
    public String getTenantName() {
        return tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月17日 下午2:54:46
     * @param tenantName
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    
    
    

}

