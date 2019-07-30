package com.newland.paas.sbcommon.controller;

import com.newland.paas.common.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobCallbackVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.ee.service.IExecuteEngineJobCallBack;
import com.newland.paas.sbcommon.utils.SpringContextUtil;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;

/**
 * 执行引擎回调公共Rest接口:回调业务转发
 * 
 * @author SongDi
 * @date 2018/11/05
 */
@RestController
@RequestMapping("/v1/execute-engine")
public class JobCallBackDispatch {
    private static final Log LOG = LogFactory.getLogger(JobCallBackDispatch.class);

    /**
     * 回调请求，根据BeanID转发
     * 
     * @throws ApplicationException
     */

    @PostMapping(value = "/call-back", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void call(@RequestBody BasicRequestContentVo<JobCallbackVo> jobCallbackVo) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "准备执行回调...");
        if (jobCallbackVo == null || jobCallbackVo.getParams() == null) {
            throw new IllegalArgumentException("请求参数不能为null！");
        }
        if (StringUtils.isBlank(jobCallbackVo.getParams().getJobId())) {
            throw new IllegalArgumentException("JobID不能为空！");
        }
        if (StringUtils.isBlank(jobCallbackVo.getParams().getCallBackSvrName())) {
            throw new IllegalArgumentException("CallBackSvrName不能为空！");
        }
        String jobId = jobCallbackVo.getParams().getJobId();
        String callBackSvrName = jobCallbackVo.getParams().getCallBackSvrName();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "回调JobID：", jobId);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "回调CallBackSvrName：", callBackSvrName);
        IExecuteEngineJobCallBack callBack = SpringContextUtil.getContext()
            .getBean(callBackSvrName, IExecuteEngineJobCallBack.class);
        callBack.call(jobCallbackVo.getParams());

    }

}
