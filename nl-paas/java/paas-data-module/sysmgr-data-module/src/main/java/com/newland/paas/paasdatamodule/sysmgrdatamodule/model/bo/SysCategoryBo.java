package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统分组查询对象
 */
public class SysCategoryBo extends SysCategory {
    @NotNull(message = "cpu配额不能为空")
    private Float cpuQuota;
    @NotNull(message = "内存配额不能为空")
    private Float memoryQuota;
    @NotNull(message = "存储配额不能为空")
    private Float storageQuota;
    private Float cpuQuotaUsed;
    private Float memoryQuotaUsed;
    private Float storageQuotaUsed;

    private List<Long> groupIds;

    public Float getCpuQuota() {
        return cpuQuota;
    }

    public void setCpuQuota(Float cpuQuota) {
        this.cpuQuota = cpuQuota;
    }

    public Float getMemoryQuota() {
        return memoryQuota;
    }

    public void setMemoryQuota(Float memoryQuota) {
        this.memoryQuota = memoryQuota;
    }

    public Float getStorageQuota() {
        return storageQuota;
    }

    public void setStorageQuota(Float storageQuota) {
        this.storageQuota = storageQuota;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public Float getCpuQuotaUsed() {
        return cpuQuotaUsed;
    }

    public void setCpuQuotaUsed(Float cpuQuotaUsed) {
        this.cpuQuotaUsed = cpuQuotaUsed;
    }

    public Float getMemoryQuotaUsed() {
        return memoryQuotaUsed;
    }

    public void setMemoryQuotaUsed(Float memoryQuotaUsed) {
        this.memoryQuotaUsed = memoryQuotaUsed;
    }

    public Float getStorageQuotaUsed() {
        return storageQuotaUsed;
    }

    public void setStorageQuotaUsed(Float storageQuotaUsed) {
        this.storageQuotaUsed = storageQuotaUsed;
    }
}