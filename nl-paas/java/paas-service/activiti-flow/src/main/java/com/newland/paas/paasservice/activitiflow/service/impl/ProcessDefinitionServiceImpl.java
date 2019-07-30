package com.newland.paas.paasservice.activitiflow.service.impl;

import com.newland.paas.paasservice.activitiflow.model.ProcessDefinetionVo;
import com.newland.paas.paasservice.activitiflow.service.ProcessDefinitionService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 流程定义
 *
 * @author WRP
 * @since 2019/1/30
 */
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Override
    public List<ProcessDefinetionVo> getBussinesKeyList() {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到身份服务组件实例,获取所在用户组
        ProcessDefinitionQuery processDefinitionQuery = engine.getRepositoryService().createProcessDefinitionQuery();
        processDefinitionQuery = processDefinitionQuery.latestVersion();
        //已办任务
        List<ProcessDefinetionVo> processDefinetionVos = new ArrayList();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            ProcessDefinetionVo processDefinetionVo = new ProcessDefinetionVo();
            //因为上线方案中多个流程图类型相同此处用名称作查询条件 sunxm
            if ("方案创建流程".equals(processDefinition.getName())) {
                processDefinetionVo.setKey(processDefinition.getName());
            } else {
                processDefinetionVo.setKey(processDefinition.getKey());
            }
            processDefinetionVo.setName(processDefinition.getName());
            processDefinetionVos.add(processDefinetionVo);
        }
        //去重
        List<ProcessDefinetionVo> definetionVos = new ArrayList();
        processDefinetionVos.stream().filter(distinctByKey(a -> a.getName()))
                .forEach(definetionVos:: add);
        return definetionVos;
    }

    /**
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
