package com.newland.paas.ee.service;

import java.util.Map;

import com.newland.paas.ee.installer.config.InstallerConfig;

/**
 * 健康度检查
 */
public interface IHealthExecuteEngine {
    /**
     * 判断机器是否健康
     * @param targetIp
     * @return
     */
    boolean health2machine(InstallerConfig installerConfig, String targetIp);

    /**
     * 修改机器/etc/hosts
     * @param targetIp
     * @param mapIpHostName
     * @return
     */
    boolean updateHost2machine(InstallerConfig installerConfig, String targetIp, Map<String, String> mapIpHostName,String dockerImageReposity, String ntpServer);
}
