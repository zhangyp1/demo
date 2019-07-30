package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 工组角色菜单权限请求Vo
 *
 * @author zhongqingjiang
 */
public class SysGroupRoleMenuReqVo implements Serializable {

    private Long groupRoleId;

    private List<Long> menuIdList;

    public Long getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
