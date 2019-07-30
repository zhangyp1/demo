package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabelValue;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelQueryVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelRspVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelValueReqVo;
import com.newland.paas.paasservice.sysmgr.error.SysLabelError;
import com.newland.paas.paasservice.sysmgr.service.GlbLabelService;
import com.newland.paas.paasservice.sysmgr.service.GlbLabelValueService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: paas-all
 * @description: 标签管理
 * @author: Frown
 * @create: 2019-05-30 17:00
 **/
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "common/v1/glbLabel")
@Validated
public class GlbLabelController {

    private static final Log LOG = LogFactory.getLogger(GlbLabelController.class);

    @Autowired
    private GlbLabelService glbLabelService;
    @Autowired
    private GlbLabelValueService glbLabelValueService;

    /**
     * 新增标签
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "newGlbLabel", name = "新增标签")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabel newGlbLabel(@Validated @RequestBody BasicRequestContentVo<GlbLabelReqVo> requestContentVo)
            throws ApplicationException {
        GlbLabelReqVo glbLabelReqVo = requestContentVo.getParams();
        return saveGlbLabel(glbLabelReqVo);
    }

    /**
     * 修改标签
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "updateGlbLabel", name = "修改标签")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabel updatGlbLabel(@Validated @RequestBody BasicRequestContentVo<GlbLabelReqVo> requestContentVo)
            throws ApplicationException {
        GlbLabelReqVo glbLabelReqVo = requestContentVo.getParams();
        // id不能为空
        if (glbLabelReqVo.getGlbLabelId() == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        return saveGlbLabel(requestContentVo.getParams());
    }

    /**
     * 保存标签
     * @param glbLabelReqVo
     * @throws ApplicationException
     */
    private GlbLabel saveGlbLabel(GlbLabelReqVo glbLabelReqVo) throws ApplicationException {
        GlbLabel glbLabel = new GlbLabel();
        BeanUtils.copyProperties(glbLabelReqVo, glbLabel);
        return glbLabelService.saveGlbLabel(glbLabel);
    }

    /**
     * 删除标签
     * @param id
     * @throws ApplicationException
     */

    @AuditOperate(value = "deleteGlbLabel", name = "删除标签")
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteGlbLabel(@Validated @PathVariable("id") Long id) throws ApplicationException {
        GlbLabel glbLabel = new GlbLabel();
        glbLabel.setGlbLabelId(id);
        glbLabelService.deleteGlbLabel(glbLabel);
    }

    /**
     * 获取标签
     * @param id
     * @throws ApplicationException
     */

    @AuditOperate(value = "getGlbLabel", name = "获取标签")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabel getGlbLabel(@Validated @PathVariable("id") Long id) throws ApplicationException {
        GlbLabel glbLabel = new GlbLabel();
        glbLabel.setGlbLabelId(id);
        return glbLabelService.getGlbLabel(glbLabel);
    }

    /**
     * 标签分页查询
     * @param params
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/paged-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData pageQueryGlbLabel(@Validated @RequestBody BasicPageRequestContentVo<GlbLabelQueryVo> params)
            throws ApplicationException {
        return glbLabelService.pageGlbLabel(params.getParams(), params.getPageInfo());
    }



    /**
     * 新增标签值
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "newGlbLabelValue", name = "新增标签值")
    @PostMapping(value = "/value", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabelValue newGlbLabelValue(
            @Validated @RequestBody BasicRequestContentVo<GlbLabelValueReqVo> requestContentVo)
            throws ApplicationException {
        GlbLabelValueReqVo glbLabelValueReqVo = requestContentVo.getParams();
        return saveGlbLabelValue(glbLabelValueReqVo);
    }

    /**
     * 修改标签值
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "updateGlbLabelValue", name = "修改标签值")
    @PutMapping(value = "/value", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabelValue updatGlbLabelValue(
            @Validated @RequestBody BasicRequestContentVo<GlbLabelValueReqVo> requestContentVo)
            throws ApplicationException {
        GlbLabelValueReqVo glbLabelValueReqVo = requestContentVo.getParams();
        // id不能为空
        if (glbLabelValueReqVo.getGlbLabelValueId() == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        return saveGlbLabelValue(requestContentVo.getParams());
    }

    /**
     * 保存标签值
     * @param glbLabelValueReqVo
     * @throws ApplicationException
     */
    private GlbLabelValue saveGlbLabelValue(GlbLabelValueReqVo glbLabelValueReqVo) throws ApplicationException {
        GlbLabelValue glbLabelValue = new GlbLabelValue();
        BeanUtils.copyProperties(glbLabelValueReqVo, glbLabelValue);
        return glbLabelValueService.saveGlbLabelValue(glbLabelValue);
    }

    /**
     * 删除标签值
     * @param id
     * @throws ApplicationException
     */

    @AuditOperate(value = "deleteGlbLabelValue", name = "删除标签值")
    @DeleteMapping(value = "/value/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteGlbLabelValue(@Validated @PathVariable("id") Long id) throws ApplicationException {
        GlbLabelValue glbLabelValue = new GlbLabelValue();
        glbLabelValue.setGlbLabelValueId(id);
        glbLabelValueService.deleteGlbLabelValue(glbLabelValue);
    }

    /**
     * 获取标签值
     * @param id
     * @throws ApplicationException
     */

    @AuditOperate(value = "getGlbLabelValue", name = "获取标签值")
    @GetMapping(value = "/value/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public GlbLabelValue getGlbLabelValue(@Validated @PathVariable("id") Long id) throws ApplicationException {
        GlbLabelValue glbLabelValue = new GlbLabelValue();
        glbLabelValue.setGlbLabelValueId(id);
        return glbLabelValueService.getGlbLabelValue(glbLabelValue);
    }

    /**
     * 标签值列表查询
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/value/list/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List listQueryGlbLabel(@Validated @PathVariable("id") Long id) {
        GlbLabelQueryVo queryVo = new GlbLabelQueryVo();
        queryVo.setGlbLabelId(id);
        return glbLabelValueService.listGlbLabelValue(queryVo);
    }

    /**
     * 标签分组查询
     * @param params
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/group-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbLabelRspVo> listGroupQueryGlbLabel(
            @RequestBody BasicRequestContentVo<GlbLabelQueryVo> params) throws ApplicationException {
        return glbLabelService.listGroupGlbLabel(params.getParams());
    }


    /**
     * 检查标签编码
     * @param params
     * @return
     */
    @PostMapping(value = "/check-code", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map checkCode(@Validated @RequestBody BasicRequestContentVo<GlbLabelQueryVo> params) {
        return glbLabelService.checkLabelCode(params.getParams());
    }

    /**
     * 检查标签名称
     * @param params
     * @return
     */
    @PostMapping(value = "/check-name", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map checkName(@Validated @RequestBody BasicRequestContentVo<GlbLabelQueryVo> params) {
        return glbLabelService.checkLabelName(params.getParams());
    }

}
