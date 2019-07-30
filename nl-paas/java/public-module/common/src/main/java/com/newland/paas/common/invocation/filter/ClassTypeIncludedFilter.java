
package com.newland.paas.common.invocation.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ClassNameFilter
 * @Description: 类型过滤器
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年7月13日		上午11:05:05
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class ClassTypeIncludedFilter extends AbstractExceptionFilter {

    public  Set<Class<?>>   retryExceptionClasses = null;
    
    public Throwable doFilter(Throwable e) {
        
        if(retryExceptionClasses == null || 
                retryExceptionClasses.size() < 1) return e;
        
        for(Class<?> c : retryExceptionClasses){
            if(c.isAssignableFrom(e.getClass())){
                return e;
            }
        }
        
        return null;
    }
    
    public Set<Class<?>> getRetryThrowable() {
        return retryExceptionClasses;
    }
    
    public void addRetryException(Class<?> c) {
        if(c == null) return ;
        if(retryExceptionClasses == null)  retryExceptionClasses = new HashSet<Class<?>>();
        synchronized(retryExceptionClasses){
            retryExceptionClasses.add(c);
        }
    }
    
    public void addRetryExceptions(Class<?> ... args){
        if(args != null && args.length > 0){
            for(Class<?> c : args)
                addRetryException(c);
        }
    }
    
    public void clear(){
        if(this.retryExceptionClasses != null){
            synchronized(retryExceptionClasses){
                this.retryExceptionClasses.clear();
            }
        }
    }
    
    public void remove(Class<?> c){
        synchronized (retryExceptionClasses) {
            retryExceptionClasses.remove(c);
        }
    }
    
    public void setRetryThrowable(Set<Class<?>> retryClasses) {
        this.retryExceptionClasses = retryClasses;
    }
    
}

