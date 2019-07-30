package com.newland.paas.ee.installer.clu;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class K8sClusterScaleIn extends AbstractInstaller {
    private TreeMap<String, String> parameterMap = new TreeMap<>();

    public K8sClusterScaleIn(InstallerConfig installerConfig, K8SClusterInstanceDetailInfo clusterInstanceDetailInfo,
                             TenantInstanceDetailInfo belongsTenantInfo) throws Exception {
        super(installerConfig, "uninstall_k8s_worker");
        K8sClusterUtil.checkAndAddFloatIpToMap(clusterInstanceDetailInfo.getFloatIp(), parameterMap);
        K8sClusterUtil.checkAndAddWorkerIpToMap(clusterInstanceDetailInfo.getK8sWorkIpList(), parameterMap);
        K8sClusterUtil.checkAndAddHomePath(clusterInstanceDetailInfo.getHomePath(), parameterMap);
        K8sClusterUtil.checkAndAddEtcdIpToMap(clusterInstanceDetailInfo.getEtcdIpList(), parameterMap);
        K8sClusterUtil.checkAndAddEtcdHomeToMap(clusterInstanceDetailInfo.getEtcdHome(), parameterMap);
        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
        K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
        K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
        //添加k8sWorkerHostnames变量
        List<String> k8s_worker_hostnames =  clusterInstanceDetailInfo.getK8sWorkers().stream().map(f -> f.getHostName()).collect(Collectors.toList());
    	String k8sWorkerHostnames= StringUtils.join(k8s_worker_hostnames.toArray(), " ");
		if ( StringUtils.isEmpty(k8sWorkerHostnames)) {
			throw new Exception("k8sWorkerHostnames is empty");
		}
		parameterMap.put("k8s_worker_hostnames", k8sWorkerHostnames);
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getScaleInK8sDeployStep();
    }

    @Override
    public TreeMap<String, String> getDeployParameter() {
        return parameterMap;
    }

    public OperateResult scaleIn() throws Exception {
        createAndBuildJobParam();
        return createOperatorResult();
    }
 
}

