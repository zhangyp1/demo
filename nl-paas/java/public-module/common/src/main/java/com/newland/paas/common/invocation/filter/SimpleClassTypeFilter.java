
package com.newland.paas.common.invocation.filter;
/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:SimpleClassTypeFilter
 * @Description: 简化版类过滤器
 * @Funtion List:TODO 主要函数及其功能
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年7月13日		下午2:36:32
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class SimpleClassTypeFilter extends AbstractExceptionFilter {
    /**
     * 预期类型
     */
    private  Class<?>  expertClassType = null;
    
    
    public  SimpleClassTypeFilter(){
        
    }
    
    public SimpleClassTypeFilter(Class<?>  expertClassType ){
        this.expertClassType = expertClassType;
    }
    
    protected Throwable doFilter(Throwable e) {
        if(e != null && expertClassType != null) {
            Class<?> c = e.getClass();
            if(expertClassType.isAssignableFrom(c)){
                return e;
            }
        }
        return null;
    }

    public Class<?> getExpertClassType() {
    
        return expertClassType;
    }

    public void setExpertClassType(Class<?> expertClassType) {
    
        this.expertClassType = expertClassType;
    }

}

