package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysEnvConfigBo;

import java.util.List;

/**
 * @program: paas-all
 * @description: 系统环境配置
 * @author: Frown
 * @create: 2018-10-11 10:33
 **/
public interface SysEnvConfigService {
    /**
     * 获取系统环境配置列表
     * @return
     */
    List<SysEnvConfigBo> listSysEnvConfig();

    /**
     * 根据key获取系统环境配置
     * @param key
     * @return
     */
    SysEnvConfigBo getSysEnvConfigValue(String key);
}
