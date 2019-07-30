package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysUserGroupRoleError;
import com.newland.paas.paasservice.sysmgr.service.*;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午4:44:53
 */
@Service("sysUserGroupRoleService")
public class SysUserGroupRoleServiceImpl implements SysUserGroupRoleService {

    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysGroupRoleService sysGroupRoleService;
    @Autowired
    private SysGroupRoleSysRoleService sysGroupRoleSysRoleService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    @Override
    public int groupCount() {
        return sysUserGroupRoleMapper.countBySelective(null);
    }

    @Override
    public ResultPageData<SysUserGroupRoleRespBo> page(SysUserGroupRoleReqBo params, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysUserGroupRoleRespBo> rrs = sysUserGroupRoleMapper.selectBySelective(params);
        return new ResultPageData<>(rrs);
    }

    @Override
    public List<SysUserGroupRoleRespBo> list(SysUserGroupRoleReqBo params) throws ApplicationException {
        return sysUserGroupRoleMapper.selectBySelective(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int delete(Long id) throws ApplicationException {
        SysUserGroupRole info = new SysUserGroupRole();
        info.setUserGroupRoleId(id);
        info.setDelFlag(1L);
        return sysUserGroupRoleMapper.updateByPrimaryKeySelective(info);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int removeAllAndAddUsers(SysUserGroupRoleReqBo params) throws ApplicationException {
        Long groupRoleId = params.getGroupRoleId();
        List<Long> userIds = Arrays.asList(params.getUserIds());

        SysGroupRole sysGroupRole = sysGroupRoleService.get(groupRoleId);
        if (sysGroupRole != null && sysGroupRole.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
            List<SysUserGroupRoleRespBo> beforeUserGroupRoleList = this.getUsersByGroupRole(groupRoleId);
            if (beforeUserGroupRoleList != null && beforeUserGroupRoleList.size() > 0) {
                for (SysUserGroupRoleRespBo userGroupRole : beforeUserGroupRoleList) {
                    Long userId = userGroupRole.getUserId();
                    if (!userIds.contains(userId)) {
                        // 如果撤销了用户的工组管理员权限，则注销当前该用户登录状态
                        BasicRequestContentVo<String> inputs = new BasicRequestContentVo<>();
                        restTemplateUtils.postForEntity(microservicesProperties.getAuth(), "/v1/token/logout/" + userId, inputs,
                                new ParameterizedTypeReference<Object>() {
                                });
                    }
                }
            }
        }

        this.removeAllUser(groupRoleId);
        int count = 0;
        for (Long userId : userIds) {
            this.addUser(groupRoleId, userId);
            count++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addUser(Long groupRoleId, Long userId) throws ApplicationException {
        if (groupRoleId == null) {
            throw new ApplicationException(SysUserGroupRoleError.GROUP_ROLE_ID_NOT_NULL);
        }
        if (sysUserService.selectByPrimaryKey(userId) == null) {
            throw new ApplicationException(SysUserGroupRoleError.USER_NOT_EXIST);
        }
        SysGroupRoleRespBo sysGroupRoleRespBo = sysGroupRoleService.get(groupRoleId);
        if (sysGroupRoleRespBo != null) {
            this.removeUser(groupRoleId, userId);
            // 添加新的工组角色与用户的关系
            Date now = new Date();
            SysUserGroupRole sysUserGroupRole = new SysUserGroupRole();
            sysUserGroupRole.setUserId(userId);
            sysUserGroupRole.setGroupRoleId(groupRoleId);
            sysUserGroupRole.setTenantId(RequestContext.getTenantId());
            sysUserGroupRole.setCreateDate(now);
            sysUserGroupRole.setCreatorId(RequestContext.getUserId());
            sysUserGroupRoleMapper.insertSelective(sysUserGroupRole);
            // 查询工组角色与系统角色的关系
            SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByGroupRoleId(groupRoleId);
            Long roleId = sysGroupRoleSysRole.getRoleId();
            // 新增用户与系统角色的关系
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUser.setRoleId(roleId);
            sysRoleUser.setUserId(userId);
            sysRoleUser.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            sysRoleUser.setTenantId(RequestContext.getTenantId());
            sysRoleUser.setCreatorId(RequestContext.getUserId());
            sysRoleUserService.add(sysRoleUser);

            // 设置是否管理员字段
            if (sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                this.setIsAdmin(sysGroupRoleRespBo.getGroupId(), userId, true);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void removeAllUser(Long groupRoleId) throws ApplicationException {
        SysUserGroupRoleReqBo roleParam = new SysUserGroupRoleReqBo();
        roleParam.setGroupRoleId(groupRoleId);
        List<SysUserGroupRoleRespBo> list = sysUserGroupRoleMapper.selectBySelective(roleParam);
        if (list != null && list.size() > 0)
        {
            for (SysUserGroupRoleRespBo sysUserGroupRole : list) {
                removeUser(sysUserGroupRole.getGroupRoleId(), sysUserGroupRole.getUserId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void removeUser(Long groupRoleId, Long userId) throws ApplicationException {
        SysGroupRoleRespBo sysGroupRoleRespBo = sysGroupRoleService.get(groupRoleId);
        if (sysGroupRoleRespBo != null) {
            // 删除旧的工组角色与用户的关系
            SysUserGroupRoleReqBo roleParam = new SysUserGroupRoleReqBo();
            roleParam.setGroupRoleId(groupRoleId);
            roleParam.setUserId(userId);
            sysUserGroupRoleMapper.deleteBySelective(roleParam);
            // 查询工组角色与系统角色的关系
            SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByGroupRoleId(groupRoleId);
            Long roleId = sysGroupRoleSysRole.getRoleId();
            // 删除用户与系统角色的关系
            sysRoleUserService.deleteByRoleIdAndUserId(roleId, userId);
            // 设置是否管理员字段
            if (sysGroupRoleRespBo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                this.setIsAdmin(sysGroupRoleRespBo.getGroupId(), userId, false);
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void setIsAdmin(Long groupId, Long userId, Boolean isAdmin) {
        SysGroupUserReqBo req = new SysGroupUserReqBo();
        req.setUserId(userId);
        req.setGroupId(groupId);
        List<SysGroupUserRespBo> sysGroupUserList = sysGroupUserMapper.selectBySelective(req);
        if (sysGroupUserList != null && sysGroupUserList.size() > 0) {
            SysGroupUserRespBo sysGroupUserRespBo = sysGroupUserList.get(0);
            SysGroupUser sysGroupUser = new SysGroupUser();
            sysGroupUser.setId(sysGroupUserRespBo.getId());
            if (isAdmin) {
                sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                sysGroupUserMapper.updateByPrimaryKeySelective(sysGroupUser);
            } else {
                sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_NO);
                sysGroupUserMapper.updateByPrimaryKeySelective(sysGroupUser);
            }
        }
    }

    @Override
    public List<SysUserGroupRoleRespBo> getUsersByGroupRole(Long groupRoleId) {
        return sysUserGroupRoleMapper.getUsersByGroupRole(groupRoleId);
    }

    @Override
    public List<SysUserGroupRoleRespBo> getGroupRolesByUser(Long userId, Long groupId) {
        return sysUserGroupRoleMapper.getGroupRolesByUser(userId, groupId);
    }

    @Override
    public List<SysUserGroupRoleRespBo> getAllUsersByGroup(SysUserGroupRoleReqBo params) throws ApplicationException {
        SysGroupRole sysGroupRole = sysGroupRoleService.get(params.getGroupRoleId());
        String userNameLike = params.getUserNameLike();
        return sysUserGroupRoleMapper.getAllUsersByGroup(sysGroupRole.getGroupId(),
                params.getGroupRoleId(), userNameLike);
    }

    @Override
    public int deleteBySelective(SysUserGroupRoleReqBo userGroupRoleParams) {
        return sysUserGroupRoleMapper.deleteBySelective(userGroupRoleParams);
    }

}

