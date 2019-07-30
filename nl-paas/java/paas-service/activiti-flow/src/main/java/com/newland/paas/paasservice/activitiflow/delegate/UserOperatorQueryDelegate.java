package com.newland.paas.paasservice.activitiflow.delegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 测试流程
 * 创建租户-操作人员查询
 *
 * @author wrp
 */
public class UserOperatorQueryDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLogger(UserOperatorQueryDelegate.class);

    @SuppressWarnings("rawtypes")
    @Override
    public void execute(DelegateExecution execution) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===UserOperatorQueryDelegate.execution===begin");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===applyUserId===" + execution.getVariable("applyUserId"));
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===ProcessInstanceId:" + execution.getProcessInstanceId());

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===param===" + execution.getVariable("param"));

        LOG.info(LogProperty.LOGTYPE_DETAIL, "===groupId===" + "group_1");
        execution.setVariable("groupId", "group_1");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===UserOperatorQueryDelegate.execution===end");
    }

}
