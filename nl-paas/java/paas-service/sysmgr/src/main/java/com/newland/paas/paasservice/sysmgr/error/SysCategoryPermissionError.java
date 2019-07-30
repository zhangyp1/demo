package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 系统分组权限
 *
 * @author zhongqingjiang
 */
public interface SysCategoryPermissionError {
    PaasError CATEGORY_NOT_FOUND = new PaasError("2-30-00001", "无法找到相应的系统分组");
}
