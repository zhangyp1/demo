package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysEnvConfigMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysEnvConfig;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysEnvConfigBo;
import com.newland.paas.paasservice.sysmgr.service.SysEnvConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-10-11 10:37
 **/
@Service("sysEnvConfigService")
public class SysEnvConfigServiceImpl implements SysEnvConfigService {
    @Autowired
    private SysEnvConfigMapper sysEnvConfigMapper;
    /**
     * 获取系统环境配置列表
     *
     * @return
     */
    @Override
    public List<SysEnvConfigBo> listSysEnvConfig() {
        SysEnvConfig config = new SysEnvConfig();
        List<SysEnvConfig> list = sysEnvConfigMapper.selectBySelective(config);

        return list.stream().map(item ->
                new SysEnvConfigBo(item.getEnvConfigKey(), item.getEnvConfigValue()))
                .collect(Collectors.toList());
    }

    /**
     * 根据key获取系统环境配置
     *
     * @param key
     * @return
     */
    @Override
    public SysEnvConfigBo getSysEnvConfigValue(String key) {
        SysEnvConfig config = new SysEnvConfig();
        config.setEnvConfigKey(key);
        List<SysEnvConfig> list = sysEnvConfigMapper.selectBySelective(config);
        return list == null || list.isEmpty() ? new SysEnvConfigBo()
                : new SysEnvConfigBo(list.get(0).getEnvConfigKey(), list.get(0).getEnvConfigValue());
    }
}
