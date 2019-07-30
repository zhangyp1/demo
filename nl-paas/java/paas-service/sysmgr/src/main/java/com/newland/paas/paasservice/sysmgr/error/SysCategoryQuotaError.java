package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2019-06-17 14:41
 **/
public interface SysCategoryQuotaError {
    PaasError QUOTA_OVER_ERROR = new PaasError("2-31-00001", "{0}配额小于已使用配额总和", SysMgrConstants.RESUME_LOAD);
    PaasError QUOTA_NOT_ENOUGH_ERROR = new PaasError("2-31-00002", "{0}配额不够使用", SysMgrConstants.RESUME_LOAD);
}
