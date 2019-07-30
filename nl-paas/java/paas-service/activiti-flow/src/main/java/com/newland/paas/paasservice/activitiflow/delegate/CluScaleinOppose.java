package com.newland.paas.paasservice.activitiflow.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 *
 */
public class CluScaleinOppose implements JavaDelegate {
	
    private static final Log LOG = LogFactory.getLogger(CreateTenantApproveDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {
	       LOG.info(LogProperty.LOGTYPE_DETAIL, "==审批不通过==");
		
	}

}
