package com.newland.paas.log;

/**
 * MDC设置
 * @author Administrator
 *
 */
public interface Mdc {
	/**
	 * 设置交易ID
	 * @param dealId
	 */
	public void putDealId(String dealId);
	
	public void removeDealId();
	
	/**
	 * 设置会话ID
	 */
	public void putSessionId(String sessionId);
	
	public void removeSessionId();
}
