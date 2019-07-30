package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 租户异常定义
 * Created by zkq on 2018-06-28.
 */
public interface SysTenantError {
    PaasError ADD_SYS_TENANT_ERROR = new PaasError("2-18-00001", "添加租户失败", SysMgrConstants.TRY_AGAIN_LATER);
    PaasError UPDATE_SYS_TENANT_ERROR = new PaasError("2-18-00002", "修改租户失败", SysMgrConstants.TRY_AGAIN_LATER);
    PaasError DELETE_SYS_TENANT_ERROR = new PaasError("2-18-00003", "删除租户失败", SysMgrConstants.TRY_AGAIN_LATER);
    PaasError DUPLICATE_SYS_TENANT_ERROR = new PaasError("2-18-00004", "当前租户名称已存在", SysMgrConstants.RESUME_LOAD);
    PaasError EMPTY_SYS_TENANT_ERROR = new PaasError("2-18-00005", "租户名称不能为空", SysMgrConstants.RESUME_LOAD);
    PaasError NOT_EXIST_SYS_TENANT_ERROR = new PaasError("2-18-00006", "当前租户不存在", "");

    PaasError IS_DELETE_SYS_TENANT_ERROR = new PaasError("2-18-00007", "当前租户已经被删除", "请确认该租户是否可用");
    PaasError ADD_SYS_TENANT_TIMEOUT_ERROR = new PaasError(
            "2-18-00008", "创建租户仓库超时", SysMgrConstants.TRY_AGAIN_LATER);

    PaasError TENANT_ID_NOT_NULL = new PaasError("2-18-00009", "租户id不能为空", SysMgrConstants.TRY_AGAIN_LATER);
    PaasError CAN_NOT_DELETE_SPECIAL_TENANT = new PaasError(
            "2-18-00010", "不能删除特殊租户，包括管理员租户、运营、运维", SysMgrConstants.TRY_AGAIN_LATER);

    PaasError CAN_NOT_DELETE_SYS_TENANT = new PaasError("2-18-00010", "该租户下存在{0},不允许删除", "请先确认");

}
