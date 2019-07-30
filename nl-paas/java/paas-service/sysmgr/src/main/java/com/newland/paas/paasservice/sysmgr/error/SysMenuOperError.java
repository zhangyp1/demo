/*
 *
 */
package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月28日 下午4:38:34
 */
public interface SysMenuOperError {
    PaasError DOUBLE_ORDER_NUMBER_ERROR = new PaasError("2-21-00001", "排序号重复", "请重新填写排序号");
    PaasError PARENT_NOEXIST = new PaasError("2-21-00002", "父节点不存在", "请重新选择父节点添加");
    PaasError HAS_CHILDS = new PaasError("2-21-00003", "存在子菜单不允许删除", "请先删除子菜单");
}

