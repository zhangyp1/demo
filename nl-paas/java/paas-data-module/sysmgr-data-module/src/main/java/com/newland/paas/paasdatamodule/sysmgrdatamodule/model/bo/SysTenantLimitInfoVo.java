package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.io.Serializable;

public class SysTenantLimitInfoVo implements Serializable {
    private static final long serialVersionUID = 81103073269562388L;
    // 硬盘大小
    private Integer storageNum;
    // cpu数量
    private Integer cpuNum;
    // 内存数量
    private Integer memoryNum;
    // 机器台数
    private Integer machineNum;

    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    public Integer getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(Integer cpuNum) {
        this.cpuNum = cpuNum;
    }

    public Integer getMemoryNum() {
        return memoryNum;
    }

    public void setMemoryNum(Integer memoryNum) {
        this.memoryNum = memoryNum;
    }

    public Integer getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(Integer machineNum) {
        this.machineNum = machineNum;
    }
}