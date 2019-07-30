package com.newland.paas.configtool.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.configtool.common.PageInfo;
import com.newland.paas.configtool.dao.ConfigPropertiesMapper;
import com.newland.paas.configtool.model.ConfigProperties;
import com.newland.paas.configtool.service.ConfigPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wrp
 * @Date 2017/1/13
 */
@Service
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {

    @Autowired
    private ConfigPropertiesMapper configPropertiesMapper;

    @Override
    public void findConfigProperties(PageInfo pageInfo, ConfigProperties configProperties) {
        Page<?> page = PageHelper.startPage(pageInfo.getPageNumber(), pageInfo.getPageSize());
        pageInfo.setRows(configPropertiesMapper.selectBySelective(configProperties));
        pageInfo.setTotal(page.getTotal());
    }

    @Override
    public void createConfigProperties(ConfigProperties configProperties) {
        configProperties.setLabel("master");
        configProperties.setCreateDate(new Date());
        configPropertiesMapper.insert(configProperties);
    }

    @Override
    public void updateConfigProperties(ConfigProperties configProperties) {
        configProperties.setChangeDate(new Date());
        configPropertiesMapper.updateById(configProperties);
    }

    @Override
    public void deleteConfigProperties(List<Long> configIds) {
        for (Long configId : configIds) {
            ConfigProperties configProperties = new ConfigProperties();
            configProperties.setConfigId(configId);
            configPropertiesMapper.deleteBySelective(configProperties);
        }
    }

    @Override
    public List<String> getDistinctProfile() {
        return configPropertiesMapper.selectDistinctProfile();
    }

    @Override
    public List<String> getDistinctApplication() {
        return configPropertiesMapper.selectDistinctApplication();
    }

    @Override
    public void batchUpdateProfile(String currentProfile, String deployProfile) {
        configPropertiesMapper.batchUpdateProfile(currentProfile, deployProfile);
    }

}
