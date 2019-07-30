package com.newland.paas.sbcommon.ee.service;

import com.newland.paas.paasservice.controllerapi.ee.vo.JobCallbackVo;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 * 执行引擎回调接口，各个微服务基于此接口实现REST接口
 * 
 * @author SongDi
 * @date 2018/11/05
 */
public interface IExecuteEngineJobCallBack {
    /**
     * 回调业务逻辑
     * 
     * @param jobCallbackVo
     * @throws ApplicationException
     */
    void call(JobCallbackVo jobCallbackVo) throws ApplicationException;

}
