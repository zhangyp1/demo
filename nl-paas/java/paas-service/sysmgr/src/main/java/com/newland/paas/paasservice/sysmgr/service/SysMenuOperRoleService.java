/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;

/**
 * 描述
 * @author caifeitong
 * @created 2018年6月26日 下午3:29:31
 */
public interface SysMenuOperRoleService {

    void assignAndCorrectMenuPrivileges(Long roleId, List<Long> menuIdList) throws ApplicationException;

    void assignMenuPrivileges(Long roleId, List<Long> menuIdList, Boolean isRefreshApiAuthCache) throws ApplicationException;

    void correctMenuPrivileges(long parentRoleId, Boolean isRefreshApiAuthCache) throws ApplicationException;

    List<Long> getSelectMenuIdList(Long roleId) throws ApplicationException;

    void deleteByRoleId(Long roleId) throws ApplicationException;

    void deleteByMenuId(Long menuId) throws ApplicationException;
}

