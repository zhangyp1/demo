package com.newland.paas.ee.util;

import java.util.LinkedList;
import java.util.List;

import com.newland.paas.ee.installer.AstAppletUnitRspLoadBalance;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.asset.AstAppletDeployRsp;
import com.newland.paas.ee.vo.asset.AstExternalSvrIntfRsp;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 这个类和AstUnitRspLoadBalanceUtil一样，区别只是一个是资产单元， 一个是资产子单元，注释见AstUnitRspLoadBalanceUtil.java
 */
public class AstAppletUnitRspLoadBalanceUtil {
    private static final Log log = LogFactory.getLogger(AstUnitRspLoadBalanceUtil.class);

    public static void setAstLoadBalanceConfig(List<AstAppletUnitRspLoadBalance> astAppletUnitRspLoadBalanceList,
        AssetDetailInfo assetDetailInfo, AppInstanceDetailInfo aidi) {
        for (AppLoadBalanceConfig appLoadBalanceConfig : aidi.getAppLoadBalanceConfigs())
            setAstAppletUnitRspByLoadBalance(assetDetailInfo, astAppletUnitRspLoadBalanceList, appLoadBalanceConfig);
    }

    public static List<AstAppletUnitRspLoadBalance>
        astUnitRspToDefaultLoadBalanceConfig(List<AstAppletDeployRsp> astUnitRspList) {
        List<AstAppletUnitRspLoadBalance> result = new LinkedList<>();
        for (AstAppletDeployRsp astAppletDeployRsp : astUnitRspList) {
            AstAppletUnitRspLoadBalance astUnitRspLoadBalance = new AstAppletUnitRspLoadBalance();
            astUnitRspLoadBalance.setAstAppletDeployRsp(astAppletDeployRsp);
            result.add(astUnitRspLoadBalance);
        }
        return result;
    }

    public static void setAstAppletUnitRspByLoadBalance(AssetDetailInfo assetDetailInfo,
        List<AstAppletUnitRspLoadBalance> astUnitRspLoadBalanceList, AppLoadBalanceConfig appLoadBalanceConfig) {
        AstExternalSvrIntfRsp astExternalSvrIntfRsp =
            getSvrInterfacesByEndpointName(assetDetailInfo, appLoadBalanceConfig.getEndpointName());

        if (astExternalSvrIntfRsp == null) {
            log.info(LogProperty.LOGTYPE_DETAIL,
                "can not find AstExternalSvrIntfRsp interface key=" + appLoadBalanceConfig.getEndpointName()
                    + " from AssetDetailInfo where assetId=" + assetDetailInfo.getAssetId());
            return;
        }

        AstAppletUnitRspLoadBalance astUnitRspLoadBalance =
            getAstAppletUnitRspLoadBalance(astUnitRspLoadBalanceList, astExternalSvrIntfRsp.getUnitId());
        if (astUnitRspLoadBalance == null) {
            log.info(LogProperty.LOGTYPE_DETAIL,
                "can not find AstAppletUnitRsp unit id=" + astExternalSvrIntfRsp.getUnitId()
                    + " from AssetDetailInfo where asset id=" + assetDetailInfo.getAssetId());
            return;
        }

        astUnitRspLoadBalance.addAppLoadBalanceConfig(appLoadBalanceConfig);
        log.info(LogProperty.LOGTYPE_DETAIL,
            "set AppLoadBalance appLoadBalanceId = " + appLoadBalanceConfig.getAppLoadBalanceId()
                + " to AstUnitRsp where astUnitRsp unit id="
                + astUnitRspLoadBalance.getAstAppletDeployRsp().getUnitId());
    }

    public static AstExternalSvrIntfRsp getSvrInterfacesByEndpointName(AssetDetailInfo assetDetailInfo,
        String endpointName) {
        List<AstExternalSvrIntfRsp> astExternalDependRspList = assetDetailInfo.getSvrInterfaces();
        if (astExternalDependRspList == null)
            return null;
        for (AstExternalSvrIntfRsp astExternalSvrIntfRsp : astExternalDependRspList)
            if (astExternalSvrIntfRsp.getIntfKey().equals(endpointName))
                return astExternalSvrIntfRsp;
        return null;
    }

    public static AstAppletUnitRspLoadBalance
        getAstAppletUnitRspLoadBalance(List<AstAppletUnitRspLoadBalance> astUnitRspList, Long unitId) {
        for (AstAppletUnitRspLoadBalance astUnitRspLoadBalance : astUnitRspList)
            if (astUnitRspLoadBalance.getAstAppletDeployRsp().getUnitId().equals(unitId))
                return astUnitRspLoadBalance;
        return null;
    }
}
