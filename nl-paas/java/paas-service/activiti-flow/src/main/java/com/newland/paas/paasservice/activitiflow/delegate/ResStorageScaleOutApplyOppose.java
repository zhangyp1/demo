package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

/**
 * 存储资源扩容审批：不同意
 */
public class ResStorageScaleOutApplyOppose implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CreateTenantApproveDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {
		LOG.info(LogProperty.LOGTYPE_DETAIL, "存储资源扩容申请批状态成功修改为：不同意!");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageScaleOutApplyOppose.execution===begin");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());
		// 解除预占
		Long preemptedId = Long.valueOf(execution.getVariable("preemptedId").toString());
		Long allocateId = Long.valueOf(execution.getVariable("allocateId").toString());
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};

		String responseContent = restTemplateUtils.
				postForTokenEntity(microservicesProperties.getResmgr(),
						"v1/storageRes/scaleOutOpposeApply/" + preemptedId + "/" + allocateId,
						ActivitiTokenUtils.getPaasToken(), null, responseType);
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
		execution.setVariable("result", responseContent);
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageScaleOutApplyOppose.execution===end");
	}

}
