
package com.newland.paas.common.util;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ExceptionUtils
 * @Description: 异常工具类
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年7月23日		上午9:59:47
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class ExceptionUtils {
    
    private static final Log log = LogFactory.getLogger(ExceptionUtils.class);
    
    /**
     * 异常内部的errorcode属性名称
     */
    private static String[] fieldNameOfCode = new String[]{"code"};
    
    /**
     * @Function:     getCodeFromThrowable 
     * @Description:  获取异常对象ErrorCode属性值
     *
     * @param t       异常对象
     * @return
     */
    public  static String   getCodeFromThrowable(Throwable t){
        
        String errCode = null;
        if(t == null) return errCode;
        
        for(String name : fieldNameOfCode){
            errCode = getCodeFromThrowable(t,name);
            if(errCode != null){
                break;
            }
        }
        
        return errCode;
    }
    /**
     * @Function:     getCodeFromThrowable 
     * @Description:  获取异常对象ErrorCode属性值
     *
     * @param t       异常对象
     * @param name    异常对象ErrorCode属性名称
     * @return
     */
    public  static String   getCodeFromThrowable(Throwable t,String name){
        if(t == null || name == null) return null;
        Class<?> c  = t.getClass();
        Field f = null;
        Object result = null;
        try {
            //根据Field取值
            f = c.getField(name);
            f.setAccessible(true);
            result = f.get(t);
        }catch (NoSuchFieldException e) {
            Method m = null;;
            //根据getter方法取值
            try {
                m = c.getMethod("get"+StringUtils.toUpperCaseFirstOne(name));
                if(m != null) {
                    result = m.invoke(t);
                }
            } catch (Exception e1) {
                if(log.isDebugEnabled()){
                    log.debug(LogProperty.LOGTYPE_DETAIL, 
                            "解析RetryFailureException异常编码错误(NoSuchFieldException),原因：该异常非标准平台异常","异常类:"
                            ,t.getClass().getName());
                }
            } 
        }
        catch (Exception e) {
            //Do Nothing
           if(log.isDebugEnabled()){
               log.debug(LogProperty.LOGTYPE_DETAIL, 
                       "解析RetryFailureException异常编码错误,原因：该异常非标准平台异常","异常类:"
                       ,t.getClass().getName());
           }
        }
        
        return result == null ? null : result.toString();
    }
    
   
    
}

