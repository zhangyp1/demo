package com.newland.paas.paasservice.activitiflow.delegate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.common.util.DateUtils;
import com.newland.paas.common.util.FileUtil;
import com.newland.paas.common.util.SFTPUtil;
import com.newland.paas.paasservice.activitiflow.enums.AppActionStepType;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.paasservice.controllerapi.activiti.enums.AntvNodeType;
import com.newland.paas.paasservice.controllerapi.activiti.vo.AntvEdge;
import com.newland.paas.paasservice.controllerapi.activiti.vo.AntvNode;
import com.newland.paas.paasservice.controllerapi.activiti.vo.AntvProcess;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventDefinition;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.IOParameter;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.ManualTask;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.TimerEventDefinition;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import org.springframework.core.ParameterizedTypeReference;

/**
 * 1.获取当前流程，查询流程实例参数
 * 2.获取工作项信息、planInfo、appInfo、ftpInfo
 * 3.根据应用信息创建主流程
 * 4.根据应用信息获取stepInfo
 * 5.创建每个应用的step串行子流程
 * 6.将子流程嵌入主流程
 * 7.将流程文件保存至ftp
 *
 * @author sunxm
 * */
public class DynamicCreateDeployment implements JavaDelegate {

	private static final Log LOG = LogFactory.getLogger(DynamicCreateDeployment.class);
	private static final String CONTENT = "content";
	private static final String PROC_DEV = "ProcessDevelopment_";
	private static final String PLAN_CODE = "planCode";
	private static final String START = "start";
	private static final String EXEC_ORDER = "execOrder";
	private static final String PLAN_EXEC = "PlanExec_";
	private static final String EXECEPTION = "exception_";
	private static final String EXPRESSION = "${skip == true}";
	private static final String PROC_NAME = "方案创建流程";

	/**
	 * 获取工作项列表
	 * @param planId 方案ID
	 * @return 工作项列表
	 */
	private AntvProcess getAntvProcess(Long planId) {
		//获取工作项有序列表
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		ParameterizedTypeReference<BasicResponseContentVo<AntvProcess>> responseType
				= new ParameterizedTypeReference<BasicResponseContentVo<AntvProcess>>(){};
		MicroservicesItemProperties deploy = microservicesProperties.getDeploymentPlan();
		BasicResponseContentVo<AntvProcess> resp = restTemplateUtils.getForTokenEntity(deploy,
				"v1/plan/getAntvProcessByPlanId/" + planId,
				ActivitiTokenUtils.getPaasToken(), new HashMap<>(), responseType);
		return resp.getContent();
	}

	/**
	 * 查询方案信息
	 * @param planId 方案ID
	 * @return 方案信息
	 */
	private Map<String, String> getPlanInfoMap(Long planId) {
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		MicroservicesItemProperties deploy = microservicesProperties.getDeploymentPlan();
		ParameterizedTypeReference<Object> responsePlanInfoType = new ParameterizedTypeReference<Object>(){};
		Object responsePlanInfoContent = restTemplateUtils.getForTokenEntity(deploy,
				"v1/bussforact/getPlanInfo/" + planId,
				ActivitiTokenUtils.getPaasToken(), new HashMap<>(), responsePlanInfoType);
		HashMap<String, Object> respMap = cast(responsePlanInfoContent);
		return cast(respMap.get(CONTENT));
	}

	/**
	 * 查询FTP信息
	 * @return FTP信息Map
	 */
	private Map<String, String> getFtpMap() {
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		MicroservicesItemProperties deploy = microservicesProperties.getDeploymentPlan();
		ParameterizedTypeReference<Object> responseFtpInfoType = new ParameterizedTypeReference<Object>(){};
		Object responseFtpInfoContent = restTemplateUtils.getForTokenEntity(deploy,
				"v1/bussforact/getFtpInfo",
				ActivitiTokenUtils.getPaasToken(), new HashMap<>(), responseFtpInfoType);
		HashMap<String, Object> respFtpMap = cast(responseFtpInfoContent);
		return cast(respFtpMap.get(CONTENT));
	}

