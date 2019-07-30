package com.newland.paas.paasservice.sysmgr.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.service.GlbDictService;
import com.newland.paas.paasservice.sysmgr.vo.DictNode;
import com.newland.paas.paasservice.sysmgr.vo.DictTreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 * 字典查询
 *
 * @author wrp
 * @since 2018/6/26
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "common/v1/glbDict")
@Validated
public class GlbDictController {

    private static final Log LOG = LogFactory.getLogger(GlbDictController.class);

    @Autowired
    private GlbDictService glbDictService;

    /**
     * 查询所有字典、用户PF_USER、租户PF_TENANT、工组PF_GROUP、系统分组PF_SYS_CATEGORY
     *
     * @return
     */
    @GetMapping(value = "findAll", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> findAll() {
        LOG.info(LogProperty.LOGTYPE_CALL, "findAll");
        return glbDictService.findAll();
    }

    /**
     * 查询所有字典表数据
     *
     * @return
     */
    @GetMapping(value = "getAll", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> getAll() {
        LOG.info(LogProperty.LOGTYPE_CALL, "getAll");
        return glbDictService.getAll();
    }

    /**
     * 查询所有字典表以节点返回
     *
     * @return
     */
    @GetMapping(value = "getAllNode", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<DictNode> getAllNode() {
        LOG.info(LogProperty.LOGTYPE_CALL, "getAllNode");
        return glbDictService.getAllNode();
    }

    /**
     * 查询所有字典树
     *
     * @return
     */
    @GetMapping(value = "getAllTree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public DictTreeNode<BaseTreeDo> getAllTree() {
        LOG.info(LogProperty.LOGTYPE_CALL, "getAllTree");
        return glbDictService.getAllTree();
    }

    /**
     * 查询懒加载字典树
     *
     * @return
     */
    @GetMapping(value = "getTree/{dictPcode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public DictTreeNode<BaseTreeDo> getTree(@PathVariable("dictPcode") String dictPcode) {
        LOG.info(LogProperty.LOGTYPE_CALL, "getTree");
        return glbDictService.getTree(dictPcode);
    }

    /**
     * 根据dictcode获取数据
     *
     * @return
     */
    @PostMapping(value = "getByDictCode", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> getByDictCode(@NotNull(message = "编码不能为空!") String dictCode) {
        LOG.info(LogProperty.LOGTYPE_CALL, "getByDictCode");
        return glbDictService.getByDictCode(dictCode);
    }

    /**
     * 删除字典通过dictcode
     *
     * @return
     * @throws ApplicationException
     */
    @DeleteMapping(value = "delByDictCode/{dictCode}/{dictPcode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public int delByDictCode(@PathVariable("dictCode") String dictCode,
                             @PathVariable("dictPcode") String dictPcode) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, "delByDictCode");
        return glbDictService.delByDictCodeAndPcode(dictCode, dictPcode);
    }

    /**
     * 增加字典
     *
     * @return
     */
    @PutMapping(value = "addDict/{dictCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public int addDict(@PathVariable("dictCode") String dictCode,
                       String dictPcode, @NotNull(message = "值不能为空!") String dictName) {
        LOG.info(LogProperty.LOGTYPE_CALL, "addDict");
        GlbDict glbDict = new GlbDict();
        glbDict.setDictCode(dictCode);
        glbDict.setDictName(dictName);
        if (dictPcode == null || "".equals(dictPcode)) {
            dictPcode = "-1";
        }
        glbDict.setDictPcode(dictPcode);
        return glbDictService.insertDict(glbDict);
    }

    /**
     * 新增或者修改dict
     * @param dictCode
     * @param map
     * @return
     * @throws ApplicationException
     */
    @PutMapping(value = "addOrUpdate/{dictCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public int addOrUpdate(@PathVariable("dictCode") String dictCode,
                           @RequestBody Map map) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, "addOrUpdate");
        String dictPcode = String.valueOf(map.get("dictPcode"));
        String dictName = String.valueOf(map.get("dictName"));
        String oldCode = String.valueOf(map.get("oldCode"));
        String oldPcode = String.valueOf(map.get("oldPcode"));
        GlbDict glbDict = new GlbDict();
        glbDict.setDictCode(dictCode);
        glbDict.setDictName(dictName);
        if (dictPcode == null || "".equals(dictPcode)) {
            dictPcode = "-1";
        }
        glbDict.setDictPcode(dictPcode);
        return glbDictService.insertOrUpdate(glbDict, oldCode, oldPcode);
    }

    /**
     * 根据父类编码查询字典数据
     *
     * @return
     */
    @GetMapping(value = "getByDictPcode", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> getByDictPcode(@NotNull(message = "字典父编码不能为空!") String dictPcode) {
        LOG.info(LogProperty.LOGTYPE_CALL, MessageFormat.format("getByDictPcode:{0}", dictPcode));
        return glbDictService.getByDictPcode(dictPcode);
    }

    /**
     *
     * @param dictPcode
     * @param dictCode
     * @return
     */
    @GetMapping(value = "getDict/{dictPcode}/{dictCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbDict getByDictPcode(@PathVariable("dictPcode") String dictPcode,
                                        @PathVariable("dictCode") String dictCode) {
        return glbDictService.getDict(dictPcode, dictCode);
    }
}
