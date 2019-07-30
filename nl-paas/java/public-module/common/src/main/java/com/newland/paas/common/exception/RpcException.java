package com.newland.paas.common.exception;

/**
 * Copyright (c) 2014, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:CsnException
 * @Description: RPC异常
 * 
 * @author kangweixiang
 * @version
 * @Date 2014年11月4日 下午4:54:17
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class RpcException extends NLUnCheckedException {
   
    //RPC
    public static final String ERROR_CODE_RPC  = "116";
    
    private static final long serialVersionUID = 5897891968046003683L;

    // 异常类型
    public static final String TYPE = "rpc";

    public RpcException(String code, NLThrowable ex) {
        super(code, ex);
    }

    public RpcException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public RpcException(String code, String message) {
        super(code, message);
    }

    public RpcException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public String getType() {
        return TYPE;
    }
}
