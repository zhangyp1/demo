package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.ResultBean;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysObjOperateVo;
import com.newland.paas.paasservice.sysmgr.service.ObjPermissionHubService;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryObjReqVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象权限集中管理接口
 *
 * @author zhongqingjiang
 */
@RestController
@RequestMapping("/sysmgr/v1/obj-permission-hub")
public class ObjPermissionHubController {

    @Autowired
    ObjPermissionHubService objPermissionHubService;

    /**
     * 我的系统分组树
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/my-sys-categories-tree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeNode<BaseTreeDo> getMySysCategoryTree() throws ApplicationException {
        return objPermissionHubService.getMySysCategoryTree();
    }

    /**
     * 授权系统分组树
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/granted-sys-categories-tree", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeNode<BaseTreeDo> getGrantedSysCategoryTree() throws ApplicationException {
        return objPermissionHubService.getGrantedSysCategoryTree();
    }

    /**
     * 获取授权系统分组列表
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/granted-sys-categories", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategory> listGrantedSysCategory(@RequestParam String objType) throws ApplicationException {
        return objPermissionHubService.listGrantedSysCategory(objType);
    }

    /**
     * 根据系统分组获取对象列表 - 分页
     *
     * @param params
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/objects", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysUserObj> listManagedSysCategory(@RequestBody BasicPageRequestContentVo<SysCategoryObjReqVo> params)
            throws ApplicationException {
        return objPermissionHubService.pageObjectBySysCategory(params.getParams(), params.getPageInfo());
    }

    /**
     * 对象操作权限校验
     *
     * @param objOperateVo
     * @return
     * @throws ApplicationException
     */
    @PostMapping(value = "/auth", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysObjOperateVo> auth(@RequestBody BasicRequestContentVo<List<SysObjOperateVo>> objOperateVo) throws ApplicationException {
        if (objOperateVo == null) {
            return new ArrayList<>();
        }
        return objPermissionHubService.auth(objOperateVo.getParams());
    }

    /**
     * 单个对象操作权限校验
     *
     * @param objId
     * @param opCode
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/auth/{objId}/{opCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultBean sright(@PathVariable("objId") Long objId, @PathVariable("opCode") String opCode)
            throws ApplicationException {
        return new ResultBean(objPermissionHubService.auth(objId, opCode));
    }

}
