/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;
import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 * @author linkun
 * @created 2018年6月26日 下午4:42:06
 */
public interface SysGroupRoleService {

    int groupCount() throws ApplicationException;

    ResultPageData<List<SysGroupRoleRespBo>> page(SysGroupRoleReqBo sysGroupRoleReqBo, PageInfo pageInfo) throws ApplicationException;

    List<SysGroupRoleRespBo> list(SysGroupRoleReqBo sysGroupRoleReqBo) throws ApplicationException;

    List<SysGroupRole> listByGroupIds(List<Long> groupIds) throws ApplicationException;

    List<SysGroupRole> listMyGroupRoleList(Long groupId) throws ApplicationException;

    List<SysGroupRole> listMyGroupRoleList(Boolean includeAdmin) throws ApplicationException;

    void add(SysGroupRole sysGroupRole) throws ApplicationException;

    void update(SysGroupRole sysGroupRole) throws ApplicationException;

    void delete(Long id, Boolean isForceDelete) throws ApplicationException;

    SysGroupRoleRespBo get(Long id) throws ApplicationException;

    List<MenuBO> listOptionalMenuList(Long groupRoleId) throws ApplicationException;

    List<Long> listSelectedMenuId(Long groupRoleId) throws ApplicationException;

    void assignMenu(Long groupRoleId, List<Long> menuIdList) throws ApplicationException;
    
}

