package com.newland.paas.ee.installer;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.*;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;
import java.util.Map;

public class ApplicationInstaller {
	private static final Log log = LogFactory.getLogger(ApplicationInstaller.class);

	/**
	 * 安装应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult install(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,  
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, 
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException{		
		logApp("Install application->", installConfig, appInstanceDetailInfo, 
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);		
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
				targetClusterList, belongsTenantInfo, appAssetDetailInfo)) {
			return applicationInstaller.install();
		}
		catch(Exception e) {
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);			
		}		
	}

	/**
	 * 卸载应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult uninstall(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo, 
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, 
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException{
		logApp("Uninstall application->", installConfig, appInstanceDetailInfo,  
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
					targetClusterList, belongsTenantInfo, appAssetDetailInfo) ) {
			return applicationInstaller.uninstall();			
		}
		catch(Exception e) {
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e ) ;			
		}
	}

	/**
	 * 启动应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult start(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,   
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, 
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException{	
		logApp("Start application->", installConfig, appInstanceDetailInfo, 
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
				targetClusterList, belongsTenantInfo, appAssetDetailInfo) ) {
			return applicationInstaller.start();	
		}
		catch(Exception e) {
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e ) ;			
		}
	}

	/**
	 * 停止应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult stop(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo appAssetDetailInfo, 
			List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, 
			TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException{
		logApp("Stop application->", installConfig, appInstanceDetailInfo,  
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
				targetClusterList, belongsTenantInfo, appAssetDetailInfo) ) {
			return applicationInstaller.stop();
		}
		catch(Exception e) {
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e ) ;			
		}
	}

	/**
	 * 缩容应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult scaleIn(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList,
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException {
		logApp("Scale in application->", installConfig, appInstanceDetailInfo,  
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
				targetClusterList, belongsTenantInfo, appAssetDetailInfo) ) {
			return applicationInstaller.scaleIn();
		}
		catch(Exception e) {
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e ) ;			
		}
	}

	/**
	 * 扩容应用
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public static OperateResult scaleOut(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList,
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo)  throws ApplicationException{
		logApp("Scale out application->", installConfig, appInstanceDetailInfo,  
				appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
		try (K8sApplicationInstaller applicationInstaller = new K8sApplicationInstaller(installConfig,appInstanceDetailInfo, 
				targetClusterList, belongsTenantInfo, appAssetDetailInfo) ) {
			return applicationInstaller.scaleOut();	
		}
		catch(Exception e) {	
			throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e ) ;			
		}
	}

	/**
	 * 记日志
	 * @param installConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param appAssetDetailInfo			依赖的资产详情信息
	 * @param dependInstanceInfoList	依赖的实例详情列表
	 * @param dependInstanceInfoList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	private static void logApp(String installMethod, InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo, 
			AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList, 
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo) {
		log.info(LogProperty.LOGTYPE_DETAIL, installMethod);
		log.info(LogProperty.LOGTYPE_DETAIL, "InstallConfig (gson)->" + new Gson().toJson(installConfig));
		log.info(LogProperty.LOGTYPE_DETAIL, "AppInstanceDetailInfo->" + JSONObject.toJSONString( appInstanceDetailInfo));
		log.info(LogProperty.LOGTYPE_DETAIL, "AppAssetDetailInfo->" + JSONObject.toJSONString( appAssetDetailInfo));
		log.info(LogProperty.LOGTYPE_DETAIL, "DependInstanceInfoList->" + JSONObject.toJSONString( dependInstanceInfoList));
		log.info(LogProperty.LOGTYPE_DETAIL, "TargetClusterList->" + JSONObject.toJSONString( targetClusterList));
		log.info(LogProperty.LOGTYPE_DETAIL, "BelongsTenantInfo->" + JSONObject.toJSONString( belongsTenantInfo ));
	}
}
