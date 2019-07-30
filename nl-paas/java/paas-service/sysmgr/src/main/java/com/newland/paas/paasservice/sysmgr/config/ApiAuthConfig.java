package com.newland.paas.paasservice.sysmgr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * api授权配置
 */
@Configuration
@ConfigurationProperties(prefix = "apiAuth")
public class ApiAuthConfig {

    private List<String> serviceList;

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }
}
