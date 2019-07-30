package com.newland.paas.sbcommon.utils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 错误信息报文封装
 *
 * @author wrp
 */
public class ResponseUtils {
    public static final PaasError SUCCESS = new PaasError("00000000", "操作成功", null);
    public static final PaasError EXCEPTION = new PaasError("10000000", "未知异常", "联系管理员");

	/**
	 * 业务异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(ApplicationException exception) {
		return new BasicResponseContentVo<Object>(exception.getError());
	}

	/**
	 * 系统异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(SystemException exception) {
		return new BasicResponseContentVo<Object>(exception.getError());
	}

	/**
	 * 检验异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(BindException exception) {
		StringBuffer message = new StringBuffer();
		for (ObjectError error : exception.getAllErrors()) {
			message.append(error.getDefaultMessage());
			message.append(",");
		}
		PaasError paasError = new PaasError("2-09-00001", "输入参数异常:" + message);
		return new BasicResponseContentVo<Object>(paasError);
	}

	/**
	 * 校验异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(MethodArgumentNotValidException exception) {
		List<FieldError> filedErrors = exception.getBindingResult().getFieldErrors();
		StringBuffer errorMsg = new StringBuffer();
		filedErrors.stream()
				.forEach(x -> errorMsg.append(MessageFormat.format(x.getDefaultMessage(), x.getField())).append(";"));
		PaasError paasError = new PaasError("2-09-00001", "输入参数异常:" + errorMsg);
		return new BasicResponseContentVo<Object>(paasError);
	}

	/**
	 * 校验异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(ConstraintViolationException exception) {
		Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
		StringBuffer errorMsg = new StringBuffer();
		violations.forEach(x -> errorMsg.append(x.getMessage()).append(";"));
		PaasError paasError = new PaasError("2-09-0001", "输入参数异常:" + errorMsg.toString());
		return new BasicResponseContentVo<Object>(paasError);
	}

	/**
	 * 系统异常
	 *
	 * @param exception
	 * @return
	 */
	public static BasicResponseContentVo<Object> parseException(Exception exception) {
		return new BasicResponseContentVo<Object>(EXCEPTION);
	}

}
