package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjFright;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjFrightBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysObjFrightMapper {
    int countBySelective(SysObjFright record);

    int deleteBySelective(SysObjFright record);

    int deleteByPrimaryKey(Long objFrightId);

    int deleteByGroupRoleId(@Param("groupRoleId") Long groupRoleId);

    int insert(SysObjFright record);

    int insertSelective(SysObjFright record);

    List<SysObjFright> selectBySelective(SysObjFright record);

    SysObjFright selectByPrimaryKey(Long objFrightId);

    int updateByPrimaryKeySelective(SysObjFright record);

    int updateByPrimaryKey(SysObjFright record);

    List<SysObjFrightBO> selectGroupRoleDetail(SysObjFrightBO objFrightBOParams);
}