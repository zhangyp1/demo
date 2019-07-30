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

import java.util.Map;

/**
 * 集群分区注销申请通过
 *
 * @author WRP
 * @since 2019/4/17
 */
public class CluZoneLogoutApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CluZoneLogoutApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String cluInfo = (String) execution.getVariable("cluInfo");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===cluInfo===" + cluInfo);
        BasicRequestContentVo<Map> param = new BasicRequestContentVo<>();
        param.setParams(JSON.parseObject(cluInfo, Map.class));

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        String responseContent = restTemplateUtils.
                postForTokenEntity(microservicesProperties.getClumgr(), "v1/cluster_partition/confirmLogout",
                        ActivitiTokenUtils.getPaasToken(), param, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("result", responseContent);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CluZoneLogoutApproveDelegate.execution===end");
    }

}
