package com.newland.paas.paasservice.activitiflow.controller;

import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 流程实例查询
 *
 * @since 2018/7/25
 */
@RestController
@RequestMapping("v1/processinstance")
@Validated
public class ProcessinstanceController {

    /**
     * 流程图
     *
     * @param processInstanceId 流程实例ID
     * @param response response
     * @return 返回
     */
    @GetMapping(value = "/diagram/{processInstanceId}")
    public ResponseEntity<byte[]> getProcessInstanceDiagram(
            @ApiParam(name = "processInstanceId", value = "The id of the process instance to get the diagram for.")
            @PathVariable String processInstanceId, HttpServletResponse response) {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到身份服务组件实例,获取所在用户组
        HistoricProcessInstance historicProcessInstance = engine.getHistoryService()
                .createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).finished().singleResult();
        List<String> hightlightActivitis = null;
        String processDefinitionId = "";
        if (historicProcessInstance == null) {
            ProcessInstance processInstance = engine.getRuntimeService().createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            processDefinitionId = processInstance.getProcessDefinitionId();
            hightlightActivitis = engine.getRuntimeService().getActiveActivityIds(processInstance.getId());
        } else {
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            List<HistoricActivityInstance> activityInstances = engine.getHistoryService()
                    .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                    .activityType("endEvent").list();
            hightlightActivitis = new ArrayList<>();
            for (HistoricActivityInstance historicActivityInstance : activityInstances) {
                hightlightActivitis.add(historicActivityInstance.getActivityId());
            }
        }

        ProcessDefinition pde = engine.getRepositoryService().getProcessDefinition(processDefinitionId);

        if (pde != null && pde.hasGraphicalNotation()) {
            BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(pde.getId());
            ProcessDiagramGenerator diagramGenerator = engine.getProcessEngineConfiguration()
                    .getProcessDiagramGenerator();
            InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", hightlightActivitis,
                    Collections.<String>emptyList(), engine.getProcessEngineConfiguration().getActivityFontName(),
                    engine.getProcessEngineConfiguration().getLabelFontName(),
                    engine.getProcessEngineConfiguration().getAnnotationFontName(),
                    engine.getProcessEngineConfiguration().getClassLoader(), 1.0);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "image/png");
            try {
                return new ResponseEntity(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                throw new ActivitiIllegalArgumentException("Error exporting diagram", e);
            }

        } else {
            throw new ActivitiIllegalArgumentException("Process instance with id '"
                    + "' has no graphical notation defined.");

        }

    }
}
