package com.newland.paas.ee.installer.clu;

import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.resource.ResourceVo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class K8sClusterScaleOut extends AbstractInstaller{
	public K8sClusterScaleOut(InstallerConfig installerConfig, K8SClusterInstanceDetailInfo clusterInstanceDetailInfo, 
			TenantInstanceDetailInfo belongsTenantInfo ) throws Exception {
		super(installerConfig, "install_k8s_worker");
		K8sClusterUtil.checkAndAddWorkerIpToMap(clusterInstanceDetailInfo.getK8sWorkIpList(), parameterMap);
		K8sClusterUtil.checkAndAddFloatIpToMap(clusterInstanceDetailInfo.getFloatIp(),parameterMap);
		K8sClusterUtil.checkAndAddHomePath(clusterInstanceDetailInfo.getHomePath(),parameterMap);
		K8sClusterUtil.checkAndAddEtcdIpToMap(clusterInstanceDetailInfo.getEtcdIpList(), parameterMap);
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
		return InstallerConfig.getScaleOutK8sDeployStep();
 	}
	
	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}
	
	public OperateResult scaleOut() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}		
	
	private TreeMap<String,String> parameterMap = new TreeMap<>();
}

