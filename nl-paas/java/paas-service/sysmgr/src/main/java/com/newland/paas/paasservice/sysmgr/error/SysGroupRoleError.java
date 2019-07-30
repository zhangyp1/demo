/*
 *
 */
package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年7月31日 上午10:29:30
 */
public interface SysGroupRoleError {
    PaasError DUPLICATE_SYS_GROUP_ROLE_ERROR = new PaasError("2-20-00001", "当前工组角色名称已存在", "请重新输入");
    PaasError NOEXIST_GROUP = new PaasError("2-20-00002", "工组不存在", "请重新选择工组");
    PaasError HAS_USER_CANNOT_DELETE = new PaasError("2-20-00003", "该工组角色下有用户存在", "不允许删除");
    PaasError GROUPROLE_ID_NOT_NULL = new PaasError("2-20-00004", "工组角色ID不能为空", "请稍后重试");
    PaasError DEFAULT_GROUP_ROLE_CANNOT_UPDATE = new PaasError("2-20-00005", "默认工组角色不允许修改", "不允许修改");
    PaasError DEFAULT_GROUP_ROLE_CANNOT_DELETE = new PaasError("2-20-00006", "默认工组角色不允许删除", "不允许删除");
    PaasError GROUP_ROLE_NOT_EXIST = new PaasError("2-20-00007", "工组角色不存在");
    PaasError OVER_OPTIONAL_MENU_SCOPE = new PaasError("2-20-00008", "选择的菜单超出可选范围");
}
