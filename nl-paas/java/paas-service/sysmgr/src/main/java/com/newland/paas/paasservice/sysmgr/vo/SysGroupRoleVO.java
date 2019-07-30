package com.newland.paas.paasservice.sysmgr.vo;

/**
 * @Description: 成员所属工组分页列表查询返回参数中的工组角色VO
 * @Author: SongYJ
 * @Date: 2018/8/6
 */
public class SysGroupRoleVO {

    private Long roleId;

    private String roleName;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
