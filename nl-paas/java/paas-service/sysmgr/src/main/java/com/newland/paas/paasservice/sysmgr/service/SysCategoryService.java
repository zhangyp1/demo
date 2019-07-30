package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryCount;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysCategoryBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * @program: paas-all
 * @description: 系统分组服务
 * @author: Frown
 * @create: 2018-07-30 14:51
 **/
public interface SysCategoryService {
    /**
     * 保存系统分组
     *
     * @param sysCategory
     * @throws ApplicationException
     */
    void saveSysCategory(SysCategoryBo sysCategory) throws ApplicationException;

    /**
     * 删除系统分组
     *
     * @param sysCategory
     * @throws ApplicationException
     */
    void deleteSysCategory(SysCategory sysCategory) throws ApplicationException;

    /**
     * 查询系统分组列表
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    List<SysCategory> listSysCategory(SysCategory sysCategory) throws ApplicationException;

    /**
     * 分页查询系统分组
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    ResultPageData pageQuerySysCategory(SysCategory sysCategory, PageInfo pageInfo) throws ApplicationException;

    /**
     * 获取系统分组
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    SysCategoryBo getSysCategory(SysCategory sysCategory) throws ApplicationException;

    /**
     * 获取系统分组树
     *
     * @return
     */
    TreeNode<BaseTreeDo> getSysCategoryTree();

    /**
     * 根据工组ID列表获取系统分组列表
     *
     * @param groupIdList
     * @return
     * @throws ApplicationException
     */
    List<SysCategoryVO> listSysCategoryByGroupId(List<Long> groupIdList) throws ApplicationException;

    /**
     * 查询系统分组列表
     *
     * @return
     * @throws ApplicationException
     */
    List<SysCategoryVO> listSysCategory() throws ApplicationException;

    /**
     * 根据操作代码查询有权限的系统分组列表
     *
     * @param menuOperCode
     * @return
     * @throws ApplicationException
     */
    List<SysCategoryVO> listSysCategoryByMenuOperCode(String menuOperCode) throws ApplicationException;

    /**
     * 查询子系统分组
     *
     * @param record
     * @return
     */
    List<SysCategory> listSubSysCategory(SysCategory record);

    /**
     * 查询子系统分组（包括根节点）
     *
     * @param record
     * @return
     */
    List<SysCategory> listSubAndCurSysCategory(SysCategory record);

    /**
     * 查询父系统分组（包括根节点）
     *
     * @param record
     * @return
     */
    List<SysCategory> listParentAndCurSysCategory(SysCategory record);

    /**
     * 查询我管理的系统分组
     *
     * @return
     */
    List<SysCategoryVo> listManagedSysCategory() throws ApplicationException;

    /**
     * 查询我的系统分组
     *
     * @return
     */
    List<SysCategoryVo> listMySysCategory() throws ApplicationException;

    /**
     * 查询我管理的系统分组-分页
     *
     * @return
     */
    ResultPageData<SysCategoryVo> pageManagedSysCategory(Long byGroupId, String likeCategoryName, PageInfo pageInfo) throws ApplicationException;

    /**
     * 根据系统分组ID查询
     *
     * @return
     * @throws ApplicationException
     */
    List<SysCategory> listSysCategoryByIds(List<Long> sysCategoryIds) throws ApplicationException;

    /**
     * 根据系统分组ID查询
     *
     * @return
     * @throws ApplicationException
     */
    List<SysCategoryVo> listSysCategoryVoByIds(List<Long> sysCategoryIds) throws ApplicationException;

    /**
     * 按层级统计系统分组数量
     *
     * @return
     */
    List<SysCategoryCount> countSysCategory();

}
