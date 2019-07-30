package com.newland.paas.common.exception;

/**
 * @Author: zhoufeilong
 * @Description:
 * @Date: 2017-10-22 23:32
 * @Modified By: zhoufeilong
 */
public class ResourceManageException extends NLUnCheckedException{
    private static final long serialVersionUID = -2455522774051643565L;

    //异常类型
    public static final String TYPE = "resource_manage";

    public ResourceManageException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ResourceManageException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public ResourceManageException(String code, String message) {
        super(code, message);
    }

    public ResourceManageException(String code, NLThrowable ex) {
        super(code, ex);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
