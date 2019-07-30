package com.newland.paas.advice.request;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.constants.ServiceInfo;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 拦截器
 *
 * @author wrp
 */
@Component
public class ApiRequestInterceptors extends HandlerInterceptorAdapter {

    private static final Log LOG = LogFactory.getLogger(ApiRequestInterceptors.class);

    private static final int THREE = 3;
    @Autowired
    private Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 设置调用链traceId，当前线程的日志都会加上此ID
        Span span = tracer.getCurrentSpan();
        // 设置调用链traceId，当前线程的日志都会加上此ID
        LOG.addMDC(LogProperty.LOGCONFIG_DEALID, span.traceIdString());
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("trace_info:{0}", span.toString()));
        LOG.info(LogProperty.LOGTYPE_DETAIL, span.traceIdString() + ":" + ServiceInfo.getValue());

        String requestUri = request.getRequestURI();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "请求路径:" + requestUri);
        // application/json(RequestBody)
        if (request instanceof BodyRequestWrapper) {
            BodyRequestWrapper requestWrapper = (BodyRequestWrapper) request;
            LOG.info(LogProperty.LOGTYPE_DETAIL, "RequestBody请求参数:" + new String(requestWrapper.getBody()).replaceAll("\n|\r|\t", ""));
        }
        // form param
        Map<String, String[]> pramMap = request.getParameterMap();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String[]> entry : pramMap.entrySet()) {
            sb.append(entry.getKey() + "=" + request.getParameter(entry.getKey()) + ";");
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "formParam请求参数:", sb.toString());

        // 清除token
        RequestContext.setToken(null);
        RequestContext.set(null);
        // 设置token
        String tokens = request.getHeader("Authorization");
        if (requestUri.contains("/paas/activitiflow")) {
            LOG.info(LogProperty.LOGTYPE_DETAIL, "Activiti-Authorization:" + tokens);
            tokens = request.getHeader("PAAS-Authorization");
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "Authorization:" + tokens);
        parseToken(tokens);
        return true;
    }

    /**
     * 解析token
     * date: 2018年10月23日 下午2:39:01
     *
     * @param tokens
     * @author gancy
     * @since JDK 1.8
     */
    public static void parseToken(String tokens) {
        if (!StringUtils.isEmpty(tokens)) {
            tokens = tokens.replaceAll("Bearer ", "");
            String[] tokenArray = tokens.split("\\.");
            if (tokenArray.length != THREE) {
                return;
            }
            String tokenData = tokenArray[1];
            String tokenDecode = null;
            try {
                tokenDecode = TextCodec.BASE64URL.decodeToString(tokenData);
            } catch (IllegalArgumentException e) {
                LOG.error(LogProperty.LOGTYPE_DETAIL, "字符串不是base64编码！", e);
            }
            LOG.info(LogProperty.LOGTYPE_DETAIL, "token-decode:" + tokenDecode);
            if (!StringUtils.isEmpty(tokenDecode)) {
                RequestContext.setToken(tokens);
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
    }

}
