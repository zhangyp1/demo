
package com.newland.paas.ee.installer.clu;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.cluster.CluZoneVo;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;

import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:K8sClusterZoneInstaller
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 *
 */
public class K8sClusterZoneInstaller extends AbstractInstaller{
	
	public K8sClusterZoneInstaller(InstallerConfig installerConfig,
			ClusterInstanceDetailInfo cluInstanceInfo,
			Map<String, Object> paramMaps,
			CluZoneVo cluZoneVo) throws Exception {
		    super(installerConfig, "install_k8szone_etcd");
			
		    K8SClusterInstanceDetailInfo cluInsInfo = (K8SClusterInstanceDetailInfo)cluInstanceInfo;
		    String master_float_ip = cluInsInfo.getMasterFloatIp();
		    ZoneDetailVo detail = (ZoneDetailVo) paramMaps.get(ZoneConstants.ZONE_PREFIX+cluZoneVo.getClusterId());
		    
		    //String zones = Json.toJson(cluZoneVo.getClusterZones());
		    //parameterMap.put("zoneDetails",zones);
			parameterMap.put("k8s_master_float_ip", master_float_ip);
			parameterMap.put("k8s_home", cluInsInfo.getHomePath());
			parameterMap.put("name_space", ZoneConstants.ZONE_PREFIX+String.valueOf(detail.getTenantId()));
			parameterMap.put("limit_cpu", String.valueOf(detail.getCpuQuota()));
			parameterMap.put("limit_memory", String.valueOf(detail.getMemoryQuota())+"Gi");
		    parameterMap.put("pubftp_address_path", installerConfig.getPubFtpAddress());
		    String pubFtpUserName = installerConfig.getPubFtpUserName();
			if ( StringUtils.isEmpty(pubFtpUserName))
				throw new Exception("k8s pubFtp user name is empty");
			parameterMap.put("pubftp_user_name", pubFtpUserName);

			String pubFtpPassword = installerConfig.getPubFtpPassword();
			if ( StringUtils.isEmpty(pubFtpPassword))
				throw new Exception("k8s pubFtp password is empty");
			parameterMap.put("pubftp_password", pubFtpPassword);
			//parameterMap.put("ansible_alias", getInstallConfig().getSelectJenkinsConfig().getJenkinsAnsibleAlias());
			parameterMap.put("docker_image_reposity", installerConfig.getClusterImagePath());
			parameterMap.put("docker_username", installerConfig.getClusterImagePassword());
			parameterMap.put("docker_password", installerConfig.getClusterImageUserName());
		    parameterMap.put("docker_server_name_space", detail.getTenantInfo().getImageProject());
			parameterMap.put("docker_username_name_space", detail.getTenantInfo().getImageUsername());
			parameterMap.put("docker_password_name_space", detail.getTenantInfo().getImagePassword());
			parameterMap.put("install_yaml_file", "create_name_space.yaml");
			parameterMap.put("install_step_name", "install_zone");
	}

	@Override public String getDeployStep() throws Exception{
		return InstallerConfig.getInstallK8sZoneDeployStage();
		//return InstallerConfig.getInstallK8sZoneTestDeployStage();
 	}
	
	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}
	
	
	public OperateResult antiInstall() throws Exception {
		parameterMap.put("install_yaml_file", "delete_name_space.yaml");
		parameterMap.put("install_step_name", "antiinstall_zone");
		createAndBuildJobParam( );
		return createOperatorResult();
	}
	
	public OperateResult install() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}		

	private TreeMap<String,String> parameterMap = new TreeMap<>();
}

