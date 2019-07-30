package com.newland.paas.sbcommon.activitiflow.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.activiti.vo.TaskListVo;
import com.newland.paas.sbcommon.activitiflow.ActivitiflowService;
import com.newland.paas.sbcommon.activitiflow.Utils.ActivitiUtils;
import com.newland.paas.sbcommon.activitiflow.errorcode.ActivtiErrorCode;
import com.newland.paas.sbcommon.activitiflow.vo.ActivitiRunProcessInput;
import com.newland.paas.sbcommon.activitiflow.vo.ActivitiTaskExecuteInput;
import com.newland.paas.sbcommon.activitiflow.vo.ActivitiTaskStartOutput;
import com.newland.paas.sbcommon.activitiflow.vo.ActivitiVariable;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;

@Service
public class ActivitiflowServiceImpl implements ActivitiflowService {
	private static final Log LOG = LogFactory.getLogger(ActivitiflowServiceImpl.class);
	@Autowired
	private RestTemplateUtils restTemplateUtils;
	@Autowired
	private MicroservicesProperties microservicesProperties;
	  // 异常消息
    public static final String ACTIVTI_ERROR_CODE = "9999";
	  // 异常消息
    public static final String ACTIVTI_ERROR_VALUE = "发起流程失败";
    // 异常消息
    public static final String ACTIVTI_GETGROUP_VALUE = "获取groupId失败";

    
	@Override
	public String startRunProcess(String processDefinitionKey, String activitiName, List<ActivitiVariable> variables,
			String account,String tenantId) throws ApplicationException {
	    String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
		// 赋值发起流程输入参数
		ActivitiRunProcessInput activitiRunProcessInput = new ActivitiRunProcessInput();
		// 提供申请工作流
		activitiRunProcessInput.setProcessDefinitionKey(processDefinitionKey);
		activitiRunProcessInput.setBusinessKey(activitiName);
		// 变量
		activitiRunProcessInput.setVariables(variables);
		// 发起流程
		String processInstanceId = "";
		try {
			ActivitiTaskStartOutput response = runProcess(activitiRunProcessInput, activitiToken);
			// 流程实例ID(非空：启动成功)
			processInstanceId = response.getId();
		} catch (Exception e) {
			LOG.errorLog(LogProperty.LOGTYPE_DETAIL, ACTIVTI_ERROR_CODE, e.getMessage(), ACTIVTI_ERROR_VALUE);
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_RUN_PROCESS_FAIL);
		}
		if ("".equals(processInstanceId)) {
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_RUN_PROCESS_FAIL);
		}
		LOG.info(LogProperty.LOGTYPE_DETAIL, "============流程实例ID:" + processInstanceId);
		return processInstanceId;
	}

	@Override
	public void taskExecuteProcessId(String processInstanceId, Map<String, String> parameter,
			String account,String tenantId) throws ApplicationException {
		 String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
		// 通过processInstanceId查询->TaskList任务列表
		BasicResponseContentVo<List<TaskListVo>> taskListVo = restTemplateUtils.getForTokenEntity(
				microservicesProperties.getActivitiFlow(), "/v1/task/getByInstanceId/" + processInstanceId,
				activitiToken, new HashMap<>(),
				new ParameterizedTypeReference<BasicResponseContentVo<List<TaskListVo>>>() {
				});
		String taskId = "0";
		if (taskListVo != null) {
			// 获取最后一次任务信息最后一次taskId最大的为未处理的任务
			List<TaskListVo> taskLists = taskListVo.getContent();
			for (TaskListVo taskList : taskLists) {
				if (Integer.parseInt(taskId) < Integer.parseInt(taskList.getTaskId())) {
					taskId = taskList.getTaskId();
				}
			}
		} else {
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_TASKLIST_FAIL);
		}
		// 如果获取到taskId空直接抛出异常
		if ("0".equals(taskId)) {
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_TASKLIST_FAIL);
		}
		this.taskExecuteTaskId(taskId, parameter, account,tenantId);
	}

	@Override
	public void taskExecuteTaskId(String taskId, Map<String, String> parameter,String account,String tenantId) {
		String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
		// 任务认领
		taskClaim(taskId, activitiToken,account,tenantId);
		ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
		};
		ActivitiTaskExecuteInput activitiTaskExecuteInput = new ActivitiTaskExecuteInput();
		activitiTaskExecuteInput.setTaskId(taskId);
		List<Map<String, String>> properties = new ArrayList<>();
		// 获取parameter值 存到List<Map<String, String>中
		Set<String> keys = parameter.keySet();
		for (String key : keys) {
			Map<String, String> propertie = new HashMap<>();
			propertie.put("id", key);
			propertie.put("value", parameter.get(key));
			properties.add(propertie);
		}
		activitiTaskExecuteInput.setProperties(properties);
		LOG.info(LogProperty.LOGTYPE_CALL, "activiti流程处理:", JSONObject.toJSONString(activitiTaskExecuteInput));
		// 提交form/form-data 执行任务
		restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(), "form/form-data", activitiToken,
				activitiTaskExecuteInput, responseType);
	}

	/**
	 * 任务认领
	 * 
	 * @param taskId
	 *            任务id
	 * @param reqToken
	 *            请求token
	 */
	public void taskClaim(String taskId, String activitiToken, String account,String tenantId) {
		String account_tenantId=account+"_"+tenantId;
		Map<String, String> map = new LinkedHashMap<>();
		map.put("action", "claim");
		map.put("assignee", account_tenantId);
		LOG.info(LogProperty.LOGTYPE_CALL, "activiti任务认领taskId[" + taskId + "]:", JSONObject.toJSONString(map));
		ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
		};
		restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(), "runtime/tasks/" + taskId,
				activitiToken, map, responseType);
	}
	

	@Override
	public void getTaskClaim(String taskId, String account,String tenantId) {
		String account_tenantId=account+"_"+tenantId;
		String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
		Map<String, String> map = new LinkedHashMap<>();
		map.put("action", "claim");
		map.put("assignee", account_tenantId);
		LOG.info(LogProperty.LOGTYPE_CALL, "activiti任务认领taskId[" + taskId + "]:", JSONObject.toJSONString(map));
		ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>() {
		};
		restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(), "runtime/tasks/" + taskId,
				activitiToken, map, responseType);
	}

	/**
	 * @描述: 发起流程
	 * @param activitiRunProcessInput
	 * @param activitiToken
	 * @return
	 * @创建人：zyp @创建时间：2019/06/07
	 */
	public ActivitiTaskStartOutput runProcess(ActivitiRunProcessInput activitiRunProcessInput, String activitiToken) {
		ParameterizedTypeReference<ActivitiTaskStartOutput> responseType = new ParameterizedTypeReference<ActivitiTaskStartOutput>() {
		};
		LOG.info(LogProperty.LOGTYPE_CALL, "============发起流程开始:", JSONObject.toJSONString(activitiRunProcessInput));
		ActivitiTaskStartOutput response = restTemplateUtils.postForTokenEntity(
				microservicesProperties.getActivitiFlow(), "runtime/process-instances", activitiToken,
				activitiRunProcessInput, responseType);
		LOG.info(LogProperty.LOGTYPE_CALL, "============发起流程结束:", JSONObject.toJSONString(response));
		return response;
	}
	
	@Override
	public String getGroupId(String passToken) throws ApplicationException {
		String groupId = "";
		try {
			BasicResponseContentVo<String> resp = this.restTemplateUtils.getForTokenEntity(
					microservicesProperties.getSysmgr(), "/v1/groupMgr/tenantRootGroup", passToken, new HashMap<>(),
					new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
					});
			groupId = resp.getContent();
		} catch (Exception e) {
			LOG.errorLog(LogProperty.LOGTYPE_DETAIL, ACTIVTI_ERROR_CODE, e.getMessage(), ACTIVTI_GETGROUP_VALUE);
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_GROUP_FAIL);
		}
		if ("".equals(groupId)) {
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_GROUP_FAIL);
		}
		// 获取运营的根工组管理员
		return groupId;
	}

	@Override
	public String getYYGroupId(String passToken) throws ApplicationException {
		String groupId = "";
		try {
			BasicResponseContentVo<String> resp = this.restTemplateUtils.getForTokenEntity(
					microservicesProperties.getSysmgr(), "/v1/groupMgr/yyTenantRootGroup", passToken, new HashMap<>(),
					new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
					});
			groupId = resp.getContent();
		} catch (Exception e) {
			LOG.errorLog(LogProperty.LOGTYPE_DETAIL, ACTIVTI_ERROR_CODE, e.getMessage(), ACTIVTI_GETGROUP_VALUE);
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_GROUP_FAIL);
		}
		if ("".equals(groupId)) {
			throw new ApplicationException(ActivtiErrorCode.ACTIVTI_GET_GROUP_FAIL);
		}
		// 获取运营的根工组管理员
		return groupId;
	}

	@Override
	public void rollBackToAssignWorkFlow(String processInstanceId, String account,String tenantId) throws ApplicationException {
		 ParameterizedTypeReference<Map<String, Object>> responseType =
		            new ParameterizedTypeReference<Map<String, Object>>() {};
		String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
	    restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(),
		            "v1/task/rollBackToAssignWorkFlow?processInstanceId=" + processInstanceId, activitiToken, new HashMap<>(),
		            responseType);
	}

	@Override
	public void rollBackToGroupWorkFlow(String processInstanceId, String account,String tenantId, String groupId)
			throws ApplicationException {
		 ParameterizedTypeReference<Map<String, Object>> responseType =
		            new ParameterizedTypeReference<Map<String, Object>>() {};
			String activitiToken = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
		    restTemplateUtils.postForTokenEntity(microservicesProperties.getActivitiFlow(),
			            "v1/task/rollBackToGroupIdWorkFlow?processInstanceId=" + processInstanceId+"&groupId="+groupId, activitiToken, new HashMap<>(),
			            responseType);
			
	}

	@Override
	public void deleteTaskWorkFlow(String processInstanceId, String account,String tenantId) throws ApplicationException {
		String token = ActivitiUtils.buildUserTenantIdActivitiToken(account,tenantId);
        // 流程作废
        ParameterizedTypeReference<Map<String, Object>> responseType =
            new ParameterizedTypeReference<Map<String, Object>>() {};
        restTemplateUtils.deleteForTokenEntity(microservicesProperties.getActivitiFlow(),
            "v1/task/withdrawTask?processInstanceId=" + processInstanceId, token, new HashMap<>(), responseType);
	}
	
	
}
