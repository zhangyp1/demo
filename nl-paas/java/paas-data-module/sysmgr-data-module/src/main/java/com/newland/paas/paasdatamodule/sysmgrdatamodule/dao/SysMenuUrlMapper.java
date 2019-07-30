package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuUrlMapper {
    int countBySelective(SysMenuUrl record);

    int deleteBySelective(SysMenuUrl record);

    int insert(SysMenuUrl record);

    int insertSelective(SysMenuUrl record);

    List<SysMenuUrl> selectBySelective(SysMenuUrl record);

    List<SysMenuUrlBO> selectSysMenuUrlBOBySelective(SysMenuUrlBO sysMenuUrlBO);

    List<SysMenuUrlBO> selectSysMenuUrlBOByMenuIds(@Param("menuIds") List<Long> menuIds);
}