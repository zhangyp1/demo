package com.newland.paas.microservicemanager.configcenter.service.impl;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.microservicemanager.configcenter.service.ConfigPropertiesService;
import com.newland.paas.microservicemanager.configcenter.utils.ConfigBean;
import com.newland.paas.microservicemanager.configcenter.utils.PropertiesUtils;
import com.newland.paas.paasdatamodule.configcenterdatamodule.dao.ConfigPropertiesMapper;
import com.newland.paas.paasdatamodule.configcenterdatamodule.model.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author WRP
 * @since 2018/11/28
 */
@Service
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {

    private static final Log LOG = LogFactory.getLogger(ConfigPropertiesServiceImpl.class);

    @Autowired
    private ConfigPropertiesMapper configPropertiesMapper;

    private static final String CONFIG_PATH = "classpath:config-bak/";
    private static final String FILE_TYPE = ".yml";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<String> syncProperties2DB(List<String> applicationNames, String active) {
        List<String> configFiles = new ArrayList<>();
        for (String applicationName : applicationNames) {
            ConfigBean configBean = PropertiesUtils.readProperties(CONFIG_PATH + applicationName + FILE_TYPE, active);
            Properties properties = configBean.getProperties();

            Enumeration<?> e = properties.propertyNames();
            ConfigProperties params = new ConfigProperties();
            params.setApplication(applicationName);
            params.setLabel("master");
            params.setProfile(active);
            configPropertiesMapper.deleteBySelective(params);
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                LOG.info(LogProperty.LOGTYPE_DETAIL, key + "=" + value);
                ConfigProperties configProperties = new ConfigProperties();
                configProperties.setConfigKey(key);
                configProperties.setConfigValue(value);
                configProperties.setApplication(applicationName);
                configProperties.setLabel("master");
                configProperties.setProfile(active);
                configPropertiesMapper.insertSelective(configProperties);
            }
            configFiles.addAll(configBean.getConfigFiles());
        }

        return configFiles;
    }

}
