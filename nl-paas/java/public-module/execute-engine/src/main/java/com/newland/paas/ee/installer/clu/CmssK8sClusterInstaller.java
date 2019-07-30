package com.newland.paas.ee.installer.clu;

import com.google.gson.Gson;
import com.newland.paas.common.util.Json;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.CmssK8sCluInstanceDetailInfo;
import com.newland.paas.ee.vo.NodeInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CmssK8sClusterInstaller extends AbstractInstaller {

    private TreeMap<String, String> parameterMap = new TreeMap<>();

    private static final Log log = LogFactory.getLogger(CmssK8sClusterInstaller.class);

    public CmssK8sClusterInstaller(InstallerConfig installerConfig, CmssK8sCluInstanceDetailInfo clusterInstanceDetailInfo,
                                   TenantInstanceDetailInfo belongsTenantInfo) throws Exception {
        super(installerConfig, "install_cmssk8s_etcd");


        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
        K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
        K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);

        parameterMap.put("pubftp_address_path", installerConfig.getPubFtpAddress());

        String pubFtpUserName = installerConfig.getPubFtpUserName();
        if (StringUtils.isEmpty(pubFtpUserName))
            throw new Exception("k8s pubFtp user name is empty");
        parameterMap.put("pubftp_user_name", pubFtpUserName);
        parameterMap.put("clusterName", clusterInstanceDetailInfo.getName());

        String pubFtpPassword = installerConfig.getPubFtpPassword();
        if (StringUtils.isEmpty(pubFtpPassword))
            throw new Exception("k8s pubFtp password is empty");
        parameterMap.put("pubftp_password", pubFtpPassword);
        //parameterMap.put("ansible_alias", getInstallConfig().getSelectJenkinsConfig().getJenkinsAnsibleAlias());
        parameterMap.put("docker_image_reposity", installerConfig.getClusterImagePath());

        //prometheus和fluentd使用的参数，为了让ywadmin也可以创建，添加如下参数
        parameterMap.put("appclusterNode", clusterInstanceDetailInfo.getKubemaster().get(0).getIp());
        parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getKubemaster().get(0).getIp());
        parameterMap.put("registryHost", belongsTenantInfo.getImageProject());
        parameterMap.put("logClusterId", clusterInstanceDetailInfo.getLogClusterId().toString());
        parameterMap.put("cluster_uid", clusterInstanceDetailInfo.getCluster_uid());
        parameterMap.put("logClusterKafkaClusterBrokerList", clusterInstanceDetailInfo.getKafkaBrokerUrl());
        parameterMap.put("k8sLogPath", clusterInstanceDetailInfo.getK8sLogPath());
        // 这个/usr/local/bin是苏研k8s路径，执行kubectl使用
        parameterMap.put("k8sHome", "/usr/local/bin");
        parameterMap.put("k8s_home", "/usr/local/bin");
        parameterMap.put("imageUsername", belongsTenantInfo.getImageUsername());
        parameterMap.put("imagePassword", belongsTenantInfo.getImagePassword());
        parameterMap.put("imageProjectDir", belongsTenantInfo.getImageProjectDir());

        long tenantId = belongsTenantInfo.getTenantId();
        if(tenantId > 100l){
            parameterMap.put("tenantZone", "tenantZone");
            parameterMap.put("name_space", ZoneConstants.ZONE_PREFIX+String.valueOf(belongsTenantInfo.getTenantId()));
            parameterMap.put("limit_cpu", String.valueOf(clusterInstanceDetailInfo.getCpuQuota()));
            parameterMap.put("limit_memory", String.valueOf(clusterInstanceDetailInfo.getMemoryQuota())+"Gi");
            parameterMap.put("tenant_harbor_path", installerConfig.getPubFtpAddress());
            parameterMap.put("k8s_home", "/usr/local/bin");
            parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getKubemaster().get(0).getIp());
            parameterMap.put("ansible_alias", getInstallConfig().getSelectJenkinsConfig().getJenkinsAnsibleAlias());
            parameterMap.put("docker_image_reposity", installerConfig.getClusterImagePath());
            parameterMap.put("docker_username", installerConfig.getClusterImageUserName());
            parameterMap.put("docker_password", installerConfig.getClusterImagePassword());
            parameterMap.put("docker_server_name_space", belongsTenantInfo.getImageProject());
            parameterMap.put("docker_username_name_space", belongsTenantInfo.getImageUsername());
            parameterMap.put("docker_password_name_space", belongsTenantInfo.getImagePassword());
            parameterMap.put("install_step_name", "install_zone");
        }


        List<NodeInfo> nodeInfoList = clusterInstanceDetailInfo.getKubenode();
        if(nodeInfoList != null && nodeInfoList.size()>0){
            List<String> nodeIpsList = nodeInfoList.stream().map(f -> f.getIp()).collect(Collectors.toList());
            //eg:[10.1.1.1, 10.1.1.2, 10.1.1.3, 10.1.1.4, 10.1.1.5, 10.1.1.6, 10.1.1.7] 南京只要一个worker地址就好
            //parameterMap.put("k8s_work_address", nodeIpsList.toString().replaceAll(" ","").replace("]","").replace("[",""));
            parameterMap.put("k8s_work_address", nodeIpsList.get(0));
        }else{
            throw new Exception("k8s worker ip is empty");
        }

        //调用苏研不需要的参数，否则会报错，因此在这里设置为null
        clusterInstanceDetailInfo.setKafkaBrokerUrl(null);
        clusterInstanceDetailInfo.setLogClusterId(null);
        clusterInstanceDetailInfo.setName(null);
        clusterInstanceDetailInfo.setCpuQuota(null);
        clusterInstanceDetailInfo.setMemoryQuota(null);
        clusterInstanceDetailInfo.getKubemaster().forEach(nodeInfo->{
            nodeInfo.setPassword("");
        });
        clusterInstanceDetailInfo.getKubenode().forEach(nodeInfo->{
            nodeInfo.setPassword("");
        });
        clusterInstanceDetailInfo.setController_cluster(null);
        parameterMap.put("req_body", Json.toJson(clusterInstanceDetailInfo));
        log.info(LogProperty.LOGTYPE_DETAIL, "CMSS K8s install parameter map (gson)->" + new Gson().toJson(parameterMap));
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getInstallEtcdCmssK8sDeployStep();
    }

    @Override
    public TreeMap<String, String> getDeployParameter() {
        return parameterMap;
    }

    public OperateResult install() throws Exception {
        createAndBuildJobParam();
        return createOperatorResult();
    }

}

