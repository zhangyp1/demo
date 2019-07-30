package com.newland.paas.sbcommon.init;

import java.util.List;

/**
 * 应用上下文
 * @author Administrator
 *
 */
public class ApplicationContext {
	
	public static final int HEART_OUTTIME = 120;

	//redis中hashkey  PAASMGR_RUN_PROCEDURE/{appname}/{appid}
	public static final String APP_HASHKEY = "PAASMGR_RUN_PROC";

	public static String getAppId() {
		return AppInfo.getIns().getAppId();
	}

	public static String getAddress() {
		return AppInfo.getIns().getAddress();
	}

	public static String getLocalPath() {
		return AppInfo.getIns().getLocalPath();
	}
	
	/**
	 * 获取与本进程APPNAME一样的进程列表
	 * @return
	 */
	public static List<AppInfo> getAppInfoList(){
		return AppInitRegister.getAppInfoList();
	}
}
