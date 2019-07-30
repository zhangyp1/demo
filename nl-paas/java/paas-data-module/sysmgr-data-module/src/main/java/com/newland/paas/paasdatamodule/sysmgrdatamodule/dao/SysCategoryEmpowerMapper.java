package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryEmpower;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryGrant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统分组赋权
 *
 * @author zhongqingjiang
 */
public interface SysCategoryEmpowerMapper {
    int countBySelective(SysCategoryEmpower record);

    int deleteBySelective(SysCategoryEmpower record);

    int deleteByPrimaryKey(Long categoryEmpowerId);

    int deleteByGroupRoleId(@Param("groupRoleId") Long groupRoleId);

    int deleteByGroupRoleIds(@Param("groupRoleIds") List<Long> groupRoleIds);

    int insert(SysCategoryEmpower record);

    int insertSelective(SysCategoryEmpower record);

    List<SysCategoryEmpower> selectBySelective(SysCategoryEmpower record);

    SysCategoryEmpower selectByPrimaryKey(Long categoryEmpowerId);

    List<SysCategoryEmpower> selectByGroupRoleIds(@Param("groupRoleIds") List<Long> groupRoleIds);

    int updateByPrimaryKeySelective(SysCategoryEmpower record);

    int updateByPrimaryKey(SysCategoryEmpower record);
}