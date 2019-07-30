package com.newland.paas.paasservice.sysmgr.temp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobCallbackVo;

/**
 * 例子、示例代码
 *
 * @author WRP
 * @since 2019/1/30
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/job/")
public class ScpEgController {

    private static final Log LOG = LogFactory.getLogger(ScpEgController.class);

    /**
     * 示例接口：根据scpmock回调获取信息
     *
     * @param jobCallbackVo
     */
    @PostMapping(value = "callback", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void callback(@RequestBody JobCallbackVo jobCallbackVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "回调日志：callback,jobCallbackVo:" + JSON.toJSONString(jobCallbackVo));

    }


}
