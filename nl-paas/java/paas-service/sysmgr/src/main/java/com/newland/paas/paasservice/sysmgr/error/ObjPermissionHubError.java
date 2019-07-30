package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 对象权限Hub
 *
 * @author zhongqingjiang
 */
public interface ObjPermissionHubError {
    PaasError SYS_CATEGORY_ID_IS_NULL = new PaasError("2-23-00001", "系统分组ID不能为空");
    PaasError OBJ_ID_IS_NULL = new PaasError("2-23-00002", "对象ID不能为空");
    PaasError OBJ_OPERATE_IS_NULL = new PaasError("2-23-00003", "对象操作不能为空");
}
