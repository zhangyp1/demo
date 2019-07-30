package com.newland.paas.sbcommon.init;

/**
 * 应用程序实例对象
 * @author Administrator
 *
 */
public class AppInfo {
	private String appId;	//该应用唯一标识 appname + UUID
	private String appName;
	private String startTime;	//启动时间
	private String address;	//IP地址
	private String heartbeatTime;	//心跳时间
	private String localPath;	//程序所在路径
	
	private static AppInfo appInfo = null;
	
	protected AppInfo() {
		
	}
	
	public static AppInfo getIns() {
		if(appInfo == null) {
			appInfo = new AppInfo();
		}
		return appInfo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public static AppInfo getAppInfo() {
		return appInfo;
	}

	public static void setAppInfo(AppInfo appInfo) {
		AppInfo.appInfo = appInfo;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(String heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}
}
