package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRoleSysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysGroupRoleError;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午4:44:53
 */
@Service("sysGroupRoleService")
public class SysGroupRoleServiceImpl implements SysGroupRoleService {

    @Autowired
    private SysGroupRoleMapper sysGroupRoleMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysGroupRoleSysRoleService sysGroupRoleSysRoleService;
    @Autowired
    private SysMenuOperService sysMenuOperService;
    @Autowired
    private SysMenuOperRoleService sysMenuOperRoleService;
    @Autowired
    private SysUserGroupRoleService sysUserGroupRoleService;
    @Autowired
    private SysCategoryEmpowerService sysCategoryEmpowerService;
    @Autowired
    private SysObjService sysObjService;

    @Override
    public int groupCount() throws ApplicationException {
        return sysGroupRoleMapper.countBySelective(null);
    }

    @Override
    public ResultPageData<List<SysGroupRoleRespBo>> page(SysGroupRoleReqBo sysGroupRoleReqBo, PageInfo pageInfo) {
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysGroupRoleRespBo> rrs = sysGroupRoleMapper.selectBySelective(sysGroupRoleReqBo);
        pageInfo.setTotalRecord(page.getTotal());
        return new ResultPageData(rrs, pageInfo);
    }

    @Override
    public List<SysGroupRoleRespBo> list(SysGroupRoleReqBo sysGroupRoleReqBo) throws ApplicationException {
        return sysGroupRoleMapper.selectBySelective(sysGroupRoleReqBo);
    }

    @Override
    public List<SysGroupRole> listByGroupIds(List<Long> groupIds) throws ApplicationException {
        return sysGroupRoleMapper.selectByGroupIds(groupIds);
    }

    @Override
    public List<SysGroupRole> listMyGroupRoleList(Long groupId) throws ApplicationException {
        List<SysGroupRole> myGroupRoleList = new ArrayList<>();
        Long userId = RequestContext.getUserId();
        List<SysUserGroupRoleRespBo> sysUserGroupRoleRespBos = sysUserGroupRoleService.getGroupRolesByUser(userId, groupId);
        for (SysUserGroupRoleRespBo sysUserGroupRoleRespBo : sysUserGroupRoleRespBos) {
            SysGroupRole sysGroupRole = sysGroupRoleMapper.selectByPrimaryKey(sysUserGroupRoleRespBo.getGroupRoleId());
            if (sysGroupRole != null) {
                myGroupRoleList.add(sysGroupRole);
            }
        }
        return myGroupRoleList;
    }

    @Override
    public List<SysGroupRole> listMyGroupRoleList(Boolean includeAdmin) throws ApplicationException {
        List<SysGroupRole> myGroupRoleList = new ArrayList<>();
        Long userId = RequestContext.getUserId();
        Long tenantId = RequestContext.getTenantId();
        List<SysGroupRole> list = sysGroupRoleMapper.selectMyGroupRole(userId, tenantId);
        if (list != null && list.size() > 0) {
            for (SysGroupRole sysGroupRole : list) {
                if (includeAdmin == false && sysGroupRole.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                    continue;
                }
                myGroupRoleList.add(sysGroupRole);
            }
        }
        return myGroupRoleList;
    }

