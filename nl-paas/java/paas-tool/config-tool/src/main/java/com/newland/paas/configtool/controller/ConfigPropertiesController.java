package com.newland.paas.configtool.controller;

import com.alibaba.fastjson.JSON;
import com.newland.paas.configtool.common.Constans;
import com.newland.paas.configtool.common.PageInfo;
import com.newland.paas.configtool.common.ResultBean;
import com.newland.paas.configtool.model.ConfigProperties;
import com.newland.paas.configtool.service.ConfigPropertiesService;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理
 *
 * @author WRP
 * @since 2018/10/20
 */
@RestController
@RequestMapping(value = "config")
public class ConfigPropertiesController {

    private static final Log LOG = LogFactory.getLogger(ConfigPropertiesController.class);

    @Autowired
    private ConfigPropertiesService configPropertiesService;

    /**
     * 获取查询栏下拉数据
     *
     * @param configProperties
     * @return
     */
    @RequestMapping(value = "getSearchOptions", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, List<String>> getSearchOptions(ConfigProperties configProperties) {
        List<String> profiles = configPropertiesService.getDistinctProfile();
        List<String> applications = configPropertiesService.getDistinctApplication();
        Map<String, List<String>> result = new HashMap<>();
        result.put("profiles", profiles);
        result.put("applications", applications);
        return result;
    }

    /**
     * 列表查询
     *
     * @param pageInfo
     * @param configProperties
     * @return
     */
    @RequestMapping(value = "list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public PageInfo findConfigProperties(PageInfo pageInfo, ConfigProperties configProperties) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===findConfigProperties===pageInfo:"
                + JSON.toJSONString(pageInfo) + ",configProperties:" + JSON.toJSONString(configProperties));
        configPropertiesService.findConfigProperties(pageInfo, configProperties);
        return pageInfo;
    }

    /**
     * 新增
     *
     * @param configProperties
     * @return
     */
    @RequestMapping(value = "add", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultBean add(@Validated ConfigProperties configProperties) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===add===configProperties:" + JSON.toJSONString(configProperties));
        ResultBean rb = new ResultBean();
        configPropertiesService.createConfigProperties(configProperties);
        rb.setFlag(Constans.SUCCESS);
        return rb;
    }

    /**
     * 修改
     *
     * @param configProperties
     * @return
     */
    @RequestMapping(value = "update", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultBean update(ConfigProperties configProperties) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===update===configProperties:" + JSON.toJSONString(configProperties));
        ResultBean rb = new ResultBean();
        configPropertiesService.updateConfigProperties(configProperties);
        rb.setFlag(Constans.SUCCESS);
        return rb;
    }

    /**
     * 删除
     *
     * @param configIds
     * @return
     */
    @RequestMapping(value = "delete", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultBean delete(@RequestBody List<Long> configIds) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===delete===configIds:" + JSON.toJSONString(configIds));
        ResultBean rb = new ResultBean();
        configPropertiesService.deleteConfigProperties(configIds);
        rb.setFlag(Constans.SUCCESS);
        return rb;
    }

    /**
     * 修改环境
     *
     * @param currentProfile
     * @param deployProfile
     * @return
     */
    @RequestMapping(value = "batchUpdateProfile/{currentProfile}/{deployProfile}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultBean batchUpdateProfile(@PathVariable("currentProfile") String currentProfile,
                                         @PathVariable("deployProfile") String deployProfile) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "===batchUpdateProfile===currentProfile:"
                + currentProfile + ",deployProfile:" + deployProfile);
        ResultBean rb = new ResultBean();
        if (currentProfile.equals(deployProfile)) {
            rb.setFlag(Constans.FAIL);
            rb.setMsg("环境名相同");
        } else {
            configPropertiesService.batchUpdateProfile(currentProfile, deployProfile);
            rb.setFlag(Constans.SUCCESS);
        }
        return rb;
    }

}
