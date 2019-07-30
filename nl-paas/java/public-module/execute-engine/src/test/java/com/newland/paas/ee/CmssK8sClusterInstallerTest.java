package com.newland.paas.ee;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.installer.config.JenkinsConfig;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.cluster.CluZoneVo;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CmssK8sClusterInstallerTest {
	//@Test
	public void testMain() {
		try {
			testClusterInstall();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testClusterInstall()  throws Exception{
		JenkinsConfig jenkinsConfig = new JenkinsConfig();
		jenkinsConfig.setJenkinsAnsibleAlias("ansible104");
		jenkinsConfig.setJenkinsUrl("http://192.168.186.128:8080");
		jenkinsConfig.setJekinsUserName("admin");
		jenkinsConfig.setJenkinsPassword("paas");
		List<JenkinsConfig> jenkinsList = new LinkedList<>();
		jenkinsList.add(jenkinsConfig);
		InstallerConfig installerConfig = new InstallerConfig();
		installerConfig.setJenkins(jenkinsList);
		installerConfig.setClusterImagePath("10.1.8.16:5000");
		/*
		CmssK8sCluInstanceDetailInfo cluInfo  = new CmssK8sCluInstanceDetailInfo();
		//设置kubemaster
		List<NodeInfo> kubemaster =new ArrayList<NodeInfo>();
		for(int i=0;i<2;i++){
			if(i==0){
		      NodeInfo nodeInfo = new NodeInfo();
		      nodeInfo.setIp("10.132.46.113");
		      nodeInfo.setHostname("paas-deploy-master-1");
		      nodeInfo.setPassword("123456");
		      kubemaster.add(nodeInfo);
			}else if(i == 1){
				 NodeInfo nodeInfo = new NodeInfo();
			     nodeInfo.setIp("10.132.46.114");
			     nodeInfo.setHostname("paas-deploy-cic-1");
			     nodeInfo.setPassword("123456");
			     kubemaster.add(nodeInfo);
			}
		}
		cluInfo.setKubemaster(kubemaster);
		//设置kubenode
		List<NodeInfo> kubenode = new ArrayList<NodeInfo>();
		for(int i=0;i<2;i++){
			if(i==0){
		      NodeInfo nodeInfo = new NodeInfo();
		      nodeInfo.setIp("10.132.46.113");
		      nodeInfo.setHostname("paas-deploy-master-1");
		      nodeInfo.setPassword("123456");
		      kubenode.add(nodeInfo);
			}else if(i == 1){
				 NodeInfo nodeInfo = new NodeInfo();
			     nodeInfo.setIp("10.132.46.114");
			     nodeInfo.setHostname("paas-deploy-cic-1");
			     nodeInfo.setPassword("123456");
			     kubenode.add(nodeInfo);
			}
		}
		cluInfo.setKubenode(kubenode);
		//设置etcd
		List<String> etcd = new ArrayList<String>();
		etcd.add("paas-deploy-master-1");
		cluInfo.setEtcd(etcd);
		//设置user_management
		List<String> user_management = new ArrayList<String>();
		user_management.add("paas-deploy-master-1");
		cluInfo.setUser_management(user_management);
		//设置resource_management
		List<String> resource_management = new ArrayList<String>();
		resource_management.add("paas-deploy-master-1");
		cluInfo.setResource_management(resource_management);
		//设置appstore
		List<String> appstore = new ArrayList<String>();
		appstore.add("paas-deploy-master-1");
		cluInfo.setAppstore(appstore);
		//设置ingress
		List<String> ingress = new ArrayList<String>();
		ingress.add("paas-deploy-master-1");
		cluInfo.setIngress(ingress);
		//设置webssh
		List<String> webssh = new ArrayList<String>();
		webssh.add("paas-deploy-master-1");
		cluInfo.setWebssh(webssh);
		//设置alertmanager
		List<String> alertmanager = new ArrayList<String>();
		alertmanager.add("paas-deploy-master-1");
		cluInfo.setAlertmanager(alertmanager);
		//设置alertmanager
		List<String> openresty = new ArrayList<String>();
		openresty.add("paas-deploy-cic-1");
		cluInfo.setOpenresty(openresty);
		//设置harbor
		cluInfo.setHarbor("paas-deploy-cic-1");
		//设置es
		List<String> es = new ArrayList<String>();
		es.add("paas-deploy-master-1");
		cluInfo.setEs(openresty);
		cluInfo.setController_cluster("true");
		cluInfo.setDomain_uid("1235666");
		
		System.out.println(Json.toJson(cluInfo));
		*/
		TenantInstanceDetailInfo belongsTenantInfo = new TenantInstanceDetailInfo();
		belongsTenantInfo.setAstAddress("10.1.8.13:/home/dragon/harbor182");
		belongsTenantInfo.setImageProject("10.1.8.16:5000");
		
		K8SClusterInstanceDetailInfo detailInfo = new K8SClusterInstanceDetailInfo();
		detailInfo.setMasterFloatIp("10.1.8.109");
		detailInfo.setHomePath("/home/k8s/");
		CluZoneVo zoneVo = new CluZoneVo();
		List<ZoneDetailVo> zoneDetailList = new ArrayList<ZoneDetailVo>();
		
		ZoneDetailVo zoneDetailVo1 = new ZoneDetailVo();
		zoneDetailVo1.setCpuQuota(new Float(1));
		zoneDetailVo1.setMemoryQuota(new Float(1024));
		zoneDetailVo1.setTenantId(101l);
		zoneDetailVo1.setTenantInfo(belongsTenantInfo);
		zoneDetailList.add(zoneDetailVo1);
		
		ZoneDetailVo zoneDetailVo2 = new ZoneDetailVo();
		zoneDetailVo2.setCpuQuota(new Float(1));
		zoneDetailVo2.setMemoryQuota(new Float(1029));
		zoneDetailVo2.setTenantId(102l);
		zoneDetailList.add(zoneDetailVo2);
		zoneDetailVo2.setTenantInfo(belongsTenantInfo);
		
		zoneVo.setClusterZones(zoneDetailList);
		
		ExecuteEngineMgt engineMgt = new ExecuteEngineMgt();
		engineMgt.installZone(installerConfig, detailInfo, null, zoneVo);
	}

}
