/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.controllerapi.harbormgr.vo.RepoReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.*;
import com.newland.paas.paasservice.sysmgr.vo.ApproveBo;
import com.newland.paas.paasservice.sysmgr.vo.SysTenantWithRoleVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月27日 下午2:14:56
 */
public interface SysTenantService extends BaseService<SysTenant> {
    /**
     * 描述 租户总数
     *
     * @return
     */
    int tenantCount();

    /**
     * 描述       新增租户
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:36:43
     */
    SysTenant addSysTenant(SysTenant sysTenant) throws ApplicationException;

    /**
     * 新增租户并且添加租户管理员列表
     * 描述
     *
     * @param sysTenantVo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 上午11:24:53
     */
    SysTenant addSysTenantAndMembers(SysTenantReqBo sysTenantVo) throws ApplicationException;

    /**
     * 描述       修改租户
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:36:36
     */
    SysTenant updateSysTenant(SysTenant sysTenant) throws ApplicationException;

    /**
     * 修改租户并且修改管理员列表
     * 描述
     *
     * @param sysTenantVo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午2:23:40
     */
    SysTenant updateSysTenantAndMembers(SysTenantReqBo sysTenantVo) throws ApplicationException;

    /**
     * 描述   删除租户
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:55:29
     */
    int deleteSysTenant(Long id) throws ApplicationException;

    /**
     * 描述   查询租户列表
     *
     * @param sysTenantVo
     * @param pageInfo
     * @return
     * @author linkun
     * @created 2018年6月27日 下午3:01:17
     */
    ResultPageData<SysTenant> querySysTenants(SysTenant sysTenantVo, PageInfo pageInfo);

    /**
     * 查询租户个数
     * 描述
     *
     * @param tenantName
     * @param tenantId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月28日 下午4:22:48
     */
    int countByTenantName(String tenantName, Long tenantId) throws ApplicationException;

    /**
     * @param ids
     * @return
     */
    List<SysTenant> getSysTenantsByIds(String[] ids);

    /**
     * 获取租户列表
     * 描述
     *
     * @return
     * @author linkun
     * @created 2018年7月5日 下午2:58:11
     */
    ResultPageData<SysTenantBo> getSysTenantsByPage(SysTenantReqVo sysTenantVo, PageInfo pageInfo);

    /**
     * 获取租户并且包括管理员列表
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午3:36:52
     */
    SysTenantRespVo getSysTenantAndAdmins(Long id) throws ApplicationException;

    /**
     * 页面获取租户并且包括管理员列表
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午3:36:52
     */
    SysTenantRespVo getPageSysTenantAndAdmins(Long id) throws ApplicationException;

    /**
     *
     * @param tenantId
     * @return
     * @throws ApplicationException
     */
    CreateTenantDetail getCreateTenantDetail(Long tenantId) throws ApplicationException;

    /**
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 上午10:19:47
     */
    void joinTenantSubmitProcess(SysTenantFlowReqBo params) throws ApplicationException;

    /**
     * 描述
     *
     * @param taskId
     * @return
     * @author linkun
     * @created 2018年8月17日 上午11:21:10
     */
    SysTenantFlowReqBo joinTenantGetTaskVar(Long taskId);

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年8月17日 上午11:26:32
     */
    boolean joinTenantNextProcess(SysTenantFlowReqBo params);

    /**
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 下午1:17:15
     */
    boolean joninTenantApprove(SysTenantFlowReqBo params) throws ApplicationException;

    /**
     * 描述
     *
     * @param userId
     * @return
     * @author linkun
     * @created 2018年8月20日 上午9:55:34
     */
    List<SysTenant> getAllTenantsCanJoin(Long userId);

    /**
     * 通过仓库查找租户
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     */
    int countTenantByDepot(SysTenant sysTenant) throws ApplicationException;

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年8月30日 下午7:12:34
     */
    List<SysTenantBo> listSysTenantBo(SysTenantReqVo params);

    /**
     * 根据租户ID获取租户信息
     *
     * @param tenantId
     * @return
     */
    SysTenantBo getSysTenantBo(Long tenantId);

    /**
     * 根据租户ID获取带平台角色的租户信息
     *
     * @param tenantId
     * @param userId
     * @return
     */
    SysTenantWithRoleVo getSysTenantWithRoleVo(Long tenantId, Long userId);

    /**
     * 根据租户id查询配额
     *
     * @param tenantId
     * @return
     */
    SysTenantLimitStaVo getLimitsByTenantId(Long tenantId);

    /**
     * 获取租户资源配额
     *
     * @param tenantId
     * @return
     */
    List<SysTenantLimitBo> listSysTenantLimits(Long tenantId);

    /**
     * 添加租户资源配额
     *
     * @param limitList 资源配额列表
     * @param tenantId  租户id
     * @return
     */
    int addSysTenantLimits(List<SysTenantLimitVo> limitList, Long tenantId);

    /**
     * 获取租户信息
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     */
    SysTenant getSysTenant(SysTenant sysTenant) throws ApplicationException;

    /**
     * 通过租户code查询租户
     *
     * @param tenantCode
     * @return
     */
    List<SysTenant> listTenantByTenantCode(String tenantCode);

    /**
     * 租户申请
     * @param reqInfo
     */
    void tenantApply(BasicRequestContentVo<SysTenantApplyVo> reqInfo) throws ApplicationException;

    /**
     * 提供方申请流程提交
     * @param params
     * @throws ApplicationException
     */
    void providerTenantSubmitProcess(SysTenantProviderApplyVo params) throws ApplicationException;

    /**
     * 查询租户镜像列表
     *
     * @param repoReqVo
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    ResultPageData queryTenantImageRepo(RepoReqVo repoReqVo, PageInfo pageInfo) throws ApplicationException;

    /**
     * 审批创建申请
     * @param approveBo
     */
    void approveSysTenant(ApproveBo approveBo);

    /**
     * 新增租户审批回调
     * @param sysTenantApplyVo
     */
    void approveNewSysTenant(SysTenantApplyVo sysTenantApplyVo) throws ApplicationException;

    /**
     * 审批租户提供方申请
     * @param approveBo
     */
    void approveSysTenantProvider(ApproveBo approveBo);


    /**
     * 租户提供方审批回调
     * @param applyVo
     */
    void approveStatusSysTenantProvider(SysTenantProviderApplyVo applyVo) throws ApplicationException;

    /**
     * 获取申请租户工单详情
     * @param proinstanceId
     * @return
     */
    SysTenantApplyBo applySysTenantDetail(Long proinstanceId);

    /**
     * 获取提供方申请流程详情
     * @param proinstanceId
     * @return
     */
    SysTenantProviderApplyVo getSysTenantProviderDetail(Long proinstanceId);

}

