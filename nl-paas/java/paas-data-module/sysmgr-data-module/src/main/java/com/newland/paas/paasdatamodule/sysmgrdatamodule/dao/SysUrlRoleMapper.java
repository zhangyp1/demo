package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUrlRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleRespBo;

public interface SysUrlRoleMapper {
    int countBySelective(SysUrlRoleReqBo record);

    int deleteBySelective(SysUrlRoleReqBo record);
    
    List<SysUrlRoleRespBo> selectBySelective(SysUrlRoleReqBo record);

    
    SysUrlRoleRespBo selectByPrimaryKey(Long groupId);
    int deleteByPrimaryKey(Long groupId);
    

    int insert(SysUrlRole record);

    int insertSelective(SysUrlRole record);

    int updateByPrimaryKeySelective(SysUrlRole record);

    int updateByPrimaryKey(SysUrlRole record);
    
    List<SysUrlRoleRespBo> getUrlsByRole(@Param("roleId")Long roleId);
    
}