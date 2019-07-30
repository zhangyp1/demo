package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
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

import java.util.Map;

/**
 * 存储资源释放审批：不同意
 */
public class ResStorageFreeApplyOppose implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CreateTenantApproveDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {
		LOG.info(LogProperty.LOGTYPE_DETAIL, "存储资源释放审批：不同意!");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageFreeApplyOppose.execution===begin");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);

		Long allocateId = Long.valueOf(execution.getVariable("resStorageAllocateId").toString());
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};

		String responseContent = restTemplateUtils.
				postForTokenEntity(microservicesProperties.getResmgr(),"v1/storageRes/opposeFreeApply/" + + allocateId,
						ActivitiTokenUtils.getPaasToken(), null, responseType);
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
		execution.setVariable("result", responseContent);
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageFreeApplyOppose.execution===end");

	}

}
