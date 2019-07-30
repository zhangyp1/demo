/*
 *
 */
package com.newland.paas.paasservice.sysmgr.error;

import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * 描述
 * @author linkun
 * @created 2018年6月28日 下午4:38:34
 */
public interface SysUserError {
    PaasError USER_LOGIN_ERROR = new PaasError("2-18-00001", "用户登录失败", "请确认用户名和密码是否正确");
    PaasError USER_IS_DELETE = new PaasError("2-18-00001", "用户已经被删除", "请确认用户是否可用");
    PaasError USER_LOGIN_CAPTCHA_ERROR = new PaasError("2-18-00003", "验证码输入错误", "验证码输入错误");

    PaasError USER_LOGIN_CAPTCHA_EXPIRE = new PaasError("2-18-00003", "验证码过期", "请刷新验证码");

    PaasError DUP_SYSUSER_NAME_ERROR = new PaasError("2-18-00008", "当前用户名称已存在", SysMgrConstants.RESUME_LOAD);
    PaasError DUP_SYSUSER_ACCOUNT_ERROR = new PaasError("2-18-00009", "当前用户账号已存在", SysMgrConstants.RESUME_LOAD);
    PaasError SYSUSER_STATUS_ERROR = new PaasError("2-18-00010", "设置用户状态错误", SysMgrConstants.RESUME_LOAD);
    PaasError SYSUSER_ID_NULL_ERROR = new PaasError("2-18-00011", "用户id为空", SysMgrConstants.RESUME_LOAD);

}

