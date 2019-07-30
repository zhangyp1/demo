package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasservice.sysmgr.vo.InquireSysRolePageListVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 系统角色
 *
 * @author zhongqingjiang
 */
public interface SysRoleService {

    ResultPageData<SysRole> getAllSysRoleForTenant(BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException;

    ResultPageData<SysRole> getAllSysRoleForAdmin(BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException;

    List<SysRole> listSysRoleByUserId(Long userId);

    List<SysRole> listSysRole(Long tenantId, Long userId);

    /********** 工组角色模板 **********/

    List<SysRole> listGroupRoleTemplate() throws ApplicationException;

    SysRole getGroupRoleTemplate(String roleName) throws ApplicationException;

    Long getGroupAdminRoleId();

    Long getGroupOperatorRoleId();

    Long getGroupObserverRoleId();

    /********** 工组角色 **********/

    void insertGroupRole(SysRole sysRole) throws ApplicationException;

    void insertGroupRole(SysRole sysRole, List<Long> menuIdList) throws ApplicationException;

    void insertGroupRole(SysRole sysRole, Long templateRoleId) throws ApplicationException;

    void updateGroupRole(SysRole sysRole) throws ApplicationException;

    void deleteGroupRole(Long roleId) throws ApplicationException;

    SysRole getGroupRole(Long roleId) throws ApplicationException;

    List<SysRole> selectByParentRoleId(Long parentRoleId) throws ApplicationException;

    List<SysRole> selectByMenuOperId(Long menuOperId) throws ApplicationException;

    List<SysRole> selectByMenuOperCode(String menuOperCode) throws ApplicationException;
}
