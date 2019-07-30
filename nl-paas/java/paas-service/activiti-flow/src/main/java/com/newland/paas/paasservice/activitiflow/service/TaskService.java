package com.newland.paas.paasservice.activitiflow.service;

import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.paasservice.activitiflow.model.TaskListVo;
import com.newland.paas.paasservice.activitiflow.model.TaskQueryVo;
import com.newland.paas.paasservice.activitiflow.model.TaskVo;
import com.newland.paas.paasservice.controllerapi.dmp.vo.DmpPlanVo;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;
import java.util.Map;

/**
 * 任务
 *
 * @author WRP
 * @since 2019/1/30
 */
public interface TaskService {

    /**
     * 历史任务
     *
     * @param inputVo inputVo
     * @param userName userName
     * @return return
     */
    ResultPageData<TaskVo> getHistoryTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName);

    /**
     * 我的任务
     *
     * @param inputVo inputVo
     * @param userName userName
     * @return return
     */
    ResultPageData<TaskVo> getMyTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName);

    /**
     * 当前任务
     *
     * @param inputVo inputVo
     * @param userName userName
     * @return return
     */
    ResultPageData<TaskVo> getCurrentTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName);

    /**
     * 根据流程实例查找任务
     *
     * @param instanceId instanceId
     * @return return
     */
    List<TaskListVo> getTaskListByInstanceId(String instanceId);

    /**
     * 作废流程
     *
     * @param processInstanceId processInstanceId
     * @param userName userName
     */
    void withdrawTask(String processInstanceId, String userName);

    /**
     * 完成任务
     *
     * @param taskId taskId
     * @param variables variables
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * 获取流程变量
     *
     * @param params params
     * @return return
     */
    Object getTaskVariables(Map<String, Object> params);

    /**
     * 退回到指定环节
     *
     * @param processInstanceId processInstanceId
     * @param userName userName
     */
    void rollBackToAssignWorkFlow(String processInstanceId, String userName);
    
    /**
     * 退回到指定环节GroupId
     *
     * @param processInstanceId processInstanceId
     * @param userName userName
     */
    void rollBackToGroupIdWorkFlow(String processInstanceId, String userName,String groupId);


    /**
     * 根据用户和参数查询当前待办工单数
     *
     * @param dmpPlanVos 方案列表
     * @param userName 用户名
     * @return 带待办工单数方案列表
     */
    List<DmpPlanVo> getTaskCount(List<DmpPlanVo> dmpPlanVos, String userName);

    /**
     *
     * @return SessionInfo
     */
    SessionInfo getSessionInfo();

    /**
     * 任务认领
     * @param taskId 任务ID
     * @param userName 用户名
     */
    void claimTask(String taskId, String userName);

    /**
     * 取消认领
     * @param taskId 任务ID
     */
    void unClaimTask(String taskId);

    /**
     * 根据子流程流程实例ID干掉父流程和全部子流程
     *
     * @param processId 流程实例ID
     */
    void destroyProcess(String processId);

    /**
     * 查询任务的审批人是否不是租户隔离用户
     *
     * @param taskId
     * @return
     */
    Boolean isNotTenantAccountApprove(String taskId);
}
