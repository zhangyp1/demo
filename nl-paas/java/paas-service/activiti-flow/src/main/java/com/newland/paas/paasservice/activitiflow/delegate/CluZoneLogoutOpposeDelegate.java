package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 集群分区注销申请不通过
 *
 * @author WRP
 * @since 2019/4/17
 */
public class CluZoneLogoutOpposeDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CluZoneLogoutOpposeDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===无需回滚===");

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantApproveDelegate.execution===end");
    }

}
