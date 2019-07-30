
package com.newland.paas.common.invocation.filter;
/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ExceptionFileter
 * @Description: 异常过滤器,支持过滤器链。
 * @Funtion List:TODO 主要函数及其功能
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年7月13日		上午10:44:48
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public abstract class AbstractExceptionFilter {
    /**
     * 下一个过滤器链
     */
    private  AbstractExceptionFilter nextFilter ;
    /**
     * 是否向下传播
     */
    private  boolean isPropagated = true;
    
    /**
     * @Function:     filte 
     * @Description:  执行异常过滤
     *
     * @param e       待过滤异常
     * @return        满足过滤条件返回原异常，否则返回NULL
     */
    public   Throwable  filter(Throwable e) {
        
        Throwable t = doFilter(e);
        
        if(t != null){
            return t;
        }
        else if(isPropagated && this.nextFilter != null){
            return this.nextFilter.doFilter(e);
        }
        else {
            return t;
        }
    }
    /**
     * @Function:     doFilter 
     * @Description:  异常过滤实现
     *
     * @param e
     * @return
     */
    protected  abstract  Throwable doFilter(Throwable e);
    
    protected boolean hasNextFilter(){
        return this.nextFilter != null;
    }
    
    public AbstractExceptionFilter getNextFilter() {
        return nextFilter;
    }

    public void setNextFilter(AbstractExceptionFilter nextFilter) {
        this.nextFilter = nextFilter;
    }
    
    public void appendChildren(AbstractExceptionFilter nextFilter){
        
        AbstractExceptionFilter filter = this;
        
        while(filter != null && filter.hasNextFilter()){
            filter = filter.getNextFilter();
        }
        
        filter.setNextFilter(nextFilter);
    }

    public boolean isPropagated() {
    
        return isPropagated;
    }

    public void setPropagated(boolean isPropagated) {
    
        this.isPropagated = isPropagated;
    }
}

