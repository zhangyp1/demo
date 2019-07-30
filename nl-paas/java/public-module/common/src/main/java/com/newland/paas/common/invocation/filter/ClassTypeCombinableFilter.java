
package com.newland.paas.common.invocation.filter;
/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ClassTypeCombinableFilter
 * @Description:  
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年10月19日		下午3:31:40
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class ClassTypeCombinableFilter extends AbstractExceptionFilter{
    /*
     * 需包含的异常类型
     */
    private ClassTypeIncludedFilter  includeFilter ;
    /*
     * 需排除的异常类型
     */
    private ClassTypeExcludeFilter   excludeFilter;
    
    public ClassTypeCombinableFilter(){
        
    }
    public ClassTypeCombinableFilter(ClassTypeIncludedFilter  includeFilter,
            ClassTypeExcludeFilter   excludeFilter){
        this.includeFilter = includeFilter;
        this.excludeFilter = excludeFilter;
    }
    /**
     * @Function:     filte 
     * @Description:  执行异常过滤
     *
     * @param e       待过滤异常
     * @return        满足过滤条件返回原异常，否则返回NULL
     */
    public   Throwable  filter(Throwable e) {
        return doFilter(e);
    }
    
    protected Throwable doFilter(Throwable e) {
        Throwable t = e;
        //先执行排除过滤器，不满足条件返回NULl
        if(excludeFilter != null) {
            t = excludeFilter.filter(e);
            if(t == null) return t;
        }
        //再执包含过滤器
        if(includeFilter != null){
            t = includeFilter.filter(e);
        }
        return t;
    }
    
    public ClassTypeIncludedFilter getIncludeFilter() {
    
        return includeFilter;
    }
    public ClassTypeExcludeFilter getExcludeFilter() {
    
        return excludeFilter;
    }
    public void setIncludeFilter(ClassTypeIncludedFilter includeFilter) {
    
        this.includeFilter = includeFilter;
    }
    public void setExcludeFilter(ClassTypeExcludeFilter excludeFilter) {
    
        this.excludeFilter = excludeFilter;
    }

}

