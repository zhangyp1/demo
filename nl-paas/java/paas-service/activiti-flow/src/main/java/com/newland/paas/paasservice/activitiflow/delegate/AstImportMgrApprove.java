package com.newland.paas.paasservice.activitiflow.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 资产入库申请审
 */
public class AstImportMgrApprove implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CreateTenantApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "资产入库申请审批状态成功修改为：同意!");
    }

}
