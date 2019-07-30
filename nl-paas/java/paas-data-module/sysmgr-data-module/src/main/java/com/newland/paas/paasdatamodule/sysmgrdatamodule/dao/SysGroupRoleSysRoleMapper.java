package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRoleSysRole;

import java.util.List;

/**
 * 工组角色与系统角色关系
 *
 * @author zhongqingjiang
 */
public interface SysGroupRoleSysRoleMapper {
    int countBySelective(SysGroupRoleSysRole record);

    int deleteBySelective(SysGroupRoleSysRole record);

    int deleteByPrimaryKey(Long groupRoleSysRoleId);

    int insert(SysGroupRoleSysRole record);

    int insertSelective(SysGroupRoleSysRole record);

    List<SysGroupRoleSysRole> selectBySelective(SysGroupRoleSysRole record);

    SysGroupRoleSysRole selectByPrimaryKey(Long groupRoleSysRoleId);

    int updateByPrimaryKeySelective(SysGroupRoleSysRole record);

    int updateByPrimaryKey(SysGroupRoleSysRole record);
}