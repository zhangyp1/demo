package com.newland.paas.paasservice.activitiflow.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.activitiflow.model.TaskCompleteVo;
import com.newland.paas.paasservice.activitiflow.model.TaskListVo;
import com.newland.paas.paasservice.activitiflow.model.TaskQueryVo;
import com.newland.paas.paasservice.activitiflow.model.TaskVo;
import com.newland.paas.paasservice.activitiflow.service.TaskService;
import com.newland.paas.paasservice.controllerapi.dmp.vo.DmpPlanVo;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务查询
 *
 * @since 2018/7/25
 */
@RestController
@RequestMapping("v1/task")
@Validated
@AuditObject("任务管理")
public class TaskController {

    @Autowired
    private TaskService taskService;
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String STR_LINK = "_";
    private static final int STR_INDEX = -1;
    private static final Log LOG = LogFactory.getLogger(TaskController.class);
    /**
     * 查找当前代办任务
     *
     * @param inputVo inputVo
     * @param request request
     * @return 返回
     */
    @AuditOperate(value = "currenttask", name = "代办任务查询")
    @PostMapping(value = "/currenttask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<TaskVo> getCurrentTask(
            @RequestBody @Validated BasicPageRequestContentVo<TaskQueryVo> inputVo, HttpServletRequest request) {
        //获取用户信息
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);