    @Override
    public void add(SysGroupRole sysGroupRole) throws ApplicationException {
        sysGroupRole.setCreatorId(RequestContext.getUserId());
        sysGroupRole.setTenantId(RequestContext.getTenantId());
        SysGroup group = sysGroupMapper.selectByPrimaryKey(sysGroupRole.getGroupId());
        if (group == null) {
            throw new ApplicationException(SysGroupRoleError.NOEXIST_GROUP);
        }
        // 新增工组角色
        SysGroupRoleReqBo nameCountQuery = new SysGroupRoleReqBo();
        nameCountQuery.setGroupRoleName(sysGroupRole.getGroupRoleName());
        nameCountQuery.setGroupId(sysGroupRole.getGroupId());
        if (sysGroupRoleMapper.countBySelective(nameCountQuery) > 0) {
            throw new ApplicationException(SysGroupRoleError.DUPLICATE_SYS_GROUP_ROLE_ERROR);
        }
        sysGroupRoleMapper.insertSelective(sysGroupRole);
        // 新增系统角色
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(sysGroupRole.getGroupRoleName());
        sysRole.setDescription("工组ID：" + sysGroupRole.getGroupId());
        Long parentRoleId;
        Long templateRoleId;
        switch (sysRole.getRoleName()) {
            case SysMgrConstants.ROLE_NAME_GROUP_ADMIN:
                parentRoleId = sysRoleService.getGroupAdminRoleId();
                templateRoleId = sysRoleService.getGroupAdminRoleId();
                sysRole.setParentRoleId(parentRoleId);
                sysRoleService.insertGroupRole(sysRole, templateRoleId);
                break;
            case SysMgrConstants.ROLE_NAME_GROUP_OPERATOR:
                parentRoleId = sysRoleService.getGroupOperatorRoleId();
                templateRoleId = sysRoleService.getGroupOperatorRoleId();
                sysRole.setParentRoleId(parentRoleId);
                sysRoleService.insertGroupRole(sysRole, templateRoleId);
                break;
            case SysMgrConstants.ROLE_NAME_GROUP_OBSERVER:
                parentRoleId = sysRoleService.getGroupObserverRoleId();
                templateRoleId = sysRoleService.getGroupObserverRoleId();
                sysRole.setParentRoleId(parentRoleId);
                sysRoleService.insertGroupRole(sysRole, templateRoleId);
                break;
            default:
                parentRoleId = sysRoleService.getGroupOperatorRoleId();
                sysRole.setParentRoleId(parentRoleId);
                // 非默认工组角色，至少拥有 公共权限 菜单
                List<Long> menuIdList = new ArrayList<>();
                menuIdList.add(0L);
                sysRoleService.insertGroupRole(sysRole, menuIdList);
        }
        // 新增系统角色与工组角色的关系
        SysGroupRoleSysRole sysGroupRoleSysRole = new SysGroupRoleSysRole();
        sysGroupRoleSysRole.setGroupRoleId(sysGroupRole.getGroupRoleId());
        sysGroupRoleSysRole.setRoleId(sysRole.getRoleId());
        sysGroupRoleSysRoleService.add(sysGroupRoleSysRole);
    }

