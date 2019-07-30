package com.newland.paas.ee.installer;

import com.newland.paas.ee.constant.AssetConstants;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;

import java.util.Map;

public class AssetDeployStepBuilder {
	private String stagePrefix;
	private String stagePrefixHuman;
	private AssetDetailInfo assetDetailInfo;
	private Map<String, ClusterInstanceDetailInfo> targetClusterList;
	private TenantInstanceDetailInfo belongsTenantInfo;
	private AppInstanceDetailInfo appInstanceDetailInfo;
	private InstallerConfig installerConfig;

	public AssetDeployStepBuilder (String stagePrefix, String stagePrefixHuman, AssetDetailInfo assetDetailInfo,
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo,
			AppInstanceDetailInfo appInstanceDetailInfo, InstallerConfig installerConfig) {
		this.stagePrefix = stagePrefix;
		this.stagePrefixHuman = stagePrefixHuman;
		this.assetDetailInfo = assetDetailInfo;
		this.targetClusterList = targetClusterList;
		this.belongsTenantInfo = belongsTenantInfo;
		this.appInstanceDetailInfo = appInstanceDetailInfo;
		this.installerConfig = installerConfig;
	}

	public String constructUnitDeployStep() throws Exception {
		String assetType = assetDetailInfo.getAssetType();
		//根据资产类型是子应用和子服务，调用对应的生成脚本步骤
		if (assetType.equals(AssetConstants.ASSET_TYPE_SUB_APPLICATION) || assetType.equals(AssetConstants.ASSET_TYPE_SUB_SERVICE))
			return new AssetSubAppDeployStepBuilder(stagePrefix, stagePrefixHuman, assetDetailInfo,
					targetClusterList, belongsTenantInfo, appInstanceDetailInfo, installerConfig).constructUnitDeployStep();
		else
			return new AssetAppDeployStepBuilder(stagePrefix, stagePrefixHuman, assetDetailInfo,
					targetClusterList, belongsTenantInfo, appInstanceDetailInfo, installerConfig).constructUnitDeployStep();
	}
}
