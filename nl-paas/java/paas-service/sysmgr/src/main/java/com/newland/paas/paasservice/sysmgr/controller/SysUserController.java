/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.advice.request.SessionDetail;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserAllInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserRO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserDeptInfoBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.LoginInfoVO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.paasservice.sysmgr.common.SysUserStatusConsts;
import com.newland.paas.paasservice.sysmgr.error.SysUserError;
import com.newland.paas.paasservice.sysmgr.service.CaptchaValidateService;
import com.newland.paas.paasservice.sysmgr.service.SysUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年7月9日 下午4:13:24
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/userMgr")
@Validated
@AuditObject("用户管理")
public class SysUserController {

    private static final Integer STRENGTH = 12;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CaptchaValidateService captchaValidateService;

    /**
     * 校验用户密码
     *
     * @param
     * @param respon
     * @return
     * @throws UnsupportedEncodingException
     * @author linkun
     * @created 2018年6月25日 下午4:16:52
     */
    @PostMapping(value = "/validate")
    public LoginInfoVO validate(@Validated @RequestBody BasicRequestContentVo<LoginInfoVO> reqInfo,
        HttpServletResponse respon) throws ApplicationException {
        LoginInfoVO user = new LoginInfoVO();
        LoginInfoVO input = reqInfo.getParams();

        String username = input.getUsername();
        String password = input.getPassword();
        Boolean captchaValid;
        try {
            captchaValid = captchaValidateService.captchaValidate(input);
        } catch (ApplicationException e) {
            if (e.getError().getCode().equals(SysUserError.USER_LOGIN_CAPTCHA_EXPIRE.getCode())) {
                // 验证码过期
                user.setCaptcha("-2");
                return user;
            } else {
                throw e;
            }
        }
        if (!captchaValid) {
            // 验证码错误
            user.setCaptcha("-1");
            return user;
        }
        // 登录用户
        SysUser info = sysUserService.getUserByAccount(username);
        if (info == null) {
            return user;
        }
        if (StringUtils.isNotEmpty(info.getStatus())
            && !info.getStatus().equalsIgnoreCase(SysUserStatusConsts.ENABLED.getValue())) {
            user.setCaptcha("-3");
            return user;
        }
        // 验证密码是否正确
        if (encoder.matches(password, info.getPassword())) {
            user.setUserId(info.getUserId());
            user.setUsername(info.getUsername());
            user.setAccount(info.getAccount());
        }
        return user;
    }

    /**
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/session/list")
    public List<SessionDetail> sessionList() {
        return sysUserService.getSessionList();
    }

    /**
     * 查询用户列表 描述
     *
     * @param user
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午4:24:08
     */
    @GetMapping
    public List<TenantUserBO> listQuery(TenantUserRO user) {
        return sysUserService.getSysUsers(user);
    }

