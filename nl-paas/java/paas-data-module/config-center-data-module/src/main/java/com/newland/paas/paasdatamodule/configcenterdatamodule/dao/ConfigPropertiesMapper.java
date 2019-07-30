package com.newland.paas.paasdatamodule.configcenterdatamodule.dao;

import com.newland.paas.paasdatamodule.configcenterdatamodule.model.ConfigProperties;

import java.util.List;

public interface ConfigPropertiesMapper {

    int deleteBySelective(ConfigProperties record);

    int insert(ConfigProperties record);

    int insertSelective(ConfigProperties record);

    int updateById(ConfigProperties record);

    List<ConfigProperties> selectBySelective(ConfigProperties record);

}