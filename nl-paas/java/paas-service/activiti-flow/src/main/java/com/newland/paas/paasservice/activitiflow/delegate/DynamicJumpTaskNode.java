package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据异常处理参数实现动态任务节点跳转
 *
 * @author sunxm
 */
public class DynamicJumpTaskNode implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(DynamicJumpTaskNode.class);
    private static final String EXEC_TYPE_REDO = "redo";
    private static final String EXEC_TYPE_SKIP = "skip";

    /**
     * 1.获取流程参数（重新执行(redo)、跳过(skip)、回退）
     * 2.获取异常来源activityId
     * 3.重新执行则跳转到异常来源节点
     * 4.跳过则设置skip参数并跳转到异常来源节点
     * 5.回退则调用回滚接口并跳转到异常来源节点
     *
     * @param execution DelegateExecution
     */
    @Override
    public void execute(DelegateExecution execution) {
        String execType = (String)execution.getVariable("execType");
        Object exceptionskip = execution.getVariable("exceptionskip");
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String processInstanceId = execution.getProcessInstanceId();
        BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(execution.getProcessDefinitionId());
        String curActId = execution.getCurrentActivityId();
        FlowNode excepFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActId);
        FlowNode endFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement("end");
        if (exceptionskip != null && !(boolean)exceptionskip) {
            String fromActId = "";
            if (EXEC_TYPE_REDO.equals(execType)) {
                fromActId = String.valueOf(execution.getVariable("fromActId"));
            } else {
                fromActId = String.valueOf(execution.getVariable("nextActivityId")
                        == null ? "" : execution.getVariable("nextActivityId"));
            }
            if ((fromActId == null || "".equals(fromActId)) && EXEC_TYPE_SKIP.equals(execType)) {
                jumpTask(excepFlowNode, endFlowNode, "endSequenceFlowId"); //子流程结束
                LOG.info(execution.getId() + "子流程结束");
            } else {
                FlowNode fromFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(fromActId);
                jumpTask(excepFlowNode, fromFlowNode, "newSequenceFlowId");
                Map<String, Object> processVariables = new HashMap<>();
                processVariables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
                processVariables.put("skip", false);
                processVariables.put("exceptionskip", true);
                engine.getRuntimeService().setVariables(processInstanceId, processVariables);
                LOG.info(execution.getId() + "异常处理完成");
            }
        } else {
            jumpTask(excepFlowNode, endFlowNode, "endSequenceFlowId");
            LOG.info(execution.getId() + "子流程结束");
        }
    }

    /**
     * 节点跳转
     * @param source 来源
     * @param target 去向
     * @param flowId 顺序流ID
     */
    private void jumpTask(FlowNode source, FlowNode target, String flowId) {
        source.getOutgoingFlows().clear(); //清理活动方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>(); //建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId(flowId);
        newSequenceFlow.setSourceFlowElement(source);
        newSequenceFlow.setTargetFlowElement(target);
        newSequenceFlowList.add(newSequenceFlow);
        source.setOutgoingFlows(newSequenceFlowList);
    }
}



