
package com.newland.paas.ee.installer.clu;

import java.util.Map;
import java.util.TreeMap;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:K8sClusterZoneUpdater
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 *
 */
public class K8sClusterZoneUpdater extends AbstractInstaller{
	
	public K8sClusterZoneUpdater(InstallerConfig installerConfig,
			ClusterInstanceDetailInfo cluInstanceInfo,
			Map<String, Object> paramMaps,
			ZoneDetailVo detail) throws Exception {
		    super(installerConfig, "install_k8szone_etcd");
			
		    K8SClusterInstanceDetailInfo cluInsInfo = (K8SClusterInstanceDetailInfo)cluInstanceInfo;
		    String master_float_ip = cluInsInfo.getMasterFloatIp();
		    
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
			parameterMap.put("install_yaml_file", "update_name_space.yaml");
			parameterMap.put("install_step_name", "updatezoneresquota");
	}

	@Override public String getDeployStep() throws Exception{
		return InstallerConfig.getInstallK8sZoneDeployStage();
 	}
	
	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}
	
	public OperateResult install() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}		

	private TreeMap<String,String> parameterMap = new TreeMap<>();
}

