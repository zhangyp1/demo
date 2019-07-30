package com.newland.paas.ee.installer;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationConstants;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.*;

import java.util.Map;

public class K8sApplicationInstaller extends AbstractInstaller{
	/**
	 * 构造函数
	 * @param installerConfig		安装配置
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @param assetDetailInfo			依赖的资产详情信息
	 */
	public K8sApplicationInstaller(	InstallerConfig installerConfig, AppInstanceDetailInfo appInstanceDetailInfo,
			Map<String, ClusterInstanceDetailInfo> targetClusterList,
			TenantInstanceDetailInfo belongsTenantInfo, AssetDetailInfo assetDetailInfo ) {
		super(installerConfig);
		this.appInstanceDetailInfo = appInstanceDetailInfo;
		this.targetClusterList = targetClusterList;
		this.belongsTenantInfo = belongsTenantInfo;
		this.assetDetailInfo = assetDetailInfo;
	}

	/**
	 * 提取pipeline执行脚本
	 * @return pipeline执行脚本
	 */
	@Override public String getDeployStep() throws Exception {
		constructPreprocessingDeployStep();
		if ( stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY)) {
			sb.append(new DependAppDeployStepBuilder(assetDetailInfo, targetClusterList, appInstanceDetailInfo).constructDeployStep());
		}

		sb.append(new AssetDeployStepBuilder(stagePrefix, stagePrefixHuman, assetDetailInfo,
				targetClusterList, belongsTenantInfo, appInstanceDetailInfo, getInstallConfig() ).constructUnitDeployStep());
		return replaceApplicationNameVersionAstAddressAstUserAstPassword();
	}

	/**
	 * 替换对应的应用名字, 应用版本,资产地址，资产用户，资产密码
	 */
	public String replaceApplicationNameVersionAstAddressAstUserAstPassword() throws Exception {
		String installDeployStep = sb.toString();

		if ( StringUtils.isEmpty(belongsTenantInfo.getAstUsername()))
			throw new Exception("TenantInstanceDetailInfo asset user name is empty  where tenant id=" + belongsTenantInfo.getTenantId());
		installDeployStep = installDeployStep.replace("{{ast_sshd_user}}", belongsTenantInfo.getAstUsername());

		if ( StringUtils.isEmpty(belongsTenantInfo.getAstPassword()))
			throw new Exception("TenantInstanceDetailInfo asset password is empty  where tenant id=" + belongsTenantInfo.getTenantId());
		installDeployStep = installDeployStep.replace("{{ast_sshd_password}}", belongsTenantInfo.getAstPassword());

		if (StringUtils.isEmpty(belongsTenantInfo.getAstAddress()))
			throw new Exception("TenantInstanceDetailInfo asset address is empty  where tenant id=" + belongsTenantInfo.getTenantId());
		installDeployStep = installDeployStep.replace( "{{tenant_harbor_path}}", belongsTenantInfo.getAstAddress());

		if (StringUtils.isEmpty(assetDetailInfo.getAssetName()))
			throw new Exception("AssetDetailInfo asset name is empty where AssetDetailInfo.asset_id = " + assetDetailInfo.getAssetId());
		installDeployStep = installDeployStep.replace("{{application_name}}", assetDetailInfo.getAssetName());

		if (StringUtils.isEmpty(assetDetailInfo.getSelectedVersion()))
			throw new Exception("AssetDetailInfo selected version is empty where AssetDetailInfo.asset_id = " + assetDetailInfo.getAssetId());
		installDeployStep = installDeployStep.replace("{{application_version}}", assetDetailInfo.getSelectedVersion());

		return installDeployStep;
	}

	/**
	 * 首先要执行的是一些通用步骤, 比如建立jenkins workspace, 拷贝*.tar.gz到workspace
	 */
	void constructPreprocessingDeployStep ()  throws Exception {
		sb.append(InstallerConfig.getPreprocessingDeployStep() );
	}

	/**
	 * 安装应用
	 */
	public OperateResult install() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_DEPLOY;
		stagePrefixHuman = "安装";
		return invokeJob();
	}

	/**
	 * 卸载应用
	 */
	public OperateResult uninstall() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_UNLOAD;
		stagePrefixHuman = "卸载";
		return invokeJob();
	}

	/**
	 * 启动应用
	 */
	public OperateResult start() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_START;
		stagePrefixHuman = "启动";
		return invokeJob();
	}

	/**
	 * 停止应用
	 */
	public OperateResult stop() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_STOP;
		stagePrefixHuman = "停止";
		return invokeJob();
	}

	/**
	 * 应用扩容
	 */
	public OperateResult scaleOut() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_SCALE_OUT;
		stagePrefixHuman = "扩容";
		return invokeJob();
	}

	/**
	 * 应用缩容
	 */
	public OperateResult scaleIn() throws Exception{
		stagePrefix = ApplicationConstants.APPLICATION_PF_SCALE_IN;
		stagePrefixHuman = "缩容";
		return invokeJob();
	}

	private OperateResult  invokeJob() throws Exception {
		createAndBuildJob( stagePrefix +  "-" + appInstanceDetailInfo.getAppName() );
		return createOperatorResult();	
	}
	
	private AppInstanceDetailInfo appInstanceDetailInfo;
	private Map<String, ClusterInstanceDetailInfo> targetClusterList;
	private TenantInstanceDetailInfo belongsTenantInfo;
	private AssetDetailInfo assetDetailInfo;	
	private StringBuilder sb = new StringBuilder();
	private String stagePrefix; 
	private String stagePrefixHuman;
}

