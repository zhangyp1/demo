package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjSright;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjSrightBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysObjSrightMapper {
    int countBySelective(SysObjSright record);

    int deleteBySelective(SysObjSright record);

    int deleteByPrimaryKey(Long objFrightId);

    int deleteByGroupId(@Param("groupId") Long groupId);

    int insert(SysObjSright record);

    int insertSelective(SysObjSright record);

    List<SysObjSright> selectBySelective(SysObjSright record);

    SysObjSright selectByPrimaryKey(Long objFrightId);

    int updateByPrimaryKeySelective(SysObjSright record);

    int updateByPrimaryKey(SysObjSright record);

    List<SysObjSrightBO> selectGroupDetail(SysObjSrightBO params);

    List<SysObjSrightBO> selectObjSrightByParams(@Param(value = "objId") Long objId, @Param(value = "list") List<Long> groupIds);
}