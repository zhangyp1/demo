package com.newland.paas.common.invocation;


import com.newland.paas.common.invocation.handle.retry.RetryContext;

/**
 * Copyright (c) 2015, NEWLAND,LTD All Rights Reserved.
 *
 * @ClassName:IInvocation
 * @Description: 方法调用接口抽象。<br/>
 *               主要目的为：解耦方法调用与方法执行。
 * @Function List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年6月18日下午3:03:43
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public interface IInvocation {
    /**
     * @Function:     handle 
     * @Description:  调用处理 
     *
     * @param ctx     重试上下文件
     * @param args    调用参数
     * @return
     */
	public Object handle(RetryContext ctx, Object... args) throws Throwable;
}