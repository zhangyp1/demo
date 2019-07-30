/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqEx;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantMemberReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.paasservice.sysmgr.service.SysTenantMemberService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述
 * 
 * @author linkun
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/memberMgr")
@Validated
@AuditObject("租户成员管理")
public class SysTenantMemberController {

    @Autowired
    private SysTenantMemberService sysTenantMemberService;

    /**
     * 查询租户下的所有用户列表数据
     * 
     * @return List
     * @throws ApplicationException
     */
    @GetMapping(value = "/list")
    public List<TenantUserVO> pagedQuery() throws ApplicationException {
        Long tenantId = RequestContext.getTenantId();
        return sysTenantMemberService.getMembersByTenant(tenantId, null);
    }

    /**
     * 查询租户下的所有用户列表数据
     *
     * @return List
     * @throws ApplicationException
     */
    @GetMapping(value = "/list/{tenantId}")
    public List<TenantUserVO> getSysTenantMemberList(@PathVariable("tenantId") Long tenantId)
            throws ApplicationException {
        return sysTenantMemberService.getMembersByTenant(tenantId, null);
    }

    /**
     * 查询租户下的所有用户分页列表数据
     *
     * @param params
     * @return ResultPageData
     * @throws ApplicationException
     */
    @PostMapping(value = "/paged-list")
    public ResultPageData<TenantUserVO> pagedQuery(@RequestBody BasicPageRequestContentVo<SysTenantMemberReqBO> params)
            throws ApplicationException {
        return sysTenantMemberService.getMembersByTenant1(params.getParams(), params.getPageInfo());
    }

    /**
     * 查询非某租户成员的用户列表数据
     * 
     * @return List
     * @throws ApplicationException
     */
    @PostMapping(value = "/listnomember/")
    public List<TenantUserVO> queryNoMemberUsers(
            @RequestBody BasicPageRequestContentVo<SysTenantMemberReqEx> reqInfo) throws ApplicationException {
        return sysTenantMemberService.getNoMemberUsers(reqInfo.getParams());
    }

    /**
     * 新增或更新租户成员
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/addMembers")
    @AuditOperate(value = "addMembers", name = "租户添加成员")
    public int addMembers(@RequestBody BasicRequestContentVo<SysRoleUserReqBo> reqInfo) throws ApplicationException {
        return sysTenantMemberService.addMembers(reqInfo.getParams());
    }

    /**
     * 根据主键ID获取成员详情
     * 
     * @param id 主键id
     * @return TenantUserVO
     * @throws ApplicationException
     */
    @GetMapping(value = "/{id}")
    public TenantUserVO getMember(@PathVariable("id") Long id) throws ApplicationException {
        return sysTenantMemberService.getMemberById(id);
    }

    /**
     * 设置租户管理员
     * 描述
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/setAdmin")
    @AuditOperate(value = "setAdmin", name = "设置租户管理员")
    public void setAdmin(@RequestBody BasicRequestContentVo<SysTenantMemberReqVo> reqInfo) throws ApplicationException {
        sysTenantMemberService.setAdmin(reqInfo.getParams());
    }
}
