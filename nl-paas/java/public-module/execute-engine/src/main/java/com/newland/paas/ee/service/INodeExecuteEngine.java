package com.newland.paas.ee.service;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.NodeInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

public interface INodeExecuteEngine {

	/**
	 * 
	 * @Function:     cordonNode 
	 * @Description:  cordonNode
	 *
	 * @param installConfig
	 * @param clusterInstanceDetailInfo
	 * @param belongsTenantInfo
	 * @param nodeInfo
	 * @return OperateResult
	 */
	OperateResult cordonNode(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,TenantInstanceDetailInfo belongsTenantInfo, NodeInfo nodeInfo);
	/**
	 * 
	 * @Function:     uncordonNode 
	 * @Description:  uncordonNode
	 *
	 * @param installConfig
	 * @param cidi
	 * @param belongsTenantInfo
	 * @param nodeInfo
	 * @return OperateResult
	 */
	OperateResult uncordonNode(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi,TenantInstanceDetailInfo belongsTenantInfo, NodeInfo nodeInfo);
}
