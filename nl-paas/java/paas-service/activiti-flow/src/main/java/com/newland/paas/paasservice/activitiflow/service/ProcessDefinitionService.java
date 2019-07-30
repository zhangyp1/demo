package com.newland.paas.paasservice.activitiflow.service;

import com.newland.paas.paasservice.activitiflow.model.ProcessDefinetionVo;

import java.util.List;

/**
 * 流程定义
 *
 * @author WRP
 * @since 2019/1/30
 */
public interface ProcessDefinitionService {

    /**
     * 获取流程定义KEY列表
     *
     * @return return
     */
    List<ProcessDefinetionVo> getBussinesKeyList();
}
