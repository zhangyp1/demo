/*
 *
 */
package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 * @author linkun
 * @created 2018年8月21日 下午3:20:56
 */
public interface SysTenantMemberError {
	PaasError IS_MEMBER_OR_IS_JOINING = new PaasError("2-28-00001", "已经加入租户或正在审批中", "请选择其他租户加入");
	PaasError TENANT_ID_IS_NULL = new PaasError("2-28-00002", "租户ID不能为空", "请确认租户ID不能为空");
	PaasError NO_PERMISSION_CHANGE_ZHADMIN =
			new PaasError("2-28-00003", "当前用户没有权限设置租户管理员，如需操作请使用运营管理员", "如需操作请使用运营管理员");
	PaasError UNEXPECTED_TENANT_ID =
			new PaasError("2-28-00004", "租户ID不符合要求", "请确认租户ID是否正确");
	PaasError NON_ADMIN_IS_NOT_SUPPORTED =
			new PaasError("2-28-00005", "无法撤销管理员，该租户下的成员只能为管理员");
	PaasError ROLE_USER_IS_NOT_FOUND =
			new PaasError("2-28-00006", "找不到用户对应的角色信息", "请确认用户或角色是否存在");

}
