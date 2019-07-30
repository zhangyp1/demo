/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.common.exception.NLCheckedException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysUrlRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.constants.RedisKeyConstants;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;

/**
 * 用户url关系 描述
 *
 * @author linkun
 * @created 2018年8月6日 下午2:52:56
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/urlRoleMgr")
@Validated
@AuditObject("用户url关系管理")
public class SysUrlRoleController {
    private static final Log LOGGER = LogFactory.getLogger(SysUrlRoleController.class);

    @Autowired
    private SysUrlRoleService sysUrlRoleService;

    @Autowired
    private SysMgrConfig sysMgrConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取所有url 描述
     *
     * @return
     * @throws NLCheckedException
     * @author linkun
     * @created 2018年8月6日 下午3:45:06
     */
    @GetMapping("/getAllUrls")
    public Map<String, List<SysUrlRoleRespBo>> getAllUrls() {
        LOGGER.info(LogProperty.LOGTYPE_SYS, "==============请求所有的api的url=========");
        Map<String, List<SysUrlRoleRespBo>> resultMap = new HashMap<>();
        List<String> docApps = sysMgrConfig.getDocApps();
        for (String docApp : docApps) {
            List<SysUrlRoleRespBo> list = new ArrayList<>();
            // dev:PAAS:API:DOCS:sysmgr
            String jsonStr = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_PAAS_API_DOCS_PREFIX + docApp);
            LOGGER.info(LogProperty.LOGTYPE_SYS, "==============url的Redis的key值:"
                    + RedisKeyConstants.REDIS_PAAS_API_DOCS_PREFIX + docApp + "==============");
            LOGGER.info(jsonStr);
            JSONObject jo = JSONObject.parseObject(jsonStr);
            String basePath = jo.getString("basePath");
            String host = jo.getString("host");
            JSONObject pathsJo = jo.getJSONObject("paths");
            for (String pathKey : pathsJo.keySet()) {
                String path = pathKey;
                JSONObject pathJo = pathsJo.getJSONObject(pathKey);
                for (String operationKey : pathJo.keySet()) {
                    if (operationKey.equals("operationMap")) {
                        JSONObject operationJo = pathJo.getJSONObject(operationKey);
                        for (String methodKey : operationJo.keySet()) {
                            String method = methodKey;
                            SysUrlRoleRespBo info = new SysUrlRoleRespBo();
                            info.setBasePath(basePath);
                            info.setHost(host);
                            info.setPath(path);
                            info.setMethod(method);
                            info.setUrl(method + ":" + basePath + path);
                            list.add(info);
                        }
                    }
                }
            }
            list.sort(Comparator.comparing(SysUrlRoleRespBo::getPath));
            resultMap.put(docApp, list);
        }

        return resultMap;
    }

    /**
     * 角色添加url列表 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月6日 下午4:54:45
     */
    @PostMapping("/addUrls")
    @AuditOperate(value = "addUrls", name = "角色添加url列表")
    public int addUrls(@RequestBody BasicRequestContentVo<SysUrlRoleReqBo> reqInfo) throws ApplicationException {
        return sysUrlRoleService.addUrls(reqInfo.getParams());
    }

    /**
     * 根据角色获取url列表 描述
     *
     * @param roleId
     * @return
     * @author linkun
     * @created 2018年8月6日 下午4:54:56
     */
    @GetMapping("/getUrlsByRole/{roleId}")
    public List<SysUrlRoleRespBo> getUrlsByRole(@PathVariable("roleId") Long roleId) {
        return sysUrlRoleService.getUrlsByRole(roleId);
    }
}
