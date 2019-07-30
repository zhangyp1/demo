package com.newland.paas.sbcommon.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 获取客户端IP
 * 
 * @author SongDi
 * @date 2018/11/09
 */
public class ClientIP {
    public static final String PAAS_LOCAL_IP = "PAAS-Local-IP";
    public static final String PAAS_PUBLIC_IP = "PAAS-Public-IP";
    public static final String X_REAL_IP = "X-Real-IP";
    private static final String NULL = "null";

    /**
     * 冗余获取客户端IP
     * 
     * @param request
     * @return
     */
    public static String get(HttpServletRequest request) {
        String ip = request.getHeader(PAAS_LOCAL_IP);
        if (StringUtils.isEmpty(ip) || NULL.equals(ip)) {
            ip = request.getHeader(PAAS_PUBLIC_IP);
        }
        if (StringUtils.isEmpty(ip) || NULL.equals(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }
        if (StringUtils.isEmpty(ip) || NULL.equals(ip)) {
            ip = request.getRemoteAddr();
        }
        return distinct(ip);
    }

    private static String distinct(String ip) {
        if (StringUtils.isEmpty(ip) || NULL.equals(ip)) {
            return "";
        }
        if (ip.indexOf(',') == -1) {
            return ip;
        }
        String[] ips = ip.split(",");

        Set<String> ipSet = new HashSet<>(Arrays.asList(ips));
        return StringUtils.join(ipSet, ',');
    }
}
