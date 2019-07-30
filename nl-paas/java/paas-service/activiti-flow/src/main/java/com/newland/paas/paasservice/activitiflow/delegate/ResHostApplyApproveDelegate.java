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
 * 主机资源申请-同意任务
 *
 * @author WRP
 * @since 2018/8/16
 */
public class ResHostApplyApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(ResHostApplyApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResHostApplyApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        Map paramMap = JSON.parseObject(param, Map.class);

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        String responseContent = restTemplateUtils.
                postForTokenEntity(microservicesProperties.getResmgr(), "v1/resources/confirmApply",
                ActivitiTokenUtils.getPaasToken(), paramMap, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("result", responseContent);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ResHostApplyApproveDelegate.execution===end");
    }

}
