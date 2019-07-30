package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 测试流程
 * 创建租户-数据预存
 * @author wrp
 */
public class CreateTenantDataPreStorageDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(CreateTenantDataPreStorageDelegate.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void execute(DelegateExecution execution) {

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantDataPreStorageDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        String param = (String) execution.getVariable("param");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + param);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===CreateTenantDataPreStorageDelegate.execution===end");
    }

}
