package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 * 用于记录分配给具体某个角色的菜单列表
 *
 * @author caifeitong
 * @date 2018/7/31 10:30
 */
public class SysMenuOperRoleVo {

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 选中的菜单ID列表
     */
    private List<Long> menuIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
