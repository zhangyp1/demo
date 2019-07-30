/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.ListUtils;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantMemberReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.common.SysUserStatusConsts;
import com.newland.paas.paasservice.sysmgr.error.SysTenantMemberError;
import com.newland.paas.paasservice.sysmgr.service.SysGroupUserService;
import com.newland.paas.paasservice.sysmgr.service.SysRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantMemberService;
import com.newland.paas.paasservice.sysmgr.service.SysUserGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述
 *
 * @author linkun
 */
@Service
public class SysTenantMemberServiceImpl implements SysTenantMemberService {

    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;

    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysGroupUserService sysGroupUserService;
    @Autowired
    private SysUserGroupRoleService sysUserGroupRoleService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    @Override
    public List<SysTenantMember> selectBySelective(SysTenantMember params) {
        return sysTenantMemberMapper.selectBySelective(params);
    }

    @Override
    public SysTenantMember selectByPrimaryKey(Long id) {
        return sysTenantMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int deleteByPrimaryKey(Long id) {
        return sysTenantMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBySelective(SysTenantMember params) {
        return sysTenantMemberMapper.deleteBySelective(params);
    }

    @Override
    public int insert(SysTenantMember params) {
        return sysTenantMemberMapper.insert(params);
    }

    @Override
    public int insertSelective(SysTenantMember params) {
        return sysTenantMemberMapper.insertSelective(params);
    }

    @Override
    public int countBySelective(SysTenantMember params) {
        return sysTenantMemberMapper.countBySelective(params);
    }

    @Override
    public int updateByPrimaryKeySelective(SysTenantMember params) {
        return sysTenantMemberMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    public int updateByPrimaryKey(SysTenantMember params) {
        return sysTenantMemberMapper.updateByPrimaryKey(params);
    }

    @Override
    public int memberCount(Long tenantId) {
        SysTenantMember params = null;
        if (tenantId != null) {
            params = new SysTenantMember();
            params.setTenantId(tenantId);
        }
        return sysTenantMemberMapper.countBySelective(params);
    }

    @Override
    public List<TenantUserVO> getMembersByTenant(Long tenantId, String userName) {
        List<TenantUserBO> rrs = sysTenantMapper.getSysTenantUsers(tenantId, null, userName);
        List<TenantUserVO> tenantUserVOs = new ArrayList<>();
        for (TenantUserBO bo : rrs) {
            TenantUserVO vo = new TenantUserVO();
            BeanUtils.copyProperties(bo, vo);
            tenantUserVOs.add(vo);
        }
        return tenantUserVOs;
    }

    @Override
    public ResultPageData<TenantUserVO> getMembersByTenant1(SysTenantMemberReqBO sysTenantMemberReqBO, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        Long tenantId = RequestContext.getTenantId();
        String userName = sysTenantMemberReqBO.getUserName();
        List<TenantUserBO> rrs = sysTenantMapper.getSysTenantUsers(tenantId, null, userName);
        return new ResultPageData(rrs);
    }

    @Override
    public List<TenantUserVO> getNoMemberUsers(SysTenantMemberReqEx params) {
        List<TenantUserBO> rrs = sysTenantMapper.getNoMemberUsers(params.getTenantId(), params.getUserNameLike());
        List<TenantUserVO> tenantUserVOs = new ArrayList<>();
        List<Long> userIds = params.getUserIds();
        for (TenantUserBO bo : rrs) {
            TenantUserVO vo = new TenantUserVO();
            BeanUtils.copyProperties(bo, vo);
            if (userIds != null && !userIds.isEmpty()) {
                if (!userIds.contains(bo.getUserId())) {
                    tenantUserVOs.add(vo);
                }
            } else {
                tenantUserVOs.add(vo);
            }
        }
        return tenantUserVOs;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int addMembers(SysRoleUserReqBo params) throws ApplicationException {
        Long tenantId = params.getTenantId();
        if (tenantId == null) {
            throw new ApplicationException(SysTenantMemberError.TENANT_ID_IS_NULL);
        }
        RoleUserReqBo[] roleUsers = params.getRoleUsers();

        // 获取该租户管理员列表
        SysTenantMember tenantIsAdminPassedParam = new SysTenantMember();
        tenantIsAdminPassedParam.setTenantId(params.getTenantId());
        tenantIsAdminPassedParam.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
        tenantIsAdminPassedParam.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        List<SysTenantMember> isAdminTenantMembers = sysTenantMemberMapper.selectBySelective(tenantIsAdminPassedParam);
        List<Long> isAdminUserIds = ListUtils.transformedList(isAdminTenantMembers, SysTenantMember::getUserId);
        // 非运营管理员角色，不允许新增或修改租户管理员
        if (!RequestContext.getSession().getRoleIdList().contains(SysMgrConstants.ROLE_ID_YY_ADMIN)) {
            List<Long> isAdminUserIdsNew = Arrays.asList(roleUsers).stream()
                    .filter(roleUser -> roleUser.getRoleId().equals(SysMgrConstants.ROLE_ID_ZH_ADMIN))
                    .map(RoleUserReqBo::getUserId).sorted().collect(Collectors.toList());
            List<Long> isAdminUserIdsOld = isAdminUserIds.stream().sorted().collect(Collectors.toList());
            // 排除普通用户申请创建租户情况
            if (!isAdminUserIdsOld.isEmpty()) {
                if (isAdminUserIdsOld.size() != isAdminUserIdsNew.size()) {
                    throw new ApplicationException(SysTenantMemberError.NO_PERMISSION_CHANGE_ZHADMIN);
                } else {
                    for (int i = 0; i < isAdminUserIdsOld.size(); i++) {
                        if (isAdminUserIdsOld.get(i).longValue() != isAdminUserIdsNew.get(i).longValue()) {
                            throw new ApplicationException(SysTenantMemberError.NO_PERMISSION_CHANGE_ZHADMIN);
                        }
                    }
                }
            }
        }
        // 新增新的用户角色关系
        this.addGroupRoleUser(roleUsers, tenantId);
        // 新增新的租户成员
        this.addTenantMember(roleUsers, tenantId);
        // 删除无效数据
        this.deleteInvalidData(roleUsers, tenantId);
        // 运营管理员创建租户时缺省关联后台程序用户为租户管理员
        this.insertBackSysUserRelation(tenantId);
        return 1;
    }
    private void addTenantMember(RoleUserReqBo[] roleUsers, Long tenantId) {
        // 新增新的租户成员
        for (RoleUserReqBo roleUser : roleUsers) {
            int index = Arrays.binarySearch(SysMgrConstants.ROLE_ID_LIST_OF_ADMIN, roleUser.getRoleId());
            Boolean isAdminNow = index >= 0;
            SysTenantMember sysTenantMemberReq = new SysTenantMember();
            sysTenantMemberReq.setTenantId(tenantId);
            sysTenantMemberReq.setUserId(roleUser.getUserId());
            sysTenantMemberReq.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            List<SysTenantMember> sysTenantMembers = sysTenantMemberMapper.selectBySelective(sysTenantMemberReq);
            if (sysTenantMembers != null && !sysTenantMembers.isEmpty()) {
                // 旧租户成员
                SysTenantMember sysTenantMember = sysTenantMembers.get(0);
                Boolean isAdminBefore = sysTenantMember.getIsAdmin().equals(SysMgrConstants.IS_ADMIN_YES);
                if (isAdminBefore && !isAdminNow) {
                    sysTenantMember.setIsAdmin(SysMgrConstants.IS_ADMIN_NO);
                    sysTenantMemberMapper.updateByPrimaryKey(sysTenantMember);
                    // 如果撤销了用户的租户管理员权限，则注销当前该用户登录状态
                    logoutUser(roleUser.getUserId());
                } else if (!isAdminBefore && isAdminNow) {
                    sysTenantMember.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                    sysTenantMemberMapper.updateByPrimaryKey(sysTenantMember);
                }
            } else {
                // 新租户成员
                SysTenantMember info = new SysTenantMember();
                info.setTenantId(tenantId);
                info.setUserId(roleUser.getUserId());
                info.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
                info.setCreatorId(RequestContext.getUserId());
                // 角色是管理员的则设置为租户管理员
                if (isAdminNow) {
                    info.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                }
                sysTenantMemberMapper.insertSelective(info);
            }
        }
    }
    private void addGroupRoleUser(RoleUserReqBo[] roleUsers, Long tenantId) {
        for (RoleUserReqBo roleUser : roleUsers) {
            // 已存在的用户角色关系不变
            SysRoleUserReqBo sysRoleUserReqBo = new SysRoleUserReqBo();
            sysRoleUserReqBo.setTenantId(tenantId);
            sysRoleUserReqBo.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            sysRoleUserReqBo.setRoleId(roleUser.getRoleId());
            sysRoleUserReqBo.setUserId(roleUser.getUserId());
            List<SysRoleUserRespBo> sysRoleUserRespBos = sysRoleUserMapper.selectBySelective(sysRoleUserReqBo);
            if (sysRoleUserRespBos != null && !sysRoleUserRespBos.isEmpty()) {
                continue;
            }
            SysRoleUser info = new SysRoleUser();
            info.setTenantId(tenantId);
            info.setRoleId(roleUser.getRoleId());
            info.setUserId(roleUser.getUserId());
            info.setCreatorId(RequestContext.getUserId());
            info.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            // 获取根工组信息
            SysGroupReqBo sysGroup = new SysGroupReqBo();
            sysGroup.setTenantId(tenantId);
            sysGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
            List<SysGroup> rootGroupInfoList = sysGroupMapper.selectBySelective(sysGroup);
            Long rootGroupId = rootGroupInfoList.get(0).getGroupId();
            // 删除旧工组成员数据
            SysGroupUserReqBo sysGroupUserReqBo = new SysGroupUserReqBo();
            sysGroupUserReqBo.setGroupId(rootGroupId);
            sysGroupUserReqBo.setUserId(roleUser.getUserId());
            sysGroupUserMapper.deleteBySelective(sysGroupUserReqBo);
            // 角色是管理员的则设置为租户管理员
            int index = Arrays.binarySearch(SysMgrConstants.ROLE_ID_LIST_OF_ADMIN, roleUser.getRoleId());
            if (index >= 0) {
                // 如果新增的用户是租户管理员
                // 新增用户至根工组，并设置为工组管理员
                SysGroupUser sysGroupUser = new SysGroupUser();
                sysGroupUser.setGroupId(rootGroupId);
                sysGroupUser.setUserId(roleUser.getUserId());
                sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                sysGroupUser.setTenantId(tenantId);
                sysGroupUser.setCreatorId(RequestContext.getUserId());
                sysGroupUserMapper.insertSelective(sysGroupUser);
            }
            // 新增角色用户
            sysRoleUserMapper.insertSelective(info);
        }
    }
    private void deleteInvalidData(RoleUserReqBo[] roleUsers, Long tenantId) throws ApplicationException {
        // 删除不是租户成员的工组成员
        Long[] userIds = new Long[roleUsers.length];
        for (int i = 0; i < roleUsers.length; i++) {
            RoleUserReqBo bo = roleUsers[i];
            userIds[i] = bo.getUserId();
        }
        SysGroupUserReqBo groupUserParams = new SysGroupUserReqBo();
        groupUserParams.setTenantId(tenantId);
        groupUserParams.setNotInUserIds(userIds);
        sysGroupUserService.deleteBySelective(groupUserParams);
        SysUserGroupRoleReqBo userGroupRoleParams = new SysUserGroupRoleReqBo();
        userGroupRoleParams.setNotInUserIds(userIds);
        userGroupRoleParams.setTenantId(tenantId);
        sysUserGroupRoleService.deleteBySelective(userGroupRoleParams);

        // 删除无效用户角色关系
        SysRoleUserReqBo sysRoleUserReqBo = new SysRoleUserReqBo();
        sysRoleUserReqBo.setTenantId(tenantId);
        sysRoleUserReqBo.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        List<SysRoleUserRespBo> sysRoleUserRespBos = sysRoleUserMapper.selectBySelective(sysRoleUserReqBo);
        for (SysRoleUserRespBo sysRoleUserRespBo : sysRoleUserRespBos) {
            Boolean bFind = false;
            for (RoleUserReqBo roleUser : roleUsers) {
                SysRole sysGroupRole = sysRoleService.getGroupRole(sysRoleUserRespBo.getRoleId());
                if (sysGroupRole == null) {
                    // 平台角色
                    if (Objects.equals(roleUser.getUserId(), sysRoleUserRespBo.getUserId())
                            && Objects.equals(roleUser.getRoleId(), sysRoleUserRespBo.getRoleId())) {
                        bFind = true;
                    }
                } else {
                    // 工组角色
                    if (Objects.equals(roleUser.getUserId(), sysRoleUserRespBo.getUserId())) {
                        bFind = true;
                    }
                }
            }
            if (!bFind) {
                sysRoleUserMapper.deleteByPrimaryKey(sysRoleUserRespBo.getId());
            }
        }
        // 删除无效租户成员
        SysTenantMember sysTenantMemberReq = new SysTenantMember();
        sysTenantMemberReq.setTenantId(tenantId);
        sysTenantMemberReq.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        List<SysTenantMember> sysTenantMembers = sysTenantMemberMapper.selectBySelective(sysTenantMemberReq);
        for (SysTenantMember sysTenantMember : sysTenantMembers) {
            Boolean bFind = false;
            for (RoleUserReqBo roleUser : roleUsers) {
                if (Objects.equals(roleUser.getUserId(), sysTenantMember.getUserId())) {
                    bFind = true;
                }
            }
            if (!bFind) {
                sysTenantMemberMapper.deleteByPrimaryKey(sysTenantMember.getId());
                logoutUser(sysTenantMember.getUserId());
            }
        }
    }
    private void insertBackSysUserRelation(Long tenantId) {
        // 运营理管理员创建租户时缺省关联后台程序用户为租户管理员
        SysTenantMember sysTenantMember = null;
        SysRoleUser sysRoleUser = null;
        SysUser sysUserParam = new SysUser();
        sysUserParam.setStatus(SysUserStatusConsts.NOTLOGIN.getValue());
        sysUserParam.setDelFlag(Short.valueOf("0"));
        List<SysUser> listSysUser = this.sysUserMapper.selectBySelective(sysUserParam);
        for (Iterator<?> iterator = listSysUser.iterator(); iterator.hasNext();) {
            SysUser sysUser = (SysUser) iterator.next();
            // 加入系统租户成员
            sysTenantMember = new SysTenantMember();
            sysTenantMember.setUserId(sysUser.getUserId());
            sysTenantMember.setTenantId(tenantId);
            sysTenantMember.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            sysTenantMember.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
            sysTenantMember.setWorkType("加入租户");
            this.sysTenantMemberMapper.insertSelective(sysTenantMember);
            // 加入角色成员
            sysRoleUser = new SysRoleUser();
            sysRoleUser.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            sysRoleUser.setRoleId(SysMgrConstants.ROLE_ID_ZH_ADMIN);
            sysRoleUser.setTenantId(tenantId);
            sysRoleUser.setUserId(sysUser.getUserId());
            this.sysRoleUserMapper.insertSelective(sysRoleUser);
        }
    }

    @Override
    public TenantUserVO getMemberById(Long id) {
        TenantUserBO bo = sysTenantMemberMapper.selectMemberById(id);
        TenantUserVO vo = new TenantUserVO();
        BeanUtils.copyProperties(bo, vo);
        return vo;
    }

    @Override
    public Boolean isTenantAdmin(Long tenantId, Long userId) {
        SysTenantMember sysTenantMember = new SysTenantMember();
        sysTenantMember.setUserId(userId);
        sysTenantMember.setTenantId(tenantId);
        sysTenantMember.setDelFlag((short) 0);
        sysTenantMember.setStatus("1");
        List<SysTenantMember> result = sysTenantMemberMapper.selectBySelective(sysTenantMember);
        if (CollectionUtils.isEmpty(result)) {
            return false;
        }
        SysTenantMember sysTenantMember2 = result.get(0);
        return "1".equals(sysTenantMember2.getIsAdmin().toString());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void setAdmin(SysTenantMemberReqVo params) throws ApplicationException {
        // 非运营管理员角色，不允许新增或修改租户管理员
        if (!RequestContext.getSession().getRoleIdList().contains(SysMgrConstants.ROLE_ID_YY_ADMIN)) {
            throw new ApplicationException(SysTenantMemberError.NO_PERMISSION_CHANGE_ZHADMIN);
        }
        Long tenantId = params.getTenantId();
        Long userId = params.getUserId();
        Short isAdmin = params.getIsAdmin();

        // 根据是否是租户管理员，修改对应的用户角色
        SysRoleUserReqBo sysRoleUserReqBo = new SysRoleUserReqBo();
        sysRoleUserReqBo.setUserId(userId);
        sysRoleUserReqBo.setTenantId(tenantId);
        sysRoleUserReqBo.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        List<SysRoleUserRespBo> sysRoleUserRespBoList = sysRoleUserMapper.selectBySelective(sysRoleUserReqBo);
        if (sysRoleUserRespBoList == null || sysRoleUserRespBoList.isEmpty()) {
            throw new ApplicationException(SysTenantMemberError.ROLE_USER_IS_NOT_FOUND);
        }
        SysRoleUserRespBo sysRoleUser = sysRoleUserRespBoList.get(0);
        if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNYIN)) {
            // 运营租户
            // 当前运营只有一个角色：运营管理员，因此运营租户成员都只能是运营租户的管理员
            if (isAdmin.equals(SysMgrConstants.IS_ADMIN_NO)) {
                throw new ApplicationException(SysTenantMemberError.NON_ADMIN_IS_NOT_SUPPORTED);
            }
        } else if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNWEI)) {
            // 运维租户
            sysRoleUser.setRoleId(isAdmin.equals(SysMgrConstants.IS_ADMIN_YES) ? SysMgrConstants.ROLE_ID_YW_ADMIN : SysMgrConstants.ROLE_ID_YW_NORMAL);
        } else if (tenantId.equals(SysMgrConstants.TENANT_ID_PINGTAI)) {
            // 平台管理租户
            // 当前平台管理只有一个角色：平台管理员，因此平台管理租户成员都只能是平台管理租户的管理员
            if (isAdmin.equals(SysMgrConstants.IS_ADMIN_NO)) {
                throw new ApplicationException(SysTenantMemberError.NON_ADMIN_IS_NOT_SUPPORTED);
            }
        } else if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
            // 普通租户
            sysRoleUser.setRoleId(isAdmin.equals(SysMgrConstants.IS_ADMIN_YES) ? SysMgrConstants.ROLE_ID_ZH_ADMIN : SysMgrConstants.ROLE_ID_ZH_NORMAL);
        } else {
            // 其他租户ID
            throw new ApplicationException(SysTenantMemberError.UNEXPECTED_TENANT_ID);
        }
        sysRoleUserMapper.updateByPrimaryKey(sysRoleUser);
        // 更新租户成员
        SysTenantMember tenantUserParam = new SysTenantMember();
        tenantUserParam.setTenantId(tenantId);
        tenantUserParam.setUserId(userId);
        List<SysTenantMember> sysTenantMembers = sysTenantMemberMapper.selectBySelective(tenantUserParam);
        if (sysTenantMembers != null && !sysTenantMembers.isEmpty()) {
            SysTenantMember sysTenantMember = sysTenantMembers.get(0);
            sysTenantMember.setIsAdmin(isAdmin);
            sysTenantMemberMapper.updateByPrimaryKeySelective(sysTenantMember);
        }
        // 更新用户工组关系
        updateRelation(tenantId, userId, isAdmin);
        if (isAdmin.equals(SysMgrConstants.IS_ADMIN_NO)) {
            // 如果撤销了用户的租户管理员权限，则注销当前该用户登录状态
            logoutUser(userId);
        }
    }

