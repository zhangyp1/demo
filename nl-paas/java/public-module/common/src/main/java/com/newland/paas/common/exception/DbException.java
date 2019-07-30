package com.newland.paas.common.exception;

/**
 * Copyright (c) 2014, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:DbException
 * @Description: TODO 数据库异常
 * @Funtion List:TODO
 * 
 * @author xuw
 * @version
 * @Date 2014年11月21日 下午2:43:04
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class DbException extends NLUnCheckedException {
    private static final long serialVersionUID = 1L;
    //异常类型
    public static final String TYPE = "db"; 
    
    public DbException(String code , String message,Throwable throwable){
        super(code, message,throwable);
    }

    public DbException(String code, Exception e){
        super(code, e);
    }
    
    @Override
    public String getType() {
        return TYPE;
        
    }
}
