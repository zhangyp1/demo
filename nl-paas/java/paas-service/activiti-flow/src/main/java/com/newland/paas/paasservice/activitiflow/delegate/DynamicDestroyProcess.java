package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.history.HistoricProcessInstanceQuery;


/**
 * 销毁父流程及全部子流程
 *
 * @author sunxm
 */
public class DynamicDestroyProcess implements JavaDelegate {

	private static final Log LOG = LogFactory.getLogger(DynamicDestroyProcess.class);

	@Override
	public void execute(DelegateExecution execution) {
		String parentProcInstanceId = (String)execution.getVariable("parentProcInstanceId");
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
		HistoricProcessInstanceQuery query = engine.getHistoryService().createHistoricProcessInstanceQuery();
		long cnt = query.processInstanceId(parentProcInstanceId).unfinished().count();
		if (cnt >= 1) {
			execution.setVariable("isFinished", false);
			runtimeService.deleteProcessInstance(parentProcInstanceId, "销毁" + parentProcInstanceId);
			LOG.info("销毁成功");
		} else {
			execution.setVariable("isFinished", true);
			LOG.info("方案创建流程已执行结束：" + parentProcInstanceId);
		}
	}

}
