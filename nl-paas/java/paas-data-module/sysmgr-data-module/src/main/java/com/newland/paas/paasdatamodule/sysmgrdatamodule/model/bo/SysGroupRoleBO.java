package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;

import java.util.List;

/**
 * @Description: 成员所属工组分页列表查询返回参数中的工组角色BO
 * @Author: SongYJ
 * @Date: 2018/8/6
 */
public class SysGroupRoleBO {

    private Long roleId;

    private String roleName;

    private List<GlbDict> operates;

    private List<GlbDict> selectOperates;

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

    public List<GlbDict> getOperates() {
        return operates;
    }

    public void setOperates(List<GlbDict> operates) {
        this.operates = operates;
    }

    public List<GlbDict> getSelectOperates() {
        return selectOperates;
    }

    public void setSelectOperates(List<GlbDict> selectOperates) {
        this.selectOperates = selectOperates;
    }
}
