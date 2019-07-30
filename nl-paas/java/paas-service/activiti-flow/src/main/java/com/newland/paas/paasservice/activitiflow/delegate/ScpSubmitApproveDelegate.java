package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 脚本提交-同意任务
 *
 * @author WRP
 * @since 2019/2/25
 */
public class ScpSubmitApproveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(ScpSubmitApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ScpSubmitApproveDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ScpSubmitApproveDelegate.execution===end");
    }

}