    @Override
    public void update(SysGroupRole sysGroupRole) throws ApplicationException {
        SysGroup group = sysGroupMapper.selectByPrimaryKey(sysGroupRole.getGroupId());
        if (group == null) {
            throw new ApplicationException(SysGroupRoleError.NOEXIST_GROUP);
        }

        SysGroupRoleRespBo sysGroupRoleRespBo = this.get(sysGroupRole.getGroupRoleId());
        if (sysGroupRoleRespBo == null) {
            throw new ApplicationException(SysGroupRoleError.GROUP_ROLE_NOT_EXIST);
        }
        if (sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)
                || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR)
                || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER)) {
            throw new ApplicationException(SysGroupRoleError.DEFAULT_GROUP_ROLE_CANNOT_UPDATE);
        }

        SysGroupRoleReqBo nameCountQuery = new SysGroupRoleReqBo();
        nameCountQuery.setGroupRoleName(sysGroupRole.getGroupRoleName());
        nameCountQuery.setNeqId(sysGroupRole.getGroupId());
        nameCountQuery.setGroupId(sysGroupRole.getGroupId());
        if (sysGroupRoleMapper.countBySelective(nameCountQuery) > 0) {
            throw new ApplicationException(SysGroupRoleError.DUPLICATE_SYS_GROUP_ROLE_ERROR);
        }

        sysGroupRoleMapper.updateByPrimaryKeySelective(sysGroupRole);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(Long id, Boolean isForceDelete) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException(SysGroupRoleError.GROUPROLE_ID_NOT_NULL);
        }
        SysGroupRoleRespBo sysGroupRoleRespBo = this.get(id);
        if (sysGroupRoleRespBo != null) {
            if (!isForceDelete) {
                if (sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)
                        || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR)
                        || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER)) {
                    throw new ApplicationException(SysGroupRoleError.DEFAULT_GROUP_ROLE_CANNOT_DELETE);
                }
                SysUserGroupRoleReqBo userGroupRolesCountQuery = new SysUserGroupRoleReqBo();
                userGroupRolesCountQuery.setGroupRoleId(id);
                if (sysUserGroupRoleMapper.countBySelective(userGroupRolesCountQuery) > 0) {
                    throw new ApplicationException(SysGroupRoleError.HAS_USER_CANNOT_DELETE);
                }
            }
            // 删除工组角色
            sysGroupRoleMapper.deleteByPrimaryKey(id);
            // 删除用户与工组角色关系
            SysUserGroupRoleReqBo userGroupRoleParams = new SysUserGroupRoleReqBo();
            userGroupRoleParams.setGroupRoleId(id);
            List<SysUserGroupRoleRespBo> sysUserGroupRoleRespBos = sysUserGroupRoleService.list(userGroupRoleParams);
            for (SysUserGroupRoleRespBo sysUserGroupRoleRespBo : sysUserGroupRoleRespBos) {
                Long userGroupRoleId = sysUserGroupRoleRespBo.getUserGroupRoleId();
                sysUserGroupRoleService.delete(userGroupRoleId);
            }
            // 删除工组角色与系统角色的关系
            SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByGroupRoleId(id);
            if (sysGroupRoleSysRole != null) {
                sysGroupRoleSysRoleService.delete(sysGroupRoleSysRole.getGroupRoleSysRoleId());
                // 删除对应的系统角色
                sysRoleService.deleteGroupRole(sysGroupRoleSysRole.getRoleId());
            }
            // 删除系统分组权限赋权
            sysCategoryEmpowerService.deleteByGroupRoleId(id);
            // 删除对象权限授权
            sysObjService.deleteFrightByGroupRoleId(id);
        }
    }

    @Override
    public SysGroupRoleRespBo get(Long id) throws ApplicationException {
        return sysGroupRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MenuBO> listOptionalMenuList(Long groupRoleId) throws ApplicationException {
        // 获取所有菜单
        List<MenuBO> allMenuList = sysMenuOperService.getMenusList();
        // 过滤掉隐藏的菜单
        List<MenuBO> visibleMenuList = new ArrayList<>();
        for (MenuBO menuBO : allMenuList) {
            if ("true".equals(menuBO.getHidden())) {
                continue;
            }
            visibleMenuList.add(menuBO);
        }
        // 获取可选的菜单ID列表
        List<Long> optionalMenuIdList = this.listOptionalMenuId();
        // 过滤菜单
        List<MenuBO> optionalMenuList = new ArrayList<>();
        for (MenuBO menuBO : visibleMenuList) {
            if (optionalMenuIdList.contains(menuBO.getMenuId())) {
                optionalMenuList.add(menuBO);
            }
        }
        return optionalMenuList;
    }

    private List<Long> listOptionalMenuId() throws ApplicationException {
        // 获取工组操作员模板的菜单
        List<SysRole> groupRoleTemplateList = sysRoleService.listGroupRoleTemplate();
        Long roleId = null;
        for (SysRole sysRole : groupRoleTemplateList) {
            if (sysRole.getRoleName().contains(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR)) {
                roleId = sysRole.getRoleId();
                break;
            }
        }
        List<Long> optionalMenuIdList = sysMenuOperRoleService.getSelectMenuIdList(roleId);
        // 特殊菜单处理，过滤掉菜单ID为0的 公共权限 菜单
        optionalMenuIdList.remove(0L);
        return optionalMenuIdList;
    }

    @Override
    public List<Long> listSelectedMenuId(Long groupRoleId) throws ApplicationException {
        SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByGroupRoleId(groupRoleId);
        Long roleId = sysGroupRoleSysRole.getRoleId();
        List<Long> selectedMenuIdList = sysMenuOperRoleService.getSelectMenuIdList(roleId);
        return selectedMenuIdList;
    }

    @Override
    public void assignMenu(Long groupRoleId, List<Long> menuIdList) throws ApplicationException {
        SysGroupRoleRespBo sysGroupRoleRespBo = this.get(groupRoleId);
        if (sysGroupRoleRespBo == null) {
            throw new ApplicationException(SysGroupRoleError.GROUP_ROLE_NOT_EXIST);
        }
        if (sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)
                || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR)
                || sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER)) {
            throw new ApplicationException(SysGroupRoleError.DEFAULT_GROUP_ROLE_CANNOT_UPDATE);
        }
        // 检查是否超过可选菜单范围
        List<Long> optionalMenuIdList = this.listOptionalMenuId();
        for (Long menuId : menuIdList) {
            if (!optionalMenuIdList.contains(menuId)) {
                throw new ApplicationException(SysGroupRoleError.OVER_OPTIONAL_MENU_SCOPE);
            }
        }
        SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByGroupRoleId(groupRoleId);
        Long roleId = sysGroupRoleSysRole.getRoleId();
        sysMenuOperRoleService.assignAndCorrectMenuPrivileges(roleId, menuIdList);
    }
}