	/**
	 * 查询步骤列表
	 * @param appId 应用ID
	 * @return 步骤列表
	 */
	private List<LinkedHashMap<String, String>> getAppStepList(Long appId) {
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		MicroservicesItemProperties deploy = microservicesProperties.getDeploymentPlan();
		ParameterizedTypeReference<Object> responseAppStep
				= new ParameterizedTypeReference<Object>(){};
		Object responseAppStepContent = restTemplateUtils.getForTokenEntity(deploy,
				"v1/bussforact/getAppStepList/" + appId,
				ActivitiTokenUtils.getPaasToken(), new HashMap<>(), responseAppStep);
        LinkedHashMap<String, Object> response = cast(responseAppStepContent);
		return cast(response.get(CONTENT));
	}

	@Override
	public void execute(DelegateExecution execution) {
		LOG.info("========顺序编排完成，动态创建流程开始====");
		Long planId = (Long) execution.getVariable("planId"); //获取当前流程参数planId
		String sendTaskGroupCode = String.valueOf(execution.getVariable("sendTaskGroupCode"));
		Map<String, String> planInfoMap = getPlanInfoMap(planId); //查询方案信息
		Map<String, String> ftpMap = getFtpMap(); //查询FTP信息
		BpmnModel model = new BpmnModel(); //创建主流程
		Process process = new Process();
		model.addProcess(process);
		process.setId(PROC_DEV + planInfoMap.get(PLAN_CODE));
		process.setName(PROC_NAME);
		AntvProcess antvProcess = getAntvProcess(planId);
		for (AntvNode node : antvProcess.getNodes()) {
			if (AntvNodeType.START.getIndex() == node.getNodeType()) {
				process.addFlowElement(createStartEvent(node.getId()));
			} else if (AntvNodeType.END.getIndex() == node.getNodeType()) {
				process.addFlowElement(createEndEvent(node.getId()));
			} else if (AntvNodeType.GATEWAY.getIndex() == node.getNodeType()) {
				process.addFlowElement(createParallelGateway(node.getId()));
			} else {
                //获取step信息生成串行子流程
                List<LinkedHashMap<String, String>> appStepList = getAppStepList(node.getAppId());
                String appProcessId = createChildProcess(appStepList, node,
                        planInfoMap, ftpMap, sendTaskGroupCode); //循环step生成串行子流程
                String businessKey = PLAN_EXEC + node.getAppName();
                CallActivity callA = createCallActivity(node.getId(), node.getAppName(),
                        appProcessId, node.getAppCode(), businessKey);
                process.addFlowElement(callA);
			}
		}
        for (AntvEdge edge : antvProcess.getEdges()) {
            process.addFlowElement(createSequenceFlow(edge.getSource(), edge.getTarget()));
        }
		String url = planInfoMap.get("tenantCode") + "/" + planInfoMap.get("projectCode")
				+ "/" + planInfoMap.get(PLAN_CODE)	+ "/applications/dynflow/";
		saveFile(ftpMap, url, PROC_DEV + planInfoMap.get(PLAN_CODE), model);
	}