    private void updateRelation(Long tenantId, Long userId, Short isAdmin) {
        // 获取该租户的ROOT工组信息
        SysGroupReqBo sysGroupReqBo = new SysGroupReqBo();
        sysGroupReqBo.setTenantId(tenantId);
        sysGroupReqBo.setGroupName("ROOT");
        List<SysGroup> sysGroupList = sysGroupMapper.selectBySelective(sysGroupReqBo);
        if (sysGroupList == null || sysGroupList.isEmpty()) {
            return;
        }
        SysGroup rootGroupInfo = sysGroupList.get(0);
        // 获取该用户在ROOT工组的用户信息
        SysGroupUserRespBo rootGroupUserInfo = null;
        SysGroupUserReqBo sysGroupUserReqBo = new SysGroupUserReqBo();
        sysGroupUserReqBo.setUserId(userId);
        sysGroupUserReqBo.setGroupId(rootGroupInfo.getGroupId());
        List<SysGroupUserRespBo> sysGroupUserRespBos = sysGroupUserMapper.selectBySelective(sysGroupUserReqBo);
        if (sysGroupUserRespBos != null && !sysGroupUserRespBos.isEmpty()) {
            rootGroupUserInfo = sysGroupUserRespBos.get(0);
        }
        if (isAdmin == SysMgrConstants.IS_ADMIN_YES) {
            if (rootGroupUserInfo == null) {
                // 该用户是租户管理员，且ROOT工组中不存在该信息，则新增该用户为ROOT工组成员，并设置为工组管理员
                SysGroupUser sysGroupUser = new SysGroupUser();
                sysGroupUser.setUserId(userId);
                sysGroupUser.setGroupId(rootGroupInfo.getGroupId());
                sysGroupUser.setTenantId(tenantId);
                sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                sysGroupUserMapper.insertSelective(sysGroupUser);
            } else {
                // 更新该用户对应的ROOT工组成员信息
                SysGroupUser sysGroupUser = new SysGroupUser();
                sysGroupUser.setId(rootGroupUserInfo.getId());
                sysGroupUser.setUserId(userId);
                sysGroupUser.setTenantId(tenantId);
                sysGroupUser.setGroupId(rootGroupInfo.getGroupId());
                sysGroupUser.setIsAdmin(isAdmin);
                sysGroupUserMapper.updateByPrimaryKeySelective(sysGroupUser);
            }
        } else if (rootGroupUserInfo != null)  {
            sysGroupUserMapper.deleteByPrimaryKey(rootGroupUserInfo.getId());
        }
    }

    private void logoutUser(Long userId) {
        BasicRequestContentVo<String> inputs = new BasicRequestContentVo<>();
        restTemplateUtils.postForEntity(microservicesProperties.getAuth(), "/v1/token/logout/" + userId, inputs,
                new ParameterizedTypeReference<Object>() {
                });
    }
}
