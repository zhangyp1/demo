package com.newland.paas.common.exception;

/**
 * @Author: zhoufeilong
 * @Description: 业务级公共异常
 * @Date: 2017-10-22 22:40
 * @Modified By:
 */
public class CommonBusinessException extends NLUnCheckedException {

    private static final long serialVersionUID = -2501901688618249496L;

    //异常类型
    public static final String TYPE = "common_business";

    public CommonBusinessException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public CommonBusinessException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public CommonBusinessException(String code, String message) {
        super(code, message);
    }

    public CommonBusinessException(String code, NLThrowable ex) {
        super(code, ex);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
