package com.newland.paas.ee.vo;

/**
 * 四大操作(安装 卸载 启动 停止)返回结果
 * @author ai
 *
 */
public class OperateResult {
	
	/**
	 * url(不含job后缀)
	 */
	private String progressUrl;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 构建序列号
	 */
	private long buildNumber;
	/**
	 * 名称
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String passwd;
	
	public String getProgressUrl() {
	
		return progressUrl;
	}
	public void setProgressUrl(String progressUrl) {
	
		this.progressUrl = progressUrl;
	}
	public String getJobName() {
	
		return jobName;
	}
	public void setJobName(String jobName) {
	
		this.jobName = jobName;
	}
	public long getBuildNumber() {
	
		return buildNumber;
	}
	public void setBuildNumber(long buildNumber) {
	
		this.buildNumber = buildNumber;
	}
	public String getUserName() {
	
		return userName;
	}
	public void setUserName(String userName) {
	
		this.userName = userName;
	}
	public String getPasswd() {
	
		return passwd;
	}
	public void setPasswd(String passwd) {
	
		this.passwd = passwd;
	}
	
	@Override
	public String toString() {
		return "OperateResult [progressUrl=" + progressUrl + ", jobName="
				+ jobName + ", buildNumber=" + buildNumber + ", userName=" + userName + ", passwd=" + passwd
				+ "]";
	}

}
