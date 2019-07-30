package com.newland.paas.ee.installer;
import java.util.List;

public class EtcdInstaller {		
	/*public void installEtcd( List<String> etcdIps, String etcdHome,  String ansibleAlias , String tenantHarborPath) throws Exception {

		String deployStep = getInstallDeployStep(etcdIps, etcdHome, ansibleAlias , tenantHarborPath );

		try ( JenkinsServer jenkinsServer = new JenkinsServer(new URI(InstallerConfig.getJenkinsUrl()), 
				InstallerConfig.getJekinsUserName(), InstallerConfig.getJeninsPassword()))
		{
			JenkinUtil.runTempJob(jenkinsServer, "install_etcd" , deployStep );
		}
	}*/
	
	/*public String getInstallDeployStep(List<String> etcdIps, String etcdHome, String ansibleAlias , String tenantHarborPath)  throws Exception{
		String installEtcdDeployStep = InstallerConfig.getInstallEtcdDeployStep();
		installEtcdDeployStep = installEtcdDeployStep.replace("{{etcd_each_line_data}}", getEtcdEachLineData(etcdIps));
		installEtcdDeployStep = installEtcdDeployStep.replace("{{etcd_initial_cluster}}", getEtcdInitialCluster(etcdIps));
		installEtcdDeployStep = installEtcdDeployStep.replace("{{etcd_home}}", etcdHome);        
		installEtcdDeployStep = installEtcdDeployStep.replace("{{ansible_alias}}", ansibleAlias);
		installEtcdDeployStep = installEtcdDeployStep.replace("{{tenant_harbor_path}}", tenantHarborPath);
		installEtcdDeployStep = installEtcdDeployStep.replace("{{application_name}}", "etcd");		
        return installEtcdDeployStep;
	}*/
	
	private String getEtcdEachLineData( List<String> etcdIps ) {
		StringBuilder sb = new StringBuilder();
		int count = 1;
		for ( String etcdIp : etcdIps ) {
			sb.append(etcdIp);
			sb.append(" ETCD_NAME=etcd");
			sb.append(count++);
			sb.append(" myhostname=");
			sb.append(etcdIp);
			sb.append("\n");
		}		
		return sb.toString();
	}
	
	/*private String getEtcdInitialCluster(List<String> etcdIps) {
		StringBuilder sb = new StringBuilder();
		int count = 1;
		for ( String etcdIp : etcdIps ) {
			sb.append("etcd");
			sb.append(count++);
			sb.append("=http://");
			sb.append(etcdIp);
			sb.append(":2380,");
		}		
		sb.setLength(sb.length()-1);		
		return sb.toString();
	}*/
	
	/*	public void uninstallEtcd( List<String> etcdIps, String etcdHome,  String ansibleAlias , String tenantHarborPath) throws Exception{
		String deployStep = getUninstallDeployStep(etcdIps, etcdHome, ansibleAlias , tenantHarborPath );

		try ( JenkinsServer jenkinsServer = new JenkinsServer(new URI(InstallerConfig.getJenkinsUrl()), 
				InstallerConfig.getJekinsUserName(), InstallerConfig.getJeninsPassword()))
		{
			JenkinUtil.runTempJob(jenkinsServer, "uninstall_etcd" , deployStep );
		}
	}*/
	
	/*public String getUninstallDeployStep(List<String> etcdIps, String etcdHome, String ansibleAlias , String tenantHarborPath)  throws Exception{
		String uninstallEtcdDeployStep  = InstallerConfig.getUninstallEtcdDeployStep();
		uninstallEtcdDeployStep = uninstallEtcdDeployStep.replace("{{etcd_each_line_data}}", getEtcdEachLineData(etcdIps));
		uninstallEtcdDeployStep = uninstallEtcdDeployStep.replace("{{etcd_home}}", etcdHome);        
		uninstallEtcdDeployStep = uninstallEtcdDeployStep.replace("{{ansible_alias}}", ansibleAlias);
		uninstallEtcdDeployStep = uninstallEtcdDeployStep.replace("{{tenant_harbor_path}}", tenantHarborPath);
		uninstallEtcdDeployStep = uninstallEtcdDeployStep.replace("{{application_name}}", "etcd");	
        return uninstallEtcdDeployStep;
	}*/
}
