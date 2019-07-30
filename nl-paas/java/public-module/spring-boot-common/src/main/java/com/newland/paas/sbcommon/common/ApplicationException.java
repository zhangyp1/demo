package com.newland.paas.sbcommon.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 系统应用异常需要捕获，如：业务异常
 * 
 * @author SongDi
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No such Order")
public class ApplicationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7640793404157007568L;
    /**
     * 错误码
     */
    private PaasError error;
    /**
     * 异常体
     */
    private Throwable throwable;

    public ApplicationException(PaasError error) {
        super(error.getDescription());
        this.error = error;
    }

    public ApplicationException(PaasError error, Throwable throwable) {
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