	/**
	 * 动态创建子流程
	 * @param appStepList 步骤列表
	 * @param node 应用信息
	 * @param planInfoMap 方案信息
	 * @param ftpMap ftp信息
	 * @param sendTaskGroupCode 任务发起人工组code
	 * @return 子流程ID
	 */
	private String createChildProcess(List<LinkedHashMap<String, String>> appStepList,
                              AntvNode node,
							  Map<String, String> planInfoMap,
							  Map<String, String> ftpMap,
							  String sendTaskGroupCode) {
		BpmnModel model = new BpmnModel();
		Process process = new Process();
		model.addProcess(process);
		String code = node.getAppCode();
		String name = node.getAppName();
		process.setId(PLAN_EXEC + code);
		process.setName(PROC_NAME);
		process.addFlowElement(createStartEvent(START));
		String lastTaskId;
		if (node.getExecTiming() == null) {
			lastTaskId = START;
		} else {
			lastTaskId = "time_" + code;
			process.addFlowElement(createIntermediate(node.getExecTiming(), lastTaskId, lastTaskId));
			process.addFlowElement(createSequenceFlow(START, lastTaskId));
		}
		//异常处理人工组
		for (LinkedHashMap<String, String> appStep : appStepList) {
			lastTaskId = createTask(process, appStep, code, lastTaskId);
		}
		//异常处理环节
		List<String> exceptinoCandidateGroups = new ArrayList<>();
		exceptinoCandidateGroups.add(sendTaskGroupCode);

		UserTask userTask = createUserTask(EXECEPTION + code,
				name + "异常处理", exceptinoCandidateGroups, "exception");
		userTask.setSkipExpression("${exceptionskip==true}");
		process.addFlowElement(userTask);
		SequenceFlow sequenceFlow = createSequenceFlow(lastTaskId, EXECEPTION + code);
		process.addFlowElement(sequenceFlow);
		lastTaskId = EXECEPTION + code;
		//再加一个serviceTask用于自由跳转
		String solveExceptionId = "solveException_" + code;
		String solveExceptionname = "solveException_" + name;
		String classUrl = "com.newland.paas.paasservice.activitiflow.delegate.DynamicJumpTaskNode";
		ServiceTask serviceTask = createServiceTask(solveExceptionId,
				solveExceptionname, classUrl, solveExceptionId);
		process.addFlowElement(serviceTask);
		process.addFlowElement(createSequenceFlow(lastTaskId, solveExceptionId));
		lastTaskId = solveExceptionId;

        process.addFlowElement(createEndEvent("end"));
        process.addFlowElement(createSequenceFlow(lastTaskId, "end"));

		String url = planInfoMap.get("tenantCode") + "/" + planInfoMap.get("projectCode") + "/"
				+ planInfoMap.get(PLAN_CODE) + "/applications/dynflow/";
		saveFile(ftpMap, url, PLAN_EXEC + code, model);
		return PLAN_EXEC + code;
	}

	/**
	 * 创建任务
	 * @param process 流程对象
	 * @param appStep 应用步骤
	 * @param code 应用名称
	 * @return 步骤ID
	 */
	private String createTask(Process process, LinkedHashMap<String, String> appStep,
                              String code, String lastTaskId) {
		String execOrder = String.valueOf(appStep.get(EXEC_ORDER));
		String stepType = AppActionStepType.getName(Long
				.parseLong(String.valueOf(appStep.get("stepType"))));
		String taskId = "step_" + code + "_" + execOrder;
		String taskName = "step_" + code + "_" + execOrder + "_" + stepType;
		if ("1".equals(String.valueOf(appStep.get("isAutoExec")))) { //自动执行设置为servicetask
			String gatewayId = "gateway0_" + code + "_" + execOrder;
			ExclusiveGateway exclusiveGateway0 = createExclusiveGateway(gatewayId);
			process.addFlowElement(exclusiveGateway0);
			process.addFlowElement(createSequenceFlow(lastTaskId, gatewayId));
			lastTaskId = gatewayId;
			String claUrl = "com.newland.paas.paasservice.activitiflow.delegate.DynamicServiceTask";
			process.addFlowElement(createServiceTask(taskId, taskName, claUrl, taskId));
			SequenceFlow gateWaySeq = createSequenceFlow(lastTaskId, taskId);
			gateWaySeq.setConditionExpression("${skip==false}");
			process.addFlowElement(gateWaySeq);
			lastTaskId = taskId;
			taskId = "gateway_" + code + "_" + execOrder;
			//加一个排他网关用于区分是否需要异步等待（当step调用脚本执行时若返回true则需等待否则进入异常无需等待）
			process.addFlowElement(createExclusiveGateway(taskId));
			process.addFlowElement(createSequenceFlow(lastTaskId, taskId));
			lastTaskId = taskId;
			String waitId = "wait_" + code + "_" + execOrder;
			String waitName = "wait_" + code + "_" + execOrder + "_" + stepType;
			process.addFlowElement(createReceiveTask(waitId, waitName)); //增加receiveTask做异步等待
			SequenceFlow sequenceFlow1 = createSequenceFlow(lastTaskId, waitId);
			sequenceFlow1.setConditionExpression("${skip==false && isWait==true}");
			process.addFlowElement(sequenceFlow1);
			taskId = "manual_" + code + "_" + execOrder;
			taskName = "manual_" + code + "_" + execOrder + "_" + stepType;
			process.addFlowElement(createManualTask(taskId, taskName)); //加一个手动任务
			SequenceFlow sequenceFlow = createSequenceFlow(lastTaskId, taskId);
			sequenceFlow.setConditionExpression("${skip == true || (skip==false && isWait == false)}");
			process.addFlowElement(sequenceFlow);
			process.addFlowElement(createSequenceFlow(waitId, taskId));
			SequenceFlow gateWaySeq1 = createSequenceFlow(gatewayId, taskId);
			gateWaySeq1.setConditionExpression(EXPRESSION);
			process.addFlowElement(gateWaySeq1);
		} else { //人工执行设置为usertask
			String groupId = String.valueOf(appStep.get("exectorCode"));
			List<String> candidateGroups = new ArrayList<>();
			candidateGroups.add(groupId);
			UserTask userTask = createUserTask(taskId, taskName, candidateGroups, taskId);
			userTask.setSkipExpression(EXPRESSION);
			process.addFlowElement(userTask);
			process.addFlowElement(createSequenceFlow(lastTaskId, taskId));
		}
		return taskId;
	}

