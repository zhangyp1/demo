package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleRespBo;
import org.apache.ibatis.annotations.Param;

public interface SysGroupRoleMapper {
    int countBySelective(SysGroupRoleReqBo record);

    int deleteBySelective(SysGroupRoleReqBo record);
    
    List<SysGroupRoleRespBo> selectBySelective(SysGroupRoleReqBo record);

    SysGroupRoleRespBo selectByPrimaryKey(Long groupRoleId);

    List<SysGroupRole> selectByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<SysGroupRole> selectMyGroupRole(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    int deleteByPrimaryKey(Long groupRoleId);

    int insert(SysGroupRole record);

    int insertSelective(SysGroupRole record);

    int updateByPrimaryKeySelective(SysGroupRole record);

    int updateByPrimaryKey(SysGroupRole record);
    
}