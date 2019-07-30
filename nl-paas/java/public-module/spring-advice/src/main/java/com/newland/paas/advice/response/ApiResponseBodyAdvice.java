package com.newland.paas.advice.response;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.config.AppVersion;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private AppVersion appVersion;
    @Autowired
    private Tracer tracer;

    private static final Log LOG = LogFactory.getLogger(ApiResponseBodyAdvice.class);
    private static final List<String> URI_LIST = new ArrayList<>();
    static {
        URI_LIST.add("/swagger-resources");
        URI_LIST.add("/api-docs");
        URI_LIST.add("/prometheus");// 采集指标
        URI_LIST.add("/activitiflow/runtime");
        URI_LIST.add("/activitiflow/v1/processinstance/diagram");
        URI_LIST.add("/paas/jobmgr/jobinfo");
        URI_LIST.add("/paas/jobmgr/jobcode");
        URI_LIST.add("/paas/jobmgr/jobgroup");
        URI_LIST.add("/paas/jobmgr/joblog");
        URI_LIST.add("/paas/jobmgr/login");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        LOG.debug(LogProperty.LOGTYPE_DETAIL, "ApiResponseBodyAdvice.ResponseAdviser.supports()");
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "ApiResponseBodyAdvice.ResponseAdviser.beforeBodyWrite()");

        String uri = request.getURI().toString();
        HttpServletRequest servletRequest = ((ServletServerHttpRequest)request).getServletRequest();
        String requestUriWithoutContextPath =
            servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
        LOG.info(LogProperty.LOGTYPE_DETAIL,
            MessageFormat.format("请求路径:{0} | responseBody={1}", requestUriWithoutContextPath, JSON.toJSONString(body)));
        BasicResponseContentVo<Object> basicResponse;

        if (isURIWhiteList(uri)) {
            return body;
        }
        if (body instanceof BasicResponseContentVo) {
            ((BasicResponseContentVo<?>)body).setAppVersion(appVersion);
            ((BasicResponseContentVo<?>)body).setTraceId(tracer.getCurrentSpan().traceIdString());
            return body;
        }
        if (body instanceof Byte) {
            return body;
        }
        if (body instanceof String) {
            return body;
        }
        if (body instanceof PaasMsg) {
            basicResponse = new BasicResponseContentVo<Object>((PaasMsg)body);
            basicResponse.setAppVersion(appVersion);
            basicResponse.setTraceId(tracer.getCurrentSpan().traceIdString());
            return basicResponse;
        }

        basicResponse = new BasicResponseContentVo<Object>(body);
        basicResponse.setAppVersion(appVersion);
        basicResponse.setTraceId(tracer.getCurrentSpan().traceIdString());
        return basicResponse;
    }

    /**
     * 不需要body封装的白名单地址
     * 
     * @return
     */
    private Boolean isURIWhiteList(String uri) {
        for (String string : URI_LIST) {
            if (uri.indexOf(string) != -1) {
                return true;
            }
        }
        return false;
    }

}
