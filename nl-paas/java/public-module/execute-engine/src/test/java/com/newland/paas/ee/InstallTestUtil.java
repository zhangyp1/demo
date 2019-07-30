package com.newland.paas.ee;
import com.newland.paas.ee.installer.config.JenkinsConfig;
import com.newland.paas.ee.vo.resource.ResourceVo;

import java.util.LinkedList;
import java.util.List;

public class InstallTestUtil {
	public static List<ResourceVo> ipToResourceVo( List<String> etcdIps )
	{
		List<ResourceVo> etcds = new LinkedList<>();		 
		for ( String ip : etcdIps ) {
			ResourceVo v = new ResourceVo();
			v.setIpAddress(ip);
			etcds.add(v);
		}		
		return etcds;
	}

	public static void addJenkinsConfig(String ansibleAlias, String url, String username, String password, List<JenkinsConfig> jenkinsList ) {
		JenkinsConfig jenkinsConfig = new JenkinsConfig();
		jenkinsConfig.setJenkinsAnsibleAlias(ansibleAlias);
		jenkinsConfig.setJenkinsUrl(url);
		jenkinsConfig.setJekinsUserName(username);
		jenkinsConfig.setJenkinsPassword(password);
		jenkinsList.add(jenkinsConfig);
	}
}
