package com.newland.paas.ee.vo;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:NodeInfo
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 *
 */
public class NodeInfo {

	private String ip;

	private String hostname;

	private String password;

	public String getIp() {

		return ip;
	}

	public void setIp(String ip) {

		this.ip = ip;
	}

	public String getHostname() {

		return hostname;
	}

	public void setHostname(String hostname) {

		this.hostname = hostname;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NodeInfo [ip=");
		builder.append(ip);
		builder.append(", hostname=");
		builder.append(hostname);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}

}
