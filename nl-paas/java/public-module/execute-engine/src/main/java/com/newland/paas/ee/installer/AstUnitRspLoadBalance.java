package com.newland.paas.ee.installer;

import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.asset.AstUnitRsp;

import java.util.LinkedList;
import java.util.List;

//资产单元和其对应的负载均衡列表
public class AstUnitRspLoadBalance {
	public AstUnitRsp getAstUnitRsp() {
		return astUnitRsp;
	}

	public void setAstUnitRsp(AstUnitRsp astUnitRsp) {
		this.astUnitRsp = astUnitRsp;
	}

	public List<AppLoadBalanceConfig> getAppLoadBalanceConfig() {
		return appLoadBalanceConfigList ;
	}

	public void addAppLoadBalanceConfig(AppLoadBalanceConfig appLoadBalanceConfig) {
		appLoadBalanceConfigList.add(appLoadBalanceConfig);
	}

	public boolean haveAppLoadBalanceConfig() {
		return !appLoadBalanceConfigList.isEmpty();
	}

	private AstUnitRsp astUnitRsp;
	private List<AppLoadBalanceConfig> appLoadBalanceConfigList = new LinkedList<AppLoadBalanceConfig>();
}
