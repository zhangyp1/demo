package com.newland.paas.ee.vo.cluster;

/**
 * @author zkq
 * @Description com.newland.paas.ee.vo.cluster.ClusterQuotaVo
 * @Date 2018/6/28
 */
public class ClusterQuotaVo{
    private Long tenantId;              //集群配额的租户ID
    private Integer cpuQuota;           //集群配额的CPU配额
    private Integer memoryQuota;        //集群配额的内存配额

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCpuQuota() {
        return cpuQuota;
    }

    public void setCpuQuota(Integer cpuQuota) {
        this.cpuQuota = cpuQuota;
    }

    public Integer getMemoryQuota() {
        return memoryQuota;
    }

    public void setMemoryQuota(Integer memoryQuota) {
        this.memoryQuota = memoryQuota;
    }
}