    /**
     *
     * @param params
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/paged-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData pageQuerySysUser(@RequestBody BasicPageRequestContentVo<SysUser> params)
        throws ApplicationException {
        return sysUserService.pageQuerySysUser(params.getParams(), params.getPageInfo());
    }

    /**
     * 删除用户
     * 
     * @param userId
     * @throws ApplicationException
     */
    @DeleteMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @AuditOperate(value = "deleteSysUser", name = "删除用户")
    public void deleteSysUser(@PathVariable("userId") Long userId) throws ApplicationException {
        if (userId == null) {
            throw new ApplicationException(SysUserError.SYSUSER_ID_NULL_ERROR);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setDelFlag((short) 1);
        sysUserService.saveSysUser(sysUser, null);
    }

    /**
     * 新增用户
     * 
     * @param requestContentVo
     * @throws ApplicationException
     */
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @AuditOperate(value = "newSysUser", name = "新增用户")
    public void newSysUser(@RequestBody BasicRequestContentVo<SysUser> requestContentVo) throws ApplicationException {
        SysUser sysUser = requestContentVo.getParams();
        sysUser.setPassword(encoder.encode(sysUser.getPassword()));
        saveSysUser(sysUser);
    }

    /**
     * 修改用户
     * 
     * @param requestContentVo
     * @throws ApplicationException
     */
    @PutMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @AuditOperate(value = "updateSysUser", name = "修改用户")
    public void updateSysUser(@RequestBody BasicRequestContentVo<SysUser> requestContentVo)
        throws ApplicationException {
        SysUser sysUser = requestContentVo.getParams();
        String password = sysUser.getPassword();
        if (StringUtils.isNotEmpty(password)) {
            sysUser.setPassword(encoder.encode(password));
        } else {
            sysUser.setPassword(null);
        }
        saveSysUser(sysUser);
    }

    /**
     *
     * @param sysUser
     * @throws ApplicationException
     */
    private void saveSysUser(SysUser sysUser) throws ApplicationException {
        sysUserService.saveSysUser(sysUser, null);
    }

    /**
     *
     * @param userId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/allInfo/{userId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysUserAllInfoBO getSysUserAllInfo(@PathVariable("userId") Long userId) throws ApplicationException {
        if (userId == null) {
            throw new ApplicationException(SysUserError.SYSUSER_ID_NULL_ERROR);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        SysUserAllInfoBO sysUserAllInfo = sysUserService.getSysUserAllInfo(sysUser);
        sysUserAllInfo.setPassword(null);
        return sysUserAllInfo;
    }

    /**
     * 修改用户状态
     * 
     * @param userId
     * @param status
     * @throws ApplicationException
     */
    @PutMapping(value = "/{userId}/status/{status}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @AuditOperate(value = "status", name = "修改用户状态")
    public void status(@PathVariable("userId") Long userId, @PathVariable("status") String status)
        throws ApplicationException {
        if (userId == null) {
            throw new ApplicationException(SysUserError.SYSUSER_ID_NULL_ERROR);
        }

        if (status == null || !SysUserStatusConsts.includes(status)) {
            throw new ApplicationException(SysUserError.SYSUSER_STATUS_ERROR);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setStatus(status);
        sysUserService.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 查询用户归属的部门 描述
     *
     * @return
     * @throws ApplicationException
     * @author caifeitong
     * @created 2018年7月25日 下午3:04:08
     */
    @GetMapping(value = "/deptList")
    public List<UserDeptInfoBO> listQuery() {
        return sysUserService.getUserDeptList();
    }

    /**
     * 查询未分配角色的用户信息(用户ID，用户名，部门)
     *
     * @return
     * @throws ApplicationException
     * @author caifeitong
     * @created 2018年7月25日 下午3:04:08
     */
    @PostMapping(value = "/usersLeft")
    public List<SysUser> getUnRoleSysUser(@RequestBody BasicRequestContentVo<SysUser> vo) {
        return sysUserService.getUnRoleSysUser(vo);
    }

    /**
     * 查询具体已经分配某个角色的所有用户信息(用户ID，用户名，部门)
     *
     * @return
     * @throws ApplicationException
     * @author caifeitong
     * @created 2018年7月25日 下午3:04:08
     */
    @GetMapping(value = "/usersRight")
    public List<SysUser> getSysRoleUser(Long roleId) {
        return sysUserService.getSysRoleUser(roleId);
    }

    /**
     * @return UserId
     * @throws ApplicationException
     * @author panyang
     * @created 2018年7月31日 下午9:04:08
     */
    @PostMapping(value = "/getUserId")
    public LoginInfoVO getUserId(@RequestBody BasicRequestContentVo<LoginInfoVO> reqInfo) throws ApplicationException {
        LoginInfoVO vo = new LoginInfoVO();
        SysUser user = sysUserService.getUserByAccount(reqInfo.getParams().getAccount());
        if (user != null) {
            vo.setUserId(user.getUserId());
            vo.setAccount(user.getAccount());
            vo.setUsername(user.getUsername());
        }
        return vo;
    }

    /**
     * 根据用户账户和租户code查询用户信息
     *
     * @param account
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/getUserInfoByAccount/{account}/{tenantCode}")
    public TenantUserVO getUserInfoByAccountAndTenantCode(@PathVariable("account") String account,
        @PathVariable("tenantCode") String tenantCode) {
        return sysUserService.getUserInfoByAccountAndTenantCode(account, tenantCode);
    }

}
