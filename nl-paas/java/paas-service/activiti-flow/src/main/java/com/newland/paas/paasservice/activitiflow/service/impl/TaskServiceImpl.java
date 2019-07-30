package com.newland.paas.paasservice.activitiflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.common.SystemErrorCode;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.dao.ActHiProcinstMapper;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.dao.ActHiTaskinstMapper;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.dao.ActRuTaskMapper;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.HistoricProcessInstanceBo;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.HistoricTaskInstanceBo;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.RunTaskBo;
import com.newland.paas.paasservice.activitiflow.model.Constant;
import com.newland.paas.paasservice.activitiflow.model.TaskListVo;
import com.newland.paas.paasservice.activitiflow.model.TaskQueryVo;
import com.newland.paas.paasservice.activitiflow.model.TaskVo;
import com.newland.paas.paasservice.activitiflow.service.TaskService;
import com.newland.paas.paasservice.controllerapi.dmp.vo.DmpPlanVo;
import com.newland.paas.sbcommon.activiti.ActTokenUtil;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import io.jsonwebtoken.impl.TextCodec;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务
 *
 * @author WRP
 * @since 2019/1/30
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final Log LOG = LogFactory.getLogger(TaskServiceImpl.class);
    private static final Integer TOKEN_LENGTH = 3;
    private static final String DATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static final String STR_LINK = "_";
    private static final int STRINDEX = -1;
    private static final String PLAN_CREATE_NAME = "方案创建流程";

    @Autowired
    private ActHiProcinstMapper actHiProcinstMapper;
    @Autowired
    private ActRuTaskMapper actRuTaskMapper;
    @Autowired
    private ActHiTaskinstMapper actHiTaskinstMapper;

    @Override
    public ResultPageData<TaskVo> getHistoryTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName) {
        // 拼查询参数
        TaskQueryVo taskQueryVo = inputVo.getParams();
        HistoricTaskInstanceBo historicTaskInstanceBo = new HistoricTaskInstanceBo();
        historicTaskInstanceBo.setProcessInstanceId(taskQueryVo.getId());
        historicTaskInstanceBo.setUsers(getUsers(userName));
        historicTaskInstanceBo.setGroups(getUserGroup(userName));
        historicTaskInstanceBo.setFinished(taskQueryVo.getFinished());
        historicTaskInstanceBo.setBusinessKey(taskQueryVo.getName());
        if (!StringUtils.isEmpty(taskQueryVo.getProcessDifintionKey())) {
            if (PLAN_CREATE_NAME.equals(taskQueryVo.getProcessDifintionKey())) {
                historicTaskInstanceBo.setProcessDefinitionName(taskQueryVo.getProcessDifintionKey());
            } else {
                historicTaskInstanceBo.setProcessDifintionKey(taskQueryVo.getProcessDifintionKey());
            }
        }
        PageInfo pageInfo = inputVo.getPageInfo();
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<HistoricTaskInstanceEntityImpl> list = actHiTaskinstMapper.selectBySelective(historicTaskInstanceBo);
        pageInfo.setTotalRecord(page.getTotal());
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<TaskVo> taskVos = new ArrayList<>(list.size());
        for (HistoricTaskInstance historicTaskInstance : list) {
            TaskVo taskVo = new TaskVo();
            taskVo.setTaskName(historicTaskInstance.getName());
            taskVo.setProcessId(historicTaskInstance.getProcessInstanceId());
            taskVo.setFormKey(historicTaskInstance.getDescription() == null ? Constant.DEFAULT_FORM_KEY
                    : historicTaskInstance.getDescription());
            HistoricProcessInstanceQuery historicProcessInstanceQuery = engine.getHistoryService()
                    .createHistoricProcessInstanceQuery();
            historicProcessInstanceQuery.processInstanceId(historicTaskInstance.getProcessInstanceId());
            HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.singleResult();
            taskVo.setStarter(getUserName(historicProcessInstance.getStartUserId()));
            taskVo.setProcessName(historicProcessInstance.getBusinessKey());
            DateFormat bf = new SimpleDateFormat(DATE_YYYYMMDDHHMMSS);
            taskVo.setStarTime(historicProcessInstance.getStartTime() != null
                    ? bf.format(historicProcessInstance.getStartTime()) : null);
            taskVo.setEndTime(historicProcessInstance.getEndTime() != null
                    ? bf.format(historicProcessInstance.getEndTime()) : null);
            taskVo.setDefineName(historicProcessInstance.getProcessDefinitionName());
            // 当前任务
            Task task = engine.getTaskService().createTaskQuery().processInstanceId(historicProcessInstance.getId())
                    .singleResult();
            if (task == null) {
//            	 LOG.info(LogProperty.LOGTYPE_DETAIL, "taskId:"+historicTaskInstance.getId());
//                List<Comment> comments = engine.getTaskService().getTaskComments(historicTaskInstance.getId());
//                if(comments.size()>0){
////                	 taskVo.setCurrentTask(comments.get(0).getFullMessage());
//                	 taskVo.setEndTime(comments.get(0).getTime() != null? bf.format(comments.get(0).getTime()) : null);
//                }
                taskVo.setCurrentTask("完成");
                taskVo.setTaskId(null);

            } else {
                taskVo.setTaskId(task.getId());
                taskVo.setCurrentTask(task.getName());
            }
            taskVos.add(taskVo);
        }
        return new ResultPageData<>(taskVos, pageInfo);
    }

    @Override
    public ResultPageData<TaskVo> getMyTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName) {
        // 拼查询参数
        TaskQueryVo taskQueryVo = inputVo.getParams();
        HistoricProcessInstanceBo historicProcessInstanceBo = new HistoricProcessInstanceBo();
        historicProcessInstanceBo.setUsers(getUsers(userName));
        historicProcessInstanceBo.setFinished(taskQueryVo.getFinished());
        historicProcessInstanceBo.setProcessInstanceId(taskQueryVo.getId());
        historicProcessInstanceBo.setBusinessKey(taskQueryVo.getName());
        if (!StringUtils.isEmpty(taskQueryVo.getProcessDifintionKey())) {
            if (PLAN_CREATE_NAME.equals(taskQueryVo.getProcessDifintionKey())) {
                historicProcessInstanceBo.setProcessDefinitionName(taskQueryVo.getProcessDifintionKey());
            } else {
                historicProcessInstanceBo.setProcessDifintionKey(taskQueryVo.getProcessDifintionKey());
            }
        }
        PageInfo pageInfo = inputVo.getPageInfo();
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<HistoricProcessInstanceEntityImpl> list = actHiProcinstMapper.selectBySelective(historicProcessInstanceBo);
        pageInfo.setTotalRecord(page.getTotal());
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<TaskVo> taskVos = new ArrayList<>(list.size());
        for (HistoricProcessInstance historicProcessInstance : list) {
            //historicProcessInstanceQuery不支持BusinessKey模糊查询改成此方式;过滤上线方案中的子流程
//            if ((taskQueryVo.getName() != null && !"".equals(taskQueryVo.getName())
//                    && !historicProcessInstance.getBusinessKey().contains(taskQueryVo.getName().trim()))
//                    || (PLAN_CREATE_NAME.equals(historicProcessInstance.getProcessDefinitionName())
//                    && !StringUtils.isEmpty(historicProcessInstance.getSuperProcessInstanceId()))) {
//                continue;
//            }
            TaskVo taskVo = new TaskVo();
            taskVo.setProcessId(historicProcessInstance.getId());
            taskVo.setProcessName(historicProcessInstance.getBusinessKey());
            taskVo.setDefineName(historicProcessInstance.getProcessDefinitionName());
            DateFormat bf = new SimpleDateFormat(DATE_YYYYMMDDHHMMSS);
            taskVo.setStarTime(historicProcessInstance.getStartTime() != null
                    ? bf.format(historicProcessInstance.getStartTime()) : null);
            taskVo.setEndTime(historicProcessInstance.getEndTime() != null
                    ? bf.format(historicProcessInstance.getEndTime()) : null);
            Task task = engine.getTaskService().createTaskQuery().processInstanceId(historicProcessInstance.getId())
                    .singleResult();
            taskVo.setCurrentTask(task != null ? task.getName() : "完成");
            taskVo.setTaskId(task != null ? task.getId() : null);

            String startUserId = historicProcessInstance.getStartUserId();
            taskVo.setStarter(getUserName(startUserId));
            BpmnModel bpmnModel = engine.getRepositoryService()
                    .getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            FlowElement flowElement = bpmnModel.getFlowElement("startevent1");
            if (flowElement != null) {
                taskVo.setFormKey(flowElement.getDocumentation() == null || "".equals(flowElement.getDocumentation())
                        ? Constant.DEFAULT_FORM_KEY : flowElement.getDocumentation());
            } else {
                taskVo.setFormKey(Constant.DEFAULT_FORM_KEY);
            }
            taskVos.add(taskVo);
        }
        return new ResultPageData<>(taskVos, pageInfo);
    }

    @Override
    public ResultPageData<TaskVo> getCurrentTask(BasicPageRequestContentVo<TaskQueryVo> inputVo, String userName) {
        // 拼查询参数
        TaskQueryVo taskQueryVo = inputVo.getParams();
        RunTaskBo runTaskBo = new RunTaskBo();
        runTaskBo.setProcessInstanceId(taskQueryVo.getId());
        runTaskBo.setUsers(getUsers(userName));
        runTaskBo.setGroups(getUserGroup(userName));
        runTaskBo.setBusinessKey(taskQueryVo.getName());
        if (!StringUtils.isEmpty(taskQueryVo.getProcessDifintionKey())) {
            //上线方案definitionName去重后key查询改为name  @modify sunxm
            if (PLAN_CREATE_NAME.equals(taskQueryVo.getProcessDifintionKey())) {
                runTaskBo.setProcessDefinitionName(taskQueryVo.getProcessDifintionKey());
            } else {
                runTaskBo.setProcessDifintionKey(taskQueryVo.getProcessDifintionKey());
            }
        }
        //@modify 增加流程实例参数查询条件 sunxm
//        if (!StringUtils.isEmpty(taskQueryVo.getVariableName()) && !StringUtils.isEmpty(taskQueryVo.getVariableValue())) {
//            taskQuery.processVariableValueEquals(taskQueryVo.getVariableName(), taskQueryVo.getVariableValue());
//        }
        PageInfo pageInfo = inputVo.getPageInfo();
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<TaskEntityImpl> list = actRuTaskMapper.selectBySelective(runTaskBo);
        pageInfo.setTotalRecord(page.getTotal());
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<TaskVo> taskVos = new ArrayList<>(list.size());
        for (Task task : list) {
            TaskVo taskVo = new TaskVo();
            taskVo.setTaskName(task.getName());
            taskVo.setProcessId(task.getProcessInstanceId());
            ProcessInstanceQuery processInstanceQuery = engine.getRuntimeService().createProcessInstanceQuery();
            processInstanceQuery.processInstanceId(task.getProcessInstanceId());
            ProcessInstance processInstance = processInstanceQuery.singleResult();
            String startUserId = processInstance.getStartUserId();
            taskVo.setStarter(getUserName(startUserId));
            taskVo.setProcessName(processInstance.getBusinessKey());
            DateFormat bf = new SimpleDateFormat(DATE_YYYYMMDDHHMMSS);
            taskVo.setStarTime(
                    processInstance.getStartTime() != null ? bf.format(processInstance.getStartTime()) : null);
            taskVo.setDefineName(processInstance.getProcessDefinitionName());
            taskVo.setCurrentTask(task.getName());
            taskVo.setTaskId(task.getId());
            taskVo.setFormKey(task.getDescription() == null ? Constant.DEFAULT_FORM_KEY : task.getDescription());
            //是否已被认领
            taskVo.setClaimed(task.getAssignee() != null && !"".equals(task.getAssignee()));
            taskVo.setClaimTime(task.getClaimTime() != null ? bf.format(task.getClaimTime()) : null);
            taskVos.add(taskVo);
        }
        return new ResultPageData<>(taskVos, pageInfo);
    }

    /**
     * 查询用户名
     *
     * @param userId
     * @return
     */
    private String getUserName(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            try {
                User user = engine.getIdentityService().createUserQuery().userId(userId).singleResult();
                return user == null ? "-" : user.getFirstName();
            } catch (Exception e) {
                LOG.info(LogProperty.LOGTYPE_DETAIL, "未获取到User:" + userId);
            }
        }
        return "-";
    }

    /**
     * 查询用户的工组
     * 带租户用户的工组、用户的工组
     *
     * @param userId
     * @return
     */
    private List<String> getUserGroup(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Group> groups = new ArrayList<>();
        for (String id : getUsers(userId)) {
            groups.addAll(identityService.createGroupQuery().groupMember(id).list());
        }
        if (groups.isEmpty()) {
            return Collections.emptyList();
        }
        return buildCurrentGroup(groups);
    }

    /**
     * 获取用户ID
     * 带租户用户的分为【带租户用户、用户】
     * 不带租户用户的分为【用户】
     *
     * @param userId
     * @return
     */
    private List<String> getUsers(String userId) {
        if (!userId.contains(ActTokenUtil.POUND_KEY)) {
            return Arrays.asList(userId);
        }
        return Arrays.asList(userId, userId.split(ActTokenUtil.POUND_KEY)[1]);
    }

    /**
     * @param instanceId instanceId
     * @return return
     */
    @Override
    public List<TaskListVo> getTaskListByInstanceId(String instanceId) {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到身份服务组件实例,获取所在用户组
        HistoricTaskInstanceQuery historicTaskInstanceQuery = engine.getHistoryService()
                .createHistoricTaskInstanceQuery();
        historicTaskInstanceQuery = historicTaskInstanceQuery.processInstanceId(instanceId);
        // 已办任务
        List<TaskListVo> taskListVos = new ArrayList<TaskListVo>();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.finished()
                .orderByHistoricTaskInstanceStartTime().asc().includeProcessVariables().list();
        DateFormat bf = new SimpleDateFormat(DATE_YYYYMMDDHHMMSS);
        int no = 0;
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            no++;
            TaskListVo taskListVo = new TaskListVo();
            taskListVo.setNo(no);
            taskListVo.setStartTime(historicTaskInstance.getStartTime() != null
                    ? bf.format(historicTaskInstance.getStartTime()) : null);
            taskListVo.setEndTime(
                    historicTaskInstance.getEndTime() != null ? bf.format(historicTaskInstance.getEndTime()) : null);
            taskListVo.setTaskId(historicTaskInstance.getId());
            taskListVo.setTaskName(historicTaskInstance.getName());
            //查找候选执行人
            List<HistoricIdentityLink> historicIdentityLinks = engine.getHistoryService()
                    .getHistoricIdentityLinksForTask(historicTaskInstance.getId());
            for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks) {
                if ("candidate".equals(historicIdentityLink.getType())) {
                    if (!StringUtils.isEmpty(historicIdentityLink.getGroupId())) {
                        Group group = engine.getIdentityService()
                                .createGroupQuery().groupId(historicIdentityLink.getGroupId()).singleResult();
                        taskListVo.setCandiate(group != null ? group.getName() : null);
                    } else if (!StringUtils.isEmpty(historicIdentityLink.getUserId())) {
                        try {
                            User user = engine.getIdentityService()
                                    .createUserQuery().userId(historicTaskInstance.getAssignee()).singleResult();
                            taskListVo.setCandiate(user != null ? user.getFirstName() : null);
                        } catch (Exception e) {
                            LOG.info(LogProperty.LOGTYPE_DETAIL, "未获取到startUserId:");
                        }
                    }
                }
            }
            if (StringUtils.isEmpty(historicTaskInstance.getDeleteReason())) {
                taskListVo.setStatus("已处理");
                if (historicTaskInstance.getAssignee() != null) {
                    try {
                        User user = engine.getIdentityService().createUserQuery()
                                .userId(historicTaskInstance.getAssignee()).singleResult();
                        taskListVo.setAssignee(user != null ? user.getFirstName() : null);
                    } catch (Exception e) {
                        LOG.info(LogProperty.LOGTYPE_DETAIL, "未获取到startUserId:");
                    }
                }
                LOG.info(LogProperty.LOGTYPE_DETAIL, "taskId:" + historicTaskInstance.getId());
                List<Comment> comments = engine.getTaskService().getTaskComments(historicTaskInstance.getId());
                if (comments.size() > 0) {
//                 	 taskListVo.setStatus(comments.get(0).getFullMessage());
                    taskListVo.setAction("撤回");
                } else {
                    HistoryService historyService = engine.getHistoryService();
                    HistoricDetailQuery historicDetailQuery = historyService.createHistoricDetailQuery();
                    List<HistoricDetail> historicDetails = historicDetailQuery.taskId(historicTaskInstance.getId()).list();
                    //查找审批意
//                     Map<String, Object> map = historicTaskInstance.getProcessVariables();
                    if (historicDetails.size() > 0) {
                        for (HistoricDetail historicDetail : historicDetails) {
                            HistoricFormPropertyEntityImpl historicFormPropertyEntity = (HistoricFormPropertyEntityImpl) historicDetail;
                            if ("approve".equals(historicFormPropertyEntity.getPropertyId())) {
                                if ("true".equals(historicFormPropertyEntity.getPropertyValue())) {
                                    taskListVo.setAction("同意");
                                } else {
                                    taskListVo.setAction("不同意");
                                }
                            }
                            if ("isAgree".equals(historicFormPropertyEntity.getPropertyId())) {
                                if ("1".equals(historicFormPropertyEntity.getPropertyValue())) {
                                    taskListVo.setAction("同意");
                                } else {
                                    taskListVo.setAction("不同意");
                                }
                            }
                            if ("remark".equals(historicFormPropertyEntity.getPropertyId())) {
                                taskListVo.setComments(historicFormPropertyEntity.getPropertyValue());
                            }
                        }
                    } else {
                        taskListVo.setAction("已提交");
                    }
//                     if (map != null) {
//                         if (map.get("approve") != null) {
//                             if ("true".equals(map.get("approve"))) {
//                                 taskListVo.setAction("同意");
//                             } else {
//                                 taskListVo.setAction("不同意");
//                             }
//                             taskListVo.setComments((String) map.get("remark"));
//                         }
//                         if (map.get("isAgree") != null) {
//                             if ("1".equals(map.get("isAgree"))) {
//                                 taskListVo.setAction("同意");
//                             } else {
//                                 taskListVo.setAction("不同意");
//                             }
//                             taskListVo.setComments((String) map.get("comment"));
//                         }
//                     }else{
//                    	 taskListVo.setAction("已提交");
//                     }
                }

            } else {
                // 查找流程发起人
                HistoricProcessInstanceQuery historicProcessInstanceQuery =
                        engine.getHistoryService().createHistoricProcessInstanceQuery();
                HistoricProcessInstance historicProcessInstance =
                        historicProcessInstanceQuery.processInstanceId(instanceId).singleResult();
                User user = engine.getIdentityService().createUserQuery()
                        .userId(historicProcessInstance.getStartUserId()).singleResult();
                taskListVo.setAssignee(user != null ? user.getFirstName() : null);
                taskListVo.setStatus("已作废");
                taskListVo.setAction("作废流程");
                taskListVo.setComments("作废流程");
            }
            taskListVos.add(taskListVo);
        }
        // 未完成任务
        historicTaskInstanceQuery = engine.getHistoryService().createHistoricTaskInstanceQuery();
        historicTaskInstanceQuery = historicTaskInstanceQuery.processInstanceId(instanceId);
        historicTaskInstances = historicTaskInstanceQuery.unfinished().orderByHistoricTaskInstanceStartTime().asc()
                .list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            no++;
            TaskListVo taskListVo = new TaskListVo();
            taskListVo.setStatus("执行中");
            // 执行人查找
            if (historicTaskInstance.getAssignee() != null) {
                User user = engine.getIdentityService().createUserQuery().userId(historicTaskInstance.getAssignee())
                        .singleResult();
                taskListVo.setAssignee(user != null ? user.getFirstName() : null);
            } else {
                taskListVo.setAssignee(
                        historicTaskInstance.getAssignee() != null ? historicTaskInstance.getAssignee() : null);
            }
            //查找候选执行人
            List<HistoricIdentityLink> historicIdentityLinks = engine.getHistoryService()
                    .getHistoricIdentityLinksForTask(historicTaskInstance.getId());
            for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks) {
                if ("candidate".equals(historicIdentityLink.getType())) {
                    if (!StringUtils.isEmpty(historicIdentityLink.getGroupId())) {
                        Group group = engine.getIdentityService()
                                .createGroupQuery().groupId(historicIdentityLink.getGroupId()).singleResult();
                        taskListVo.setCandiate(group != null ? group.getName() : null);
                    } else if (!StringUtils.isEmpty(historicIdentityLink.getUserId())) {
                        User user = engine.getIdentityService()
                                .createUserQuery().userId(historicTaskInstance.getAssignee()).singleResult();
                        taskListVo.setCandiate(user != null ? user.getFirstName() : null);
                    }

                }
            }
            taskListVo.setStartTime(historicTaskInstance.getStartTime() != null
                    ? bf.format(historicTaskInstance.getStartTime()) : null);
            taskListVo.setEndTime(
                    historicTaskInstance.getEndTime() != null ? bf.format(historicTaskInstance.getEndTime()) : null);
            taskListVo.setTaskId(historicTaskInstance.getId());
            taskListVo.setTaskName(historicTaskInstance.getName());
            taskListVo.setNo(no);
            taskListVos.add(taskListVo);
        }
        return taskListVos;
    }

    /**
     * @param processInstanceId processInstanceId
     * @param userName          userName
     */
    @Override
    public void rollBackToAssignWorkFlow(String processInstanceId, String userName) {

        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        Task task = engine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).singleResult();
        List<HistoricTaskInstance> htiList = engine.getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        String userId = userName;
        List<String> users = getUsers(userName);
        for (HistoricTaskInstance hti : htiList) {
            if (users.contains(hti.getAssignee())) {
                userId = hti.getAssignee();
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if (null == myTaskId) {
            throw new SystemException(new PaasError("22000002", "该任务非当前用户提交，无法撤回！"));
        }
        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(processDefinitionId);
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = engine.getHistoryService()
                .createHistoricActivityInstanceQuery().executionId(myTask.getExecutionId()).finished().list();

        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

        Execution execution = engine.getRuntimeService()
                .createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();

        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

        Authentication.setAuthenticatedUserId(userId);
        //先认领任务
        this.claimTask(task.getId(), userId);
        engine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), "撤回");

        Map<String, Object> currentVariables = new HashMap<>();
        currentVariables.put("applier", userId);
        currentVariables.put("assignee", userId);
        //完成任务
        engine.getTaskService().complete(task.getId(), currentVariables);


        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);

    }

    /**
     * @param processInstanceId processInstanceId
     * @param userName          userName
     */
    @Override
    public void rollBackToGroupIdWorkFlow(String processInstanceId, String userName, String groupId) {

        // 创建一个流程引擎 
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        Task task = engine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).singleResult();
        List<HistoricTaskInstance> htiList = engine.getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        String userId = userName;
        List<String> users = getUsers(userName);
        for (HistoricTaskInstance hti : htiList) {
            if (users.contains(hti.getAssignee())) {
                userId = hti.getAssignee();
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if (null == myTaskId) {
            throw new SystemException(new PaasError("22000002", "该任务非当前用户提交，无法撤回！"));
        }
        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(processDefinitionId);
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = engine.getHistoryService()
                .createHistoricActivityInstanceQuery().executionId(myTask.getExecutionId()).finished().list();

        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

        Execution execution = engine.getRuntimeService()
                .createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();

        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

        Authentication.setAuthenticatedUserId(userId);
        this.claimTask(task.getId(), userId);
        engine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), "撤回");
        Map<String, Object> currentVariables = new HashMap<>();
        currentVariables.put("applier", userId);
        currentVariables.put("assignee", userId);
        //完成任务
        engine.getTaskService().complete(task.getId(), currentVariables);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);

