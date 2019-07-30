package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统分组权限接口
 *
 * @author zhongqingjiang
 */
@RestController
@RequestMapping("/sysmgr/v1/sys-category-permissions")
public class SysCategoryPermissionController {

    @Autowired
    SysCategoryPermissionService sysCategoryPermissionService;

    @PostMapping(value = "/managed-sys-categories", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysCategoryPermissionVo> listManagedSysCategory(@RequestBody BasicPageRequestContentVo<SysCategoryPermissionReqVo> params)
            throws ApplicationException {
        String likeCategoryName = null;
        Long byGroupId = null;
        if (params != null && params.getParams() != null) {
            likeCategoryName = params.getParams().getLikeCategoryName();
            byGroupId = params.getParams().getByGroupId();
        }
        return sysCategoryPermissionService.pageManagedSysCategory(byGroupId, likeCategoryName, params.getPageInfo());
    }

    @GetMapping(value = "/managed-sys-categories/{categoryId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysCategoryPermissionVo listManagedSysCategory(@PathVariable Long categoryId)
            throws ApplicationException {
        return sysCategoryPermissionService.getManagedSysCategory(categoryId);
    }

    @PutMapping(value = "/managed-sys-categories/{categoryId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateManagedSysCategory(@Validated @RequestBody SysCategoryPermissionVo sysCategoryPermissionVo)
            throws ApplicationException {
        sysCategoryPermissionService.updateManagedSysCategory(sysCategoryPermissionVo);
    }

    @PostMapping(value = "/granted-sys-categories", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysCategoryPermissionVo> listGrantedSysCategory(@RequestBody BasicPageRequestContentVo<SysCategoryPermissionReqVo> params)
            throws ApplicationException {
        String likeCategoryName = null;
        Long byGroupId = null;
        if (params != null) {
            likeCategoryName = params.getParams().getLikeCategoryName();
            byGroupId = params.getParams().getByGroupId();
        }
        return sysCategoryPermissionService.pageGrantedSysCategory(byGroupId, likeCategoryName, params.getPageInfo());
    }

    @GetMapping(value = "/groups", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysGroupRespVo2> listGroup(@RequestParam Long categoryId) throws ApplicationException {
        return sysCategoryPermissionService.listGroup(categoryId);
    }

    @PostMapping(value = "/generate-form", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysCategoryPermissionVo generateForm(@RequestBody SysCategoryPermissionFormReq req)
            throws ApplicationException {
        return sysCategoryPermissionService.generateForm(req);
    }

}
