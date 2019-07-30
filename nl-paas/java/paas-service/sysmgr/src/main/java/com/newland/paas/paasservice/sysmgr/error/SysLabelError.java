package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-07-30 15:04
 **/
public interface SysLabelError {
    PaasError LABEL_DEL_ERROR = new PaasError("2-29-00001", "公共标签不允许删除", null);
    PaasError LABEL_ID_NULL_ERROR = new PaasError("2-29-00002", "标签id不能为空", null);
    PaasError LABEL_EXIST_ERROR = new PaasError("2-29-00003", "标签已经存在", null);
    PaasError LABEL_UPDATE_ERROR = new PaasError("2-29-00004", "公共标签不允许修改", null);
    PaasError LABEL_OBJECT_NULL_ERROR = new PaasError("2-29-00005", "标签对象类型不能为空", null);
    PaasError LABEL_VALUE_EXIST_ERROR = new PaasError("2-29-00006", "标签值已经存在", null);
    PaasError LABEL_VALUE_ADD_ERROR = new PaasError("2-29-00007", "公共标签不允许新增值", null);
    PaasError LABEL_HAS_VALUES_ERROR = new PaasError("2-29-00008", "标签下存在标签值，请先删除", null);
    PaasError LABEL_VALUE_UPDATE_ERROR = new PaasError("2-29-00009", "公共标签不允许修改值", null);
    PaasError LABEL_VALUE_DEL_ERROR = new PaasError("2-29-00010", "公共标签不允许删除值", null);
}
