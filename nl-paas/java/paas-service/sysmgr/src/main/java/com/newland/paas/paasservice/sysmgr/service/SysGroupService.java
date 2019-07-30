package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.vo.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 * @author linkun
 */
public interface SysGroupService {

    int groupCount() throws ApplicationException;

    ResultPageData<SysGroup> page(SysGroupReqBo params, PageInfo pageInfo) throws ApplicationException;

    List<SysGroup> list(SysGroupReqBo params) throws ApplicationException;

    List<SysGroupRespBo> listMyGroup() throws ApplicationException;

    List<SysGroupWithRoleVo> listMyGroupWithRole() throws ApplicationException;

    /**
     * 查询我管理的工组
     *
     * @return
     */
    List<SysGroupRespBo> listManagedGroup() throws ApplicationException;

    void add(SysGroup params) throws ApplicationException;

    void update(SysGroup params) throws ApplicationException;

     void delete(Long id) throws ApplicationException;

    SysGroup get(Long id) throws ApplicationException;

    /**
     * 通过工组获取系统分组列表 描述
     *
     * @param groupId
     * @return
     */
    List<SysGroupForCategoryRespBo> getCategorysByGroup(Long groupId);

    /**
     * 根据工组ID，获取子工组
     *
     * @param id
     * @return
     */
    List<SysGroup> getSubGroupById(Long id);

    List<SysGroup> getSubGroupById(List<Long> id);

    /**
     * 根据成员ID查询他的所属工组列表
     *
     * @param userId 成员ID
     * @return List
     * @throws ApplicationException
     */
    List<SysGroupForMemberRespVO> queryListByUserId(Long userId) throws ApplicationException;

    /**
     * 根据成员ID查询他的所属工组分页列表
     *
     * @param sysGroupPagingReqBO
     * @param pageInfo
     * @return ResultPageData
     * @throws ApplicationException
     */
    ResultPageData<SysGroupForMemberRespVO> pagedQueryListByUserId(SysGroupPagingReqBO sysGroupPagingReqBO,
                                                                   PageInfo pageInfo) throws ApplicationException;

    /**
     * 根据成员ID删除该成员的所有所属工组
     *
     * @param userId
     * @throws ApplicationException
     */
    void deleteGroupsByUserId(Long userId) throws ApplicationException;

    /**
     * 添加该成员的所属工组
     *
     * @param sysGroupReqVO
     * @throws ApplicationException
     */
    void addGroups(SysGroupReqVO sysGroupReqVO) throws ApplicationException;

    /**
     * 成员关联工组时，查询所有工组列表数据，以及所属工组数组
     *
     * @param userId 成员ID
     * @param tenantId 租户id
     * @return SysGroupOtherRespVO
     * @throws ApplicationException
     */
    SysGroupOtherRespVO queryAllList(Long userId, Long tenantId) throws ApplicationException;

    /**
     * 获取工组树
     *
     * @return
     */
    List<BaseTreeDo> getSysGroupTree(SysGroup sysGroup) throws ApplicationException;

    /**
     * 获取子工组
     *
     * @return
     */
    List<BaseTreeDo> listSubSysGroup(SysGroup sysGroup) throws ApplicationException;

    /**
     * 获取当前用户的租户的根工组 create by wrp
     *
     * @return
     */
    String tenantRootGroup();

    /**
     * 获取当前租户用户的归属工组的父工组 create by wrp
     *
     * @return
     */
    String tenantParentGroup();

    /**
     * 获取运营租户的根工组 create by wrp
     *
     * @return
     */
    String yyTenantRootGroup();

    /**
     * 获取运维租户的根工组 create by wrp
     *
     * @return
     */
    String ywTenantRootGroup();

    /**
     * 查询所有父工组
     *
     * @param id
     * @return
     */
    List<SysGroup> getParentGroupById(Long id);
}
