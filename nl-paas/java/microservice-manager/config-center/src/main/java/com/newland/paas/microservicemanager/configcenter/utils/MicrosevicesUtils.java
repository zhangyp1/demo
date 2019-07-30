package com.newland.paas.microservicemanager.configcenter.utils;

import com.alibaba.fastjson.JSON;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Properties;

/**
 * @author wrp
 * @Description 微服务工具类
 * @Date 2017/12/18
 */
public final class MicrosevicesUtils {

    private static final Log LOG = LogFactory.getLogger(MicrosevicesUtils.class);

    private static final String DEV = "dev";
    private static final int TWO = 2;

    private MicrosevicesUtils() {
    }

    /**
     * 将启动参数设置到系统变量中
     *
     * @param args
     */
    public static void setSystemProperty(String[] args) {
        Assert.notNull(args, "Args must not be null");
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String optionText = arg.substring(TWO, arg.length());
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

    /**
     * 根据config.profiles参数读取对应配置文件
     *
     * @return
     */
    public static Properties getPropertiesByEnv() {
        Properties properties = new Properties();
        String env = System.getProperty("config.profiles");
        if (StringUtils.isNotEmpty(env) && !DEV.equals(env)) {
            String location = "classpath:bootstrap-" + env + ".yml";
            Resource resource = ResourceUtils.getResource(location);
            if (resource.exists()) {
                LOG.info(LogProperty.LOGTYPE_DETAIL, "加载配置文件【" + location + "】");
                YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
                bean.setResources(resource);
                for (Map.Entry<Object, Object> entry : bean.getObject().entrySet()) {
                    properties.put(entry.getKey(), entry.getValue());
                }
            }
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "properties:" + JSON.toJSONString(properties));
        return properties;
    }

}