        String userName = (String) securityContextImpl.getAuthentication().getPrincipal();
        return taskService.getCurrentTask(inputVo, userName);
    }

    /**
     * 查找指定流程任务
     *
     * @param instanceId 流程实例ID
     * @return 返回
     */
    @AuditOperate(value = "instanceDetail", name = "流程详情")
    @GetMapping(value = "/getByInstanceId/{instanceId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<TaskListVo> getTaskListByInstanceId(@PathVariable("instanceId") String instanceId) {
        return taskService.getTaskListByInstanceId(instanceId);

    }

    /**
     * v
     * 查找历史任务,获取的实例不一样
     *
     * @param inputVo inputVo
     * @param request request
     * @return 返回
     */
    @AuditOperate(value = "histtask", name = "历史任务")
    @PostMapping(value = "/histtask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<TaskVo> getHistoryTask(
            @RequestBody @Validated BasicPageRequestContentVo<TaskQueryVo> inputVo, HttpServletRequest request) {
        //获取用户信息
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);
        String userName = (String) securityContextImpl.getAuthentication().getPrincipal();
        return taskService.getHistoryTask(inputVo, userName);
    }

    /**
     * 查找我发起的流程
     *
     * @param inputVo inputVo
     * @param request request
     * @return 返回
     */
    @AuditOperate(value = "mytask", name = "我发起的流程")
    @PostMapping(value = "/mytask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<TaskVo> getMyTask(
            @RequestBody @Validated BasicPageRequestContentVo<TaskQueryVo> inputVo, HttpServletRequest request) {
        //获取用户信息
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);
        String userName = (String) securityContextImpl.getAuthentication().getPrincipal();
        return taskService.getMyTask(inputVo, userName);
    }

    /**
     * 流程作废
     *
     * @param processInstanceId 流程实例ID
     * @param request           参数
     */
    @AuditOperate(value = "withdrawTask", name = "流程作废")
    @DeleteMapping(value = "withdrawTask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void withdrawTask(String processInstanceId, HttpServletRequest request) {
        String userName = getUserName(request);
        taskService.withdrawTask(processInstanceId, userName);
    }

    /**
     * 流程撤回到发起人
     *
     * @param processInstanceId 流程实例ID
     * @param request           参数
     */
    @AuditOperate(value = "rollBackToAssignWorkFlow", name = "流程撤回")
    @PostMapping(value = "rollBackToAssignWorkFlow", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void rollBackToAssignWorkFlow(String processInstanceId, HttpServletRequest request) {
        String userName = getUserName(request);
        taskService.rollBackToAssignWorkFlow(processInstanceId, userName);
    }
    
    /**
     * 流程撤回流程到groupid
     *
     * @param processInstanceId 流程实例ID
     * @param request           参数
     */
    @AuditOperate(value = "rollBackToGroupIdWorkFlow", name = "流程撤回")
    @PostMapping(value = "rollBackToGroupIdWorkFlow", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void rollBackToGroupIdWorkFlow(String processInstanceId,String groupId, HttpServletRequest request) {
    	LOG.debug(LogProperty.LOGCONFIG_DEALID, "groupId = " + groupId);
   	    LOG.debug(LogProperty.LOGCONFIG_DEALID, "processInstanceId = " + groupId);
   	    String userName = getUserName(request);
        taskService.rollBackToGroupIdWorkFlow(processInstanceId, userName,groupId);
    }

    /**
     * 获取当前请求用户名
     *
     * @param request request
     * @return 返回
     */
    private String getUserName(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);
        return (String) securityContextImpl.getAuthentication().getPrincipal();
    }

    /**
     * 完成任务
     *
     * @param taskCompleteVo vo
     */
    @PostMapping(value = "/completeTask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void completeTask(@RequestBody TaskCompleteVo taskCompleteVo) {
        taskService.completeTask(taskCompleteVo.getTaskId(), taskCompleteVo.getVariables());
    }

    /**
     * 获取任务参数
     *
     * @param params  参数
     * @return 返回
     */
    @PostMapping(value = "/getTaskVariables", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getTaskVariables(@RequestBody Map<String, Object> params) {
        return taskService.getTaskVariables(params);
    }

    /**
     * 动作执行回调函数（给receiveTask发送信号以结束当前等待环节）
     *
     * @param params 参数
     */
    @PostMapping(value = "/stopWaitTask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void stopWaitTask(@RequestBody Map<String, Object> params) {
        String processInstanceId = String.valueOf(params.get("processInstanceId"));
        boolean execResult = (boolean) params.get("execResult");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<Execution> executions = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId).list();
        Execution execution = null;
        for (Execution e : executions) {
            if (e.getActivityId() != null) {
                execution = e;
            }
        }
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
        if (execution == null) {
            return;
        }
        //为true则向receiveTask发信号
        if (execResult) {
            //activi6中signal方法改成了trigger
            processVariables.put("skip", false);
            processVariables.put("exceptionskip", true);
            Map<String, Object> variables = runtimeService.getVariables(execution.getRootProcessInstanceId());
            String groupId = String.valueOf(variables.get("groupId"));
            processVariables.put("groupId", "group_" + groupId);
        } else {
            //进入异常
            processVariables.put("skip", true);
            processVariables.put("exceptionskip", false);
        }
        runtimeService.trigger(execution.getId(), processVariables);
    }

    /**
     * 获取异常环节流程参数
     *
     * @param params  参数
     * @return 返回
     */
    @PostMapping(value = "/getTaskExcepVariables", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, String> getTaskExcepVariables(@RequestBody Map<String, Object> params) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String processInstanceId = String.valueOf(params.get("processInstanceId"));
        Map<String, String> repParam = new HashMap<>();
        String fromActId = String.valueOf(runtimeService.getVariable(processInstanceId, "fromActId"));
        String nextActivityId = String.valueOf(runtimeService.getVariable(processInstanceId, "nextActivityId"));
        int iStart = fromActId.indexOf(STR_LINK);
        int iEnd = fromActId.lastIndexOf(STR_LINK);
        if (iStart != STR_INDEX && iEnd > iStart) {
            String appCode = fromActId.substring(iStart + 1, iEnd); //应用code
            String execOrder = fromActId.substring(iEnd + 1); //step执行顺序
            repParam.put("appCode", appCode);
            repParam.put("execOrder", execOrder);
        }
        repParam.put("fromActId", fromActId);
        repParam.put("nextActivityId", nextActivityId);
        return repParam;
    }

    /**
     * 上线方案列表带出当前用户的待办工单数
     * @param dmpPlanVos 方案列表
     * @param request 请求上下文
     * @return 带待办工单数方案列表
     */
    @AuditOperate(value = "getTaskCount", name = "待办工单数")
    @PostMapping(value = "/getTaskCount", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<DmpPlanVo> getTaskCount(@RequestBody List<DmpPlanVo> dmpPlanVos, HttpServletRequest request) {
        //获取用户信息
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);
        String userName = (String) securityContextImpl.getAuthentication().getPrincipal();
        return taskService.getTaskCount(dmpPlanVos, userName);
    }

    /**
     * 认领任务
     *
     * @param param taskId
     */
    @PostMapping(value = "/claimTask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void claimTask(@RequestBody Map<String, String> param, HttpServletRequest request) {
        String taskId = param.get("taskId");
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute(SPRING_SECURITY_CONTEXT);
        String userName = (String) securityContextImpl.getAuthentication().getPrincipal();
        taskService.claimTask(taskId, userName);
    }

    /**
     * 取消认领
     *
     * @param param taskId
     */
    @PostMapping(value = "/unClaimTask", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void unClaimTask(@RequestBody Map<String, String> param) {
        String taskId = param.get("taskId");
        taskService.unClaimTask(taskId);
    }

    /**
     * 根据子流程流程实例ID干掉全部父流程和子流程
     *
     * @param param 流程实例ID
     */
    @PostMapping(value = "/destroyProcess", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void destroyProcess(@RequestBody Map<String, String> param) {
        String processId = param.get("processId");
        taskService.destroyProcess(processId);
    }

    /**
     * 查询任务的审批人是否不是租户隔离用户
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "{taskId}/isNotTenantAccountApprove", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo<Boolean> isTenantAccountApprove(@PathVariable("taskId") String taskId) {
        return new BasicResponseContentVo<>(taskService.isNotTenantAccountApprove(taskId));
    }

}
