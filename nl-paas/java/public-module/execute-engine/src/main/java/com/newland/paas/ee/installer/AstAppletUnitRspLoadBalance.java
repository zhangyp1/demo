package com.newland.paas.ee.installer;

import java.util.LinkedList;
import java.util.List;

import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.asset.AstAppletDeployRsp;

// 子资产单元和其对应的负载均衡列表
public class AstAppletUnitRspLoadBalance {

    private AstAppletDeployRsp astAppletDeployRsp;
    private List<AppLoadBalanceConfig> appLoadBalanceConfigList = new LinkedList<>();

    public AstAppletDeployRsp getAstAppletDeployRsp() {
        return astAppletDeployRsp;
    }

    public void setAstAppletDeployRsp(AstAppletDeployRsp astAppletDeployRsp) {
        this.astAppletDeployRsp = astAppletDeployRsp;
    }

    public List<AppLoadBalanceConfig> getAppLoadBalanceConfig() {
        return appLoadBalanceConfigList;
    }

    public void addAppLoadBalanceConfig(AppLoadBalanceConfig appLoadBalanceConfig) {
        appLoadBalanceConfigList.add(appLoadBalanceConfig);
    }

    public boolean haveAppLoadBalanceConfig() {
        return !appLoadBalanceConfigList.isEmpty();
    }
}
