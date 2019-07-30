package com.newland.paas.ee.vo.cluster;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 集群分区表
 */
public class CluZoneVo implements Serializable {
	private static final long serialVersionUID = 200768316293432726L;

	/**
	 * 集群ID
	 */
	@NotNull(message = "集群ID不能为空")
	private Long clusterId;

	private Long creatorId;

	/**
	 * 系统组ID
	 */
	private Long sysCategoryId;
	/**
	 * 集群分区数组
	 */
	@NotNull(message = "集群分区列表不能为空")
	private List<ZoneDetailVo> clusterZones;

	public Long getClusterId() {

		return clusterId;
	}

	public void setClusterId(Long clusterId) {

		this.clusterId = clusterId;
	}

	public Long getCreatorId() {

		return creatorId;
	}

	public void setCreatorId(Long creatorId) {

		this.creatorId = creatorId;
	}

	public Long getSysCategoryId() {

		return sysCategoryId;
	}

	public void setSysCategoryId(Long sysCategoryId) {

		this.sysCategoryId = sysCategoryId;
	}

	public List<ZoneDetailVo> getClusterZones() {

		return clusterZones;
	}

	public void setClusterZones(List<ZoneDetailVo> clusterZones) {

		this.clusterZones = clusterZones;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CluZoneVo [clusterId=");
		builder.append(clusterId);
		builder.append(", creatorId=");
		builder.append(creatorId);
		builder.append(", sysCategoryId=");
		builder.append(sysCategoryId);
		builder.append(", clusterZones=");
		builder.append(clusterZones);
		builder.append("]");
		return builder.toString();
	}

}