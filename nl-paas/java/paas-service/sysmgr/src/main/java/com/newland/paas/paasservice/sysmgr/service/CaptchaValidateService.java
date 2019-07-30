package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.controllerapi.sysmgr.vo.LoginInfoVO;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 *
 */
public interface CaptchaValidateService {
    /**
     *
     * @param captchaID
     * @param inputCaptcha
     * @return
     * @throws ApplicationException
     */
    boolean captchaValidate(String captchaID, String inputCaptcha) throws ApplicationException;

    /**
     *
     * @param loginInfoVO
     * @return
     * @throws ApplicationException
     */
    boolean captchaValidate(LoginInfoVO loginInfoVO) throws ApplicationException;
}
