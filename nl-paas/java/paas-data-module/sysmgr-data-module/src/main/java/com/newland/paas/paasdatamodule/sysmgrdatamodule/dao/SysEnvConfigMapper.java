package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysEnvConfig;
import java.util.List;

public interface SysEnvConfigMapper {
    int countBySelective(SysEnvConfig record);

    int deleteBySelective(SysEnvConfig record);

    int deleteByPrimaryKey(Long envConfigId);

    int insert(SysEnvConfig record);

    int insertSelective(SysEnvConfig record);

    List<SysEnvConfig> selectBySelective(SysEnvConfig record);

    SysEnvConfig selectByPrimaryKey(Long envConfigId);

    int updateByPrimaryKeySelective(SysEnvConfig record);

    int updateByPrimaryKey(SysEnvConfig record);
}