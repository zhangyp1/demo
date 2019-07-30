package com.newland.paas.ee.service;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.IngressInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

public interface IIngressExecuteEngine {
	OperateResult installIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo, 
			TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo);
	
	OperateResult uninstallIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi, 
			TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo );
}
