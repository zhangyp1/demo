package com.newland.paas.ee.installer.clu;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.util.LoadBalanceUtil;
import com.newland.paas.ee.util.ZoneDetailUtil;
import com.newland.paas.ee.vo.IngressInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;

import java.util.Random;
import java.util.TreeMap;

public class K8sIngressInstaller extends AbstractInstaller {

    private TreeMap<String, String> parameterMap = new TreeMap<>();

    private static final String BACKSLASH = "/";

    private static final String NGINXLOGPATH = "/nl/paas/logs/k8s/";

    public K8sIngressInstaller(String jobPrefix, InstallerConfig installerConfig, K8SClusterInstanceDetailInfo cidi,
            TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo, String installYamlFile) throws Exception {
        super(installerConfig, jobPrefix);
        if ( cidi.getK8sWorkIpList().isEmpty())
            throw new Exception("k8s cluster worker ip list is empty where cluster id=" + cidi.getClusterId());
        int k8sworkerIndex = new Random().nextInt(cidi.getK8sWorkIpList().size());
        parameterMap.put("k8s_worker_ip", cidi.getK8sWorkIpList().get(k8sworkerIndex));

        K8sClusterUtil.checkAndAddFloatIpToMap(cidi.getFloatIp(), parameterMap);
        K8sClusterUtil.checkAndAddHomePath(cidi.getHomePath(), parameterMap);
        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
        K8sClusterUtil.checkAndAddClusterImagePath(installerConfig.getClusterImagePath(), parameterMap);

        parameterMap.put("load_balance_id", LoadBalanceUtil.getLoadBalanceIdStr(ingressInfo.getLoadBalanceId()));
        if (StringUtils.isEmpty(ingressInfo.getIpAddrList()))
            throw new Exception("Ingress balance node is empty where ingress id=" + ingressInfo.getLoadBalanceId());
        parameterMap.put("pub_imageusername", installerConfig.getClusterImageUserName());
        parameterMap.put("pub_imagepassword", installerConfig.getClusterImagePassword());
        parameterMap.put("load_balance_node", ingressInfo.getIpAddrList());
        parameterMap.put("load_balance_hostname", ingressInfo.getHostName());
        parameterMap.put("default_back_end_limit_cpu", Float.toString(ingressInfo.getDefaultBackendLimitCpu()));
        parameterMap.put("default_back_end_limit_memory", Float.toString(ingressInfo.getDefaultBackendLimitMemory()) + "Gi");
        parameterMap.put("nginx_ingress_controller_limit_cpu", Float.toString(ingressInfo.getNginxIngressControllerLimitCpu()));
        parameterMap.put("nginx_ingress_controller_limit_memory", Float.toString(ingressInfo.getDefaultBackendLimitMemory()) + "Gi");
        parameterMap.put("http_port", Long.toString(ingressInfo.getHttpPort()));
        parameterMap.put("https_port", Long.toString(ingressInfo.getHttpsPort()));

        //czx add start
        parameterMap.put("status_port", ingressInfo.getStatusPort().toString());
        // the nginx log root path:/nl/paas/logs/k8s/{namespace}/{lbname}
        String appLogPath   =  NGINXLOGPATH + cidi.getDetailVo().getZoneName() + BACKSLASH + ingressInfo.getLbname() + BACKSLASH + "app";// /var/log/nginx/access.log
        String traceLogPath =  NGINXLOGPATH + cidi.getDetailVo().getZoneName() + BACKSLASH + ingressInfo.getLbname() + BACKSLASH + "trace";// /var/log/nginx/error.log
        parameterMap.put("app_log_path", appLogPath);
        parameterMap.put("trace_log_path", traceLogPath);
        //czx add end

        ZoneDetailVo zoneDetailVo = cidi.getDetailVo();
        ZoneDetailUtil.checkZoneDetail(cidi.getClusterId(), zoneDetailVo);
        parameterMap.put("name_space", zoneDetailVo.getZoneName());

        K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
        K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
        parameterMap.put("install_yaml_file", installYamlFile);

        if ( StringUtils.isEmpty(belongsTenantInfo.getImageProject()))
            throw new Exception("belongsTenantInfo.getImageProject() is null");
        parameterMap.put("registry_host", belongsTenantInfo.getImageProject());
        if ( belongsTenantInfo.getImageProjectDir()==null)
            throw new Exception("belongsTenantInfo.getImageProjectDir() is null");
        parameterMap.put("image_project_dir", belongsTenantInfo.getImageProjectDir());
        if ( StringUtils.isEmpty(belongsTenantInfo.getImageUsername()))
            throw new Exception("belongsTenantInfo.getImageUsername() is null");
        parameterMap.put("image_user_name", belongsTenantInfo.getImageUsername());
        if ( StringUtils.isEmpty(belongsTenantInfo.getImagePassword()))
            throw new Exception("belongsTenantInfo.getImagePassword() is null");
        parameterMap.put("image_password", belongsTenantInfo.getImagePassword());
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getInstallK8sIngressDeployStep();
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


