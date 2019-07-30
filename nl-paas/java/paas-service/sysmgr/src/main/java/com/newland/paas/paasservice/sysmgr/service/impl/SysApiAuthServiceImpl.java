package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysInterfaceUrlMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuUrlMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysInterfaceUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysInterfaceUrlVO;
import com.newland.paas.paasservice.sysmgr.config.ApiAuthConfig;
import com.newland.paas.paasservice.sysmgr.service.SysApiAuthService;
import com.newland.paas.paasservice.sysmgr.vo.SyncInterfaceUrlVO;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WRP
 * @since 2019/1/19
 */
@Service
public class SysApiAuthServiceImpl implements SysApiAuthService {

    private static final Log LOG = LogFactory.getLogger(SysApiAuthServiceImpl.class);

    private static final String UNDERLINE = "_";
    private static final String GET = "get";
    private static final String API_GETALLURL = "/service/v1/api/getAllUrl";
    private static final String ACTIVITI_FLOW = "activitiFlow";

    @Autowired
    private SysInterfaceUrlMapper sysInterfaceUrlMapper;
    @Autowired
    private SysMenuUrlMapper sysMenuUrlMapper;
    @Autowired
    private SysMenuOperMapper sysMenuOperMapper;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;
    @Autowired
    private ApiAuthConfig apiAuthConfig;

    @Override
    public List<SyncInterfaceUrlVO> syncInterfaceUrl() {
        List<SyncInterfaceUrlVO> results = new ArrayList<>();
        if (apiAuthConfig.getServiceList() != null) {
            apiAuthConfig.getServiceList().forEach(v -> results.add(syncServiceInterfaceUrl(v)));
        }
        return results;
    }

    @Override
    public ResultPageData<SysInterfaceUrl> interfaceUrlPageByParams(
            Long menuId, SysInterfaceUrl sysInterfaceUrl, PageInfo pageInfo) {
        SysMenuUrlBO sysMenuUrlBO = new SysMenuUrlBO();
        sysMenuUrlBO.setMenuId(menuId);
        sysMenuUrlBO.setMethod(sysInterfaceUrl.getMethod());
        sysMenuUrlBO.setUri(sysInterfaceUrl.getUri());
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysInterfaceUrl> list = sysInterfaceUrlMapper.selectBySelectiveNotExist(sysMenuUrlBO);
        return new ResultPageData(list);
    }

