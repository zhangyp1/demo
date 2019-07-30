
package com.newland.paas.common.invocation.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ClassTypeExcludeFilter
 * @Description: 不包含具体异常类型过滤器实现
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年10月19日		下午3:08:06
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class ClassTypeExcludeFilter extends AbstractExceptionFilter{
    
    public   ClassTypeExcludeFilter(){
        
    }
    
    public   ClassTypeExcludeFilter(Class<?> c){
             addExcludeClz(c);
    }
    /*
     * 不包含异常类型集
     */
    public  Set<Class<?>>   excludeClassType = null;
    /**
     * @Function:     filte 
     * @Description:  执行异常过滤
     *
     * @param e       待过滤异常
     * @return        满足过滤条件返回原异常，否则返回NULL(包含需排除范围的返回null,否则返回原异常)
     */
    public   Throwable  filter(Throwable e) {
        return doFilter(e);
    }
    
    protected Throwable doFilter(Throwable e) {
        
        if(excludeClassType == null || 
                excludeClassType.size() < 1) return e;
        
        for(Class<?> c : excludeClassType){
            if(c.isAssignableFrom(e.getClass())){
                return null;
            }
        }
        return e;
    }
    
    public Set<Class<?>> getExcludeClzType() {
        return excludeClassType;
    }
    
    public void addExcludeClz(Class<?> c) {
        if(c == null) return ;
        if(excludeClassType == null)  excludeClassType = new HashSet<Class<?>>();
        synchronized(excludeClassType){
            excludeClassType.add(c);
        }
    }
    
    public void addExcludeClz(Class<?> ... args){
        if(args != null && args.length > 0){
            for(Class<?> c : args)
                addExcludeClz(c);
        }
    }
    
    public void clearExcludeClz(){
        if(this.excludeClassType != null){
            synchronized(excludeClassType){
                this.excludeClassType.clear();
            }
        }
    }
    
    public void removeExcludeClz(Class<?> c){
        synchronized (excludeClassType) {
            excludeClassType.remove(c);
        }
    }
    
    public void setExcludeClzSet(Set<Class<?>> excludeClzTypeSet) {
        this.excludeClassType = excludeClzTypeSet;
    }

}

