package com.newland.paas.ee.installer.clu;

import java.util.TreeMap;

import com.google.gson.Gson;
import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.AbstractInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.RawClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

public class RawClusterInstaller extends AbstractInstaller {

    private TreeMap<String, String> parameterMap = new TreeMap<>();

    private static final Log log = LogFactory.getLogger(RawClusterInstaller.class);

    public RawClusterInstaller(InstallerConfig installerConfig, RawClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                   TenantInstanceDetailInfo belongsTenantInfo) throws Exception {
        super(installerConfig, "install_rawclu");
		K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
		K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
		K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
		
        checkNotEmpty("logClusterKafkaClusterBrokerList", clusterInstanceDetailInfo.getKafkaBrokerUrl());
        checkNotEmpty("rawHostIp", clusterInstanceDetailInfo.getRawHostIp());
        checkNotEmpty("rawHostName", clusterInstanceDetailInfo.getRawHostName());
        checkNotEmpty("clusterId", String.valueOf(clusterInstanceDetailInfo.getClusterId()));
        
        parameterMap.put("clusterId", String.valueOf(clusterInstanceDetailInfo.getClusterId().longValue()));
        parameterMap.put("logClusterKafkaClusterBrokerList", clusterInstanceDetailInfo.getKafkaBrokerUrl());
        parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getRawHostIp());
        parameterMap.put("rawHostName", clusterInstanceDetailInfo.getRawHostName());
        log.info(LogProperty.LOGTYPE_DETAIL, "RawClu install parameter map (gson)->" + new Gson().toJson(parameterMap));
    }

    @Override
    public String getDeployStep() throws Exception {
        return InstallerConfig.getInstallRawCluDeployStep();
    }

    public void checkNotEmpty(String paramKey,String paramVal) {
    	if(StringUtils.isEmpty(paramVal)){
    		throw new NLUnCheckedException(ErrorCode.SYSERROR_CONFIG, "参数"+paramKey+"不能为空，请校验参数!");
    	}
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

