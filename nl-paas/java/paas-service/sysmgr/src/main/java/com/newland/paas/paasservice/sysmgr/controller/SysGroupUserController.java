/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysGroupUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018-08-01 13:57:24
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/groupUserMgr")
@Validated
@AuditObject("工组用户管理")
public class SysGroupUserController {

    @Autowired
    private SysGroupUserService sysGroupUserService;

    /**
     * 根据工组ID分页获取用户列表
     * @param req
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/pageUsersByGroupId")
    public ResultPageData<List<SysGroupUserRespBo>> pageUsersByGroupId(@RequestBody BasicPageRequestContentVo<SysGroupUserReqBo> req) {
        SysGroupUserReqBo reqBo = req.getParams();
        PageInfo pageInfo = req.getPageInfo();
        if (reqBo != null && reqBo.getGroupId() == null) {
            return new ResultPageData(new ArrayList(), pageInfo);
        }
        Long groupId = reqBo.getGroupId();
        return sysGroupUserService.pageUsersByGroupId(groupId, pageInfo);
    }

    /**
     *
     * @param params
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/list")
    public List<SysGroupUserRespBo> list(SysGroupUserReqBo params) throws ApplicationException {
        return sysGroupUserService.list(params);
    }

    /**
     * 工组添加多个用户
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午3:29:49
     */
    @PostMapping("/addUsers")
    @AuditOperate(value = "addUsers", name = "工组添加多个用户")
    public int addUsers(@RequestBody BasicRequestContentVo<SysGroupUserReqBo> reqInfo) throws ApplicationException {
        return sysGroupUserService.addUsers(reqInfo.getParams());
    }

    /**
     * 用户添加多个工组
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午3:38:22
     */
    @PostMapping("/addGroups")
    @AuditOperate(value = "addGroups", name = "用户添加多个工组")
    public int addGroups(@RequestBody BasicRequestContentVo<SysGroupUserReqBo> reqInfo) throws ApplicationException {
        return sysGroupUserService.addGroups(reqInfo.getParams());
    }

    /**
     * 根据用户id获取工组列表
     * 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:25:19
     */
    @GetMapping("/getGroupsByUser/{userId}")
    public List<SysGroupUserRespBo> getGroupsByUser(@PathVariable("userId") Long userId) {
        return sysGroupUserService.getGroupsByUser(userId);
    }

    /**
     * 获取租户下所有用户
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:24:44
     */
    @PostMapping("/getAllUsersByTenant")
    public List<SysGroupUserRespBo> getAllUsersByTenant(@RequestBody BasicRequestContentVo<SysGroupUserReqBo> reqInfo) {
        return sysGroupUserService.getAllUsersByTenant(reqInfo.getParams());
    }

    /**
     * 获取租户下所有工组
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午4:24:28
     */
    @PostMapping("/getAllGroupsByTenant")
    public List<SysGroupUserRespBo> getAllGroupsByTenant(
            @RequestBody BasicRequestContentVo<SysGroupUserReqBo> reqInfo) {
        return sysGroupUserService.getAllGroupsByTenant(reqInfo.getParams());
    }

    /**
     * 设置工组管理员
     * 描述
     *
     * @param reqInfo
     * @return
     * @author linkun
     * @created 2018年8月2日 上午11:33:01
     */
    @PostMapping("/setAdmin")
    @AuditOperate(value = "setAdmin", name = "设置工组管理员")
    public int setAdmin(@RequestBody BasicRequestContentVo<SysGroupUserReqBo> reqInfo) throws ApplicationException {
        return sysGroupUserService.setAdmin(reqInfo.getParams());
    }

    /**
     * 判断是否为工组管理员
     *
     * @param groupId
     * @param userId
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/isGroupAdmin")
    public boolean isGroupAdmin(@RequestParam Long groupId, @RequestParam Long userId) {
        return sysGroupUserService.isGroupAdmin(groupId, userId);
    }

}

