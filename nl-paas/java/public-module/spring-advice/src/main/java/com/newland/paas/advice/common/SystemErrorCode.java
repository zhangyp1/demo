package com.newland.paas.advice.common;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 系统业务异常
 *
 * @author wrp
 * @since 2018/6/26
 */
public interface SystemErrorCode {

    public static final PaasError sessionError = new PaasError("20900005", "会话丢失异常");
    PaasError TASK_NOT_FOUND = new PaasError("400", "找不到任务");
    PaasError TASK_IS_CLAIMED = new PaasError("409", "任务已被认领");
}
