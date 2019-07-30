package com.newland.paas.ee.installer.clu;

import com.google.gson.Gson;
import com.newland.paas.common.util.Json;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.CmssK8sCluInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.TreeMap;

public class CmssK8sClusterScaleOut extends AbstractInstaller {

    private static final Log log = LogFactory.getLogger(CmssK8sClusterScaleOut.class);

    public CmssK8sClusterScaleOut(InstallerConfig installerConfig, CmssK8sCluInstanceDetailInfo instanceDetailInfo,
                                  TenantInstanceDetailInfo belongsTenantInfo) throws Exception {
        super(installerConfig, "scaleout_cmssk8s_worker");

        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
        K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
        K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);

        parameterMap.put("clusterName", instanceDetailInfo.getName());

        //苏研扩容只要kubenode的参数，eg:{kubenode: [{"ip":"10.132.46.113","hostname": "paas-deploy-master-1","password":"123456"},{"ip": "10.132.46.114" ,"hostname": "paas-deploy-cic-1","password": '123456'}]]}
        instanceDetailInfo.setKubemaster(null);
        instanceDetailInfo.setEtcd(null);
        instanceDetailInfo.setName(null);
        instanceDetailInfo.setController_cluster(null);
        instanceDetailInfo.getKubenode().forEach(nodeInfo -> {
            nodeInfo.setPassword("");
        });
        parameterMap.put("req_body", Json.toJson(instanceDetailInfo));
        log.info(LogProperty.LOGTYPE_DETAIL, "CMSS scale out  req_body (gson)->" + Json.toJson(instanceDetailInfo));
        log.info(LogProperty.LOGTYPE_DETAIL, "CMSS scale out  req_body (gson)->" + new Gson().toJson(instanceDetailInfo));
        log.info(LogProperty.LOGTYPE_DETAIL, "CMSS scale out  parameter map (gson)->" + new Gson().toJson(parameterMap));
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getScaleOutCmssK8sDeployStep();
    }

    @Override
    public TreeMap<String, String> getDeployParameter() {
        return parameterMap;
    }

    public OperateResult scaleOut() throws Exception {
        createAndBuildJobParam();
        return createOperatorResult();
    }

    private TreeMap<String, String> parameterMap = new TreeMap<>();
}

