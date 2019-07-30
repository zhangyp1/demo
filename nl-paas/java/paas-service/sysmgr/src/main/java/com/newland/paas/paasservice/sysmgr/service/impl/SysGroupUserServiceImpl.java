package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.ListUtils;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysGroupUserError;
import com.newland.paas.paasservice.sysmgr.service.SysGroupRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysGroupUserService;
import com.newland.paas.paasservice.sysmgr.service.SysUserGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 描述
 * 
 * @author linkun
 * @created 2018-08-01 13:57:24
 */
@Service("sysGroupUserService")
public class SysGroupUserServiceImpl implements SysGroupUserService {

    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Autowired
    private SysUserGroupRoleService sysUserGroupRoleService;
    @Autowired
    private SysGroupRoleService sysGroupRoleService;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    @Override
    public ResultPageData<List<SysGroupUserRespBo>> pageUsersByGroupId(Long groupId, PageInfo pageInfo) {
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysGroupUserRespBo> rrs = sysGroupUserMapper.getUsersByGroup(groupId);
        pageInfo.setTotalRecord(page.getTotal());
        return new ResultPageData(rrs, pageInfo);
    }

    @Override
    public List<SysGroupUserRespBo> list(SysGroupUserReqBo params) throws ApplicationException {
        return sysGroupUserMapper.selectBySelective(params);
    }

    @Override
    public int add(SysGroupUser params) throws ApplicationException {
        return sysGroupUserMapper.insertSelective(params);
    }

