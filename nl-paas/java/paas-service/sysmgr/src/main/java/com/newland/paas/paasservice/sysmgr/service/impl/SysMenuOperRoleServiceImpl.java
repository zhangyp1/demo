package com.newland.paas.paasservice.sysmgr.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.newland.paas.paasservice.sysmgr.runner.RoleApiCacheRefresher;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOperRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.runner.InitializeRunner;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 * 菜单操作与角色关系实现类
 *
 * @author caifeitong
 * @author zhongqingjiang
 */
@Service("sysMenuOperRoleService")
public class SysMenuOperRoleServiceImpl implements SysMenuOperRoleService {
    private static final Log LOGGER = LogFactory.getLogger(SysMenuOperRoleServiceImpl.class);

    @Autowired
    private SysMenuOperRoleMapper sysMenuOperRoleMapper;
    @Autowired
    private SysMenuOperMapper sysMenuOperMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private RoleApiCacheRefresher roleApiCacheRefresher;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void assignAndCorrectMenuPrivileges(Long roleId, List<Long> menuIdList) throws ApplicationException {
        assignMenuPrivileges(roleId, menuIdList, true);
        correctMenuPrivileges(roleId, true);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void assignMenuPrivileges(Long roleId, List<Long> menuIdList, Boolean isRefreshApiAuthCache) throws ApplicationException {
        if (menuIdList == null) {
            menuIdList = new ArrayList<>();
        }
        // 特殊菜单处理，自动加上菜单ID为0的 公共权限 菜单
        if (!menuIdList.contains(0L)) {
            menuIdList.add(0L);
        }
        SysMenuOperRole sysMenuOperRole = new SysMenuOperRole();
        sysMenuOperRole.setRoleId(roleId);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL, "请求的角色ID:", String.valueOf(roleId));
        sysMenuOperRoleMapper.deleteBySelective(sysMenuOperRole);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL, "批量新增菜单列表:" + menuIdList);
        List<List<Long>> menuIdListPartition = ListUtils.partition(menuIdList, 900);
        List<Long> allMenuIdList4Tree = new ArrayList<>();
        menuIdListPartition.forEach(list -> {
            List<Long> allMenuIdList4TreeTemp = sysMenuOperMapper.selectAllMenuIdList4Tree(list);
            if (!CollectionUtils.isEmpty(allMenuIdList4TreeTemp)) {
                allMenuIdList4Tree.addAll(allMenuIdList4TreeTemp);
            }
        });
        if (allMenuIdList4Tree != null && !allMenuIdList4Tree.isEmpty()) {
            sysMenuOperRoleMapper.batchInsert(allMenuIdList4Tree, roleId);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                if (isRefreshApiAuthCache) {
                    roleApiCacheRefresher.refreshApiAuthCacheByRoleId(roleId);
                }
            }
        });
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void correctMenuPrivileges(long parentRoleId, Boolean isRefreshApiAuthCache) throws ApplicationException {
        // 根据父角色的菜单操作，修正子角色的菜单操作
        List<Long> parentRoleMenuIdList = this.getSelectMenuIdList(parentRoleId);
        SysRole req = new SysRole();
        req.setParentRoleId(parentRoleId);
        List<SysRole> sysRoleList = sysRoleMapper.selectBySelective(req);
        if (sysRoleList != null && sysRoleList.size() > 0) {
            for (SysRole sysRole : sysRoleList) {
                List<Long> correctedMenuIdList = new ArrayList<>();
                List<Long> currentMenuIdList = this.getSelectMenuIdList(sysRole.getRoleId());
                if (sysRole.getRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)
                    || sysRole.getRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR)
                    || sysRole.getRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER)) {
                    // 默认工组角色，与父角色保持一致
                    correctedMenuIdList = parentRoleMenuIdList;
                } else {
                    // 非默认工组角色，则收回父角色不再拥有的菜单权限
                    for (Long menuId : currentMenuIdList) {
                        if (parentRoleMenuIdList.contains(menuId)) {
                            correctedMenuIdList.add(menuId);
                        }
                    }
                }
                // 检查与当前菜单权限是否一致
                Boolean isSame = true;
                if (correctedMenuIdList.size() != currentMenuIdList.size()) {
                    isSame = false;
                } else {
                    for (Long menuId : correctedMenuIdList) {
                        if (!currentMenuIdList.contains(menuId)) {
                            isSame = false;
                            break;
                        }
                    }
                }
                if (!isSame) {
                    this.assignMenuPrivileges(sysRole.getRoleId(), correctedMenuIdList, isRefreshApiAuthCache);
                }
            }
        }
    }

    @Override
    public List<Long> getSelectMenuIdList(Long roleId) {
        List<Long> idList = new ArrayList<>();
        List<SysMenuOperRole> list = sysMenuOperRoleMapper.selectByRoleId(roleId);
        if (list != null && !list.isEmpty()) {
            for (SysMenuOperRole s : list) {
                idList.add(s.getMenuOperId());
            }
        }
        return idList;
    }

    @Override
    public void deleteByRoleId(Long roleId) throws ApplicationException {
        SysMenuOperRole sysMenuOperRole = new SysMenuOperRole();
        sysMenuOperRole.setRoleId(roleId);
        sysMenuOperRoleMapper.deleteBySelective(sysMenuOperRole);
    }

    @Override
    public void deleteByMenuId(Long menuId) throws ApplicationException {
        SysMenuOperRole sysMenuOperRole = new SysMenuOperRole();
        sysMenuOperRole.setMenuOperId(menuId);
        sysMenuOperRoleMapper.deleteBySelective(sysMenuOperRole);
    }
}
