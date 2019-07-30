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

public class RawClusterUninstaller extends AbstractInstaller {
	 private static final Log log = LogFactory.getLogger(RawClusterUninstaller.class);
	 
	public RawClusterUninstaller(InstallerConfig installerConfig, RawClusterInstanceDetailInfo clusterInstanceDetailInfo, 
			TenantInstanceDetailInfo belongsTenantInfo ) throws Exception {
        super(installerConfig, "uninstall_rawclu");
        K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
		K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
		K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
		
        checkNotNull("clusterId", clusterInstanceDetailInfo.getClusterId());
        checkNotEmpty("rawHostIp", clusterInstanceDetailInfo.getRawHostIp());
        
        parameterMap.put("clusterId", String.valueOf(clusterInstanceDetailInfo.getClusterId().longValue()));
        parameterMap.put("k8s_master_float_ip", clusterInstanceDetailInfo.getRawHostIp());
        log.info(LogProperty.LOGTYPE_DETAIL, "RawClu install parameter map (gson)->" + new Gson().toJson(parameterMap));
	}
	
    public void checkNotNull(String paramKey,Long paramVal) {
    	if(paramVal == null){
    		throw new NLUnCheckedException(ErrorCode.SYSERROR_CONFIG, "参数"+paramKey+"不能为空，请校验参数!");
    	}
    }
    
    public void checkNotEmpty(String paramKey,String paramVal) {
    	if(StringUtils.isEmpty(paramVal)){
    		throw new NLUnCheckedException(ErrorCode.SYSERROR_CONFIG, "参数"+paramKey+"不能为空，请校验参数!");
    	}
    }
    
	@Override public String getDeployStep() throws Exception{
		return InstallerConfig.getUnInstallRawCluDeployStep();
 	}
	
	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}
	
	public OperateResult uninstall() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}		
	
	private TreeMap<String,String> parameterMap = new TreeMap<>();
}

