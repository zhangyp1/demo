package com.newland.paas.ee.util;

import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;

public class LoadBalanceUtil {
	/**
	 * 通过在负载均衡配置的Id前面加个a，得到负载均衡配置的字符串
	 * @param loadBalanceId  负载均衡配置的Id
	 * @return 负载均衡配置的字符串
	 */
	public static String getLoadBalanceIdStr(Long loadBalanceId) {
		return ZoneConstants.LOAD_BALANCE_PREFIX + Long.toString(loadBalanceId);
	}

	/**
	 * 通过在负载均衡配置的Id前面加个a，得到负载均衡配置的字符串
	 * @param appLoadBalanceConfig 负载均衡配置
	 * @return 负载均衡配置的字符串
	 */
	public static String getLoadBalanceIdStr(AppLoadBalanceConfig appLoadBalanceConfig) {
		return getLoadBalanceIdStr(appLoadBalanceConfig.getLoadBalanceId());
	}
}
