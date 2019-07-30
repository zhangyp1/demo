package com.newland.paas.configtool.service;

import com.newland.paas.configtool.common.PageInfo;
import com.newland.paas.configtool.model.ConfigProperties;

import java.util.List;

/**
 * 配置管理
 *
 * @author wrp
 * @Date 2017/1/13
 */
public interface ConfigPropertiesService {

    /**
     * 查询
     *
     * @param pageInfo
     * @param configProperties
     */
    void findConfigProperties(PageInfo pageInfo, ConfigProperties configProperties);

    /**
     * 创建
     *
     * @param configProperties
     */
    void createConfigProperties(ConfigProperties configProperties);

    /**
     * 修改
     *
     * @param configProperties
     */
    void updateConfigProperties(ConfigProperties configProperties);

    /**
     * 删除
     *
     * @param configIds
     */
    void deleteConfigProperties(List<Long> configIds);

    /**
     * 查询所有环境
     *
     * @return
     */
    List<String> getDistinctProfile();

    /**
     * 查询所有应用名
     *
     * @return
     */
    List<String> getDistinctApplication();

    /**
     * 批量修改环境
     *
     * @param currentProfile
     * @param deployProfile
     */
    void batchUpdateProfile(String currentProfile, String deployProfile);
}
