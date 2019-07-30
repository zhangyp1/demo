package com.newland.paas.common.invocation.handle.retry;


import com.newland.paas.common.invocation.IInvocation;
import com.newland.paas.common.invocation.filter.AbstractExceptionFilter;
import com.newland.paas.common.invocation.handle.retry.exception.RetryFailureException;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:IRetryStrategy
 * @Description: 重试策略接口抽象
 * @Function List:
 *
 * @author chenzc
 * @version
 * @Date 2015年6月18日下午3:07:57
 *
 * @History:// 历史修改记录 
 *                     <author>  // 修改人
 *                     <time>    // 修改时间
 *                     <version> // 版本
 *                     <desc>  // 描述修改内容
 */
public interface IRetryStrategy {

    /**
     * @Function:     execute 
     * @Description:  重试策略执行  
     *
     * @param invoSvc 调用对象引用
     * @param filter  异常过滤器
     * @param args 调用参数
     * @return
     * @throws RetryFailureException 重试失败异常
     */
    public Object execute(IInvocation invoSvc, AbstractExceptionFilter filter, Object... args)
            throws RetryFailureException;
    

}