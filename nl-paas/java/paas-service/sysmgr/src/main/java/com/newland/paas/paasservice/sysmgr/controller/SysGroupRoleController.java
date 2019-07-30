/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import java.util.List;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupRoleMenuReqVo;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;
import com.newland.paas.paasservice.sysmgr.service.SysGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月27日 下午6:48:19
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/groupRoleMgr")
@Validated
@AuditObject("工组角色管理")
public class SysGroupRoleController {

    @Autowired
    private SysGroupRoleService sysGroupRoleService;

    /**
     * 分页查询工组角色列表
     *
     * @param req
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/page")
    public ResultPageData<List<SysGroupRoleRespBo>> pagedQuery(@RequestBody BasicPageRequestContentVo<SysGroupRoleReqBo> req)
            throws ApplicationException {
        SysGroupRoleReqBo sysGroupRoleReqBo = req.getParams();
        PageInfo pageInfo = req.getPageInfo();
        return sysGroupRoleService.page(sysGroupRoleReqBo, pageInfo);
    }

    /**
     * 查询工组角色列表
     *
     * @param params
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/list")
    public List<SysGroupRoleRespBo> list(SysGroupRoleReqBo params) throws ApplicationException {
        return sysGroupRoleService.list(params);
    }

    @GetMapping("/list-my-group-role")
    public List<SysGroupRole> listMyGroupRole(@RequestParam Long groupId) throws ApplicationException {
        return sysGroupRoleService.listMyGroupRoleList(groupId);
    }

    /**
     * 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 上午9:47:36
     */
    @PostMapping
    @AuditOperate(value = "add", name = "新增工组角色")
    public void add(@Validated(value = {ValidateAdd.class}) @RequestBody BasicRequestContentVo<SysGroupRole> reqInfo)
            throws ApplicationException {
        sysGroupRoleService.add(reqInfo.getParams());
    }

    /**
     * 更新工组角色
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PutMapping
    @AuditOperate(value = "update", name = "更新工组角色")
    public void update(@Validated(value = {ValidateUpdate.class})
                          @RequestBody BasicRequestContentVo<SysGroupRole> reqInfo) throws ApplicationException {
        sysGroupRoleService.update(reqInfo.getParams());
    }

    @GetMapping("/{id}")
    public SysGroupRoleRespBo get(@PathVariable("id") Long id) throws ApplicationException {
        return sysGroupRoleService.get(id);
    }

    /**
     * 删除工组角色
     * @param id
     * @return
     * @throws ApplicationException
     */
    @DeleteMapping("/{id}")
    @AuditOperate(value = "delete", name = "删除工组角色")
    public void delete(@PathVariable("id") Long id) throws ApplicationException {
        sysGroupRoleService.delete(id, false);
    }

    @GetMapping("/optionalMenuList")
    public List<MenuBO> listOptionalMenuList(Long groupRoleId) throws ApplicationException {
        return sysGroupRoleService.listOptionalMenuList(groupRoleId);
    }

    @GetMapping("/selectedMenuIdList")
    public List<Long> listSelectedMenuId(Long groupRoleId) throws ApplicationException {
        return sysGroupRoleService.listSelectedMenuId(groupRoleId);
    }

    @PutMapping("/menuAssign")
    public void assignMenu(@RequestBody BasicRequestContentVo<SysGroupRoleMenuReqVo> req) throws ApplicationException {
        sysGroupRoleService.assignMenu(req.getParams().getGroupRoleId(), req.getParams().getMenuIdList());
    }
}

