package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色
 *
 * @author zhongqingjiang
 */
public interface SysRoleMapper {
    int countBySelective(SysRole record);

    int deleteBySelective(SysRole record);

    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    List<SysRole> selectBySelective(SysRole record);

    List<SysRole> selectForTenant(SysRole record);

    List<SysRole> selectForAdmin(SysRole record);

    List<SysRole> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    SysRole selectByPrimaryKey(Long roleId);

    List<SysRole> selectByMenuOperId(@Param("tenantId") Long tenantId, @Param("menuOperId") Long menuOperId);

    List<SysRole> selectByMenuOperCode(@Param("tenantId") Long tenantId, @Param("menuOperCode") String menuOperCode);

    List<SysRole> listSysRoleByUserId(@Param("userId") Long userId);

    List<SysRole> listSysRole(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<Long> listRoleIdOfHasChild();
}