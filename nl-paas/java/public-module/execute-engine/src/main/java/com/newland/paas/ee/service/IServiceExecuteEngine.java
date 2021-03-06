package com.newland.paas.ee.service;

import java.util.List;
import java.util.Map;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.DependInstanceInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.ProgressDetailResult;
import com.newland.paas.ee.vo.ServiceInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

public interface IServiceExecuteEngine {

	/**
	 * 安装
	 * @param serviceInstanceDetailInfo	执行的对象，如服务
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult install(InstallerConfig installConfig, ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo);

	/**
	 * 卸载
	 * @param serviceInstanceDetailInfo	执行的对象，如服务
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult uninstall(InstallerConfig installConfig, ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo);
	/**
	 * 启动
	 * @param serviceInstanceDetailInfo	执行的对象，如服务
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult start(InstallerConfig installConfig, ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo);
	/**
	 * 停止
	 * @param serviceInstanceDetailInfo	执行的对象，如服务
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult stop(InstallerConfig installConfig, ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo);
	/**
	 * 伸缩
	 * @param serviceInstanceDetailInfo	执行的对象，如服务
	 * @param paramMaps					界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult scaleOut(InstallerConfig installConfig, ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo);

	/**
	 * 获取执行过程描述
	 * @param operateResult				操作结果
	 * @return	ProgressDetailResult	进度明细信息
	 */
	public ProgressDetailResult getServiceExecuteDescribe(OperateResult operateResult);
	/**
	 * 获取执行结果信息	调用前提: job执行完成
	 * @param url						进度查询地址
	 * @return	ServiceInstanceDetailInfo	返回信息，包括：输出属性信息、endPoint、状态
	 */
	public ServiceInstanceDetailInfo getServiceExecuteResult(ServiceInstanceDetailInfo serviceInstanceDetailInfo, AssetDetailInfo svrAssetDetailInfo, OperateResult operateResult);

}