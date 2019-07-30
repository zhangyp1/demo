package com.newland.paas.configtool.interceptor;

import com.newland.paas.configtool.common.Constans;
import com.newland.paas.configtool.common.ResultBean;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * 校验异常
 *
 * @author WRP
 * @since 2019/1/14
 */
@ControllerAdvice
public class ValidExceptionAdvice {

    private static final Log LOG = LogFactory.getLogger(ValidExceptionAdvice.class);

    /**
     * 参数校验异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public ResultBean bindException(BindException ex, HttpServletRequest request,
                                    HttpServletResponse response) {
        LOG.error(LogProperty.LOGTYPE_DETAIL, "参数校验异常", ex);
        StringBuffer sb = new StringBuffer();
        ex.getFieldErrors().forEach(fieldError -> sb.append(fieldError.getDefaultMessage()).append(";"));
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return new ResultBean(Constans.FAIL, "输入参数异常:" + sb.toString());
    }

    /**
     * 参数校验异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ResultBean methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
                                                      HttpServletResponse response) {
        LOG.error(LogProperty.LOGTYPE_DETAIL, "参数校验异常", ex);
        return parseException(ex);
    }

    /**
     * 参数校验异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    public ResultBean constraintViolationException(ConstraintViolationException ex, HttpServletRequest request,
                                                   HttpServletResponse response) {
        LOG.error(LogProperty.LOGTYPE_DETAIL, "参数校验异常", ex);
        return parseException(ex);
    }

    /**
     * 校验异常
     *
     * @param exception
     * @return
     */
    public static ResultBean parseException(MethodArgumentNotValidException exception) {
        List<FieldError> filedErrors = exception.getBindingResult().getFieldErrors();
        StringBuffer errorMsg = new StringBuffer();
        filedErrors.stream()
                .forEach(x -> errorMsg.append(MessageFormat.format(x.getDefaultMessage(), x.getField())).append(";"));
        return new ResultBean(Constans.FAIL, "输入参数异常:" + errorMsg);
    }

    /**
     * 校验异常
     *
     * @param exception
     * @return
     */
    public static ResultBean parseException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuffer errorMsg = new StringBuffer();
        violations.forEach(x -> errorMsg.append(x.getMessage()).append(";"));
        return new ResultBean(Constans.FAIL, "输入参数异常:" + errorMsg.toString());
    }

}
