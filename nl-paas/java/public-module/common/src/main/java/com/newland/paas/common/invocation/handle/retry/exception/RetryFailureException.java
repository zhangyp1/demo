package com.newland.paas.common.invocation.handle.retry.exception;


import com.newland.paas.common.util.ExceptionUtils;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:RetryFailureException
 * @Description: 重试失败异常，原因不满足重试条件如：超时特定时间、次数、非需要重试异常等情况。<br/>
 *               其中：如果为非需重试异常，原异常将写入该异常的e属性中。</p>
 *               如果抛出的cause异常有带code属性(private 修饰的属性需带getter方法)则自动获取其中code值。
 * @Funtion List:
 *
 * @author chenzc
 * @version
 * @Date 2015年6月18日 下午4:56:31
 *
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class RetryFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private String code = null;
    
    public RetryFailureException() {

    }
    
    public RetryFailureException(String msg, Throwable e) {
        super(msg,e);
        String errCode = ExceptionUtils.getCodeFromThrowable(e);
        this.code = errCode;
    }
    
    public RetryFailureException(Throwable e) {
        super(e);
        String errCode = ExceptionUtils.getCodeFromThrowable(e);
        this.code = errCode;
    }
    
    public RetryFailureException(String msg) {
        super(msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
    
        this.code = code;
    }
    
}
