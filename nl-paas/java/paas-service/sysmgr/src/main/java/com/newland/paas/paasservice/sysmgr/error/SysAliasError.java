package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 别名管理异常
 */
public interface SysAliasError {

    PaasError DUPLICATE_ALIAS_CODE_ERROR = new PaasError("2-18-00001", "当前别名标识已存在", SysMgrConstants.RESUME_LOAD);
    PaasError ALIAS_OBJ_CODE_NULL_ERROR = new PaasError("2-18-00002", "当前别名标识为空", SysMgrConstants.RESUME_LOAD);
    PaasError HAS_SUB_ERROR = new PaasError("2-18-00003", "当前别名包含子别名条目,不允许删除", SysMgrConstants.RESUME_LOAD);
    PaasError DUPLICATE_ALIAS_NAME_ERROR = new PaasError("2-18-00004", "当前别名名称已存在", SysMgrConstants.RESUME_LOAD);
}
