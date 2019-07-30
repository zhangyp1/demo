package com.newland.paas.advice.valid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newland.paas.sbcommon.utils.ResponseUtils;

@ControllerAdvice
public class ValidExceptionAdvice {

	public static final Log LOG = LogFactory.getLogger(ValidExceptionAdvice.class);

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseBody
	public BasicResponseContentVo<Object> MethodArgumentNotValidException(Exception ex, HttpServletRequest request,
                                                                          HttpServletResponse response) {
		LOG.error(LogProperty.LOGTYPE_DETAIL, "参数校验异常", ex);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseUtils.parseException((MethodArgumentNotValidException) ex);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseBody
	public BasicResponseContentVo<Object> ConstraintViolationException(Exception ex, HttpServletRequest request,
                                                                       HttpServletResponse response) {
		LOG.error(LogProperty.LOGTYPE_DETAIL, "参数校验异常", ex);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseUtils.parseException((ConstraintViolationException) ex);
	}
}
