package com.newland.paas.paasservice.activitiflow.delegate;

import com.alibaba.fastjson.JSON;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.sbcommon.activiti.ActTokenUtil;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

/**
 * 逻辑集群申请通过
 * 
 * @author WRP
 * @since 2019/7/24
 */
public class LogicalClusterApplyApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(LogicalClusterApplyApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterApplyApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());
        //创建虚分区表

        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        Map paramMap = JSON.parseObject(param, Map.class);

        String zoneId = (String) execution.getVariable("zoneId");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===zoneId===" + zoneId);
        paramMap.put("zoneId", Long.valueOf(zoneId));

        paramMap.put("processInstanceId", Long.valueOf(execution.getProcessInstanceId()));

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
        BasicRequestContentVo<Map> requestVo = new BasicRequestContentVo<>();
        requestVo.setParams(paramMap);
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
        String responseContent = restTemplateUtils.postForTokenEntity(microservicesProperties.getClumgr(),
                "v1/logic/relatedCluVZone", ActTokenUtil.getPaasToken(), requestVo, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("relatedCluZone", responseContent);

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterApplyApproveDelegate.execution===end");
    }

}
