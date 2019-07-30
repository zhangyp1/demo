package com.newland.paas.microservicemanager.configcenter.service;

import java.util.List;

/**
 * @author WRP
 * @since 2018/11/28
 */
public interface ConfigPropertiesService {

    /**
     * 同步配置文件到数据库
     *
     * @param configFiles
     * @param active
     */
    List<String> syncProperties2DB(List<String> configFiles, String active);

}
