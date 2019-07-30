
package com.newland.paas.common.invocation.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ClassNameFilter
 * @Description: 类名过滤器
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
public class ClassNameIncludedFilter extends AbstractExceptionFilter {

    private  Set<String>  clzNames = null;
    
    public Throwable doFilter(Throwable e) {
        if(e == null) return null;
        String clzName = e.getClass().getName();
        
        if(clzNames == null){
            return e;
        }
        else if(clzNames.contains(clzName)){
            return e;
        }
        
        return null;
    }
    
    public void   addClzNames(String ... clzNames){
        if(clzNames != null && clzNames.length > 0){
            for(String clzName : clzNames){
                if(clzName != null){
                    addClzName(clzName);
                }
            }
        }
    }
    
    public void  addClzName(String clzName){
        if(this.clzNames == null)
            this.clzNames = new HashSet<String>();
        //加入过滤的异常类名称
        clzNames.add(clzName);
    }

    public Set<String> getClzNames() {
    
        return clzNames;
    }

    public void setClzNames(Set<String> clzNames) {
    
        this.clzNames = clzNames;
    }

}

