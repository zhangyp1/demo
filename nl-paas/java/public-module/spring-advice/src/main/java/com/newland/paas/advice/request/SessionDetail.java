package com.newland.paas.advice.request;

import java.util.Date;

public class SessionDetail extends SessionInfo {
    private String ip;

    private Date accessTime;
    // 操作系统名称
    private String osFamily;
    // 操作系统
    private String osName;
    // 浏览器名称
    private String uaFamily;
    // 浏览器版本
    private String browserVersionInfo;
    // 设备类型
    private String deviceType;
    // 浏览器
    private String uaName;
    // 类型
    private String type;

    public SessionDetail() {}

    public SessionDetail(SessionInfo sessionInfo) {
        super.setUserId(sessionInfo.getUserId());
        super.setAccount(sessionInfo.getAccount());
        super.setUserName(sessionInfo.getUserName());
        super.setTenantId(sessionInfo.getTenantId());
        super.setIat(sessionInfo.getIat());
        super.setGroupIdList(sessionInfo.getGroupIdList());
        super.setRoleIdList(sessionInfo.getRoleIdList());
        super.setSubGroupIdList(sessionInfo.getSubGroupIdList());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getUaFamily() {
        return uaFamily;
    }

    public void setUaFamily(String uaFamily) {
        this.uaFamily = uaFamily;
    }

    public String getBrowserVersionInfo() {
        return browserVersionInfo;
    }

    public void setBrowserVersionInfo(String browserVersionInfo) {
        this.browserVersionInfo = browserVersionInfo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUaName() {
        return uaName;
    }

    public void setUaName(String uaName) {
        this.uaName = uaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
