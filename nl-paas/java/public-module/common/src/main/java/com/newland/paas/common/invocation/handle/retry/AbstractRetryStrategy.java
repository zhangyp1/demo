
package com.newland.paas.common.invocation.handle.retry;


import com.newland.paas.common.invocation.filter.AbstractExceptionFilter;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:AbstractRetryStrategy
 * @Description:  调用重试策略抽象实现
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年6月29日		上午11:45:26
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public abstract class AbstractRetryStrategy implements IRetryStrategy {

    protected AbstractExceptionFilter defaultFilter;
    /**
     * 日志标识
     */
    public  String          logTag = "";
    
    /**
     * 
     * @Function:     judgeException 
     * @Description:  重试异常判断，如果为指定的异常，或指定的异常为空情况，认为抛出的异常满足条件，则执行重试，否则直接抛出异常。    
     *                 <功能详细描述>
     *
     * @param filter  过滤器
     * @param e       待处理异常
     * @return true-过滤后异常非空，false-过滤后异常为空
     */
    protected  boolean  judgeException(AbstractExceptionFilter  filter,Throwable e){
        
        if(filter != null) {
            return filter.filter(e) != null;
        }
        else if(this.defaultFilter != null){
            return this.defaultFilter.filter(e) != null;
        }
        
        return true;
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public AbstractExceptionFilter getFilter() {
    
        return defaultFilter;
    }

    public void setFilter(AbstractExceptionFilter filter) {
    
        this.defaultFilter = filter;
    } 
    
    public void appendFilter(AbstractExceptionFilter filter){
        
    }
}

