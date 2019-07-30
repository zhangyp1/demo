package com.newland.paas.ee.util;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.application.AppUnit;
import com.newland.paas.ee.vo.asset.AstExternalCtlIntfRsp;

public class AssetUtil {
	/**
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param unitId 资产单元ID
	 * @param unitName 资产单元名字
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @return pod名字
	 */
	public static String getDeployName(AssetDetailInfo assetDetailInfo, long unitId, String unitName,
			AppInstanceDetailInfo appInstanceDetailInfo) throws Exception {
		if (StringUtils.isEmpty(assetDetailInfo.getAssetName()))
			throw new Exception("AssetDetailInfo asset name is empty where asset id=" + assetDetailInfo.getAssetId());
		if ( StringUtils.isEmpty(unitName))
			throw new Exception("AstUnitRsp unit name is empty where unit id=" + unitId);
		return assetDetailInfo.getAssetName() + "-" + unitName + "-" + appInstanceDetailInfo.getAppInfoId();
	}

	/**
	 * 根据控制接口名字查找控制接口
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param ctlIntfMethodName  控制接口的名字
	 * @return 资产外部控制接口
	 */
	public static AstExternalCtlIntfRsp getAstExternalCtlIntfRspByStage(AssetDetailInfo assetDetailInfo, String ctlIntfMethodName) {
		for ( AstExternalCtlIntfRsp externalCtlIntf : assetDetailInfo.getCtlInterfaces() )
			if ( externalCtlIntf.getMethodName().equals(ctlIntfMethodName))
				return externalCtlIntf;

		return null;
	}

	/**
	 * 通过资产单元ID获取应用单元
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param unitId  资产单元ID
	 * @return 应用单元
	 */
	public static AppUnit getAppUnitByUnitId( AppInstanceDetailInfo appInstanceDetailInfo, long unitId ) {
		for ( AppUnit appUnit : appInstanceDetailInfo.getAppUnits() ) {
			if (appUnit.getUnitId().equals(unitId))
				return appUnit;
		}

		return null;
	}
}
