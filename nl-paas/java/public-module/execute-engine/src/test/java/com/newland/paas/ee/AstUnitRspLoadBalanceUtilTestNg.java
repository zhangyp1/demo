package com.newland.paas.ee;

import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.asset.AstUnitRsp;

import java.util.LinkedList;
import java.util.List;

public class AstUnitRspLoadBalanceUtilTestNg {
	List<AstUnitRsp> astUnitRspList = new LinkedList<>();
	AppInstanceDetailInfo appInstanceDetailInfo = new AppInstanceDetailInfo();

	/*
	@Test public void testNormal() {
		//1,3个可以关连, 2,4,5不可以关连
		//2 app endpoint不存在,  4 app unit不存在 , 5 astunit不存在
		prepareData();
		List<AstUnitRspLoadBalance> astUnitRspLoadBalanceList = AstUnitRspLoadBalanceUtil
				.setAstLoadBalanceConfig ( astUnitRspList, appInstanceDetailInfo );
		checkResult(astUnitRspLoadBalanceList);
	}

	private void prepareData() {
		prepareAppLoadBalanceConfig();
		prepareEndPoint() ;
		prepareAppUnit();
		prepareAstUnitRst();
	}

	void prepareAppLoadBalanceConfig() {
		List<AppLoadBalanceConfig> appLoadBalanceConfigList = new LinkedList<>();
		AppLoadBalanceConfig appLoadBalanceConfig1 = new AppLoadBalanceConfig();
		appLoadBalanceConfig1.setEndpointName("endpointname1");
		appLoadBalanceConfigList.add(appLoadBalanceConfig1);

		AppLoadBalanceConfig appLoadBalanceConfig2 = new AppLoadBalanceConfig();
		appLoadBalanceConfig2.setEndpointName("endpointname12");
		appLoadBalanceConfigList.add(appLoadBalanceConfig2);

		AppLoadBalanceConfig appLoadBalanceConfig3 = new AppLoadBalanceConfig();
		appLoadBalanceConfig3.setEndpointName("endpointname3");
		appLoadBalanceConfigList.add(appLoadBalanceConfig3);

		AppLoadBalanceConfig appLoadBalanceConfig4 = new AppLoadBalanceConfig();
		appLoadBalanceConfig4.setEndpointName("endpointname4");
		appLoadBalanceConfigList.add(appLoadBalanceConfig4);

		AppLoadBalanceConfig appLoadBalanceConfig5 = new AppLoadBalanceConfig();
		appLoadBalanceConfig5.setEndpointName("endpointname5");
		appLoadBalanceConfigList.add(appLoadBalanceConfig5);
		appInstanceDetailInfo.setAppLoadBalanceConfigs(appLoadBalanceConfigList);
	}

	void prepareEndPoint() {
		List<AppEndpoint> appEndpointList = new LinkedList<>();
		AppEndpoint appEndpoint1 = new AppEndpoint();
		appEndpoint1.setEndpointName("endpointname1");
		appEndpoint1.setAppUnitId(11L);
		appEndpointList.add(appEndpoint1);

		AppEndpoint appEndpoint3 = new AppEndpoint();
		appEndpoint3.setEndpointName("endpointname3");
		appEndpoint3.setAppUnitId(33L);
		appEndpointList.add(appEndpoint3);

		AppEndpoint appEndpoint4 = new AppEndpoint();
		appEndpoint4.setEndpointName("endpointname4");
		appEndpoint4.setAppUnitId(44L);
		appEndpointList.add(appEndpoint4);

		AppEndpoint appEndpoint5 = new AppEndpoint();
		appEndpoint5.setEndpointName("endpointname5");
		appEndpoint5.setAppUnitId(55L);
		appEndpointList.add(appEndpoint5);
		appInstanceDetailInfo.setAppEndpoints(appEndpointList);
	}

	void prepareAppUnit() {
		List<AppUnit> appUnitList = new LinkedList<>();
		AppUnit appUnit1 = new AppUnit();
		appUnit1.setAppUnitId(11L);
		appUnit1.setUnitId(111L);
		appUnitList.add(appUnit1);

		AppUnit appUnit2 = new AppUnit();
		appUnit2.setAppUnitId(22L);
		appUnit2.setUnitId(222L);
		appUnitList.add(appUnit2);

		AppUnit appUnit3 = new AppUnit();
		appUnit3.setAppUnitId(33L);
		appUnit3.setUnitId(333L);
		appUnitList.add(appUnit3);

		AppUnit appUnit5 = new AppUnit();
		appUnit5.setAppUnitId(55L);
		appUnit5.setUnitId(555L);
		appUnitList.add(appUnit5);
		appInstanceDetailInfo.setAppUnits(appUnitList);
	}

	void prepareAstUnitRst() {
		AstUnitRsp astUnitRsp1 = new AstUnitRsp();
		astUnitRsp1.setUnitId(111L);
		astUnitRsp1.setUnitName("1111");
		astUnitRspList.add(astUnitRsp1);

		AstUnitRsp astUnitRsp2 = new AstUnitRsp();
		astUnitRsp2.setUnitId(222L);
		astUnitRsp2.setUnitName("2222");
		astUnitRspList.add(astUnitRsp2);

		AstUnitRsp astUnitRsp3 = new AstUnitRsp();
		astUnitRsp3.setUnitId(333L);
		astUnitRsp3.setUnitName("3333");
		astUnitRspList.add(astUnitRsp3);

		AstUnitRsp astUnitRsp4 = new AstUnitRsp();
		astUnitRsp4.setUnitId(444L);
		astUnitRsp4.setUnitName("4444");
		astUnitRspList.add(astUnitRsp4);
	}

	void checkResult(List<AstUnitRspLoadBalance> astUnitRspLoadBalanceList) {
		AstUnitRspLoadBalance astUnitRspLoadBalance1 = astUnitRspLoadBalanceList.get(0);
		Assert.assertEquals(astUnitRspLoadBalance1.getAppLoadBalanceConfig().getEndpointName(), "endpointname1");
		Assert.assertEquals(astUnitRspLoadBalance1.getAstUnitRsp().getUnitName(), "1111");

		AstUnitRspLoadBalance astUnitRspLoadBalance2 = astUnitRspLoadBalanceList.get(1);
		Assert.assertNull(astUnitRspLoadBalance2.getAppLoadBalanceConfig());
		Assert.assertEquals(astUnitRspLoadBalance2.getAstUnitRsp().getUnitName(), "2222");

		AstUnitRspLoadBalance astUnitRspLoadBalance3 = astUnitRspLoadBalanceList.get(2);
		Assert.assertEquals(astUnitRspLoadBalance3.getAppLoadBalanceConfig().getEndpointName(), "endpointname3");
		Assert.assertEquals(astUnitRspLoadBalance3.getAstUnitRsp().getUnitName(), "3333");

		AstUnitRspLoadBalance astUnitRspLoadBalance4 = astUnitRspLoadBalanceList.get(3);
		Assert.assertNull(astUnitRspLoadBalance4.getAppLoadBalanceConfig());
		Assert.assertEquals(astUnitRspLoadBalance4.getAstUnitRsp().getUnitName(), "4444");
	}*/

}
