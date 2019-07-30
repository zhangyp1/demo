package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.sysmgr.common.ErrorCode;
import com.newland.paas.paasservice.sysmgr.service.ActivitiOperationsService;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiRunProcessInput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskExecuteInput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskOutput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskStartOutput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTasksObj;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTasksOutput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiVariable;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiVariableHistory;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiVariableHistoryOutput;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.service.impl.ActivitiOperationsServiceImpl
 * @Date 2018/8/7
 */
@Service
public class ActivitiOperationsServiceImpl implements ActivitiOperationsService {

    private static Log log = LogFactory.getLogger(ActivitiOperationsServiceImpl.class);

    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    private static final Integer HASHMAP_CAPACITY = 2;
    
    private static final String ACTIVITI_ERROR_MSG = "activiti流程处理:";
    
    private static final String INTERFACE_URL = "form/form-data";

    @Override
    public void taskExecute(String taskId) {
        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
        };
        ActivitiTaskExecuteInput activitiTaskExecuteInput = new ActivitiTaskExecuteInput();
        activitiTaskExecuteInput.setTaskId(taskId);
        log.info(LogProperty.LOGTYPE_CALL, ACTIVITI_ERROR_MSG, JSONObject.toJSONString(activitiTaskExecuteInput));
        restTemplateUtils.postForTokenEntityActiviti(microservicesProperties.getActivitiFlow(),
                INTERFACE_URL, reqToken, RequestContext.getToken(), activitiTaskExecuteInput, responseType);
    }

    @Override
    public void taskExecute(String taskId, String groupId) {
        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
        };
        ActivitiTaskExecuteInput activitiTaskExecuteInput = new ActivitiTaskExecuteInput();
        activitiTaskExecuteInput.setTaskId(taskId);
        if (StringUtils.isNotEmpty(groupId)) {
            List<Map<String, String>> properties = new ArrayList<>();
            Map<String, String> propertie = new HashMap<>();
            propertie.put("id", "groupId");
            propertie.put("value", groupId);
            properties.add(propertie);
            activitiTaskExecuteInput.setProperties(properties);
        }
        log.info(LogProperty.LOGTYPE_CALL, ACTIVITI_ERROR_MSG, JSONObject.toJSONString(activitiTaskExecuteInput));
        restTemplateUtils.postForTokenEntityActiviti(microservicesProperties.getActivitiFlow(),
                INTERFACE_URL, reqToken, RequestContext.getToken(), activitiTaskExecuteInput, responseType);
    }

    @Override
    public void taskExecute(String taskId, List<Map<String, String>> properties) {
        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        // 认领任务
        Map<String, Object> claimParams = new HashMap<>(HASHMAP_CAPACITY);
        claimParams.put("action", "claim");
        claimParams.put("assignee", RequestContext.getSession().getAccount());
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
        };
        log.info(LogProperty.LOGTYPE_CALL, "activiti认领任务:", taskId);
        restTemplateUtils.postForTokenEntityActiviti(microservicesProperties.getActivitiFlow(),
                "runtime/tasks" + "/" + taskId, reqToken, RequestContext.getToken(), claimParams,
                responseType);
        // 审批
        ActivitiTaskExecuteInput activitiTaskExecuteInput = new ActivitiTaskExecuteInput();
        activitiTaskExecuteInput.setTaskId(taskId);
        activitiTaskExecuteInput.setProperties(properties);
        log.info(LogProperty.LOGTYPE_CALL, ACTIVITI_ERROR_MSG, JSONObject.toJSONString(activitiTaskExecuteInput));
        restTemplateUtils.postForTokenEntityActiviti(microservicesProperties.getActivitiFlow(),
                INTERFACE_URL, reqToken, RequestContext.getToken(), activitiTaskExecuteInput, responseType);
    }

    @Override
    public ActivitiTaskStartOutput runProcess(ActivitiRunProcessInput activitiRunProcessInput) {
        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<ActivitiTaskStartOutput> responseType =
                new ParameterizedTypeReference<ActivitiTaskStartOutput>() {
        };
        log.info(LogProperty.LOGTYPE_CALL, "发起流程开始:", JSONObject.toJSONString(activitiRunProcessInput));
        ActivitiTaskStartOutput response = restTemplateUtils.postForTokenEntityActiviti(
                microservicesProperties.getActivitiFlow(), "runtime/process-instances",
                reqToken, RequestContext.getToken(), activitiRunProcessInput, responseType);
        log.info(LogProperty.LOGTYPE_CALL, "发起流程结束:", JSONObject.toJSONString(response));
        return response;
    }

    @Override
    public <T> T getProcessInstanceVariable(String processInstanceId, String key, Class<T> clazz) {

        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<ActivitiVariable> responseType = new ParameterizedTypeReference<ActivitiVariable>() {
        };
        String url = "runtime/process-instances/" + processInstanceId + "/variables/" + key;
        log.info(LogProperty.LOGTYPE_CALL, "获取流程实例变量:", url);
        ActivitiVariable response = restTemplateUtils.getForTokenEntity(microservicesProperties.getActivitiFlow(),
                url, reqToken, new HashMap<>(), responseType);
        log.info(LogProperty.LOGTYPE_CALL, "流程实例变量:", JSONObject.toJSONString(response));
        String value = String.valueOf(response.getValue());
        return JSONObject.parseObject(value, clazz);
    }

    //获取历史流程变量
    @Override
    public <T> T getProcessInstanceVariableHistory(String processInstanceId, String key, Class<T> clazz) {

        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<BasicResponseContentVo<ActivitiVariableHistoryOutput>> responseType =
                new ParameterizedTypeReference<BasicResponseContentVo<ActivitiVariableHistoryOutput>>() {
        };
        String url = "history/historic-variable-instances?processInstanceId="
                + processInstanceId + "&variableName=" + key;
        log.info(LogProperty.LOGTYPE_CALL, "获取流程实例变量:", url);
        BasicResponseContentVo<ActivitiVariableHistoryOutput> response = restTemplateUtils.getForTokenEntity(
                microservicesProperties.getActivitiFlow(), url, reqToken, new HashMap<>(), responseType);
        log.info(LogProperty.LOGTYPE_CALL, "流程实例变量:", JSONObject.toJSONString(response));
        String value = "";
        if (response.getContent().getData() != null) {
            for (ActivitiVariableHistory activitiVariableHistory : response.getContent().getData()) {
                value = String.valueOf(activitiVariableHistory.getVariable().getValue());
            }
        }
        return JSONObject.parseObject(value, clazz);

    }

    @Override
    public <T> T getTaskVariable(String taskId, String key, Class<T> clazz) {

        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<ActivitiVariable> responseType = new ParameterizedTypeReference<ActivitiVariable>() {
        };
        String url = "runtime/tasks/" + taskId + "/variables/" + key;
        log.info(LogProperty.LOGTYPE_CALL, "获取任务变量:", url);
        ActivitiVariable response = restTemplateUtils.getForTokenEntity(microservicesProperties.getActivitiFlow(),
                url, reqToken, new HashMap<>(), responseType);
        log.info(LogProperty.LOGTYPE_CALL, "任务变量:", JSONObject.toJSONString(response));
        String value = String.valueOf(response.getValue());
        return JSONObject.parseObject(value, clazz);
    }

    /**
     * 查询当前任务列表
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public String getTaskId(String processInstanceId) throws ApplicationException {
        if (processInstanceId == null) {
            throw new ApplicationException(new PaasError(ErrorCode.ERR_CODE_PARAM, ErrorCode.ERR_MSG_PARAM));
        }
        if (log.isDebugEnabled()) {
            log.debug(LogProperty.LOGCONFIG_DEALID, "processInstanceId=" + processInstanceId);
        }
        try {
            Map<String, Object> params = new HashMap<>();
            String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
            ParameterizedTypeReference<ActivitiTasksOutput> responseType =
                    new ParameterizedTypeReference<ActivitiTasksOutput>() {
            };
            String url = "runtime/tasks?processInstanceId=" + processInstanceId;
            log.info(LogProperty.LOGTYPE_CALL, "查询当前任务列表:", url);
            ActivitiTasksOutput response = restTemplateUtils.getForTokenEntity(
                    microservicesProperties.getActivitiFlow(), url, reqToken, params, responseType);
            log.info(LogProperty.LOGTYPE_CALL, "当前任务列表:", JSONObject.toJSONString(response));
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new ApplicationException(new PaasError(ErrorCode.ERR_CODE_SVR_TASKID_NOT_FOUND_ERROR,
                        ErrorCode.ERR_MSG_SVR_TASKID_NOT_FOUND_ERROR));
            }

            ActivitiTasksObj obj = response.getData().get(0);

            return obj.getId();
        } catch (Exception e) {
            throw new ApplicationException(new PaasError(ErrorCode.ERR_CODE_SVR_TASKID_NOT_FOUND_ERROR,
                    ErrorCode.ERR_MSG_SVR_TASKID_NOT_FOUND_ERROR));
        }
    }

    @Override
    public ActivitiTaskOutput getTask(String taskId) {
        String reqToken = ActivitiTokenUtils.buildUserActivitiToken(RequestContext.getSession().getAccount());
        ParameterizedTypeReference<ActivitiTaskOutput> responseType =
                new ParameterizedTypeReference<ActivitiTaskOutput>() {
        };
        String url = "runtime/tasks/" + taskId;
        log.info(LogProperty.LOGTYPE_CALL, "获取任务信息:", url);
        ActivitiTaskOutput response = restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(),
                url, reqToken, null, responseType);
        log.info(LogProperty.LOGTYPE_CALL, "任务信息:", JSONObject.toJSONString(response));
        return response;
    }
}
