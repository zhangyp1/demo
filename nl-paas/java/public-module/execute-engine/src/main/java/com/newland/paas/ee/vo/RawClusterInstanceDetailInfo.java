
package com.newland.paas.ee.vo;
/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:RawClusterInstanceDetailInfo
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 *
 * @author   Administrator
 * @version  
 * @Date	 2018年11月8日		上午11:09:20
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class RawClusterInstanceDetailInfo extends ClusterInstanceDetailInfo {

	private String rawHostIp;
	
	private String rawHostName;

	private String rawType;

	public String getRawHostIp() {
	
		return rawHostIp;
	}

	public void setRawHostIp(String rawHostIp) {
	
		this.rawHostIp = rawHostIp;
	}

	public String getRawType() {
	
		return rawType;
	}

	public void setRawType(String rawType) {
	
		this.rawType = rawType;
	}

	public String getRawHostName() {
	
		return rawHostName;
	}

	public void setRawHostName(String rawHostName) {
	
		this.rawHostName = rawHostName;
	}
	
	
}

