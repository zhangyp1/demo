package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-07-30 15:04
 **/
public interface SysDictError {
    PaasError DUPLICATE_ERROR = new PaasError("2-25-00001", "字典项已存在", SysMgrConstants.RESUME_LOAD);

    PaasError PCODE_ERROR = new PaasError("2-25-00002", "字典父节点不能是自己本身", SysMgrConstants.RESUME_LOAD);
    PaasError CHILDREN_EXIST_ERROR = new PaasError("2-25-00002", "已经存在子节点", SysMgrConstants.RESUME_LOAD);
}
