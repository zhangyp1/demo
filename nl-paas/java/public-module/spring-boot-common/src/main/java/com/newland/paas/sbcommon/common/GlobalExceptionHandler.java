package com.newland.paas.sbcommon.common;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.exception.NLCheckedException;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @author wrp
 * @Description 统一异常处理
 * @Date 2017/10/11
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLogger(GlobalExceptionHandler.class);
    private static final String tag = "GlobalExceptionHandler===";

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BasicResponseContentVo<Object> defaultErrorHandler(HttpServletRequest req, Throwable e) throws Throwable {
        logger.error(LogProperty.LOGTYPE_SYS, tag, e);
        BasicResponseContentVo<Object> basicResponseContentVo = null;
        Class eClazz = Throwable.class;
        // 异常为SQLException或者SQLException的子类

        if (SQLException.class.isAssignableFrom(eClazz) || e instanceof SQLException
            || DataAccessException.class.isAssignableFrom(eClazz) || e instanceof DataAccessException) {
            basicResponseContentVo =
                new BasicResponseContentVo<Object>(new PaasError(ErrorCode.SYSERROR_DATABASE_COMMON, "数据库操作异常!"));
        }
        // 异常为NLCheckedException或者NLCheckedException的子类
        else if (NLCheckedException.class.isAssignableFrom(eClazz) || e instanceof NLCheckedException) {
            NLCheckedException nlce = (NLCheckedException)e;
            basicResponseContentVo = new BasicResponseContentVo<Object>(new PaasError(nlce.getCode(), nlce.getMsg()));
        }
        // 异常为NLUnCheckedException或者NLUnCheckedException的子类
        else if (NLUnCheckedException.class.isAssignableFrom(eClazz) || e instanceof NLUnCheckedException) {
            NLUnCheckedException nluce = (NLUnCheckedException)e;
            basicResponseContentVo = new BasicResponseContentVo<Object>(new PaasError(nluce.getCode(), nluce.getMsg()));
        } else {
            basicResponseContentVo =
                new BasicResponseContentVo<Object>(new PaasError(ErrorCode.SYSERROR_UNKNOW, "系统未知异常！"));
        }
        return basicResponseContentVo;
    }

}
