package com.newland.paas.ee.installer.clu;

import com.google.gson.Gson;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.resource.ResourceVo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class K8sClusterInstaller extends AbstractInstaller{
	private static final Log log = LogFactory.getLogger(K8sClusterInstaller.class);

	public K8sClusterInstaller(InstallerConfig installerConfig, K8SClusterInstanceDetailInfo clusterInstanceDetailInfo,
			Map<String, Object> paramMaps, TenantInstanceDetailInfo belongsTenantInfo ) throws Exception {
		super(installerConfig, "install_k8s_etcd");

		K8sClusterUtil.checkAndAddMasterIpToMap(clusterInstanceDetailInfo.getK8sMasterIpList(), parameterMap);
		K8sClusterUtil.checkAndAddWorkerIpToMap(clusterInstanceDetailInfo.getK8sWorkIpList(), parameterMap);
		K8sClusterUtil.checkAndAddFloatIpToMap(clusterInstanceDetailInfo.getFloatIp(),parameterMap);
		K8sClusterUtil.checkAndAddHomePath(clusterInstanceDetailInfo.getHomePath(),parameterMap);
		K8sClusterUtil.checkAndAddEtcdIpToMap(clusterInstanceDetailInfo.getEtcdIpList(), parameterMap);
		K8sClusterUtil.checkAndAddEtcdHomeToMap(clusterInstanceDetailInfo.getEtcdHome(), parameterMap);
		K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
		K8sClusterUtil.checkAndAddClusterImagePath(installerConfig.getClusterImagePath(), parameterMap);
		K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
		K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);

		//增加hostName参数
		if(clusterInstanceDetailInfo.getK8sWorkers() != null && clusterInstanceDetailInfo.getK8sWorkers().size() > 0) {
			 Map<String, String> mapIpHostName = new HashMap<String, String>();
			for(ResourceVo vo : clusterInstanceDetailInfo.getK8sWorkers()) {
				mapIpHostName.put(vo.getHostName(), vo.getHostName());
			}
			parameterMap.put("ip_host_map", getIpHostMapStr(mapIpHostName));
		}else {
		  throw new Exception("k8sWorkers is empty");
		}
		if(clusterInstanceDetailInfo.getK8sMasters() != null && clusterInstanceDetailInfo.getK8sMasters().size() > 0) {
			 Map<String, String> mapIpHostName = new HashMap<String, String>();
			for(ResourceVo vo : clusterInstanceDetailInfo.getK8sMasters()) {
				mapIpHostName.put(vo.getHostName(), vo.getHostName());
			}
			parameterMap.put("ip_masterhost_map", getIpHostMapStr(mapIpHostName));
		}else {
		  throw new Exception("k8sWorkers is empty");
		}
		
		long tenantId = belongsTenantInfo.getTenantId();

		parameterMap.put("ingress_worker", clusterInstanceDetailInfo.getK8sWorkIpList().get(0));
		parameterMap.put("requests_cpu", String.valueOf(clusterInstanceDetailInfo.getCpuQuota()));
		parameterMap.put("request_memory", String.valueOf(clusterInstanceDetailInfo.getMemoryQuota())+"Gi");
		//prometheus和fluentd使用的参数，为了让ywadmin也可以创建，添加如下参数
		parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getFloatIp());
		parameterMap.put("appclusterNode", clusterInstanceDetailInfo.getFloatIp());
		parameterMap.put("registryHost", belongsTenantInfo.getImageProject());
		parameterMap.put("logClusterId", clusterInstanceDetailInfo.getClusterId().toString());
		parameterMap.put("logClusterKafkaClusterBrokerList", clusterInstanceDetailInfo.getKafkaBrokerUrl());
		parameterMap.put("k8sLogPath", clusterInstanceDetailInfo.getK8sLogPath());
		parameterMap.put("docker_image_reposity", installerConfig.getClusterImagePath());
		parameterMap.put("k8s_home", clusterInstanceDetailInfo.getHomePath());
		// 这个/usr/local/bin是苏研k8s路径，执行kubectl使用
		parameterMap.put("k8sHome", clusterInstanceDetailInfo.getHomePath());
		parameterMap.put("imageUsername", belongsTenantInfo.getImageUsername());
		parameterMap.put("imagePassword", belongsTenantInfo.getImagePassword());
		parameterMap.put("imageProjectDir", belongsTenantInfo.getImageProjectDir());
		parameterMap.put("imageProject", belongsTenantInfo.getImageProject());
		
	    if(tenantId > 100l){
			parameterMap.put("tenantZone", "tenantZone");
			parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getFloatIp());
			parameterMap.put("k8s_home", clusterInstanceDetailInfo.getHomePath());
			parameterMap.put("name_space", ZoneConstants.ZONE_PREFIX+String.valueOf(belongsTenantInfo.getTenantId()));
			parameterMap.put("limit_cpu", String.valueOf(clusterInstanceDetailInfo.getCpuQuota()));
			parameterMap.put("limit_memory", String.valueOf(clusterInstanceDetailInfo.getMemoryQuota())+"Gi");
			parameterMap.put("tenant_harbor_path", installerConfig.getPubFtpAddress());
			parameterMap.put("ansible_alias", getInstallConfig().getSelectJenkinsConfig().getJenkinsAnsibleAlias());
			parameterMap.put("docker_image_reposity", installerConfig.getClusterImagePath());
			parameterMap.put("docker_username", installerConfig.getClusterImageUserName());
			parameterMap.put("docker_password", installerConfig.getClusterImagePassword());
			parameterMap.put("docker_server_name_space", belongsTenantInfo.getImageProject());
			parameterMap.put("docker_username_name_space", belongsTenantInfo.getImageUsername());
			parameterMap.put("docker_password_name_space", belongsTenantInfo.getImagePassword());
			parameterMap.put("install_step_name", "install_zone");


		}
        List<String> k8sWorkIpList = clusterInstanceDetailInfo.getK8sWorkIpList();
	    if(k8sWorkIpList != null && k8sWorkIpList.size()>0){
	        parameterMap.put("k8s_work_address", clusterInstanceDetailInfo.getK8sWorkIpList().get(0));
	    }else{
	    	throw new Exception("k8s worker ip is empty");
	    }
		log.info(LogProperty.LOGTYPE_DETAIL, "K8s install parameter map (gson)->" + new Gson().toJson(parameterMap));
		///////////////////////////////
		//parameterMap.put("requests_cpu", "1");
		//parameterMap.put("request_memory", "1Gi");
		//parameterMap.put("limit_cpu", "1");
		//parameterMap.put("limit_memory", "1Gi");
		//parameterMap.put("name_space", "default");
		//parameterMap.put("install_yaml_file", "install_k8s_ingress.yaml");
		//parameterMap.put("ingress_worker", clusterInstanceDetailInfo.getK8sWorkIpList().get(0));
	}

	private String getIpHostMapStr( Map<String, String> mapIpHostName) {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		for ( Map.Entry<String, String> entry : mapIpHostName.entrySet() ) {
			if ( isFirst )
				isFirst = false;
			else
				sb.append(',');
			sb.append(entry.getKey());
			sb.append(": ");
			sb.append(entry.getValue());
		}
		return sb.toString();
	}
	
	@Override public String getDeployStep() throws Exception{
		return InstallerConfig.getInstallEtcdK8sDeployStep();
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

