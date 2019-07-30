package com.newland.paas.common.exception;

/**
 * 运行中心异常
 * 
 * @author czc
 * 
 */
public class RtcException extends NLUnCheckedException {
    
    private static final long serialVersionUID = 1L;

    //异常类型
    public static final String TYPE = "rtc";

    public RtcException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public RtcException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public RtcException(String code, String message) {
        super(code, message);
    }

    public RtcException(String code, NLThrowable ex) {
        super(code, ex);
    }

    public String getType() {
        return TYPE;
    }
}
