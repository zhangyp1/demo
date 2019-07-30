package com.newland.paas.ee.vo;

/**
 * pipeline stage执行结果
 * @author ai
 *
 */
public class MachineInfo {
	/**
	 * 主机ip地址
	 */
	private String ipAddress;
    //主机名
    private String hostName;
	/**
	 * 主机用户名称
	 */
	private String userName;
	/**
	 * 主机用户密码
	 */
	private String paasword;
    public String getIpAddress() {
    
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
    
        this.ipAddress = ipAddress;
    }
    public String getUserName() {
    
        return userName;
    }
    public void setUserName(String userName) {
    
        this.userName = userName;
    }
    public String getPaasword() {
    
        return paasword;
    }
    public void setPaasword(String paasword) {
    
        this.paasword = paasword;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "MachineInfo [ipAddress=" + ipAddress + ", userName=" + userName
                + ", paasword=" + paasword + "]";
    }

	
	
}
