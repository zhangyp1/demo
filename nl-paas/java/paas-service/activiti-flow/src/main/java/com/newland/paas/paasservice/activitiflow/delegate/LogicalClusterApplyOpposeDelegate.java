package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.sbcommon.activiti.ActTokenUtil;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

/**
 * 逻辑集群申请不通过
 *
 * @author WRP
 * @since 2019/7/24
 */
public class LogicalClusterApplyOpposeDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(LogicalClusterApplyOpposeDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterApplyOpposeDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());
        // 审批不通过，删除 clu_v_zone表
        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        Map paramMap = JSON.parseObject(param, Map.class);

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
        BasicRequestContentVo<Map> requestVo = new BasicRequestContentVo<>();
        requestVo.setParams(paramMap);
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};

        String responseContent = restTemplateUtils.deleteForTokenEntity(microservicesProperties.getClumgr(),
                "v1/logic/deleteCluVZone", ActTokenUtil.getPaasToken(), requestVo, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("deleteCluVZone", responseContent);

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterApplyOpposeDelegate.execution===end");
    }

}
