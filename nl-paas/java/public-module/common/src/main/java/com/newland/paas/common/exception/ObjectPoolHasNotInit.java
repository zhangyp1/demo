
package com.newland.paas.common.exception;


/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ObjectCacheHasNotInit
 * @Description: 对象池未初始化无法使用
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年10月28日		下午4:45:33
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class ObjectPoolHasNotInit extends NLUnCheckedException {

    private static final long serialVersionUID = 1L;

    //异常类型
    public static final String TYPE = "rtc";

    public ObjectPoolHasNotInit(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ObjectPoolHasNotInit(String code, Throwable throwable) {
        super(code, throwable);
    }

    public ObjectPoolHasNotInit(String code, String message) {
        super(code, message);
    }

    public ObjectPoolHasNotInit(String code, NLThrowable ex) {
        super(code, ex);
    }

    public String getType() {
        return TYPE;
    }
}

