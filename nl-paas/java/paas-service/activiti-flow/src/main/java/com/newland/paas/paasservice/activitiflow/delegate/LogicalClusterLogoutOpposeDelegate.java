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
 * 逻辑集群注销不通过
 *
 * @author WRP
 * @since 2019/7/24
 */
public class LogicalClusterLogoutOpposeDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(LogicalClusterLogoutOpposeDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterLogoutOpposeDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());
        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        Map paramMap = JSON.parseObject(param, Map.class);
        paramMap.put("processInstanceId", execution.getProcessInstanceId());
        BasicRequestContentVo<Map> requestParam = new BasicRequestContentVo<>();
        requestParam.setParams(paramMap);

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        String responseContent = restTemplateUtils.postForTokenEntity(microservicesProperties.getClumgr(),
                "v1/logic/logout/rollback", ActivitiTokenUtils.getPaasToken(), requestParam, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("result", responseContent);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterLogoutOpposeDelegate.execution===end");
    }

}
