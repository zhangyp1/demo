package com.newland.paas.sbcommon.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.newland.paas.sbcommon.annotation.Base64Parameter;

@Component
@Aspect
public class Base64Aspect {
	
	@Pointcut(value = "@annotation(com.newland.paas.sbcommon.annotation.Base64Parameter)")
	public void access() {

	}

	// 环绕通知,环绕增强，相当于MethodInterceptor
	@Around("access()")
	public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		Method method = methodSignature.getMethod();
		String[] parameterNames = methodSignature.getParameterNames();
		Object[] parameterNamesValues = joinPoint.getArgs();
		Base64Parameter base64Annotation = AnnotationUtils.getAnnotation(method, Base64Parameter.class);
		String[] annotationValue = base64Annotation.value();
		List<String> annotationValueList = Arrays.asList(annotationValue);
		int index = 0;
		Object parameter;
		for (String parameterName : parameterNames) {
			if (annotationValueList.contains(parameterName)) {
				parameter = parameterNamesValues[index];
				if (parameter != null && parameter instanceof String) {
					parameterNamesValues[index] = new String(Base64Utils.decodeFromString((String) parameter));
				}

			}
			index++;
		}

		return joinPoint.proceed(parameterNamesValues);

	}

}
