package com.newland.paas.prefabsql.interceptor;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器配置
 *
 * @author wrp
 * @since 2018/6/11
 */
public class ApiRequestInterceptors extends HandlerInterceptorAdapter {

    private static final Log LOG = LogFactory.getLogger(ApiRequestInterceptors.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ApplicationException {
        return true;
    }

}
