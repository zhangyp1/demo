package com.newland.paas.common.exception;

/**
 * Copyright (c) 2014, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:FileOperatorException
 * @Description: 文件操作异常
 * 
 * @author kangweixiang
 * @version
 * @Date 2014年9月1日 上午10:12:57
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class FileOperatorException extends NLCheckedException {
    private static final long serialVersionUID = -900461498231703909L;
    
    public static final String ERROR_CODE_CREATE_WORKDIR_ERROR = "011";

    private final String type = "file";

    public FileOperatorException(String code, NLThrowable ex) {
        super(code, ex);
    }

    public FileOperatorException(String code, String message,
                                 Throwable throwable) {
        super(code, message, throwable);
    }

    public FileOperatorException(String code, String message) {
        super(code, message);
    }

    public FileOperatorException(String code, Throwable throwable) {
        super(code, throwable);
    }

    @Override
    public String getType() {
        return type;
    }
}