    @Override
    public int update(SysGroupUser params) throws ApplicationException {
        return sysGroupUserMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int delete(Long id) throws ApplicationException {
        return sysGroupUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SysGroupUserRespBo get(Long id) throws ApplicationException {
        return sysGroupUserMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int addUsers(SysGroupUserReqBo params) throws ApplicationException {
        if (params.getGroupId() == null) {
            throw new ApplicationException(SysGroupUserError.GROUP_ID_IS_NULL);
        }
        Long groupId = params.getGroupId();
        Long tenantId = params.getTenantId() == null ? RequestContext.getTenantId() : params.getTenantId();
        Long[] addUserIds = params.getUserIds();
        List<Long> addUserIdList = Arrays.asList(addUserIds);

        // 获取工组信息
        SysGroup groupInfo = sysGroupMapper.selectByPrimaryKey(groupId);
        // 获取工组成员信息
        SysGroupUserReqBo groupUserReqBo = new SysGroupUserReqBo();
        groupUserReqBo.setGroupId(groupId);
        List<SysGroupUserRespBo> oldGroupUserInfoList = sysGroupUserMapper.selectBySelective(groupUserReqBo);

        // 删除被移除用户的用户与工组角色关系
        for (SysGroupUserRespBo sysGroupUserRespBo : oldGroupUserInfoList) {
            Long userId = sysGroupUserRespBo.getUserId();
            if (!addUserIdList.contains(userId)) {
                List<Long> groupIds = new ArrayList<>();
                groupIds.add(groupId);
                List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listByGroupIds(groupIds);
                if (sysGroupRoleList != null && !sysGroupRoleList.isEmpty()) {
                    for (SysGroupRole sysGroupRole : sysGroupRoleList) {
                        sysUserGroupRoleService.removeUser(sysGroupRole.getGroupRoleId(), userId);
                    }
                }
            }
        }

        // 获取旧的管理员列表
        SysGroupUserReqBo isAdminUsersParam = new SysGroupUserReqBo();
        isAdminUsersParam.setGroupId(params.getGroupId());
        isAdminUsersParam.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
        List<SysGroupUserRespBo> isAdminUsers = sysGroupUserMapper.selectBySelective(isAdminUsersParam);
        List<Long> adminUserIds = ListUtils.transformedList(isAdminUsers, SysGroupUserRespBo::getUserId);

        // 删除旧的工组用户关系
        SysGroupUserReqBo groupParam = new SysGroupUserReqBo();
        groupParam.setGroupId(params.getGroupId());
        sysGroupUserMapper.deleteBySelective(groupParam);

        // 添加新的工组用户关系
        Date now = new Date();
        int count = 0;
        Long[] userIds = params.getUserIds();
        for (Long userId : userIds) {
            if (sysUserMapper.selectByPrimaryKey(userId) == null) {
                throw new ApplicationException(SysGroupUserError.USER_NOT_EXIST);
            }
            SysGroupUser info = new SysGroupUser();
            info.setCreateDate(now);
            info.setCreatorId(RequestContext.getUserId());
            info.setTenantId(tenantId);
            info.setGroupId(params.getGroupId());
            info.setUserId(userId);
            // 重新设置管理员
            for (Long adminUserId : adminUserIds) {
                if (userId.intValue() == adminUserId.intValue()) {
                    info.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                    break;
                }
            }
            sysGroupUserMapper.insertSelective(info);
            count++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int addGroups(SysGroupUserReqBo params) throws ApplicationException {
        if (params.getUserId() == null) {
            throw new ApplicationException(SysGroupUserError.USER_ID_IS_NULL);
        }

        // 删除旧关系
        SysGroupUserReqBo userParam = new SysGroupUserReqBo();
        userParam.setUserId(params.getUserId());
        sysGroupUserMapper.deleteBySelective(userParam);

        // 添加新关系
        Date now = new Date();
        int count = 0;
        Long[] groupIds = params.getGroupIds();
        for (Long groupId : groupIds) {

            if (sysGroupMapper.selectByPrimaryKey(groupId) == null) {
                throw new ApplicationException(SysGroupUserError.GROUP_NOT_EXIST);
            }

            SysGroupUser info = new SysGroupUser();
            info.setCreateDate(now);
            info.setCreatorId(RequestContext.getUserId());
            info.setTenantId(RequestContext.getTenantId());
            info.setGroupId(groupId);
            info.setUserId(params.getUserId());
            sysGroupUserMapper.insertSelective(info);
            count++;
        }
        return count;
    }

    @Override
    public List<SysGroupUserRespBo> getGroupsByUser(Long userId) {
        return sysGroupUserMapper.getGroupsByUser(userId);
    }

    @Override
    public List<SysGroupUserRespBo> getGroupsByTenantAndUser(Long tenantId, Long userId) {
        List<SysGroupUserRespBo> sysGroupUserRespBoList = new ArrayList<>();
        SysGroupUserReqBo record = new SysGroupUserReqBo();
        record.setUserId(userId);
        record.setTenantId(tenantId);
        List<SysGroupUserRespBo> list = sysGroupUserMapper.selectBySelective(record);
        if (list != null && list.size() > 0) {
            sysGroupUserRespBoList = list;
        }
        return  sysGroupUserRespBoList;
    }

    @Override
    public List<SysGroupUserRespBo> getAllUsersByTenant(SysGroupUserReqBo params) {
        Long tenantId = params.getTenantId();
        return sysGroupUserMapper.getAllUsersByTenant(tenantId == null ? RequestContext.getTenantId() : tenantId,
                params.getGroupId(), params.getUserNameLike());
    }

    @Override
    public List<SysGroupUserRespBo> getAllGroupsByTenant(SysGroupUserReqBo params) {
        return sysGroupUserMapper.getAllGroupsByTenant(RequestContext.getTenantId(), params.getUserId());
    }

    @Override
    public int setAdmin(SysGroupUserReqBo params) throws ApplicationException {
        // 用户在ROOT工组中撤销管理员身份时，同时也要撤销租户管理员身份
        SysGroupUserRespBo sysGroupUserRespBo = sysGroupUserMapper.selectByPrimaryKey(params.getGroupUserId());
        if (sysGroupUserRespBo != null) {
            SysGroup sysGroup = sysGroupMapper.selectByPrimaryKey(sysGroupUserRespBo.getGroupId());
            if (sysGroup != null && Objects.equals(sysGroup.getGroupName(), "ROOT")) {
                SysTenantMember sysTenantMemberReq = new SysTenantMember();
                sysTenantMemberReq.setTenantId(sysGroupUserRespBo.getTenantId());
                sysTenantMemberReq.setUserId(sysGroupUserRespBo.getUserId());
                List<SysTenantMember> sysTenantMembers = sysTenantMemberMapper.selectBySelective(sysTenantMemberReq);
                if (sysTenantMembers != null && !sysTenantMembers.isEmpty()) {
                    SysTenantMember sysTenantMember = sysTenantMembers.get(0);
                    SysTenantMember sysTenantMemberRes = new SysTenantMember();
                    sysTenantMemberRes.setId(sysTenantMember.getId());
                    sysTenantMemberRes.setIsAdmin(params.getIsAdmin());
                    sysTenantMemberMapper.updateByPrimaryKeySelective(sysTenantMemberRes);
                }
            }

            List<Long> groupIds = new ArrayList<>();
            groupIds.add(sysGroupUserRespBo.getGroupId());
            List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listByGroupIds(groupIds);
            Long groupAdminRoleId = null;
            if (sysGroupRoleList != null && sysGroupRoleList.size() > 0) {
                for (SysGroupRole sysGroupRole : sysGroupRoleList) {
                    if (sysGroupRole.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                        groupAdminRoleId = sysGroupRole.getGroupRoleId();
                    }
                }
            }

            if (groupAdminRoleId != null) {
                // 设置或撤销工组管理员角色
                if (params.getIsAdmin().equals(SysMgrConstants.IS_ADMIN_YES)) {
                    sysUserGroupRoleService.addUser(groupAdminRoleId, sysGroupUserRespBo.getUserId());
                } else {
                    sysUserGroupRoleService.removeUser(groupAdminRoleId, sysGroupUserRespBo.getUserId());
                    // 如果撤销了用户的工组管理员权限，则注销当前该用户登录状态
                    BasicRequestContentVo<String> inputs = new BasicRequestContentVo<>();
                    restTemplateUtils.postForEntity(microservicesProperties.getAuth(), "/v1/token/logout/" + sysGroupUserRespBo.getUserId(), inputs,
                            new ParameterizedTypeReference<Object>() {
                            });
                }
                return 1;
            }
        }
        return 0;
    }
	@Override
	public int deleteBySelective(SysGroupUserReqBo groupUserParams) {
		return sysGroupUserMapper.deleteBySelective(groupUserParams);
	}

    @Override
    public boolean isGroupAdmin(Long groupId, Long userId) {
        // 用户对一个工组是否有管理权限的判断条件：1）是该工组的管理员 2）是该工组上级工组的管理员
        SysGroupUserReqBo sysGroupUserReqBo = new SysGroupUserReqBo();
        sysGroupUserReqBo.setUserId(userId);
        sysGroupUserReqBo.setGroupId(groupId);
        List<SysGroupUserRespBo> sysGroupUserRespBoList = sysGroupUserMapper.selectBySelective(sysGroupUserReqBo);
        if (sysGroupUserRespBoList != null && sysGroupUserRespBoList.size() > 0) {
            if (sysGroupUserRespBoList.get(0).getIsAdmin().equals(SysMgrConstants.IS_ADMIN_YES)) {
                return true;
            }
        }

        SysGroup sysGroup = sysGroupMapper.selectByPrimaryKey(groupId);
        List<Long> groupIds = new ArrayList<>();
        // 根工组
        if (Objects.equals(sysGroup.getGroupName(), "ROOT")) {
            // 查询是否为根工组管理员
            groupIds.add(groupId);
        } else {
            // 查询所有上级工组
            List<SysGroup> parentGroupList = sysGroupMapper.getParentGroupById(groupId);
            if (parentGroupList == null || parentGroupList.isEmpty()) {
                return false;
            }
            // 查询是否为上级工组管理员
            groupIds = ListUtils.transformedList(parentGroupList, SysGroup::getGroupId);
        }
        List<SysGroupUserRespBo> groupAdmins = sysGroupUserMapper.getGroupAdmins(groupIds, userId);
        return !(groupAdmins == null || groupAdmins.isEmpty());
    }
}
