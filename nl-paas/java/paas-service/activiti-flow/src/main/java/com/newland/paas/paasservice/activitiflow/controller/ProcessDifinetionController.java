package com.newland.paas.paasservice.activitiflow.controller;

import com.newland.paas.paasservice.activitiflow.model.ProcessDefinetionVo;
import com.newland.paas.paasservice.activitiflow.service.ProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程定义查询
 *
 * @since 2018/7/25
 */
@RestController
@RequestMapping("v1/processdefinetion")
@Validated
public class ProcessDifinetionController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * 查找指定流程任务
     *
     * @return 返回
     */
    @GetMapping(value = "/getBussinesKeyList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<ProcessDefinetionVo> getBussinesKeyList() {
        return processDefinitionService.getBussinesKeyList();
    }
}