	/**
	 * 保存流程文件并上传到ftp
	 * @param ftpMap FTP信息
	 * @param url 路径
	 * @param filename 文件名
	 * @param model BPMNMODEL
	 */
	private void saveFile(Map<String, String> ftpMap, String url, String filename, BpmnModel model) {
		//创建一个bpmn的xml转换器，
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
		byte[] bytes = xmlConverter.convertToXML(model);
		String rootPath = FileUtil.getRootPath();
		File file = new File(rootPath + url);
		//如果文件夹不存在
		if (!file.exists()) {
			file.mkdirs(); //创建文件夹
		}
		try (FileOutputStream fileOutputStream = new FileOutputStream(rootPath + url + filename + ".bpmn")) {
			fileOutputStream.write(bytes);
			//将文件上传到ftp
			String userName = ftpMap.get("uploadUsername");
			String password = ftpMap.get("uploadPassword");
			String host = ftpMap.get("uploadHost");
			String homePath = ftpMap.get("uploadHomePath");
			int port = Integer.parseInt(ftpMap.get("uploadPort"));
			SFTPUtil sftp = new SFTPUtil(userName, password, host, port);
			sftp.login();
			sftp.createRootDir(homePath + url);
			sftp.upload(homePath + url, rootPath + url + filename + ".bpmn");
			sftp.logout();
			//删除本地文件
			FileUtil.deleteDir(rootPath + url);
		} catch (Exception e) {
			LOG.error("DynCreate01", "DynCreate01", e, "保存流程文件失败:" + e.getMessage());
		}
	}

	/**
	 * 用户任务
	 * @param id ID
	 * @param name 名称
	 * @param candidateGroups 候选组
	 * @param documentation 描述
	 * @return 用户任务对象
	 */
	private UserTask createUserTask(String id, String name, List<String> candidateGroups, String documentation) {
		UserTask userTask = new UserTask();
		userTask.setName(name);
		userTask.setId(id);
		userTask.setCandidateGroups(candidateGroups);
		userTask.setDocumentation(documentation);
		return userTask;
	}

	/**
	 * 创建手动任务
	 * @param id 手动任务ID
	 * @param name 名称
	 * @return 手动任务对象
	 */
	private ManualTask createManualTask(String id, String name) {
		ManualTask manualTask = new ManualTask();
		manualTask.setId(id);
		manualTask.setName(name);
		return manualTask;
	}

	/**
	 * 流
	 * @param from 来源
	 * @param to 去向
	 * @return 顺序流对象
	 */
    private SequenceFlow createSequenceFlow(String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		return flow;
	}

