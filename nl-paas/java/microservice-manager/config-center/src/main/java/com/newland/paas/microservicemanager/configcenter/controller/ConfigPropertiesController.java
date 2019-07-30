package com.newland.paas.microservicemanager.configcenter.controller;

import com.newland.paas.microservicemanager.configcenter.service.ConfigPropertiesService;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WRP
 * @since 2018/11/28
 */
@RestController
@RequestMapping(value = "v1/cpMgr")
@Validated
public class ConfigPropertiesController {

    @Autowired
    private ConfigPropertiesService configPropertiesService;

    /**
     * 同步配置文件到数据库
     *
     * @param applicationNameContentVo
     * @return
     */
    @PostMapping(value = "sync/{active}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo<List<String>> sync(
            @RequestBody BasicPageRequestContentVo<List<String>> applicationNameContentVo,
            @PathVariable(value = "active") String active) {
        return new BasicResponseContentVo<>(configPropertiesService.syncProperties2DB(
                applicationNameContentVo.getParams(), active));
    }

}
