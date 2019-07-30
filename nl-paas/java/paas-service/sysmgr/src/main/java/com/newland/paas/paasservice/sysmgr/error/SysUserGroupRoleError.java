package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 工组报错
 * 描述
 * @author linkun
 * @created 2018年7月31日 上午9:32:53
 */
public interface SysUserGroupRoleError {
    PaasError GROUP_ROLE_ID_NOT_NULL = new PaasError("2-21-00001", "工组角色id不能为空", "请重新选择工组角色");
    PaasError USER_ID_NOT_NULL = new PaasError("2-21-00002", "用户id不能为空", "请重新选择用户");
    PaasError USER_NOT_EXIST = new PaasError("2-21-00003", "用户不存在", "请重新选择用户");
    PaasError GROUP_ROLE_NOT_EXIST = new PaasError("2-21-00004", "工组橘色不存在", "请重新选择工组角色");
}
