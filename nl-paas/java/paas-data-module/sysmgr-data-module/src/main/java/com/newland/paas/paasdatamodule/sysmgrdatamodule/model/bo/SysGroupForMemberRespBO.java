package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

import java.util.List;

/**
 * @Description: 成员所属工组分页列表查询返回参数BO
 * @Author: SongYJ
 * @Date: 2018/8/6
 */
public class SysGroupForMemberRespBO extends SysGroup {

    //工组角色ID
    private Long groupRoleId;

    //工组角色名称
    private String groupRoleName;

    //工组成员id
    private Long userId;

    //工组角色列表
    private List<SysGroupRoleBO> sysGroupRoleBOs;

    // 租户名称
    private String tenantName;

    // 是否工组管理员
    private Integer isGroupAdmin;

    public Long getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<SysGroupRoleBO> getSysGroupRoleBOs() {
        return sysGroupRoleBOs;
    }

    public void setSysGroupRoleBOs(List<SysGroupRoleBO> sysGroupRoleBOs) {
        this.sysGroupRoleBOs = sysGroupRoleBOs;
    }


    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Integer getIsGroupAdmin() {
        return isGroupAdmin;
    }

    public void setIsGroupAdmin(Integer isGroupAdmin) {
        this.isGroupAdmin = isGroupAdmin;
    }
}
