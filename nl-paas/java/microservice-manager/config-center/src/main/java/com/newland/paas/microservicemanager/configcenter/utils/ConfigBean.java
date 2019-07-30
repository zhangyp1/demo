package com.newland.paas.microservicemanager.configcenter.utils;

import java.util.List;
import java.util.Properties;

/**
 * @author WRP
 * @since 2018/11/28
 */
public class ConfigBean {

    private List<String> configFiles;
    private Properties properties;

    public ConfigBean() {
    }

    public ConfigBean(List<String> configFiles, Properties properties) {
        this.configFiles = configFiles;
        this.properties = properties;
    }

    public List<String> getConfigFiles() {
        return configFiles;
    }

    public void setConfigFiles(List<String> configFiles) {
        this.configFiles = configFiles;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
