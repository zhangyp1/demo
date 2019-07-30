package com.newland.paas.advice.exception;

import com.alibaba.fastjson.JSON;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.utils.ResponseUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionAdvice {

	private ThreadLocal<Object> modelHolder = new ThreadLocal<>();

	private Logger logger = LoggerFactory.getLogger(ApiExceptionAdvice.class);

	@ResponseBody
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public BasicResponseContentVo<Object> applicationExceptionReturn(ApplicationException e,
			HttpServletRequest request) {
		logger.error("业务异常!uri={} | requestBody={}", request.getRequestURI(), JSON.toJSONString(modelHolder.get()), e);
		return ResponseUtils.parseException(e);
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public BasicResponseContentVo<Object> handlerBindException(BindException e, HttpServletRequest request) {
		logger.error("系统异常!uri={} | requestBody={}", request.getRequestURI(), JSON.toJSONString(modelHolder.get()), e);
		return ResponseUtils.parseException(e);
	}

	@ResponseBody
	@ExceptionHandler(SystemException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public BasicResponseContentVo<Object> handlerSystemException(SystemException e, HttpServletRequest request) {
		logger.error("系统异常!uri={} | requestBody={}", request.getRequestURI(), JSON.toJSONString(modelHolder.get()), e);
		return ResponseUtils.parseException(e);
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		// ModelHolder 初始化0
		modelHolder.set(webDataBinder.getTarget());
	}

}
