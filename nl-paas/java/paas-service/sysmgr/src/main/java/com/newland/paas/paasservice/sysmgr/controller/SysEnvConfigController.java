/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysEnvConfigBo;
import com.newland.paas.paasservice.sysmgr.service.SysEnvConfigService;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 系统环境变量
 *
 * @author Frown
 * @created 2018-10-11
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/sysEnvConfig")
@Validated
public class SysEnvConfigController {

    @Autowired
    private SysEnvConfigService sysEnvConfigService;

    /**
     * 查询系统环境配置
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysEnvConfigBo> listQuerySysEnvConfig() {
        return sysEnvConfigService.listSysEnvConfig();
    }

    /**
     * 根据key获取系统环境配置
     * @param key
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/{key}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysEnvConfigBo getSysEnvConfig(@PathVariable("key") String key) {
        return sysEnvConfigService.getSysEnvConfigValue(key);
    }

}

