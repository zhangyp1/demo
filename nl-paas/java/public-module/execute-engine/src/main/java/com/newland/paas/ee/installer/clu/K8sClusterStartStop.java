package com.newland.paas.ee.installer.clu;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.AnsibleUtil;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;

public class K8sClusterStartStop extends AbstractInstaller {

    private InstallerConfig installerConfig;
    private String stagePrefix;
    private K8SClusterInstanceDetailInfo clusterInstanceDetailInfo;
    private String startStopYamlFile;

    /**
     * 改造K8sClusterStartStop
     *
     * @param installerConfig
     * @param clusterInstanceDetailInfo
     */
    public K8sClusterStartStop(InstallerConfig installerConfig, K8SClusterInstanceDetailInfo clusterInstanceDetailInfo) {
        super(installerConfig);
        this.clusterInstanceDetailInfo = clusterInstanceDetailInfo;
        this.installerConfig = installerConfig;
    }

    /**
     * k8s 获取下一步部署脚本
     * @return
     * @throws Exception
     */
    @Override
    public String getDeployStep() throws Exception {
        String installDeployStep = InstallerConfig.getEtcdK8sStartStopStep();

        String etcdIp = StringUtils.join(clusterInstanceDetailInfo.getEtcdIpList().toArray(), ",");
        if (StringUtils.isEmpty(etcdIp))
            throw new Exception("k8s etcd ip is empty");
        installDeployStep = installDeployStep.replace("{{etcd_each_line_data}}", AnsibleUtil.getEachLineData("", "", clusterInstanceDetailInfo.getEtcdIpList()));

        String k8sMasterIp = StringUtils.join(clusterInstanceDetailInfo.getK8sMasterIpList().toArray(), ",");
        if (StringUtils.isEmpty(k8sMasterIp))
            throw new Exception("k8s master ip is empty");
        installDeployStep = installDeployStep.replace("{{k8s_master_each_line_data}}", AnsibleUtil.getEachLineData("", "", clusterInstanceDetailInfo.getK8sMasterIpList()));

        String k8sWorkerIp = StringUtils.join(clusterInstanceDetailInfo.getK8sWorkIpList().toArray(), ",");
        if (StringUtils.isEmpty(k8sWorkerIp))
            throw new Exception("k8s worker ip is empty");
        installDeployStep = installDeployStep.replace("{{k8s_worker_each_line_data}}", AnsibleUtil.getEachLineData("", "", clusterInstanceDetailInfo.getK8sWorkIpList()));

        String pubHarborAddress = installerConfig.getPubFtpAddress();
        if (StringUtils.isEmpty(pubHarborAddress))
            throw new Exception("k8s pub harbor address is empty");
        installDeployStep = installDeployStep.replace("{{tenant_harbor_path}}", installerConfig.getPubFtpAddress());

        String astUserName = installerConfig.getPubFtpUserName();
        if (StringUtils.isEmpty(astUserName))
            throw new Exception("k8s asset user name is empty");
        installDeployStep = installDeployStep.replace("{{ast_sshd_user}}", astUserName);

        String astPassword = installerConfig.getPubFtpPassword();
        if (StringUtils.isEmpty(astPassword))
            throw new Exception("k8s ast password is empty");
        installDeployStep = installDeployStep.replace("{{ast_sshd_password}}", astPassword);

        installDeployStep = installDeployStep.replace("{{deploy_yaml_file}}", startStopYamlFile);
        installDeployStep = installDeployStep.replace("{{stage_prefix}}", stagePrefix);
        return installDeployStep;
    }

    /**
     * 启动
     * @return
     * @throws Exception
     */
    public OperateResult start() throws Exception {
        stagePrefix = "start";
        startStopYamlFile = "etcd_k8s_start.yaml";
        createAndBuildJob(stagePrefix + "-etcd-k8s");
        return createOperatorResult();
    }

    /**
     * 暂停
     * @return
     * @throws Exception
     */
    public OperateResult stop() throws Exception {
        stagePrefix = "stop";
        startStopYamlFile = "etcd_k8s_stop.yaml";
        createAndBuildJob(stagePrefix + "-etcd-k8s");
        return createOperatorResult();
    }

}

