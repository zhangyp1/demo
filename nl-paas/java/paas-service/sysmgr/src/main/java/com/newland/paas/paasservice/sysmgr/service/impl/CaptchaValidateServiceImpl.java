package com.newland.paas.paasservice.sysmgr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.LoginInfoVO;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysUserError;
import com.newland.paas.paasservice.sysmgr.service.CaptchaValidateService;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 *
 */
@Service
public class CaptchaValidateServiceImpl implements CaptchaValidateService {
    @Value("${captcha.disable:false}")
    private boolean captchaDisable;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean captchaValidate(String captchaID, String inputCaptcha) throws ApplicationException {
        // 没有禁用验证码，则开始验证用户输入的验证码
        if (!captchaDisable) {
            String captcha = redisTemplate.opsForValue().get(SysMgrConstants.REDIS_CAPTCHA + captchaID);
            if (StringUtils.isEmpty(captcha)) {
                throw new ApplicationException(SysUserError.USER_LOGIN_CAPTCHA_EXPIRE);
            }
            return captcha.equalsIgnoreCase(inputCaptcha);
        }
        return true;

    }

    @Override
    public boolean captchaValidate(LoginInfoVO loginInfoVO) throws ApplicationException {
        return this.captchaValidate(loginInfoVO.getCaptchaID(), loginInfoVO.getCaptcha());

    }

}
