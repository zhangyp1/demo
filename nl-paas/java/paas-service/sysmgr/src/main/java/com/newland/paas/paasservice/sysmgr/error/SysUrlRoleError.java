package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 * @author linkun
 * @created 2018-08-06 16:27:06
 */
public interface SysUrlRoleError {

	PaasError ROLE_ID_IS_NULL = new PaasError("2-25-00001", "角色id不能为空", "请选择角色后在提交");


}
