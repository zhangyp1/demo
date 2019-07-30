package com.newland.paas.sbcommon.common;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 非检查型异常基类，抛出此异常，外部可不捕获此异常和做相关处理。
 * 
 * 
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private PaasError error;

    /**
     * 异常体
     */
    private Throwable throwable;

    public SystemException(PaasError error) {
        super(error.getDescription());
        this.error = error;
    }

    public SystemException(PaasError error, Throwable throwable) {
        super(error.getDescription(), throwable);
        this.error = error;
        this.throwable = throwable;
    }

    public PaasError getError() {
        return error;
    }

    public void setError(PaasError error) {
        this.error = error;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
