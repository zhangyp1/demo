package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupRoleSysRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRoleSysRole;
import com.newland.paas.paasservice.sysmgr.service.SysGroupRoleSysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 工组角色与系统角色关系实现类
 *
 * @author zhongqingjiang
 */
@Service
public class SysGroupRoleSysRoleServiceImpl implements SysGroupRoleSysRoleService {

    @Autowired
    SysGroupRoleSysRoleMapper sysGroupRoleSysRoleMapper;

    @Override
    public void add(SysGroupRoleSysRole sysGroupRoleSysRole) {
        sysGroupRoleSysRole.setTenantId(RequestContext.getTenantId());
        sysGroupRoleSysRole.setCreatorId(RequestContext.getUserId());
        sysGroupRoleSysRoleMapper.insertSelective(sysGroupRoleSysRole);
    }

    @Override
    public void update(SysGroupRoleSysRole sysGroupRoleSysRole) {
        sysGroupRoleSysRoleMapper.updateByPrimaryKey(sysGroupRoleSysRole);
    }

    @Override
    public void delete(Long groupRoleSysRoleId) {
        sysGroupRoleSysRoleMapper.deleteByPrimaryKey(groupRoleSysRoleId);
    }

    @Override
    public SysGroupRoleSysRole getByRoleId(Long roleId) {
        SysGroupRoleSysRole sysGroupRoleSysRole = null;
        if (roleId != null) {
            SysGroupRoleSysRole req = new SysGroupRoleSysRole();
            req.setRoleId(roleId);
            List<SysGroupRoleSysRole> sysGroupRoleSysRoleList = sysGroupRoleSysRoleMapper.selectBySelective(req);
            if (!CollectionUtils.isEmpty(sysGroupRoleSysRoleList)) {
                sysGroupRoleSysRole = sysGroupRoleSysRoleList.get(0);
            }
        }
        return sysGroupRoleSysRole;
    }

    @Override
    public SysGroupRoleSysRole getByGroupRoleId(Long groupRoleId) {
        SysGroupRoleSysRole req = new SysGroupRoleSysRole();
        req.setGroupRoleId(groupRoleId);
        List<SysGroupRoleSysRole> sysGroupRoleSysRoleList = sysGroupRoleSysRoleMapper.selectBySelective(req);
        if (CollectionUtils.isEmpty(sysGroupRoleSysRoleList)) {
            return null;
        }
        return sysGroupRoleSysRoleList.get(0);
    }
}
