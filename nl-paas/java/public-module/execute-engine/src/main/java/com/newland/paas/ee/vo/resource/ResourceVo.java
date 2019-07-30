package com.newland.paas.ee.vo.resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ResourceVo {
    private BigDecimal id;                      //资源ID
    private String name;                        //资源名称
    private String description;                 //资源描述
    private String status;                      //状态
    private String useScope;                    //使用范围
    private String ipAddress;                   //IP地址
    private String hostName;                    //主机名
    private String regionName;                  //地域名称
    private String computerRoomName;            //机房名称
    private String computerRackName;            //机架名称
    private String resTypeName;                 //资源类型名称
    private String resCategoryName;             //资源类别名称
    private String resModelName;                //资源型号名称
    private String specName;                    //资源规格名称

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUseScope() {
        return useScope;
    }

    public void setUseScope(String useScope) {
        this.useScope = useScope;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getComputerRoomName() {
        return computerRoomName;
    }

    public void setComputerRoomName(String computerRoomName) {
        this.computerRoomName = computerRoomName;
    }

    public String getComputerRackName() {
        return computerRackName;
    }

    public void setComputerRackName(String computerRackName) {
        this.computerRackName = computerRackName;
    }

    public String getResTypeName() {
        return resTypeName;
    }

    public void setResTypeName(String resTypeName) {
        this.resTypeName = resTypeName;
    }

    public String getResCategoryName() {
        return resCategoryName;
    }

    public void setResCategoryName(String resCategoryName) {
        this.resCategoryName = resCategoryName;
    }

    public String getResModelName() {
        return resModelName;
    }

    public void setResModelName(String resModelName) {
        this.resModelName = resModelName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceVo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", status=");
		builder.append(status);
		builder.append(", useScope=");
		builder.append(useScope);
		builder.append(", ipAddress=");
		builder.append(ipAddress);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", regionName=");
		builder.append(regionName);
		builder.append(", computerRoomName=");
		builder.append(computerRoomName);
		builder.append(", computerRackName=");
		builder.append(computerRackName);
		builder.append(", resTypeName=");
		builder.append(resTypeName);
		builder.append(", resCategoryName=");
		builder.append(resCategoryName);
		builder.append(", resModelName=");
		builder.append(resModelName);
		builder.append(", specName=");
		builder.append(specName);
		builder.append("]");
		return builder.toString();
	}
    
}
