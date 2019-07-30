package com.newland.paas.paasservice.sysmgr.controller;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.service.GlbDictService;
import com.newland.paas.paasservice.sysmgr.service.SysAliasService;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;

/**
 * 别名管理
 *
 * @author CZX
 * @created 2019-01-17  15:34
 */

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/sysAliasManager")
@Validated
@AuditObject("别名管理")
public class SysAliasManagerController {
    private static final Log LOGGER = LogFactory.getLogger(SysAliasManagerController.class);

    @Autowired
    private SysAliasService sysAliasService;
    @Autowired
    private GlbDictService glbDictService;


    /**
     * 获取别名管理左侧属性结构
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/alias-tree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeNode<BaseTreeDo> getSysAliasTree() {
        List<BaseTreeDo> treeData = sysAliasService.getSysAliasTree();
        TreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new TreeNode.TreeBuilder<>();
        for (BaseTreeDo data : treeData) {
            String type = data.getType();
            List<GlbDict> dicts = glbDictService.getByDictCode(type);
            if (dicts != null && !dicts.isEmpty()) {
                GlbDict dict = dicts.get(0);
                String dictName = dict.getDictName();
                data.setName(dictName + ":" + data.getName());
            }
            treeBuilder.addNode(data.getParentId(), data.getId(), data);
        }
        return treeBuilder.build();
    }

    /**
     * 新增别名
     *
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "/addSysAlias", name = "新增别名")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysAliasObj addSysAlias(@RequestBody BasicRequestContentVo<SysAliasObj> requestContentVo)
            throws ApplicationException {
        LOGGER.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "addSysAlias,requestContentVo:{0}", JSON.toJSONString(requestContentVo)));
        SysAliasObj sysAliasObj = requestContentVo.getParams();
        return sysAliasService.addSysAlias(sysAliasObj);
    }

    /**
     * 别名更新
     *
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "/updateSysAlias", name = "更新别名")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysAliasObj updateSysAlias(@RequestBody BasicRequestContentVo<SysAliasObj> requestContentVo)
            throws ApplicationException {
        LOGGER.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "updateSysAlias,requestContentVo:{0}", JSON.toJSONString(requestContentVo)));
        return sysAliasService.updateSysAlias(requestContentVo.getParams());
    }

    /**
     * 别名删除
     *
     * @param sysAliasObjCode
     * @throws ApplicationException
     */
    @AuditOperate(value = "/deleteSysAlias", name = "删除别名")
    @DeleteMapping(value = "/{sysAliasObjCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteSysAlias(@PathVariable("sysAliasObjCode") String sysAliasObjCode)
            throws ApplicationException {
        LOGGER.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "deleteSysAlias,sysAliasObjCode:{0}", sysAliasObjCode));
        sysAliasService.deleteSysAliasObj(sysAliasObjCode);
    }

    /**
     * 查询别名
     *
     * @param sysAliasObjCode
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/{sysAliasObjCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysAliasObj getSysAliasObj(@PathVariable("sysAliasObjCode") String sysAliasObjCode)
            throws ApplicationException {
        LOGGER.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "getSysAliasObj,sysAliasObjCode:{0}", sysAliasObjCode));
        return sysAliasService.getSysAliasObj(sysAliasObjCode);
    }

    /**
     * 根据sysAliasObjCode获取列表
     * @param sysAliasObjCode
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/isExistAliasObjCode/{sysAliasObjCode}")
    public boolean isExistAppCode(@PathVariable @NotNull String sysAliasObjCode) {
        LOGGER.info(LogProperty.LOGTYPE_CALL, MessageFormat.format(
                "isExistAppCode,sysAliasObjCode:{0}", sysAliasObjCode));
        List<SysAliasObj> sysAliasObjList = sysAliasService.listSysAliasObjsBySysAliasObjCode(sysAliasObjCode);
        return sysAliasObjList != null && !sysAliasObjList.isEmpty();
    }

    /**
     * 查看别名是否存在
     * @param aliasObjName
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/isAliasObjNameExist/{aliasObjName}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Boolean isAliasObjNameExist(@PathVariable("aliasObjName") String aliasObjName)
            throws ApplicationException {
        List<SysAliasObj> list = sysAliasService.getSysAliasObjByAliasObjName(aliasObjName);
        return list != null && !list.isEmpty();
    }
}
