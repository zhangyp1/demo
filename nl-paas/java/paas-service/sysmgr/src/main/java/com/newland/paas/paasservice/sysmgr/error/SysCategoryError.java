package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-07-30 15:04
 **/
public interface SysCategoryError {
    PaasError NULL_NAME_ERROR = new PaasError("2-20-00001", "系统分组名称不能为空", SysMgrConstants.RESUME_LOAD);
    PaasError DUPLICATE_NAME_ERROR = new PaasError("2-20-00002", "系统分组名称已存在", SysMgrConstants.RESUME_LOAD);
    PaasError ID_NULL_ERROR = new PaasError("2-20-00003", "系统分组ID为空", SysMgrConstants.RESUME_LOAD);
    PaasError HAS_SUB_ERROR = new PaasError("2-20-00004", "系统分组包含子系统条目， 不允许删除", SysMgrConstants.RESUME_LOAD);
    PaasError HAS_OBJECT_ERROR = new PaasError("2-20-00005", "系统分组存在关联对象， 不允许删除", SysMgrConstants.RESUME_LOAD);
    PaasError EXCEED_MAXLEVEL_ERROR = new PaasError("2-20-00006", "无法新增子系统分组，已达到最大层级数{0}", SysMgrConstants.RESUME_LOAD);
    PaasError CAN_NOT_DELETE_ROOT = new PaasError("2-20-00007", "不允许删除根系统分组");
    PaasError SYS_CATEGORY_NOT_FOUND = new PaasError("2-20-00008", "系统分组不存在");
    PaasError NULL_CODE_ERROR = new PaasError("2-20-00009", "系统分组标识不能为空", SysMgrConstants.RESUME_LOAD);
    PaasError DUPLICATE_CODE_ERROR = new PaasError("2-20-00010", "系统分组标识已存在", SysMgrConstants.RESUME_LOAD);
    PaasError NULL_GROUP_ERROR = new PaasError("2-20-00011", "所属工组不能为空", SysMgrConstants.RESUME_LOAD);
    PaasError DUPLICATE_ROOT_GROUP_ERROR = new PaasError("2-20-00012", "ROOT工组下只能有一个系统分组", SysMgrConstants.RESUME_LOAD);
}
