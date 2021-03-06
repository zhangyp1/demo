package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantApplyVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantRespVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantVo;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

/**
 * 创建租户-反对任务
 * @author wrp
 */
public class CreateTenantOpposeDelegate implements JavaDelegate {

	private static final Log LOG = LogFactory.getLogger(CreateTenantOpposeDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {

		LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantOpposeDelegate.execution===begin");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "租户创建拒绝");

		// 获取paas服务的token
				String paasToken = ActivitiTokenUtils.getPaasToken();
				LOG.info(LogProperty.LOGTYPE_DETAIL, "===PAAS-Authorization===" + paasToken);

				String jString = (String) execution.getVariable("tenantInfo");
				SysTenantApplyVo sysTenantVo = JSON.parseObject(jString, SysTenantApplyVo.class);
				sysTenantVo.setIsAgree("0");

				BasicRequestContentVo<SysTenantVo> requestContent = new BasicRequestContentVo<>();
				MicroservicesProperties microservicesProperties = SpringBeanUtil
						.getBean(MicroservicesProperties.class);
				RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
				ParameterizedTypeReference<BasicResponseContentVo<SysTenantRespVo>> responseType =
						new ParameterizedTypeReference<BasicResponseContentVo<SysTenantRespVo>>() {
				};
				requestContent.setParams(sysTenantVo);

				restTemplateUtils.postForTokenEntity(
						microservicesProperties.getSysmgr(), "v1/tenantMgr/tenant-new",
								paasToken, requestContent,
						responseType);
				
		LOG.info(LogProperty.LOGTYPE_DETAIL, "租户创建拒绝成功!");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantOpposeDelegate.execution===end");
	}

}
