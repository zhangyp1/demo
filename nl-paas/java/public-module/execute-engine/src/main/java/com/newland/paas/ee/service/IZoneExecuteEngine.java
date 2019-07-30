
package com.newland.paas.ee.service;

import java.util.Map;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.ProgressDetailResult;
import com.newland.paas.ee.vo.cluster.CluZoneVo;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:IZoneExecuteEngine
 * @Description: IZoneExecuteEngine
 * @Funtion List:IZoneExecuteEngine
 *
 */
public interface IZoneExecuteEngine {

	/**
	 * 安装
	 * @param clusterInstanceDetailInfo	执行的对象，如集群
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param ZoneDetailInfo			集群分区详情信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult installZone(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo, Map<String, Object> paramMaps, CluZoneVo cluZoneVo);

	/**
	 * 卸载
	 * @param clusterInstanceDetailInfo	执行的对象，如集群
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param ZoneDetailInfo			集群分区详情信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult uninstallZone(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo, Map<String, Object> paramMaps, CluZoneVo cluZoneVo);
	/**
	 * 更新集群配额
	 * @param clusterInstanceDetailInfo	执行的对象，如集群
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param ZoneDetailInfo			集群分区详情信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult updateZoneQuota(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo, Map<String, Object> paramMaps, CluZoneVo cluZoneVo);
	 
	/**
	 * 获取执行过程描述
	 * @param operateResult				操作结果
	 * @return	ProgressDetailResult	进度明细信息
	 */
	public ProgressDetailResult getZoneExecuteDescribe(OperateResult operateResult);
	/**
	 * 获取执行结果信息	调用前提: job执行完成
	 * @param url						进度查询地址
	 * @return	Map<String, String>		返回信息，包括：输出属性信息、endPoint等
	 */
	public Map<String, String> getZoneExecuteResult(OperateResult operateResult);
	
	
}

