package com.newland.paas.ee.installer;
public class K8sInstaller {
	/*public void install( List<String> k8sMaster, List<String> k8sWorker , 
			String k8sMasterFloatIp, String k8sHome,  List<String> etcdIps, String ansibleAlias , String tenantHarborPath , 
			String dockerImageReposity) throws Exception{
		String deployStep = getInstallDeployStep(k8sMaster, k8sWorker , 
				k8sMasterFloatIp, k8sHome,  etcdIps, ansibleAlias , tenantHarborPath , dockerImageReposity );

		try ( JenkinsServer jenkinsServer = new JenkinsServer(new URI(InstallerConfig.getJenkinsUrl()), 
				InstallerConfig.getJekinsUserName(), InstallerConfig.getJeninsPassword()))
		{			
			JenkinUtil.runTempJob(jenkinsServer, "install_k8s" , deployStep);
		}
	}*/
	
	/*public String getInstallDeployStep(List<String> k8sMaster, List<String> k8sWorker, String k8sMasterFloatIp,
			String k8sHome,  List<String> etcdIps, String ansibleAlias , String tenantHarborPath, String dockerImageReposity) throws Exception{
		String installDeployStep = InstallerConfig.getInstallK8sDeployStep();
		installDeployStep = installDeployStep.replace("{{k8s_master_each_line_data}}", AnsibleUtil.getEachLineData("", "", k8sMaster));
		installDeployStep = installDeployStep.replace("{{k8s_worker_each_line_data}}", AnsibleUtil.getEachLineData("", "", k8sWorker));
		installDeployStep = installDeployStep.replace("{{k8s_master_float_ip}}", k8sMasterFloatIp);
		installDeployStep = installDeployStep.replace("{{k8s_home}}", k8sHome);
		installDeployStep = installDeployStep.replace("{{etcd_initial_cluster}}", getEtcdInitialCluster(etcdIps));
		installDeployStep = installDeployStep.replace("{{ansible_alias}}", ansibleAlias);
		installDeployStep = installDeployStep.replace("{{tenant_harbor_path}}", tenantHarborPath);
		installDeployStep = installDeployStep.replace("{{docker_image_reposity}}", dockerImageReposity);		
		installDeployStep = installDeployStep.replace("{{application_name}}", "k8s");			
        return installDeployStep;
	}*/
	
	/* public void uninstall( List<String> k8sMaster, List<String> k8sWorker, String k8sHome,  String ansibleAlias ,  
			String tenantHarborPath) throws Exception{
		String deployStep = getUninstallDeployStep(k8sMaster, k8sWorker, k8sHome,  ansibleAlias , tenantHarborPath);
		try ( JenkinsServer jenkinsServer = new JenkinsServer(new URI(InstallerConfig.getJenkinsUrl()), 
				InstallerConfig.getJekinsUserName(), InstallerConfig.getJeninsPassword()))
		{
			JenkinUtil.runTempJob(jenkinsServer, "uninstall_k8s" , deployStep );
		}
	} */
	
	/*private String getEtcdInitialCluster(List<String> etcdIps) {
		StringBuilder sb = new StringBuilder();
		for ( String etcdIp : etcdIps ) {
			sb.append("http://");
			sb.append(etcdIp);
			sb.append(":2379,");
		}		
		sb.setLength(sb.length()-1);		
		return sb.toString();
	}*/
	
	/*public String getUninstallDeployStep( List<String> k8sMaster, List<String> k8sWorker, String k8sHome,  String ansibleAlias, String tenantHarborPath) throws Exception{
		String uninstallDeployStep = InstallerConfig.getUninstallK8sDeployStep();
		uninstallDeployStep = uninstallDeployStep.replace("{{k8s_master_each_line_data}}", AnsibleUtil.getEachLineData("", "", k8sMaster));
		uninstallDeployStep = uninstallDeployStep.replace("{{k8s_worker_each_line_data}}", AnsibleUtil.getEachLineData("", "", k8sWorker));
		uninstallDeployStep = uninstallDeployStep.replace("{{k8s_home}}", k8sHome);
		uninstallDeployStep = uninstallDeployStep.replace("{{ansible_alias}}", ansibleAlias);
		uninstallDeployStep = uninstallDeployStep.replace("{{tenant_harbor_path}}", tenantHarborPath);
        return uninstallDeployStep;
	}	*/
}
