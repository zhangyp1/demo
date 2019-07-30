package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuotaUsed;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryQuotaReqVo;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryQuotaService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2019-06-18 10:00
 **/
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/sysCategoryQuota")
@Validated
@AuditObject("系统分组配额管理")
public class SysCategoryQuotaController {
    @Autowired
    private SysCategoryQuotaService sysCategoryQuotaService;

    /**
     * 占用系统分组配额
     *
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "useSysCategoryQuota", name = "占用系统分组配额")
    @PostMapping(value = "/use", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<Object, Object> useSysCategoryQuota(
            @Validated @RequestBody BasicRequestContentVo<SysCategoryQuotaReqVo> requestContentVo)
            throws ApplicationException {
        SysCategoryQuotaUsed quotaUsed = new SysCategoryQuotaUsed();
        BeanUtils.copyProperties(requestContentVo.getParams(), quotaUsed);
        return sysCategoryQuotaService.useSysCategoryQuota(quotaUsed);
    }

    /**
     * 释放系统分组配额
     *
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "freeSysCategoryQuota", name = "释放系统分组配额")
    @PostMapping(value = "/free", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<Object, Object> freeSysCategoryQuota(
            @Validated @RequestBody BasicRequestContentVo<SysCategoryQuotaReqVo> requestContentVo)
            throws ApplicationException {
        SysCategoryQuotaUsed quotaUsed = new SysCategoryQuotaUsed();
        BeanUtils.copyProperties(requestContentVo.getParams(), quotaUsed);
        return sysCategoryQuotaService.freeSysCategoryQuota(quotaUsed);
    }

}
