package com.newland.paas.sbcommon.activitiflow.errorcode;

import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 异常码
 * 
 * @author 
 * @date 
 */
public interface ActivtiErrorCode {

  
    PaasError ACTIVTI_RUN_PROCESS_FAIL = new PaasError("21800001", "发起申请流程失败");
    PaasError ACTIVTI_GET_TASKLIST_FAIL = new PaasError("21800002", "查询任务列表TaskList失败");
    PaasError ACTIVTI_GET_GROUP_FAIL = new PaasError("21800003", "获取groupId失败");
    

    /**
     * @Function:     getCommonErr 
     * @Description:  描述错误
     */
    static PaasError getCommonErr(String description) {
        PaasError error = new PaasError("21800001", description);
        return error;
    }
}
