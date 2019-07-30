package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.util.Set;

/**
 * @program: paas-all
 * @description: 租户仓库信息对象
 * @author: Frown
 * @create: 2018-08-07 13:57
 **/
public class SysTenantDepotBo {

    // 租户id
    private Long tenantId;
    // 租户操作id
    private Long tenantOpId;
    // 仓库类型
    private String depotType;
    // 仓库集合
    private Set<SysDepotBo> depotSet;

    public SysTenantDepotBo() {
    }

    public SysTenantDepotBo(Long tenantId, Long tenantOpId, String depotType, Set<SysDepotBo> depotSet) {
        this.tenantId = tenantId;
        this.tenantOpId = tenantOpId;
        this.depotType = depotType;
        this.depotSet = depotSet;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Set<SysDepotBo> getDepotSet() {
        return depotSet;
    }

    public void setDepotSet(Set<SysDepotBo> depotSet) {
        this.depotSet = depotSet;
    }

    public Long getTenantOpId() {
        return tenantOpId;
    }

    public void setTenantOpId(Long tenantOpId) {
        this.tenantOpId = tenantOpId;
    }

    public String getDepotType() {
        return depotType;
    }

    public void setDepotType(String depotType) {
        this.depotType = depotType;
    }
}
