package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysInterfaceUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;

import java.util.List;

public interface SysInterfaceUrlMapper {
    int countBySelective(SysInterfaceUrl record);

    int deleteBySelective(SysInterfaceUrl record);

    int deleteByPrimaryKey(String urlId);

    int insert(SysInterfaceUrl record);

    int insertSelective(SysInterfaceUrl record);

    List<SysInterfaceUrl> selectBySelective(SysInterfaceUrl record);

    SysInterfaceUrl selectByPrimaryKey(String urlId);

    int updateByPrimaryKeySelective(SysInterfaceUrl record);

    int updateByPrimaryKey(SysInterfaceUrl record);

    List<SysInterfaceUrl> selectBySelectiveNotExist(SysMenuUrlBO record);

}