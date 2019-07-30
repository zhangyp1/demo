package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysAliasObjVO;
import com.newland.paas.paasservice.sysmgr.service.SysAliasObjService;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

/**
 * 别名关系管理
 *
 * @author WRP
 * @since 2019/1/11
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/aliasObj")
@Validated
public class SysAliasObjController {

    public static final Log LOG = LogFactory.getLogger(SysAliasObjController.class);

    @Autowired
    private SysAliasObjService sysAliasObjService;

    /**
     * 外部接口：别名列表查询
     * 根据查询条件(类型、父级)获取别名列表
     * 注：当objCodeP为0时，查询类型下所有数据
     * @param aliasObjType 别名对象类型
     * @param objCodeP     父对象CODE
     */
    @GetMapping(value = "aliasList/{aliasObjType}/{objCodeP}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysAliasObjVO> aliasList(@PathVariable("aliasObjType") String aliasObjType,
                                         @PathVariable("objCodeP") String objCodeP) {
        LOG.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "aliasList,aliasObjType:{0},objCodeP:{1}", aliasObjType, objCodeP));
        return sysAliasObjService.getAliasList(aliasObjType, objCodeP);
    }

    /**
     * 外部接口：别名翻译查询
     * 根据CODE获取引用对象CODE
     *
     * @param aliasObjType 别名对象类型
     * @param aliasObjCode 别名对象CODE
     */
    @GetMapping(value = "getObjCode/{aliasObjType}/{aliasObjCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo<String> getObjCode(@PathVariable("aliasObjType") String aliasObjType,
                                                     @PathVariable("aliasObjCode") String aliasObjCode) {
        LOG.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "getObjCode,aliasObjType{0},aliasObjCode:{1}", aliasObjType, aliasObjCode));
        return new BasicResponseContentVo<>(sysAliasObjService.getObjCode(aliasObjType, aliasObjCode));
    }

}
