package com.newland.paas.ee.vo;

import java.util.List;

/**
 * 资源实例详细信息
 * @author ai
 *
 */
public class ResourceDetailInfo {
	private List<MachineInfo> machineInfoList;

    private String harborServer;

    private String ntpServer;

    public List<MachineInfo> getMachineInfoList() {
    
        return machineInfoList;
    }

    public void setMachineInfoList(List<MachineInfo> machineInfoList) {
    
        this.machineInfoList = machineInfoList;
    }

    public String getHarborServer() {
        return harborServer;
    }

    public void setHarborServer(String harborServer) {
        this.harborServer = harborServer;
    }

    public String getNtpServer() {
        return ntpServer;
    }

    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    @Override
    public String toString() {
        return "ResourceDetailInfo{" +
                "machineInfoList=" + machineInfoList +
                ", harborServer='" + harborServer + '\'' +
                ", ntpServer='" + ntpServer + '\'' +
                '}';
    }
}
