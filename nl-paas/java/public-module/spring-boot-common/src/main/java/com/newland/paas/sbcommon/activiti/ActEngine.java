package com.newland.paas.sbcommon.activiti;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.activiti.vo.IDMembershipVo;
import com.newland.paas.sbcommon.activiti.vo.ExecTaskPropertie;
import com.newland.paas.sbcommon.activiti.vo.ExecTaskVo;
import com.newland.paas.sbcommon.activiti.vo.ProcessInstancesVariableHistory;
import com.newland.paas.sbcommon.activiti.vo.ProcessInstancesVariables;
import com.newland.paas.sbcommon.activiti.vo.StartProcessVariable;
import com.newland.paas.sbcommon.activiti.vo.StartProcessVo;
import com.newland.paas.sbcommon.activiti.vo.TaskRespVo;
import com.newland.paas.sbcommon.activiti.vo.VariableHistory;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单引擎
 *
 * @author WRP
 * @since 2019/7/1
 */
@Component
public class ActEngine {

    private static final Log LOG = LogFactory.getLogger(ActEngine.class);

    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    /**
     * 发起流程
     *
     * @param startProcessVo
     * @param tenantId
     * @param account
     * @return 流程实例ID
     */
    public Long startProcess(StartProcessVo startProcessVo, Long tenantId, String account) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "startProcess,startProcessVo:" + JSON.toJSONString(startProcessVo)
                + ",tenantId:" + tenantId + ",account:" + account);
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        Map<String, Object> responseContent = restTemplateUtils.postForTokenEntity(
                microservicesProperties.getActivitiFlow(), StartProcessVo.INTERFACE_URL, actToken,
                startProcessVo, new ParameterizedTypeReference<Map<String, Object>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "startProcess,responseContent:" + JSON.toJSONString(responseContent));
        return Long.valueOf(responseContent.get("id").toString());
    }

    /**
     * 发起流程并执行流程草拟
     *
     * @param startProcessVo
     * @param tenantId
     * @param account
     * @return 流程实例ID
     */
    public Long startProcessAndExecProcessDraft(StartProcessVo startProcessVo, Long tenantId, String account) {
        Long processInstanceId = startProcess(startProcessVo, tenantId, account);
        execNextTask(processInstanceId, null, tenantId, account);
        return processInstanceId;
    }

    /**
     * 修改流程
     *
     * @param processInstanceId
     * @param variables
     * @param tenantId
     * @param account
     */
    public void updateProcess(Long processInstanceId, List<StartProcessVariable> variables,
                              Long tenantId, String account) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "updateProcess,processInstanceId:" + processInstanceId
                + ",variables:" + JSON.toJSONString(variables) + ",tenantId:" + tenantId + ",account:" + account);
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        restTemplateUtils.putForToken(microservicesProperties.getActivitiFlow(),
                "runtime/process-instances/" + processInstanceId + "/variables", actToken, variables,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });
    }

    /**
     * 修改流程并执行流程草拟
     *
     * @param processInstanceId
     * @param variables
     * @param tenantId
     * @param account
     */
    public void updateProcessAndExecProcessDraft(Long processInstanceId, List<StartProcessVariable> variables,
                                                 Long tenantId, String account) {
        updateProcess(processInstanceId, variables, tenantId, account);
        execNextTask(processInstanceId, null, tenantId, account);
    }

    /**
     * 执行任务
     *
     * @param execTaskVo
     * @param tenantId
     * @param account
     * @return 流程实例ID
     */
    public void execTask(ExecTaskVo execTaskVo, Long tenantId, String account) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "execTask,execTaskVo:" + JSON.toJSONString(execTaskVo)
                + ",tenantId:" + tenantId + ",account:" + account);

        String assignee = ActTokenUtil.getActAccount(tenantId, account);
        String actToken = ActTokenUtil.buildActToken(tenantId, account);

        // 判断任务处理人是否不是租户隔离用户，是：账号不加租户ID
        String isNotTenantAccountUrl = MessageFormat.format("v1/task/{0}/isNotTenantAccountApprove",
                execTaskVo.getTaskId().toString());
        BasicResponseContentVo<Boolean> resp = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getActivitiFlow(), isNotTenantAccountUrl, actToken, new HashMap<>(),
                new ParameterizedTypeReference<BasicResponseContentVo<Boolean>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "isNotTenantAccountApprove,resp:" + JSON.toJSONString(resp));
        if (resp != null && resp.getContent()) {
            actToken = ActTokenUtil.buildActToken(null, account);
            assignee = ActTokenUtil.getActAccount(null, account);
        }

        String url = MessageFormat.format(ExecTaskVo.CLAIM_URL, String.valueOf(execTaskVo.getTaskId()));
        Map<String, Object> claimParams = new HashMap<>();
        claimParams.put("action", "claim");
        claimParams.put("assignee", assignee);
        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<Map<String, Object>>() {
                };
        // 认领任务
        Map<String, Object> responseContent = restTemplateUtils.postForTokenEntity(
                microservicesProperties.getActivitiFlow(),
                url, actToken, claimParams, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "execTask,claim|responseContent:" + JSON.toJSONString(responseContent));

        // 审批
        responseContent = restTemplateUtils.postForTokenEntityActiviti(
                microservicesProperties.getActivitiFlow(), ExecTaskVo.APPROVAL_URL,
                actToken, ActTokenUtil.getToken(), execTaskVo, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "execTask,approval|responseContent:" + JSON.toJSONString(responseContent));
    }

    /**
     * 作废流程
     *
     * @param processInstanceId
     * @param tenantId
     * @param account
     */
    public void obsoleteProcess(Long processInstanceId, Long tenantId, String account) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "obsoleteProcess,processInstanceId:" + processInstanceId
                + ",tenantId:" + tenantId + ",account:" + account);
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        restTemplateUtils.deleteForTokenEntity(microservicesProperties.getActivitiFlow(),
                "v1/task/withdrawTask?processInstanceId=" + processInstanceId, actToken,
                new HashMap<>(), new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    /**
     * 撤回任务
     *
     * @param processInstanceId
     * @param tenantId
     * @param account
     */
    public void withdrawTask(Long processInstanceId, Long tenantId, String account) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "withdrawTask,processInstanceId:" + processInstanceId
                + ",tenantId:" + tenantId + ",account:" + account);
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(),
                "v1/task/rollBackToAssignWorkFlow?processInstanceId=" + processInstanceId, actToken,
                new HashMap<>(), new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    /**
     * 获取流程实例变量
     *
     * @param processInstanceId
     * @param variableName
     * @param tenantId
     * @param account
     * @param
     * @return
     */
    public ProcessInstancesVariables getProcessInstanceVariables(Long processInstanceId, String variableName,
                                                                 Long tenantId, String account) {
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        String url = MessageFormat.format(ProcessInstancesVariables.INTERFACE_URL,
                String.valueOf(processInstanceId), variableName);
        ProcessInstancesVariables responseContent = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getActivitiFlow(), url, actToken, new HashMap<>(),
                new ParameterizedTypeReference<ProcessInstancesVariables>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "responseContent:" + JSON.toJSONString(responseContent));
        return responseContent;
    }

    /**
     * 获取历史流程实例变量
     *
     * @param processInstanceId
     * @param variableName
     * @param tenantId
     * @param account
     * @return
     */
    public String getProcessInstanceVariableHistory(Long processInstanceId, String variableName,
                                                    Long tenantId, String account) {
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        String url = "history/historic-variable-instances?processInstanceId="
                + processInstanceId + "&variableName=" + variableName;
        BasicResponseContentVo<ProcessInstancesVariableHistory> responseContent = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getActivitiFlow(), url, actToken, new HashMap<>(),
                new ParameterizedTypeReference<BasicResponseContentVo<ProcessInstancesVariableHistory>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "responseContent:" + JSON.toJSONString(responseContent));
        String value = "";
        if (responseContent.getContent().getData() != null) {
            for (VariableHistory variableHistory : responseContent.getContent().getData()) {
                value = String.valueOf(variableHistory.getVariable().getValue());
            }
        }
        return value;
    }

    /**
     * 自审批
     *
     * @param processInstanceId
     * @param candidateKey
     * @param properties
     * @param tenantId
     * @param account
     */
    public boolean ownApprove(Long processInstanceId, String candidateKey, List<ExecTaskPropertie> properties,
                              Long tenantId, String account, ActCallBack actCallBack) {

        LOG.info(LogProperty.LOGTYPE_DETAIL, "ownApprove,processInstanceId:" + processInstanceId
                + ",candidateKey:" + candidateKey + ",properties:" + JSON.toJSONString(properties)
                + ",tenantId:" + tenantId + ",account:" + account);
        boolean isOwn = isOwnApprove(processInstanceId, candidateKey, tenantId, account);
        if (isOwn) {
            LOG.info(LogProperty.LOGTYPE_DETAIL, "ownApprove,是自审批，开始执行审批动作！");
            // 先执行回调方法
            if (actCallBack != null) {
                actCallBack.execute();
            }
            // 执行审批工单
            execNextTask(processInstanceId, properties, tenantId, account);
        }
        return isOwn;
    }

    /**
     * 是否自审批
     *
     * @param processInstanceId
     * @param candidateKey
     * @param tenantId
     * @param account
     * @return
     */
    public boolean isOwnApprove(Long processInstanceId, String candidateKey, Long tenantId, String account) {

        LOG.info(LogProperty.LOGTYPE_DETAIL, "isOwnApprove,processInstanceId:" + processInstanceId
                + ",candidateKey:" + candidateKey + ",tenantId:" + tenantId + ",account:" + account);
        if (StringUtils.isEmpty(candidateKey)) {
            return false;
        }
        ProcessInstancesVariables candidates = getProcessInstanceVariables(processInstanceId,
                candidateKey, tenantId, account);
        if (candidates == null || StringUtils.isEmpty(candidates.getValue())) {
            return false;
        }
        // 审批人是否为自己
        if (candidates.equals(ActTokenUtil.getActAccount(tenantId, account)) ||
                candidates.equals(ActTokenUtil.getActAccount(null, account))) {
            return true;
        } else {
            // 自己是否包含在审批组
            String actToken = ActTokenUtil.buildActToken(tenantId, account);
            BasicRequestContentVo<IDMembershipVo> inputVo = new BasicRequestContentVo<>();
            inputVo.setParams(new IDMembershipVo(ActTokenUtil.getActAccount(tenantId, account), candidates.getValue().toString()));
            BasicResponseContentVo<Boolean> resp = restTemplateUtils.postForTokenEntity(
                    microservicesProperties.getActivitiFlow(), "v1/id/isOwn", actToken, inputVo,
                    new ParameterizedTypeReference<BasicResponseContentVo<Boolean>>() {
                    });
            LOG.info(LogProperty.LOGTYPE_DETAIL, "isOwnApprove,resp:" + JSON.toJSONString(resp));
            if (resp != null && resp.getContent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 自执行，跨租户强制执行
     *
     * @param processInstanceId
     * @param candidateKey
     * @param properties
     * @param tenantId
     * @param account
     * @param actCallBack
     */
    public void selfExec(Long processInstanceId, String candidateKey, List<ExecTaskPropertie> properties,
                         Long tenantId, String account, ActCallBack actCallBack) {

        LOG.info(LogProperty.LOGTYPE_DETAIL, "selfExec,processInstanceId:" + processInstanceId
                + ",candidateKey:" + candidateKey + ",properties:" + JSON.toJSONString(properties)
                + ",tenantId:" + tenantId + ",account:" + account);

        ProcessInstancesVariables candidates = getProcessInstanceVariables(processInstanceId,
                candidateKey, tenantId, account);
        // 查不到处理人
        if (candidates == null || StringUtils.isEmpty(candidates.getValue())) {
            throw new SystemException(new PaasError("no_found_candidateUser", "查不到处理人！"));
        }
        // 先执行回调方法
        if (actCallBack != null) {
            actCallBack.execute();
        }
        // 执行审批工单
        execNextTask(processInstanceId, properties, null, candidates.getValue().toString());
    }

    /**
     * 执行流程的下一个任务（自审批调用此方法）
     *
     * @param processInstanceId
     * @param properties
     * @param tenantId
     * @param account
     */
    public void execNextTask(Long processInstanceId, List<ExecTaskPropertie> properties,
                             Long tenantId, String account) {
        String actToken = ActTokenUtil.buildActToken(tenantId, account);
        String url = MessageFormat.format(TaskRespVo.INTERFACE_URL, String.valueOf(processInstanceId));
        BasicResponseContentVo<List<TaskRespVo>> tasks = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getActivitiFlow(), url, actToken,
                new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<List<TaskRespVo>>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "execNextTask,tasks" + JSON.toJSONString(tasks));
        if (tasks != null && !CollectionUtils.isEmpty(tasks.getContent())) {
            TaskRespVo task = tasks.getContent().get(tasks.getContent().size() - 1);
            Long taskId = Long.valueOf(task.getTaskId());
            ExecTaskVo execTaskVo = new ExecTaskVo();
            execTaskVo.setTaskId(taskId);
            execTaskVo.addVariables(ExecTaskPropertie.APPROVE, ExecTaskPropertie.APPROVE_TRUE);
            execTaskVo.addVariables(ExecTaskPropertie.IS_AGREE, ExecTaskPropertie.IS_AGREE_TRUE);
            execTaskVo.addVariables(ExecTaskPropertie.REMARK, "确认");
            if (!CollectionUtils.isEmpty(properties)) {
                properties.forEach(v -> execTaskVo.addVariables(v));
            }
            execTask(execTaskVo, tenantId, account);
        }
    }

    /**
     * 获取运营租户的根工组
     *
     * @return
     */
    public String getYyTenantRootGroup() {
        BasicResponseContentVo<String> resp = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getSysmgr(), "v1/groupMgr/yyTenantRootGroup", ActTokenUtil.getToken(),
                new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "getYyTenantRootGroup,resp:" + JSON.toJSONString(resp));
        return resp.getContent();
    }

    /**
     * 获取运维租户的根工组
     *
     * @return
     */
    public String getYwTenantRootGroup() {
        BasicResponseContentVo<String> resp = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getSysmgr(), "v1/groupMgr/ywTenantRootGroup", ActTokenUtil.getToken(),
                new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "ywTenantRootGroup,resp:" + JSON.toJSONString(resp));
        return resp.getContent();
    }

    /**
     * 获取当前用户的租户的根工组
     *
     * @return
     */
    public String getTenantRootGroup() {
        BasicResponseContentVo<String> resp = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getSysmgr(), "v1/groupMgr/tenantRootGroup", ActTokenUtil.getToken(),
                new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "getTenantRootGroup,resp:" + JSON.toJSONString(resp));
        return resp.getContent();
    }

    /**
     * 获取当前租户用户的归属工组的父工组
     *
     * @return
     */
    public String getTenantParentGroup() {
        BasicResponseContentVo<String> resp = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getSysmgr(), "v1/groupMgr/tenantParentGroup", ActTokenUtil.getToken(),
                new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });
        LOG.info(LogProperty.LOGTYPE_DETAIL, "getTenantParentGroup,resp:" + JSON.toJSONString(resp));
        return resp.getContent();
    }

}
