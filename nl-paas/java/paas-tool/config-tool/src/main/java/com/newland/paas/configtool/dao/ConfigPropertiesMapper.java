package com.newland.paas.configtool.dao;


import com.newland.paas.configtool.model.ConfigProperties;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigPropertiesMapper {

    int deleteBySelective(ConfigProperties record);

    int insert(ConfigProperties record);

    int insertSelective(ConfigProperties record);

    int updateById(ConfigProperties record);

    List<ConfigProperties> selectBySelective(ConfigProperties record);

    List<String> selectDistinctProfile();

    List<String> selectDistinctApplication();

    void batchUpdateProfile(@Param("currentProfile") String currentProfile, @Param("deployProfile") String deployProfile);
}