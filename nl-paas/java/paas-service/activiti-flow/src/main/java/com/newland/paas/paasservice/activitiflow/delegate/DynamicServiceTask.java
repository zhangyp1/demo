package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.PaasError;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * 每一步step执行的内容
 * @author sunxm
 */
public class DynamicServiceTask implements JavaDelegate {
	private static final Log LOG = LogFactory.getLogger(DynamicServiceTask.class);

	private static final String STR_LINK = "_";

	private static final int STRINDEX = -1;

	private static final String EXCEPTIOINSKIP = "exceptionskip";

	private static final String ISWAIT = "isWait";

	/**
	 * 1.获取task的documentation
	 * 2.解析documentation中的appCode、execOrder
	 * 3.调用接口
	 * 4.根据返回值判断是否进入异常流程
	 * 5.异常处理
	 * @param execution DelegateExecution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
		Object skip = execution.getVariable("skip");
		ServiceTask serviceTask = (ServiceTask) execution.getCurrentFlowElement();
		String formKey = serviceTask.getDocumentation(); //documentation中配置的参数
		if (formKey == null) {
			throw new SystemException(new PaasError("DSKERROR_NULL", "参数错误！"));
		}
		String procDefId = execution.getProcessDefinitionId();
		if (skip != null && (boolean)skip) {
			variables.put("skip", true);
			variables.put(EXCEPTIOINSKIP, false);
			execution.setVariables(variables);
		} else { //解析参数
			int iStart = formKey.indexOf(STR_LINK);
			int iEnd = formKey.lastIndexOf(STR_LINK);
			if (iStart != STRINDEX && iEnd > iStart) {
				String appCode = formKey.substring(iStart + 1, iEnd); //应用code
				String execOrder = formKey.substring(iEnd + 1); //step执行顺序
				String fromActId = execution.getCurrentActivityId(); //获取当前activityID
				String nextActivityId = getNextActId(procDefId, execOrder); //获取下一步activityID
				Map<String, Object> reqParams = new HashMap<>(); //调用方案生产接口
				reqParams.put("appCode", appCode);
				reqParams.put("execOrder", execOrder);
				reqParams.put("processInstanceId", execution.getProcessInstanceId());
				reqParams.put("currentActivityId", execution.getCurrentActivityId());
				//获取返回值为true则进入到receiveTask等待，false则进入异常流程
				Map<String, Object> result = getExecResult(reqParams);
				boolean execResult = (boolean)result.get("execResult");
				boolean isWait = (boolean)result.get(ISWAIT);
				variables.put("fromActId", fromActId);
				variables.put("nextActivityId", nextActivityId);
				if (execResult) {
					variables.put("skip", false);
					variables.put(ISWAIT, isWait);
					variables.put(EXCEPTIOINSKIP, true);
					execution.setVariables(variables);
				} else { //进入异常
					variables.put(ISWAIT, false);
					variables.put("skip", true);
					variables.put(EXCEPTIOINSKIP, false);
					execution.setVariables(variables);
					LOG.info("进入异常处理");
				}
			}
		}
	}

	/**
	 * 获取下一步activityId
	 * @param processDefinitionId 流程实例ID
	 * @param execOrder 执行顺序
	 * @return 下一步activityId
	 */
	private String getNextActId(String processDefinitionId, String execOrder) {
		String nextActivityId = "";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processList = bpmnModel.getProcesses();
		for (Process process : processList) {
			for (FlowElement flowElement : process.getFlowElements()) {
				String stepId = flowElement.getId();
				if (StringUtils.isNotEmpty(stepId)) {
					nextActivityId = getNextActIdImpl(stepId, execOrder);
				}
			}
		}
		return nextActivityId;
	}

	/**
	 *
	 * @param stepId 步骤ID
	 * @param execOrder 执行顺序
	 * @return 下一步ID
	 */
	private String getNextActIdImpl(String stepId, String execOrder) {
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
	 * 查询执行结果
	 * @param reqParams 入参
	 * @return 执行结果
	 */
	Map<String, Object> getExecResult(Map<String, Object> reqParams) {
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>(){};
		MicroservicesItemProperties env = microservicesProperties.getDeploymentPlan();
		String token = ActivitiTokenUtils.getPaasToken();
		String url = "/v1/plan/product/activiti/flowCallBack";
		Object responseContent = restTemplateUtils.postForTokenEntity(env, url, token, reqParams, responseType);
		//获取返回值为true则进入到receiveTask等待，false则进入异常流程
		return (Map<String, Object>) ((LinkedHashMap) responseContent).get("content");
	}

}
