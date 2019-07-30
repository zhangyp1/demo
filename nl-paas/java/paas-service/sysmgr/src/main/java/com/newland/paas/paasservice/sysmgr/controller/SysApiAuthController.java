package com.newland.paas.paasservice.sysmgr.controller;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysInterfaceUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;
import com.newland.paas.paasservice.sysmgr.runner.InitializeRunner;
import com.newland.paas.paasservice.sysmgr.runner.RoleApiCacheRefresher;
import com.newland.paas.paasservice.sysmgr.service.SysApiAuthService;
import com.newland.paas.paasservice.sysmgr.vo.SyncInterfaceUrlVO;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接口api权限
 *
 * @author WRP
 * @since 2019/1/19
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/apiAuth")
public class SysApiAuthController {

    private static final Log LOG = LogFactory.getLogger(SysApiAuthController.class);

    @Autowired
    private SysApiAuthService sysApiAuthService;
    @Autowired
    private RoleApiCacheRefresher roleApiCacheRefresher;

    /**
     * 同步全量接口地址信息
     */
    @GetMapping(value = "syncInterfaceUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SyncInterfaceUrlVO> syncInterfaceUrl() {
        LOG.info(LogProperty.LOGTYPE_CALL, "syncInterfaceUrl");
        return sysApiAuthService.syncInterfaceUrl();
    }

    /**
     * 创建接口
     *
     * @param requestContentVo
     */
    @PostMapping(value = "createInterfaceUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void createInterfaceUrl(@RequestBody BasicPageRequestContentVo<SysInterfaceUrl> requestContentVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "createInterfaceUrl:" + JSON.toJSONString(requestContentVo));
        sysApiAuthService.createInterfaceUrl(requestContentVo.getParams());
    }

    /**
     * 修改接口
     *
     * @param requestContentVo
     */
    @PostMapping(value = "updateInterfaceUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateInterfaceUrl(@RequestBody BasicPageRequestContentVo<SysInterfaceUrl> requestContentVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "updateInterfaceUrl:" + JSON.toJSONString(requestContentVo));
        sysApiAuthService.updateInterfaceUrl(requestContentVo.getParams());
    }

    /**
     * 删除接口
     *
     * @param urlId
     */
    @GetMapping(value = "deleteInterfaceUrl/{urlId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteInterfaceUrl(@PathVariable("urlId") String urlId) {
        LOG.info(LogProperty.LOGTYPE_CALL, "deleteInterfaceUrl:" + urlId);
        sysApiAuthService.deleteInterfaceUrl(urlId);
    }

    /**
     * 接口列表
     *
     * @param requestContentVo
     */
    @PostMapping(value = "interfaceUrlList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysInterfaceUrl> interfaceUrlList(@RequestBody BasicPageRequestContentVo<SysInterfaceUrl> requestContentVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "interfaceUrlList:" + JSON.toJSONString(requestContentVo));
        return sysApiAuthService.interfaceUrlList(requestContentVo.getParams(), requestContentVo.getPageInfo());
    }

    /**
     * 菜单列表
     *
     * @param urlId
     */
    @PostMapping(value = "selectMenuByUrl/{urlId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysMenuOper> selectMenuByUrl(@PathVariable("urlId") String urlId) {
        LOG.info(LogProperty.LOGTYPE_CALL, "selectMenuByUrl:" + urlId);
        return sysApiAuthService.selectMenuByUrl(urlId);
    }

    /**
     * 接口列表
     *
     * @param requestContentVo
     * @return
     */
    @PostMapping(value = "interfaceUrlPage/{menuId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysInterfaceUrl> interfaceUrlPage(
            @PathVariable("menuId") Long menuId,
            @RequestBody BasicPageRequestContentVo<SysInterfaceUrl> requestContentVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "interfaceUrlPage,requestContentVo:" + JSON.toJSONString(requestContentVo));
        return sysApiAuthService.interfaceUrlPageByParams(menuId,
                requestContentVo.getParams(), requestContentVo.getPageInfo());
    }

    /**
     * 菜单URL_接口列表
     *
     * @param requestContentVo
     * @return
     */
    @PostMapping(value = "menuInterfaceUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysMenuUrlBO> menuInterfaceUrl(@RequestBody BasicPageRequestContentVo<SysMenuUrlBO> requestContentVo) {
        LOG.info(LogProperty.LOGTYPE_CALL, "interfaceUrlPage,requestContentVo:" + JSON.toJSONString(requestContentVo));
        return sysApiAuthService.menuInterfaceUrlByParams(requestContentVo.getParams());
    }

    /**
     * 修改 菜单URL关联关系
     *
     * @param menuId
     * @param requestContentVo
     * @return
     */
    @PostMapping(value = "updateMenuUrlRel/{menuId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateMenuUrlRel(@PathVariable("menuId") Long menuId,
                                 @RequestBody BasicRequestContentVo<List<String>> requestContentVo) throws Exception {
        LOG.info(LogProperty.LOGTYPE_CALL, "updateMenuUrlRel,requestContentVo:" + JSON.toJSONString(requestContentVo));
        boolean isRemove = sysApiAuthService.updateMenuUrlRel(menuId, requestContentVo.getParams());
        if (isRemove) {
            roleApiCacheRefresher.refreshAll();
        } else {
            roleApiCacheRefresher.refreshApiAuthCacheByMenuId(menuId);
        }
    }

}
