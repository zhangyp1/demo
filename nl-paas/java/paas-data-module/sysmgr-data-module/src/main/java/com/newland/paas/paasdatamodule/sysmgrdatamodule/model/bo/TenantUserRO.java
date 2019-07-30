/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;

/**
 * 描述
 * @author linkun
 * @created 2018年7月9日 下午3:24:04
 */
public class TenantUserRO extends SysUser {
    /**
     * 租户id
     */
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}

