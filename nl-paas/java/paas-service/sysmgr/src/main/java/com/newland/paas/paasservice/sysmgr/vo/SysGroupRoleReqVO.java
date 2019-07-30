package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 * @Description: 成员在工组中角色的修改接口请求参数VO
 * @Author: SongYJ
 * @Date: 2018/8/7
 */
public class SysGroupRoleReqVO {

    private Long userId;

    private Long groupId;

    private List<Long> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
