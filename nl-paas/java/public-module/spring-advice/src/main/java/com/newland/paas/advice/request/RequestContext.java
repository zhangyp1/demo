package com.newland.paas.advice.request;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newland.paas.advice.common.SystemErrorCode;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.SystemException;

import io.jsonwebtoken.impl.TextCodec;

/**
 * 请求上下文
 *
 * @author newland
 */
public class RequestContext {
    private static final Log LOG = LogFactory.getLogger(RequestContext.class);
    /**
     * 当前请求的token信息
     */
    private static NamedThreadLocal<String> tokenThreadLocal = new NamedThreadLocal<>("token");

    /**
     * t当前请求的token转换后的用户信息
     */
    private static NamedThreadLocal<SessionInfo> sessionInfoThreadLocal = new NamedThreadLocal<>("SessionInfo");
    private static final int THREE = 3;

    public static String getToken() {
        checkSessionInfo();
        return tokenThreadLocal.get();
    }

    public static String getTokenOrNull() {
        return tokenThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);

        setSessionByToken(token);

    }

    private static void setSessionByToken(String token) {
        if (token == null) {
            set(null);
            return;
        }
        String[] tokenArray = token.split("\\.");
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
        if (!StringUtils.isEmpty(tokenDecode)) {
            SessionInfo sessionInfo = JSON.parseObject(tokenDecode, new TypeReference<SessionInfo>() {});
            RequestContext.set(sessionInfo);
        }
    }

    public static SessionInfo getSession() {
        checkSessionInfo();
        return sessionInfoThreadLocal.get();
    }

    public static Long getUserId() {
        checkSessionInfo();
        return getSession().getUserId();
    }

    public static Long getTenantId() {
        checkSessionInfo();
        return getSession().getTenantId();
    }

    public static Boolean isManagement() {
        Long id = 0L;
        Long id2 = 1L;
        return id.equals(getSession().getTenantId()) | id2.equals(getSession().getTenantId());
    }

    public static void set(SessionInfo sessionInfo) {
        sessionInfoThreadLocal.set(sessionInfo);
    }

    public static void clear() {
        sessionInfoThreadLocal.remove();
        tokenThreadLocal.remove();
    }

    public static void checkSessionInfo() {
        // 本地免登入
        // SessionInfo sessionInfo = new SessionInfo(12L, "运营租户管理员",132L);
        // sessionInfo.setAccount("yyadmin");
        // sessionInfoThreadLocal.set(sessionInfo);

        SessionInfo sessionInfo = sessionInfoThreadLocal.get();
        if (sessionInfo == null) {
            throw new SystemException(SystemErrorCode.sessionError);
        }
    }

}