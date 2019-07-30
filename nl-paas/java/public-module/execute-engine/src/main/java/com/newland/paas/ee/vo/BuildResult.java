package com.newland.paas.ee.vo;

/**
 * 单次构建总结果
 * @author ai
 *
 */
public class BuildResult {
	public static final String STATUS_SUCCESS = "SUCCESS";


	private long buildNumber;
	private String status;
	private long startTimeMillis;
	private long endTimeMillis;
	private long durationMillis;
	private long queueDurationMillis;
	private long pauseDurationMillis;
	public long getBuildNumber() {
	
		return buildNumber;
	}
	public void setBuildNumber(long buildNumber) {
	
		this.buildNumber = buildNumber;
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
	public long getEndTimeMillis() {
	
		return endTimeMillis;
	}
	public void setEndTimeMillis(long endTimeMillis) {
	
		this.endTimeMillis = endTimeMillis;
	}
	public long getDurationMillis() {
	
		return durationMillis;
	}
	public void setDurationMillis(long durationMillis) {
	
		this.durationMillis = durationMillis;
	}
	public long getQueueDurationMillis() {
	
		return queueDurationMillis;
	}
	public void setQueueDurationMillis(long queueDurationMillis) {
	
		this.queueDurationMillis = queueDurationMillis;
	}
	public long getPauseDurationMillis() {
	
		return pauseDurationMillis;
	}
	public void setPauseDurationMillis(long pauseDurationMillis) {
	
		this.pauseDurationMillis = pauseDurationMillis;
	}
	
	@Override
	public String toString() {
		return "BuildResult [buildNumber=" + buildNumber + ", status=" + status
				+ ", startTimeMillis=" + startTimeMillis + ", endTimeMillis="
				+ endTimeMillis + ", durationMillis=" + durationMillis
				+ ", queueDurationMillis=" + queueDurationMillis
				+ ", pauseDurationMillis=" + pauseDurationMillis + "]";
	}
	
}