//        String newtaskId = "0";
//        List<TaskListVo>  taskLists2=getTaskListByInstanceId(processInstanceId);
//        for (TaskListVo taskList : taskLists2) {
//			System.out.println(taskList.getTaskId());
//			if (Integer.parseInt(newtaskId) < Integer.parseInt(taskList.getTaskId())) {
//				newtaskId = taskList.getTaskId();
//			}
//		}
//        //添加分配组
//        engine.getTaskService().addCandidateGroup(newtaskId,groupId);
//        engine.getTaskService().setAssignee(newtaskId,null);  
    }

    /**
     * @param processInstanceId processInstanceId
     * @param userName          userName
     */
    @Override
    public void withdrawTask(String processInstanceId, String userName) {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 查找流程发起人
        ProcessInstanceQuery processInstanceQuery = engine.getRuntimeService().createProcessInstanceQuery();
        ProcessInstance processInstance = processInstanceQuery.processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) {
            throw new SystemException(new PaasError("22000002", "流程不存在！"));
        }
        String startUserId = processInstance.getStartUserId();
        List<String> users = getUsers(userName);
        // 作废人为流程发起人
        // 未经审核的流程可以直接删除
        if (users.contains(startUserId)) {
            List<HistoricTaskInstance> htis = engine.getHistoryService().createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId).unfinished()
                    .orderByHistoricTaskInstanceStartTime().asc().list();
            if (CollectionUtils.isEmpty(htis)) {
                throw new SystemException(new PaasError("22000001", "流程已结束，无法作废！"));
            } else {
                for (HistoricTaskInstance task : htis) {
                    boolean isDraft = !("processDraft".equals(task.getTaskDefinitionKey()) && startUserId.equals(task.getAssignee()));
                    if (isDraft) {
                        throw new SystemException(new PaasError("22000001", "流程不处于【流程草拟】，无法作废！"));
                    }
                }
                engine.getRuntimeService().deleteProcessInstance(processInstanceId, "作废流程");
            }
        }
    }

    /**
     * @param taskId    taskId
     * @param variables variables
     */
    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        if ("0".equals(variables.get("isAutoExec"))) { //手动执行时存流程变量
            //获取当前activityId
            String processInstanceId = String.valueOf(variables.get("processInstanceId"));
            org.activiti.engine.TaskService taskSer = engine.getTaskService();
            Task task = taskSer.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            String activityId = task.getDescription();
            int iStart = activityId.indexOf(STR_LINK);
            int iEnd = activityId.lastIndexOf(STR_LINK);
            if (iStart != STRINDEX && iEnd > iStart) {
                String execOrder = activityId.substring(iEnd + 1); //step执行顺序
                String nextActivityId = getNextActId(task.getProcessDefinitionId(), execOrder); //获取下一步activityID
                variables.put("fromActId", activityId);
                variables.put("nextActivityId", nextActivityId);


            }
            variables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
            if ((boolean) variables.get("execResult")) {
                variables.put("skip", false);
                variables.put("exceptionskip", true);
            } else {
                variables.put("skip", true);
                variables.put("exceptionskip", false);
            }
        }
        engine.getTaskService().complete(taskId, variables);
    }

    /**
     * 获取下一步activityId
     *
     * @param processDefinitionId
     * @param execOrder
     * @return
     */
    String getNextActId(String processDefinitionId, String execOrder) {
        String nextActivityId = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<Process> processList = bpmnModel.getProcesses();
        for (Process process : processList) {
            for (FlowElement flowElement : process.getFlowElements()) {
                String stepId = flowElement.getId();
                if (com.newland.paas.common.util.StringUtils.isNotEmpty(stepId)) {
                    nextActivityId = getNextActIdImpl(stepId, execOrder);
                }
            }
        }
        return nextActivityId;
    }

    /**
     * @param stepId
     * @param execOrder
     * @return
     */
    String getNextActIdImpl(String stepId, String execOrder) {
        String nextActivityId = "";
        int iNextStart = stepId.indexOf(STR_LINK);
        int iNextEnd = stepId.lastIndexOf(STR_LINK);
        if (iNextStart != STRINDEX && iNextEnd > iNextStart
                && stepId.startsWith("step_")) {
            String stepIdPre = stepId.substring(0, iNextStart);
            int stepIdOrder = Integer.parseInt(stepId.substring(iNextEnd + 1));
            int nextOrder = Integer.parseInt(execOrder) + 1;
            if ("step".equals(stepIdPre)
                    && stepIdOrder == nextOrder) {
                nextActivityId = stepId;
            }
        }
        return nextActivityId;
    }

    /**
     * @param params params
     * @return return
     */
    @Override
    public Object getTaskVariables(Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        String variableName = (String) params.get("key");
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        return engine.getTaskService().getVariable(taskId, variableName);
    }

    /**
     * 获取当前用户选择的当前租户的工组
     *
     * @param groupList groupList
     * @return return
     */
    private List<String> buildCurrentGroup(List<Group> groupList) {
        SessionInfo sessionInfo = getSessionInfo();
        List<Long> groupIdList = sessionInfo.getGroupIdList();
        List<Long> subGroupIdList = sessionInfo.getSubGroupIdList();
        Set<String> groups = new HashSet<>();
        for (Group group : groupList) {
            String id = group.getId();
            String groupId = id.replaceAll("group_", "");
            if (groupId.contains("role_")) {
                groups.add(group.getId());
            } else {
                Long groupIdTmp = Long.valueOf(groupId);
                if (groupIdList.contains(groupIdTmp) || subGroupIdList.contains(groupIdTmp)) {
                    groups.add(group.getId());
                }
            }
        }
        return new ArrayList<>(groups);
    }

    /**
     * 获取当前用户选择的当前租户的工组
     *
     * @param groupList groupList
     * @return return
     */
    public List<String> getSGroup(List<Group> groupList) {
        List<String> groups = new ArrayList<>();
        for (Group group : groupList) {
            groups.add(group.getId());
        }
        return groups;
    }

    /**
     * 获取用户信息
     *
     * @return return
     */
    @Override
    public SessionInfo getSessionInfo() {
        String tokens = ActivitiTokenUtils.getPaasToken();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "PAAS-Authorization:" + tokens);
        if (!org.springframework.util.StringUtils.isEmpty(tokens)) {
            tokens = tokens.replaceAll("Bearer ", "");
            String[] tokenArray = tokens.split("\\.");
            if (tokenArray.length != TOKEN_LENGTH) {
                throw new SystemException(SystemErrorCode.sessionError);
            }
            String tokenData = tokenArray[1];
            String tokenDecode = null;
            try {
                tokenDecode = TextCodec.BASE64URL.decodeToString(tokenData);
            } catch (IllegalArgumentException e) {
                LOG.error(LogProperty.LOGTYPE_DETAIL, "字符串不是base64编码！", e);
                throw new SystemException(SystemErrorCode.sessionError);
            }
            LOG.info(LogProperty.LOGTYPE_DETAIL, "token-decode:" + tokenDecode);
            if (!org.springframework.util.StringUtils.isEmpty(tokenDecode)) {
                return JSON.parseObject(tokenDecode, new TypeReference<SessionInfo>() {
                });
            }
        }
        throw new SystemException(SystemErrorCode.sessionError);
    }

    @Override
    public void claimTask(String taskId, String userName) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        try {
            engine.getTaskService().claim(taskId, userName);
        } catch (ActivitiObjectNotFoundException e) {
            throw new SystemException(SystemErrorCode.TASK_NOT_FOUND);
        } catch (ActivitiTaskAlreadyClaimedException e) {
            throw new SystemException(SystemErrorCode.TASK_IS_CLAIMED);
        }
    }

    @Override
    public void unClaimTask(String taskId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        try {
            engine.getTaskService().unclaim(taskId);
        } catch (ActivitiObjectNotFoundException e) {
            throw new SystemException(SystemErrorCode.TASK_NOT_FOUND);
        }
    }

    /**
     * 根据父流程流程实例ID干掉父流程和全部子流程
     *
     * @param processId 流程实例ID
     */
    @Override
    public void destroyProcess(String processId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getRuntimeService().deleteProcessInstance(processId, "销毁" + processId);
    }

    @Override
    public Boolean isNotTenantAccountApprove(String taskId) {
        String assignee = actRuTaskMapper.getAssigneeByTaskId(taskId);
        if (!StringUtils.isEmpty(assignee) && !assignee.contains(ActTokenUtil.POUND_KEY)) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户和参数查询当前待办工单数
     *
     * @param dmpPlanVos 方案列表
     * @param userName   用户名
     * @return 带待办工单数方案列表
     */
    @Override
    public List<DmpPlanVo> getTaskCount(List<DmpPlanVo> dmpPlanVos, String userName) {
        // 创建一个流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        for (DmpPlanVo dmpPlanVo : dmpPlanVos) {
            TaskQuery taskQuery = engine.getTaskService().createTaskQuery();
            List<String> groups = getUserGroup(userName);
            // 所在用户组的任务加上自己所认领的任务
            if (!groups.isEmpty()) {
                taskQuery = taskQuery.or().taskCandidateGroupIn(groups);
            } else {
                taskQuery = taskQuery.or();
            }
            for (String id : getUsers(userName)) {
                taskQuery = taskQuery.taskCandidateOrAssigned(id).taskAssignee(id);
            }
            taskQuery = taskQuery.endOr();
            taskQuery = taskQuery.processDefinitionName(PLAN_CREATE_NAME);
            taskQuery.processVariableValueEquals("planCode", dmpPlanVo.getPlanCode());
            HistoricProcessInstanceQuery historicProcessInstanceQuery = engine.getHistoryService()
                    .createHistoricProcessInstanceQuery();
            historicProcessInstanceQuery.processDefinitionKey("ProcessCreatePlan")
                    .unfinished().variableValueEquals("planCode", dmpPlanVo.getPlanCode());
            dmpPlanVo.setTaskCount(taskQuery.count());
            long processCount = historicProcessInstanceQuery.count();
            if (processCount == 1L) {
                List<HistoricProcessInstance> list = historicProcessInstanceQuery.list();
                HistoricProcessInstance instance = list.get(0);
                dmpPlanVo.setParentProcInstanceId(instance.getId());
            }
        }
        return dmpPlanVos;
    }

}
