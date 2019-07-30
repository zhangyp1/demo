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
 * 持久卷绑定集群申请-同意任务
 *
 * @author WRP
 * @since 2019/7/4
 */
public class BindClusterVolApplyApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(BindClusterVolApplyApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===BindClusterVolApplyApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        Map paramMap = JSON.parseObject(param, Map.class);

        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
        BasicRequestContentVo<Map> requestVo = new BasicRequestContentVo<>();
        requestVo.setParams(paramMap);
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        String responseContent = restTemplateUtils.postForTokenEntity(microservicesProperties.getClumgr(),
                "v1/vol/bindClusterVol", ActTokenUtil.getPaasToken(), requestVo, responseType);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===responseContent:===" + responseContent);
        execution.setVariable("bindResult", responseContent);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===BindClusterVolApplyApproveDelegate.execution===end");
    }

}
