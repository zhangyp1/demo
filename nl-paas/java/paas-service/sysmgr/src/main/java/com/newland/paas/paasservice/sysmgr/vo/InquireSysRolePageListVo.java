package com.newland.paas.paasservice.sysmgr.vo;


/**
 * @author caifeitong
 * @description 角色分页查询
 * @created 2018-7-27 下午1:46:39
 */
public class InquireSysRolePageListVo {

    private String roleName;

    private Long tenantId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