    @Override
    public List<SysMenuUrlBO> menuInterfaceUrlByParams(SysMenuUrlBO sysMenuUrlBO) {
        return sysMenuUrlMapper.selectSysMenuUrlBOBySelective(sysMenuUrlBO);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean updateMenuUrlRel(Long menuId, List<String> urlIds) {
        boolean isRemove = false;
        SysMenuUrl sysMenuUrl = new SysMenuUrl();
        sysMenuUrl.setMenuId(menuId);
        List<SysMenuUrl> sysMenuUrls = sysMenuUrlMapper.selectBySelective(sysMenuUrl);
        for (SysMenuUrl v : sysMenuUrls) {
            sysMenuUrl.setUrlId(v.getUrlId());
            if (!urlIds.contains(v.getUrlId())) {
                sysMenuUrlMapper.deleteBySelective(sysMenuUrl);
                if (!isRemove) {
                    isRemove = true;
                }
            }
        }
        List<String> urlIdsOld = sysMenuUrls.stream().map(SysMenuUrl::getUrlId).collect(Collectors.toList());
        urlIds.forEach(v -> {
            if (!urlIdsOld.contains(v)) {
                sysMenuUrl.setUrlId(v);
                sysMenuUrl.setCreateId(RequestContext.getUserId());
                sysMenuUrlMapper.insertSelective(sysMenuUrl);
            }
        });
        return isRemove;
    }

    @Override
    public void createInterfaceUrl(SysInterfaceUrl sysInterfaceUrl) {
        String src = sysInterfaceUrl.getServiceId() + UNDERLINE
                + sysInterfaceUrl.getMethod() + UNDERLINE + sysInterfaceUrl.getUri();
        String urlId = Base64Utils.encodeToString(src.getBytes());
        sysInterfaceUrl.setUrlId(urlId);
        saveInterfaceUrl(urlId, sysInterfaceUrl);
    }

    @Override
    public void updateInterfaceUrl(SysInterfaceUrl sysInterfaceUrl) {
        deleteInterfaceUrl(sysInterfaceUrl.getUrlId());
        String src = sysInterfaceUrl.getServiceId() + UNDERLINE
                + sysInterfaceUrl.getMethod() + UNDERLINE + sysInterfaceUrl.getUri();
        String urlId = Base64Utils.encodeToString(src.getBytes());
        sysInterfaceUrl.setUrlId(urlId);
        saveInterfaceUrl(urlId, sysInterfaceUrl);
    }

    @Override
    public void deleteInterfaceUrl(String urlId) {
        SysMenuUrl sysMenuUrl = new SysMenuUrl();
        sysMenuUrl.setUrlId(urlId);
        List<SysMenuUrl> sysMenuUrls = sysMenuUrlMapper.selectBySelective(sysMenuUrl);
        if (!CollectionUtils.isEmpty(sysMenuUrls)) {
            SysMenuOper sysMenuOper = sysMenuOperMapper.selectByPrimaryKey(sysMenuUrls.get(0).getMenuId());
            throw new SystemException(new PaasError("21800101", "无法删除，该接口已被菜单【" + sysMenuOper.getName() + "】使用！"));
        }
        sysInterfaceUrlMapper.deleteByPrimaryKey(urlId);
    }

    @Override
    public ResultPageData<SysInterfaceUrl> interfaceUrlList(SysInterfaceUrl sysInterfaceUrl, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysInterfaceUrl> list = sysInterfaceUrlMapper.selectBySelective(sysInterfaceUrl);
        return new ResultPageData(list);
    }

    @Override
    public List<SysMenuOper> selectMenuByUrl(String urlId) {
        return sysMenuOperMapper.selectMenuByUrl(urlId);
    }

    /**
     * 将对应serviceId服务的接口同步到数据库
     *
     * @param serviceId
     * @return 同步结果
     */
    private SyncInterfaceUrlVO syncServiceInterfaceUrl(String serviceId) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "serviceId:" + serviceId);
        try {
            MicroservicesItemProperties microservicesItemProperties = getMicroservicesItemProperties(serviceId);
            LOG.info(LogProperty.LOGTYPE_DETAIL, "microservicesItemProperties:"
                    + JSON.toJSONString(microservicesItemProperties));
            BasicResponseContentVo<List<SysInterfaceUrlVO>> result;
            ParameterizedTypeReference responseType = new ParameterizedTypeReference
                    <BasicResponseContentVo<List<SysInterfaceUrlVO>>>() {
            };
            if (ACTIVITI_FLOW.equals(serviceId)) {
                result = restTemplateUtils.getForTokenEntity(microservicesItemProperties, API_GETALLURL,
                        "Basic YWRtaW46YWRtaW4=", new HashMap<>(), responseType);
            } else {
                result = restTemplateUtils.getForEntity(microservicesItemProperties, API_GETALLURL,
                        new HashMap<>(), responseType);
            }
            LOG.info(LogProperty.LOGTYPE_DETAIL, serviceId + "-result:" + JSON.toJSONString(result));
            result.getContent().forEach(v -> {
                SysInterfaceUrl sysInterfaceUrl = buildSysInterfaceUrl(v,
                        microservicesItemProperties.getServiceId(), microservicesItemProperties.getContentPath());
                String src = sysInterfaceUrl.getServiceId() + UNDERLINE
                        + sysInterfaceUrl.getMethod() + UNDERLINE + sysInterfaceUrl.getUri();
                String urlId = Base64Utils.encodeToString(src.getBytes());
                sysInterfaceUrl.setUrlId(urlId);
                saveInterfaceUrl(urlId, sysInterfaceUrl);
            });
            return new SyncInterfaceUrlVO(serviceId, true);
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, "同步失败！", e);
            return new SyncInterfaceUrlVO(serviceId, false, "同步失败！" + e.getMessage());
        }
    }

    /**
     * 根据serviceId获取MicroservicesItemProperties
     *
     * @param serviceId
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private MicroservicesItemProperties getMicroservicesItemProperties(String serviceId)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> mpClass = Class.forName(MicroservicesProperties.class.getName());
        String firstChar = serviceId.substring(0, 1).toUpperCase();
        String methodName = GET + firstChar + serviceId.substring(1);
        Method method = mpClass.getMethod(methodName);
        return (MicroservicesItemProperties) method.invoke(microservicesProperties);
    }

    /**
     * 构造接口url对象
     *
     * @param vo
     * @param serviceId
     * @param contentPath
     * @return
     */
    private SysInterfaceUrl buildSysInterfaceUrl(SysInterfaceUrlVO vo, String serviceId, String contentPath) {
        SysInterfaceUrl sysInterfaceUrl = new SysInterfaceUrl();
        sysInterfaceUrl.setServiceId(serviceId);
        sysInterfaceUrl.setMethod(vo.getMethod());
        sysInterfaceUrl.setUri(contentPath + vo.getUri());
        sysInterfaceUrl.setClassMethod(vo.getClassMethod());
        return sysInterfaceUrl;
    }

    /**
     * 保存接口api
     * 不存在插入
     *
     * @param urlId
     * @param sysInterfaceUrl
     */
    private void saveInterfaceUrl(String urlId, SysInterfaceUrl sysInterfaceUrl) {
        if (sysInterfaceUrlMapper.selectByPrimaryKey(urlId) == null) {
            sysInterfaceUrlMapper.insertSelective(sysInterfaceUrl);
        }
    }

}
