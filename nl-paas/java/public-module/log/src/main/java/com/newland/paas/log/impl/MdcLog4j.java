package com.newland.paas.log.impl;

import com.newland.paas.log.LogProperty;
import com.newland.paas.log.Mdc;
import org.apache.logging.log4j.ThreadContext;

/**
 * MDC设置log4j实现
 * @author Administrator
 *
 */
public class MdcLog4j implements Mdc {

	/**
	 * 设置交易ID
	 * @param dealId
	 */
	public void putDealId(String dealId) {
		ThreadContext.put(LogProperty.LOGCONFIG_DEALID, dealId);
	}

	public void removeDealId() {
		ThreadContext.remove(LogProperty.LOGCONFIG_DEALID);
	}

	/**
	 * 设置会话ID
	 */
	public void putSessionId(String sessionId) {
		ThreadContext.put(LogProperty.LOGCONFIG_SESSIONID, sessionId);
	}

	public void removeSessionId() {
		ThreadContext.remove(LogProperty.LOGCONFIG_SESSIONID);
	}
}