	/**
	 * 启动事件
	 * @return 启动事件对象
	 */
    private StartEvent createStartEvent(String id) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(id);
		return startEvent;
	}

	/**
	 * 结束事件
	 * @return 结束事件对象
	 */
    private EndEvent createEndEvent(String id) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(id);
		return endEvent;
	}
	
	/**
	 * 并行网关
	 * @param id 网关ID
	 * @return 并行网关对象
	 */
    private ParallelGateway createParallelGateway(String id) {
		ParallelGateway parallelGateway = new ParallelGateway();
		parallelGateway.setId(id);
		return parallelGateway;
	}

	/**
	 * 排他网关
	 * @param id 网关ID
	 * @return 排他网关对象
	 */
    private ExclusiveGateway createExclusiveGateway(String id) {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(id);
		return exclusiveGateway;
	}

	/**
	 * 创建等待任务
	 * @param id 等待任务ID
	 * @param name 名称
	 * @return 等待任务对象
	 */
    private ReceiveTask createReceiveTask(String id, String name) {
		ReceiveTask receiveTask = new ReceiveTask();
		receiveTask.setId(id);
		receiveTask.setName(name);
		return receiveTask;
	}

	/**
	 * 创建serviceTask
	 * @param id ID
	 * @param name 名称
	 * @param classUrl 类路径
	 * @param documentation 描述
	 * @return ServiceTask对象
	 */
    private ServiceTask createServiceTask(String id, String name, String classUrl, String documentation) {
		ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(id);
		serviceTask.setName(name);
		//"com/newland/plan/activiti/Sxmtest001"
		serviceTask.setImplementation(classUrl);
		serviceTask.setImplementationType("class");
		serviceTask.setDocumentation(documentation);
		return serviceTask;
	}

	/**
	 * 创建调用活动
	 * @param id ID
	 * @param name 名称
	 * @param calledElement 调用目标
	 * @param documentation 描述
	 * @param businessKey 业务key
	 * @return 调用活动对象
	 */
    private CallActivity createCallActivity(String id,
		String name, String calledElement, String documentation, String businessKey) {
		CallActivity callActivity = new CallActivity();
		callActivity.setId(id);
		callActivity.setName(name);
		callActivity.setCalledElement(calledElement);
		callActivity.setDocumentation(documentation);
		callActivity.setBusinessKey(businessKey);
		List<IOParameter> inParameters = new ArrayList<>();
		IOParameter iOParameter = new IOParameter();
		iOParameter.setSource(PLAN_CODE);
		iOParameter.setTarget(PLAN_CODE);
		IOParameter iOParameter1 = new IOParameter();
		iOParameter1.setSource("skip");
		iOParameter1.setTarget("skip");
		IOParameter iOParameter2 = new IOParameter();
		iOParameter2.setSource("exceptionskip");
		iOParameter2.setTarget("exceptionskip");
		inParameters.add(iOParameter);
		inParameters.add(iOParameter1);
		inParameters.add(iOParameter2);
		callActivity.setInParameters(inParameters);
		return callActivity;
	}

	/**
	 * 创建定时中间捕获事件
	 * @return 定时器中间事件
	 */
	private IntermediateCatchEvent createIntermediate(Date time, String id, String name) {
		String date = DateUtils.formatDate(time, DateUtils.DATE_SIMP_FORMAT2);
		String tim = DateUtils.formatDate(time, "HH:mm:ss");
		IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
		List<EventDefinition> eventDefinitions = new ArrayList<>();
		TimerEventDefinition eventDefinition = new TimerEventDefinition();
		eventDefinition.setTimeDate(date + "T" + tim);
		eventDefinitions.add(eventDefinition);
		intermediateCatchEvent.setEventDefinitions(eventDefinitions);
		intermediateCatchEvent.setId(id);
		intermediateCatchEvent.setName(name);
		return intermediateCatchEvent;
	}

    /**
     * 消除unchecked cast
     * @param obj 入参对象
     * @param <T> 泛型类
     * @return 返回cast结果
     */
    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }
}
