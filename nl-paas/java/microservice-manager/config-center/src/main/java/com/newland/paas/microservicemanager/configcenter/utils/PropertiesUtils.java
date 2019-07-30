package com.newland.paas.microservicemanager.configcenter.utils;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Properties工具类， 可载入多个yml文件，
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值，
 *
 * @author WRP
 * @version 2017-12-30
 */
public final class PropertiesUtils {

    private static final Log LOG = LogFactory.getLogger(PropertiesUtils.class);

    private PropertiesUtils() {
    }

    /**
     * 获取平台及模块相关的配置文件，根据文件列表
     */
    private static String[] releadInstance(String configFile, String profiles) {
        // 平台及模块相关的配置文件列表
        Set<String> configs = new LinkedHashSet<>();

        // 无环境配置文件
        Set<String> set = new LinkedHashSet<>();
        set.add(configFile);
        String[] configFiles = set.toArray(new String[set.size()]);

        for (String location : configFiles) {
            configs.add(location);
            if (StringUtils.isNotBlank(profiles)) {
                if (location.endsWith(".yml")) {
                    configs.add(StringUtils.substringBeforeLast(location, ".yml") + "-" + profiles + ".yml");
                }
            }
        }
        configFiles = configs.toArray(new String[configs.size()]);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "Loading paas config: " + configFiles);
        return configFiles;
    }

    /**
     * 载入多个文件，路径使用Spring Resource格式，相同的属性在最后载入的文件中的值将会覆盖之前的值。
     */
    public static ConfigBean readProperties(String configFile, String active) {

        Properties properties = new Properties();

        String[] configFiles = releadInstance(configFile, active);

        List<String> existConfigFiles = new ArrayList<>();

        for (String location : configFiles) {
            try {
                Resource resource = ResourceUtils.getResource(location);
                if (resource.exists()) {
                    if (location.endsWith(".yml")) {
                        LOG.info(LogProperty.LOGTYPE_DETAIL, "加载配置文件【" + location + "】");
                        existConfigFiles.add(location);
                        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
                        bean.setResources(resource);
                        for (Map.Entry<Object, Object> entry : bean.getObject().entrySet()) {
                            properties.put(PropertiesUtils.toString(entry.getKey()),
                                    PropertiesUtils.toString(entry.getValue()));
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(LogProperty.LOGTYPE_DETAIL, "Load " + location + " failure. ", e);
            }
        }

        return new ConfigBean(existConfigFiles, properties);
    }

    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    private static String toString(final Object obj) {
        return obj == null ? "" : obj.toString();
    }

}
