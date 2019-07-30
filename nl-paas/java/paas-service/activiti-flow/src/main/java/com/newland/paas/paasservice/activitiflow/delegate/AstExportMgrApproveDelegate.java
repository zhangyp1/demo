package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 资产导出申请通过
 */
public class AstExportMgrApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(AstExportMgrApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===AstExportMgrApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===AstExportMgrApproveDelegate.execution===end");
    }

}
