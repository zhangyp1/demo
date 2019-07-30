package com.newland.paas.sbcommon.activiti;

import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 工单token工具
 *
 * @author WRP
 * @since 2019/7/1
 */
public class ActTokenUtil {

    public static final String POUND_KEY = "#";
    public static final String COLON = ":";
    public static final String BASIC = "Basic ";
    public static final String PAAS_TOKEN = "PAAS-Authorization";
    public static final String TOKEN = "Authorization";
    public static final String BEARER = "Bearer ";

    /**
     * 当前请求的token信息
     */
    private static NamedThreadLocal<String> tokenThreadLocal = new NamedThreadLocal<>("threadLocalToken");

    public static String getTokenByThreadLocal() {
        return tokenThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(BEARER + token);
    }

    /**
     * 获取工单账户
     *
     * @param tenantId
     * @param account
     * @return
     */
    public static String getActAccount(Long tenantId, String account) {
        if (StringUtils.isEmpty(account)) {
            throw new SystemException(new PaasError("act_account_error", "工单账号为空！"));
        }
        if (null == tenantId) {
            return account;
        }
        return tenantId + POUND_KEY + account;
    }

    /**
     * 构造工单token
     *
     * @param tenantId
     * @param account
     * @return
     */
    public static String buildActToken(Long tenantId, String account) {
        String actAccount = getActAccount(tenantId, account) + COLON + getActAccount(tenantId, account);
        return BASIC + Base64Utils.encodeToString(actAccount.getBytes());
    }

    /**
     * 获取PAAS服务token
     * 使用场景：activiti服务调用需要token的微服务后台接口
     *
     * @return
     */
    public static String getPaasToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(PAAS_TOKEN);
    }

    /**
     * 获取当前线程token
     *
     * @return
     */
    public static String getToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return getTokenByThreadLocal();
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(TOKEN);
    }

}
