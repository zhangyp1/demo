package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 * @author linkun
 * @created 2018-08-01 13:57:24
 */
public interface SysGroupUserError {
	PaasError GROUP_ID_IS_NULL = new PaasError("2-22-00001", "工组id为空", "请先选中工组再添加用户");
	PaasError USER_ID_IS_NULL = new PaasError("2-22-00002", "用户id为空", "请先选中用户再添加工组");
	PaasError USER_NOT_EXIST = new PaasError("2-22-00003", "用户不存在", "请重新添加用户");
	PaasError GROUP_NOT_EXIST = new PaasError("2-22-00004", "工组不存在", "请重新添加工组");
}
