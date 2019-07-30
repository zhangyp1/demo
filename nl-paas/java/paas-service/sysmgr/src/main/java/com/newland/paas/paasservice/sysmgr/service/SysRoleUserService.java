package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 * @author linkun
 * @created 2018-08-09 08:53:46
 */
public interface SysRoleUserService {

    ResultPageData<SysRoleUserRespBo> page(SysRoleUserReqBo params, PageInfo pageInfo) throws ApplicationException;

    List<SysRoleUserRespBo> list(SysRoleUserReqBo params) throws ApplicationException;

	int add(SysRoleUser params) throws ApplicationException;

	int update(SysRoleUser params) throws ApplicationException;

	int delete(Long id) throws ApplicationException;

	void deleteByRoleId(Long roleId) throws ApplicationException;

	void deleteByRoleIdAndUserId(Long roleId, Long userId) throws ApplicationException;

	SysRoleUserRespBo get(Long id) throws ApplicationException;

	List<SysRoleUserRespBo> getRoleUsersByTenant(Long tenantId);
    
}

