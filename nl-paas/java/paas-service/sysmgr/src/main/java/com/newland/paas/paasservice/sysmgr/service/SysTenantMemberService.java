/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqEx;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantMemberReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述 租户成员信息
 * 
 * @author linkun
 */
public interface SysTenantMemberService extends BaseService<SysTenantMember> {

    /**
     * 租户成员数量统计
     *
     * @param tenantId
     * @return
     * @throws ApplicationException
     */
    int memberCount(Long tenantId) throws ApplicationException;

    /**
     * 查询租户成员列表 描述
     * 
     * @param tenantId
     * @param userName
     * @return List
     * @throws ApplicationException
     */
    List<TenantUserVO> getMembersByTenant(Long tenantId, String userName) throws ApplicationException;

    /**
     * 查询租户成员列表 描述
     * 
     * @param sysTenantMemberReqBO
     * @param pageInfo
     * @return ResultPageData
     * @throws ApplicationException
     */
    ResultPageData<TenantUserVO> getMembersByTenant1(SysTenantMemberReqBO sysTenantMemberReqBO, PageInfo pageInfo)
        throws ApplicationException;

    /**
     * 查询非某租户成员的用户列表数据
     * 描述
     * @author linkun
     * @created 2018年8月22日 上午10:37:00
     * @param params
     * @return
     * @throws ApplicationException
     */
    List<TenantUserVO> getNoMemberUsers(SysTenantMemberReqEx params) throws ApplicationException;

    /**
     * 新增或更新租户成员
     *
     * @param sysRoleUserReqBo
     * @return
     * @throws ApplicationException
     */
    int addMembers(SysRoleUserReqBo sysRoleUserReqBo) throws ApplicationException;

    /**
     * 根据主键id获取成员详情
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    TenantUserVO getMemberById(Long id) throws ApplicationException;

    /**
     * 是否租户管理员
     *
     * @param tenantId
     * @param userId
     * @return
     */
    Boolean isTenantAdmin(Long tenantId, Long userId);

    /**
     * 设置租户管理员
     * @param params
     * @throws ApplicationException
     */
    void setAdmin(SysTenantMemberReqVo params) throws ApplicationException;

}
