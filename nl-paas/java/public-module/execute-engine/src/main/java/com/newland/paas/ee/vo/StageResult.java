package com.newland.paas.ee.vo;

/**
 * pipeline stage执行结果
 * @author ai
 *
 */
public class StageResult {
	/**
	 * 阶段名称
	 */
	private String stageName;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 开始时间
	 */
	private long startTimeMillis;
	/**
	 * 持续时间
	 */
	private long durationMillis;
	/**
	 * 暂停持续时间
	 */
	private long pauseDurationMillis;
	
	public String getStageName() {
	
		return stageName;
	}
	public void setStageName(String stageName) {
	
		this.stageName = stageName;
	}
	public String getStatus() {
	
		return status;
	}
	public void setStatus(String status) {
	
		this.status = status;
	}
	public long getStartTimeMillis() {
	
		return startTimeMillis;
	}
	public void setStartTimeMillis(long startTimeMillis) {
	
		this.startTimeMillis = startTimeMillis;
	}
	public long getDurationMillis() {
	
		return durationMillis;
	}
	public void setDurationMillis(long durationMillis) {
	
		this.durationMillis = durationMillis;
	}
	public long getPauseDurationMillis() {
	
		return pauseDurationMillis;
	}
	public void setPauseDurationMillis(long pauseDurationMillis) {
	
		this.pauseDurationMillis = pauseDurationMillis;
	}
	
	@Override
	public String toString() {
		return "StageResult [stageName=" + stageName + ", status=" + status
				+ ", startTimeMillis=" + startTimeMillis + ", durationMillis="
				+ durationMillis + ", pauseDurationMillis="
				+ pauseDurationMillis + "]";
	}
	
}
