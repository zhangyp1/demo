package com.newland.paas.ee.installer;

import com.newland.paas.common.util.Json;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.MachineInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.ResourceDetailInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.List;

/**
 * @author chenshen
 * @Description com.newland.paas.ee.installer.SecretFreeInstaller
 * @Date 2018/7/9
 */
public class SecretFreeInstaller extends AbstractInstaller{
    private static final Log log = LogFactory.getLogger(SecretFreeInstaller.class);

    private ResourceDetailInfo resourceDetailInfo;
    private String depotPath;

    public SecretFreeInstaller(InstallerConfig installerConfig, ResourceDetailInfo resourceDetailInfo, String depotPath) {
        super(installerConfig);
        this.resourceDetailInfo = resourceDetailInfo;
        this.depotPath = depotPath;

        if (log.isDebugEnabled()){
            log.debug(LogProperty.LOGTYPE_DETAIL,"SecretFreeInstaller: "+ Json.toJson(resourceDetailInfo));
        }
    }

    @Override
    public String getDeployStep() throws Exception {
        List<MachineInfo> machineInfoList =  resourceDetailInfo.getMachineInfoList();
        String secretFreeDeployStep = InstallerConfig.getSecretFreeDeployStep();
        secretFreeDeployStep = secretFreeDeployStep.replace("{{hostlist}}", getHostList(machineInfoList));
        secretFreeDeployStep = secretFreeDeployStep.replace("{{hosts_data}}", getHostsData(machineInfoList));
        //secretFreeDeployStep = secretFreeDeployStep.replace("{{ansible_alias}}", getInstallConfig().getSelectJenkinsConfig().getJenkinsAnsibleAlias());
        secretFreeDeployStep = secretFreeDeployStep.replace("{{depot_path}}", depotPath);
        return secretFreeDeployStep;
    }

    public OperateResult push() throws Exception{
        createAndBuildJob( "ssh-copy-id");
        return createOperatorResult();
    }

    private String getHostsData(List<MachineInfo> machineInfoList ) {
        StringBuilder sb = new StringBuilder();
        for (MachineInfo machineInfo : machineInfoList) {
            sb.append( machineInfo.getIpAddress() + " ansible_user=" + machineInfo.getUserName() + " ansible_ssh_pass=\"" +  machineInfo.getPaasword()  + "\"");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getHostList(List<MachineInfo> machineInfoList ) {
        StringBuilder sb = new StringBuilder();
        for (MachineInfo machineInfo : machineInfoList) {
            sb.append(machineInfo.getIpAddress() + " ");
        }
        return sb.toString();
    }

}
