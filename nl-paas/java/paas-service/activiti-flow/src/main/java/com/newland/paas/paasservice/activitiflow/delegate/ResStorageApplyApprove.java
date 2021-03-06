package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

/**
 * 存储资源申请审批同意
 */
public class ResStorageApplyApprove implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CreateTenantApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "存储资源申请申请审批状态成功修改为：同意!");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageApplyApprove.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String path = (String) execution.getVariable("path");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===path:" + JSON.toJSONString(path));
        Long resStorageAllocateId = Long.valueOf(execution.getVariable("resStorageAllocateId").toString());
        BasicRequestContentVo<String> reqVo = new BasicRequestContentVo<>();
        reqVo.setParams(path);
        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        String responseContent = restTemplateUtils.
                postForTokenEntity(microservicesProperties.getResmgr(), "v1/storageRes/confirmApply/" + resStorageAllocateId,
                        ActivitiTokenUtils.getPaasToken(), reqVo, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("result", responseContent);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResStorageApplyApprove.execution===end");
    }

}