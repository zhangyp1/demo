package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.sysmgr.vo.ObjectTypeVo;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryPermissionFormReq;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryPermissionVo;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupRespVo2;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 系统分组权限
 *
 * @author zhongqingjiang
 */
public interface SysCategoryPermissionService {

    List<SysCategoryPermissionVo> listManagedSysCategory() throws ApplicationException;

    ResultPageData<SysCategoryPermissionVo> pageManagedSysCategory(Long byGroupId, String likeCategoryName, PageInfo pageInfo) throws ApplicationException;

    SysCategoryPermissionVo getManagedSysCategory(Long categoryId) throws ApplicationException;

    void updateManagedSysCategory(SysCategoryPermissionVo sysCategoryPermissionVo) throws ApplicationException;

    ResultPageData<SysCategoryPermissionVo> pageGrantedSysCategory(Long byGroupId, String likeCategoryName, PageInfo pageInfo) throws ApplicationException;

    List<SysGroupRespVo2> listGroup(Long categoryId) throws ApplicationException;

    SysCategoryPermissionVo generateForm(SysCategoryPermissionFormReq req) throws ApplicationException;

}
