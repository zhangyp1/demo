package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantFlowReqBo;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.core.ParameterizedTypeReference;

/**
 *
 */
public class TenantJoinOpposeDelegate implements JavaDelegate {

	private static final Log LOG = LogFactory.getLogger(TenantJoinOpposeDelegate.class);
	@Override
	public void execute(DelegateExecution execution) {
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===TenantJoinOpposeDelegate.execution===begin");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "租户加入拒绝");

		// 获取paas服务的token
		String paasToken = ActivitiTokenUtils.getPaasToken();
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===PAAS-Authorization===" + paasToken);

		String jString = (String) execution.getVariable("reqInfo");
		SysTenantFlowReqBo sysTenantMemberReqVo = JSON.parseObject(jString, SysTenantFlowReqBo.class);
		sysTenantMemberReqVo.setIsAgree("0");

		//获取实例id
		String processInstanceId = execution.getProcessInstanceId();


		BasicRequestContentVo<SysTenantFlowReqBo> requestContent = new BasicRequestContentVo<>();
		MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
		RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
		ParameterizedTypeReference<BasicResponseContentVo<Boolean>> responseType =
				new ParameterizedTypeReference<BasicResponseContentVo<Boolean>>() {
				};
		requestContent.setParams(sysTenantMemberReqVo);

		BasicResponseContentVo<Boolean> responseContent = restTemplateUtils.postForTokenEntity(
				microservicesProperties.getSysmgr(),
				"v1/tenantMgr/joinTenant/approve",
				paasToken, requestContent, responseType);
		if (responseContent.getContent() == null || !responseContent.getContent()) {
			LOG.info(LogProperty.LOGTYPE_DETAIL, processInstanceId + "执行失败！");
			throw new NLUnCheckedException("20002001", "租户加入失败！");
		}
		
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===TenantJoinOpposeDelegate.execution===end");

	}

}
