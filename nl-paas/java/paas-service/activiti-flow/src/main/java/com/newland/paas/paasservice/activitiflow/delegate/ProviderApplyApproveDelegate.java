package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.datacommonmodule.common.GlbDictConstants;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantProviderApplyVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantRespVo;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

/**
 * 提供方申请-同意任务
 *
 * @author WRP
 * @since 2019/2/25
 */
public class ProviderApplyApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(ProviderApplyApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProviderApplyApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());


        // 获取paas服务的token
        String paasToken = ActivitiTokenUtils.getPaasToken();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===PAAS-Authorization===" + paasToken);

        String jString = (String) execution.getVariable("tenantInfo");
        SysTenantProviderApplyVo sysTenantVo = JSON.parseObject(jString, SysTenantProviderApplyVo.class);
        sysTenantVo.setSupplierStatus(GlbDictConstants.APPROVE_STATUS_APPROVEY.dictCode);

        BasicRequestContentVo<SysTenantProviderApplyVo> requestContent = new BasicRequestContentVo<>();
        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
        ParameterizedTypeReference<BasicResponseContentVo<SysTenantRespVo>> responseType = new
                ParameterizedTypeReference<BasicResponseContentVo<SysTenantRespVo>>() {
                };
        requestContent.setParams(sysTenantVo);

        restTemplateUtils.postForTokenEntity(
                microservicesProperties.getSysmgr(), "v1/tenantMgr/tenant-provider-status",
                paasToken, requestContent,
                responseType);

        LOG.info(LogProperty.LOGTYPE_DETAIL, "租户提供方申请成功!");

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProviderApplyApproveDelegate.execution===end");
    }

}
