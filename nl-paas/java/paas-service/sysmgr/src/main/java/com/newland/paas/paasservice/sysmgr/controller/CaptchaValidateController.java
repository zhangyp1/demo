package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.LoginInfoVO;
import com.newland.paas.paasservice.sysmgr.service.CaptchaValidateService;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;

/**
 * 
 * 描述
 * 
 * @author linkun
 * @created 2018年6月25日 下午4:16:41
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/captcha")
public class CaptchaValidateController {

    @Autowired
    private CaptchaValidateService captchaValidateService;

    /**
     * 验证码校验
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/validate")
    public Boolean captchaValidate(@RequestBody BasicRequestContentVo<LoginInfoVO> reqInfo)
            throws ApplicationException {
        LoginInfoVO input = reqInfo.getParams();
        return captchaValidateService.captchaValidate(input);
    }

    /**
     * 验证码校验
     * @param captchaID
     * @param inputCaptcha
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/validate/{captchaID}/{captcha}")
    public Boolean captchaValidate(@PathVariable("captchaID") String captchaID,
        @PathVariable("captcha") String inputCaptcha) throws ApplicationException {
        return captchaValidateService.captchaValidate(captchaID, inputCaptcha);
    }
}
