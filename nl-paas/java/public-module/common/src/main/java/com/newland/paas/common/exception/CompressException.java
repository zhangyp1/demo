package com.newland.paas.common.exception;

/**
 * @Author: zhoufeilong
 * @Description: 压缩异常
 * @Date: 2017-10-26 09:43
 * @Modified By:
 */
public class CompressException extends NLUnCheckedException {

    private static final long serialVersionUID = 5326574707699663679L;

    //异常类型
    public static final String TYPE = "compress";

    public CompressException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public CompressException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public CompressException(String code, String message) {
        super(code, message);
    }

    public CompressException(String code, NLThrowable ex) {
        super(code, ex);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
