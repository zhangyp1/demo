package com.newland.paas.configtool.common;

import org.springframework.util.Assert;

/**
 * @author wrp
 * @Description 微服务工具类
 * @Date 2017/12/18
 */
public class MicrosevicesUtils {

    /**
     * 将启动参数设置到系统变量中
     *
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
                    optionValue = optionText.substring(optionText.indexOf("=") + 1, optionText.length());
                } else {
                    optionName = optionText;
                }
                if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
                    throw new IllegalArgumentException("Invalid argument syntax: " + arg);
                }
                System.setProperty(optionName, optionValue);
            }
        }

    }

}
