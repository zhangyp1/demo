/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.common.util.TreeBuilder;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysCategoryBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;
import com.newland.paas.paasservice.sysmgr.error.SysCategoryError;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryService;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryTreeListReqVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述
 *
 * @author Frown
 * @created 2018-07-30 14:51
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/sysCategory")
@Validated
@AuditObject("系统分组管理")
public class SysCategoryController {
    @Autowired
    private SysCategoryService sysCategoryService;

    /**
     * 新增系统分组
     * 
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "newSysCategory", name = "新增系统分组")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void newSysCategory(@RequestBody BasicRequestContentVo<SysCategoryBo> requestContentVo)
        throws ApplicationException {
        SysCategoryBo sysCategoryReq = requestContentVo.getParams();
        sysCategoryService.saveSysCategory(sysCategoryReq);
    }

    /**
     * 更新系统分组
     * 
     * @param requestContentVo
     * @throws ApplicationException
     */
    @AuditOperate(value = "updateSysCategory", name = "更新系统分组")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateSysCategory(@RequestBody BasicRequestContentVo<SysCategoryBo> requestContentVo)
        throws ApplicationException {
        SysCategoryBo sysCategoryReq = requestContentVo.getParams();
        // id不能为空
        if (sysCategoryReq.getSysCategoryId() == null) {
            throw new ApplicationException(SysCategoryError.ID_NULL_ERROR);
        }
        sysCategoryService.saveSysCategory(sysCategoryReq);
    }

    /**
     * 删除系统分组
     * 
     * @param id
     * @throws ApplicationException
     */

    @AuditOperate(value = "deleteSysCategory", name = "删除系统分组")
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteSysCategory(@PathVariable("id") Long id) throws ApplicationException {
        SysCategory sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(id);
        sysCategoryService.deleteSysCategory(sysCategory);
    }

    /**
     * 分页查询
     * 
     * @param params
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/paged-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData pageQuerySysCategory(@RequestBody BasicPageRequestContentVo<SysCategory> params)
        throws ApplicationException {
        return sysCategoryService.pageQuerySysCategory(params.getParams(), params.getPageInfo());
    }

    /**
     * 查询
     * 
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategory> listQuerySysCategory(SysCategory sysCategory) throws ApplicationException {
        return sysCategoryService.listSysCategory(sysCategory);
    }

    /**
     * 获取详情
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysCategoryBo getSysCategory(@PathVariable("id") Long id) throws ApplicationException {
        SysCategory sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(id);
        return sysCategoryService.getSysCategory(sysCategory);
    }

    /**
     * 获取左侧树形
     * 
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/sys-category-tree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeNode<BaseTreeDo> getSysCategoryTree() {
        return sysCategoryService.getSysCategoryTree();
    }

    /**
     * 获取系统分组树列表
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/tree-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategoryVO> treeListQuerySysCategory() throws ApplicationException {
        return sysCategoryService.listSysCategory();
    }

    /**
     * 根据操作代码查询有权限的系统分组列表
     *
     * @param req
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/sys-category-tree-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategoryVO> listSysCategoryByMenuOperCode(
        @RequestBody BasicRequestContentVo<SysCategoryTreeListReqVo> req) throws ApplicationException {
        return sysCategoryService.listSysCategoryByMenuOperCode(req.getParams().getMenuOperCode());
    }

    /**
     * 根据操作代码查询有权限的系统分组列表
     *
     * @param req
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/sys-category-tree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeBuilder<SysCategoryVO>.TreeNode<SysCategoryVO> getSysCategoryByMenuOperCodeTree(
        @RequestBody BasicRequestContentVo<SysCategoryTreeListReqVo> req) throws ApplicationException {
        List<SysCategoryVO> result =
            sysCategoryService.listSysCategoryByMenuOperCode(req.getParams().getMenuOperCode());
        TreeBuilder<SysCategoryVO> tree = new TreeBuilder<>();
        tree.addNodes(SysCategoryVO::getSysCategoryId, SysCategoryVO::getSysCategoryName,
            SysCategoryVO::getSysCategoryPid, result);
        return tree.build();
    }

    /**
     * 子系统分组
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/listSubs/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategory> listSubs(@PathVariable("id") Long id) {
        SysCategory sysCategoryReq = new SysCategory();
        sysCategoryReq.setSysCategoryId(id);
        return sysCategoryService.listSubAndCurSysCategory(sysCategoryReq);
    }

    /**
     * 父系统分组
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/listParents/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategory> listParents(@PathVariable("id") Long id) {
        SysCategory sysCategoryReq = new SysCategory();
        sysCategoryReq.setSysCategoryId(id);
        return sysCategoryService.listParentAndCurSysCategory(sysCategoryReq);
    }
}
