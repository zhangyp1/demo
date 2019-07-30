package com.newland.paas.sbcommon.utils;

import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wrp
 * @Description 微服务工具类
 * @Date 2017/12/18
 */
public class MicrosevicesUtils {

    private static final String COMPUTER_NAME = "COMPUTERNAME";
    private static final String UN_KNOWN_HOST = "UnknownHost";
    private static final String PF_NODE_IP = "PF_NODE_IP";

    /**
     * 将启动参数设置到系统变量中
     * @param args
     */
    public static void setSystemProperty(String[] args) {
        Assert.notNull(args, "Args must not be null");
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String optionText = arg.substring(2, arg.length());
                String optionName;
                String optionValue = null;
                if (optionText.contains("=")) {
                    optionName = optionText.substring(0, optionText.indexOf("="));
                    optionValue = optionText.substring(optionText.indexOf("=")+1, optionText.length());
                }
                else {
                    optionName = optionText;
                }
                if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
                    throw new IllegalArgumentException("Invalid argument syntax: " + arg);
                }
                System.setProperty(optionName, optionValue);
            }
        }

    }

    /**
     * 获取主机名
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostName() throws UnknownHostException {
        if (System.getenv(COMPUTER_NAME) != null) {
            return System.getenv(COMPUTER_NAME);
        } else {
            return getHostNameForLiunx();
        }
    }

    /**
     * 获取主机IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取linux主机名
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostNameForLiunx() throws UnknownHostException {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            // host = "hostname: hostname"
            String host = uhe.getMessage();
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return UN_KNOWN_HOST;
        }
    }

    /**
     * 获取物理主机IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getPhysicalIp() {
        return System.getenv(PF_NODE_IP);
    }

}
