package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 租户统计
 *
 * @author zhongqingjiang
 */
public class TenantStatisticVo implements Serializable {

    public TenantStatisticVo() {
        this.tenantCount = 0;
    }

    public int tenantCount;

    public int getTenantCount() {
        return tenantCount;
    }

    public void setTenantCount(int tenantCount) {
        this.tenantCount = tenantCount;
    }

    public void addTenantCount() {
        this.tenantCount++;
    }
}
