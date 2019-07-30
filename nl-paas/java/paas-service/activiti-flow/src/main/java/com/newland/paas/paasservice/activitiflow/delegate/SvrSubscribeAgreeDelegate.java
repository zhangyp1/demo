package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务订阅-同意
 *
 * @author wrp
 */
public class SvrSubscribeAgreeDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(SvrSubscribeAgreeDelegate.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void execute(DelegateExecution execution) {

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===SvrSubscribeAgreeDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);


        LOG.info(LogProperty.LOGTYPE_DETAIL, "===SvrSubscribeAgreeDelegate.execution===end");
    }

}
