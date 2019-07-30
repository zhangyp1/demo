package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 工组报错
 * 描述
 *
 * @author linkun
 * @created 2018年7月31日 上午9:32:53
 */
public interface SysGroupError {
    PaasError PARENT_NOT_EXIST = new PaasError("2-19-00001", "父节点不存在", "请修改父节点");
    PaasError DUPLICATE_SYS_GROUP_ERROR = new PaasError("2-19-00002", "当前工组名称已存在", "请重新输入");
    PaasError DEL_HAS_CHILD_GROUP = new PaasError("2-19-00003", "当前工组存在子工组", "不允许删除");
    PaasError HAS_CATEGORY_CANNOT_DELETE = new PaasError("2-19-00004", "当前工组存在关联的系统分组", "不允许删除");
    PaasError GROUP_ID_NOT_NULL = new PaasError("2-19-00005", "工组id不能为空", "请稍后重试");
    PaasError EXCEED_MAXLEVEL_ERROR = new PaasError("2-19-00006", "无法新增子工组，已达到最大层级数{0}", SysMgrConstants.RESUME_LOAD);


}
