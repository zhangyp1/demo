package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOperRole;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SysMenuOperRoleMapper {
    int countBySelective(SysMenuOperRole record);

    SysMenuOperRole selectByPrimaryKey(Long id);

    List<SysMenuOperRole> selectBySelective(SysMenuOperRole record);

    List<SysMenuOperRole> selectByRoleId(@Param("roleId") Long roleId);

    int deleteByPrimaryKey(Long id);

    int deleteBySelective(SysMenuOperRole record);

    int insert(SysMenuOperRole record);

    int insertSelective(SysMenuOperRole record);

    int updateByPrimaryKeySelective(SysMenuOperRole record);

    int batchInsert(@Param("list") List<Long> menuIdList, @Param("roleId") Long roleId);
}