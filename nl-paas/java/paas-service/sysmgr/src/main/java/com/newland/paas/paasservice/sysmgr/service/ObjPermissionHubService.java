package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysObjOperateVo;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryObjReqVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 对象权限集中管理，集中管理对象权限和系统分组权限
 *
 * @author zhongqingjiang
 */
public interface ObjPermissionHubService {

    /**
     * 我的系统分组树
     *
     * @return
     * @throws ApplicationException
     */
    TreeNode<BaseTreeDo> getMySysCategoryTree() throws ApplicationException;

    /**
     * 授权系统分组树
     *
     * @return
     * @throws ApplicationException
     */
    TreeNode<BaseTreeDo> getGrantedSysCategoryTree() throws ApplicationException;

    /**
     * 获取授权系统分组列表
     *
     * @param objType
     * @return
     * @throws ApplicationException
     */
    List<SysCategory> listGrantedSysCategory(String objType) throws ApplicationException;

    /**
     * 根据系统分组获取对象列表 - 分页
     *
     * @param req
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    ResultPageData<SysUserObj> pageObjectBySysCategory(SysCategoryObjReqVo req, PageInfo pageInfo) throws ApplicationException;

    /**
     * 对象操作权限校验
     *
     * @param objOperates
     * @return
     */
    List<SysObjOperateVo> auth(List<SysObjOperateVo> objOperates) throws ApplicationException;

    /**
     * 单个对象操作权限校验
     *
     * @param objId
     * @param operateCode
     * @return
     */
    Boolean auth(Long objId, String operateCode) throws ApplicationException;

}
