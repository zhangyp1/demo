package com.newland.paas.ee.util;

import com.newland.paas.ee.installer.AstUnitRspLoadBalance;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.asset.AstExternalSvrIntfRsp;
import com.newland.paas.ee.vo.asset.AstUnitRsp;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.LinkedList;
import java.util.List;

public class AstUnitRspLoadBalanceUtil {
	private static final Log log = LogFactory.getLogger(AstUnitRspLoadBalanceUtil.class);

	/**
	 * 设置资产单元的负载均衡配置
	 * @param astUnitRspLoadBalanceList 资产单元负载均衡列表
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param aidi		执行的对象，如应用
	 */
	public static void setAstLoadBalanceConfig ( List<AstUnitRspLoadBalance> astUnitRspLoadBalanceList,
			AssetDetailInfo assetDetailInfo, AppInstanceDetailInfo aidi ) {
		for ( AppLoadBalanceConfig appLoadBalanceConfig : aidi.getAppLoadBalanceConfigs())
			setAstUnitRspByLoadBalance(assetDetailInfo, astUnitRspLoadBalanceList, appLoadBalanceConfig);
	}

	/**
	 * 转化List<AstUnitRsp>为List<AstUnitRspLoadBalance>， AstUnitRspLoadBalance的appLoadBalanceConfigList缺省为empty List,
	 * 后续代码进行填充
	 * @param astUnitRspList 资产单元列表
	 * @return AstUnitRspLoadBalance列表
	 */
	public static List<AstUnitRspLoadBalance> astUnitRspToDefaultLoadBalanceConfig( List<AstUnitRsp> astUnitRspList ) {
		List<AstUnitRspLoadBalance> result = new LinkedList<>();
		for ( AstUnitRsp astUnitRsp : astUnitRspList ) {
			AstUnitRspLoadBalance astUnitRspLoadBalance = new AstUnitRspLoadBalance();
			astUnitRspLoadBalance.setAstUnitRsp(astUnitRsp);
			result.add(astUnitRspLoadBalance);
		}
		return result;
	}

	/**
	 * 通过负载均衡配置，找到对应的资产单元， 然后绑定它们
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param astUnitRspLoadBalanceList  资产单元负载均衡配置列表
	 * @param appLoadBalanceConfig  负载均衡配置
	 */
	public static void setAstUnitRspByLoadBalance( AssetDetailInfo assetDetailInfo, List<AstUnitRspLoadBalance> astUnitRspLoadBalanceList,
			AppLoadBalanceConfig appLoadBalanceConfig ) {
		//通过负载均衡配置的接入点名称,找到外部服务接口
		AstExternalSvrIntfRsp astExternalSvrIntfRsp = getSvrInterfacesByEndpointName(assetDetailInfo, appLoadBalanceConfig.getEndpointName());

		if ( astExternalSvrIntfRsp==null) {
			log.info(LogProperty.LOGTYPE_DETAIL, "can not find AstExternalSvrIntfRsp interface key="+ appLoadBalanceConfig.getEndpointName() +
					" from AssetDetailInfo where assetId=" + assetDetailInfo.getAssetId());
			return;
		}

		//通过外部服务接口的单元ID，找到对应的 资产单元负载均衡配置列表
		AstUnitRspLoadBalance astUnitRspLoadBalance = getAstUnitRspLoadBalance(astUnitRspLoadBalanceList, astExternalSvrIntfRsp.getUnitId());
		if ( astUnitRspLoadBalance == null ) {
			log.info(LogProperty.LOGTYPE_DETAIL, "can not find AstUnitRsp unit id="+ astExternalSvrIntfRsp.getUnitId() +
					" from AssetDetailInfo where asset id=" + assetDetailInfo.getAssetId());
			return;
		}

		//设置资产单元负载均衡配置列表为对应的负载均衡配置
		astUnitRspLoadBalance.addAppLoadBalanceConfig(appLoadBalanceConfig);
		log.info(LogProperty.LOGTYPE_DETAIL, "set AppLoadBalance appLoadBalanceId = " + appLoadBalanceConfig.getAppLoadBalanceId() +
				" to AstUnitRsp where astUnitRsp unit id="+ astUnitRspLoadBalance.getAstUnitRsp().getUnitId());
	}

	/**
	 * 通过接入点名称找到资产外部服务接口
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param endpointName  接入点名称
	 * @return 接入点名称为endpointName的资产外部服务接口
	 */
	public static AstExternalSvrIntfRsp getSvrInterfacesByEndpointName(AssetDetailInfo assetDetailInfo, String endpointName ) {
		List<AstExternalSvrIntfRsp> astExternalDependRspList = assetDetailInfo.getSvrInterfaces();
		if ( astExternalDependRspList == null )
			return null;
		for ( AstExternalSvrIntfRsp astExternalSvrIntfRsp : astExternalDependRspList ) {
			if (astExternalSvrIntfRsp.getIntfKey().equals(endpointName))
				return astExternalSvrIntfRsp;
		}

		return null;
	}

	/**
	 * 通过资产单元ID，找到对应的 资产单元负载均衡配置列表
	 * @param  astUnitRspList 资产单元负载均衡配置列表
	 * @unitId  资产单元ID
	 * @return 资产单元负载均衡配置列表
	 */
	public  static AstUnitRspLoadBalance getAstUnitRspLoadBalance(List<AstUnitRspLoadBalance> astUnitRspList, Long unitId) {
		for (AstUnitRspLoadBalance astUnitRspLoadBalance : astUnitRspList)
			if ( astUnitRspLoadBalance.getAstUnitRsp().getUnitId().equals(unitId))
				return astUnitRspLoadBalance;
		return null;
	}
}
