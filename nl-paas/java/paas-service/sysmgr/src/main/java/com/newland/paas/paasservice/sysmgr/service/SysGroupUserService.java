/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 * 
 * @author linkun
 * @created 2018-08-01 13:57:24
 */
public interface SysGroupUserService {

    ResultPageData<List<SysGroupUserRespBo>> pageUsersByGroupId(Long groupId, PageInfo pageInfo);

    List<SysGroupUserRespBo> list(SysGroupUserReqBo params) throws ApplicationException;

    int add(SysGroupUser params) throws ApplicationException;

    int update(SysGroupUser params) throws ApplicationException;

    int delete(Long id) throws ApplicationException;

    SysGroupUserRespBo get(Long id) throws ApplicationException;

    int addUsers(SysGroupUserReqBo params) throws ApplicationException;

    int addGroups(SysGroupUserReqBo params) throws ApplicationException;

    List<SysGroupUserRespBo> getGroupsByUser(Long userId);

    List<SysGroupUserRespBo> getAllUsersByTenant(SysGroupUserReqBo params);

    List<SysGroupUserRespBo> getAllGroupsByTenant(SysGroupUserReqBo params);

    int setAdmin(SysGroupUserReqBo params) throws ApplicationException;

    List<SysGroupUserRespBo> getGroupsByTenantAndUser(Long tenantId, Long userId);

	int deleteBySelective(SysGroupUserReqBo groupUserParams);

    boolean isGroupAdmin(Long groupId, Long userId);

}
