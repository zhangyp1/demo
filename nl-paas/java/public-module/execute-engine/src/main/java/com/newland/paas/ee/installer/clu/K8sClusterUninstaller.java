package com.newland.paas.ee.installer.clu;

import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

import java.util.TreeMap;

public class K8sClusterUninstaller extends AbstractInstaller {
	public K8sClusterUninstaller(InstallerConfig installerConfig, K8SClusterInstanceDetailInfo clusterInstanceDetailInfo, 
			TenantInstanceDetailInfo belongsTenantInfo ) throws Exception {
		super(installerConfig, "uninstall_k8s_etcd");
		K8sClusterUtil.checkAndAddMasterIpToMap(clusterInstanceDetailInfo.getK8sMasterIpList(), parameterMap);
		K8sClusterUtil.checkAndAddWorkerIpToMap(clusterInstanceDetailInfo.getK8sWorkIpList(), parameterMap);
		K8sClusterUtil.checkAndAddHomePath(clusterInstanceDetailInfo.getHomePath(),parameterMap);
		K8sClusterUtil.checkAndAddEtcdIpToMap(clusterInstanceDetailInfo.getEtcdIpList(), parameterMap);
		K8sClusterUtil.checkAndAddEtcdHomeToMap(clusterInstanceDetailInfo.getEtcdHome(), parameterMap);
		K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
		K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
		K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
	}
	
	@Override public String getDeployStep() throws Exception{
		return InstallerConfig.getUninstallK8sEtcdDeployStep();
 	}
	
	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}
	
	public OperateResult uninstall() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}		
	
	private TreeMap<String,String> parameterMap = new TreeMap<>();
}

