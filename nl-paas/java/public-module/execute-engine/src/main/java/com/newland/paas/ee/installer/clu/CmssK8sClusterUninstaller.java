package com.newland.paas.ee.installer.clu;

import java.util.TreeMap;

import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.CmssK8sCluInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

public class CmssK8sClusterUninstaller extends AbstractInstaller {

    private TreeMap<String, String> parameterMap = new TreeMap<>();

    public CmssK8sClusterUninstaller(InstallerConfig installerConfig, CmssK8sCluInstanceDetailInfo instanceDetailInfo,
                                     TenantInstanceDetailInfo belongsTenantInfo) throws Exception {
        super(installerConfig, "uninstall_cmssk8s_etcd");

        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
        K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
        K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);

        parameterMap.put("clusterName", instanceDetailInfo.getName());
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getUninstallCmssK8sEtcdDeployStep();
    }

    @Override
    public TreeMap<String, String> getDeployParameter() {
        return parameterMap;
    }

    public OperateResult uninstall() throws Exception {
        createAndBuildJobParam();
        return createOperatorResult();
    }


}

