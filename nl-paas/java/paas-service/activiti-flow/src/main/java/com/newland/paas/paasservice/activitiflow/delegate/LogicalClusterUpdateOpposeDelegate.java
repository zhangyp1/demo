package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 逻辑集群变更不通过
 *
 * @author WRP
 * @since 2019/7/24
 */
public class LogicalClusterUpdateOpposeDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(LogicalClusterUpdateOpposeDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterUpdateOpposeDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===LogicalClusterUpdateOpposeDelegate.execution===end");
    }

}
