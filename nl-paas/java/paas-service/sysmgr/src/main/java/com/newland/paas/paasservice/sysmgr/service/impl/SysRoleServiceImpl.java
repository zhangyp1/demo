package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysRoleUserService;
import com.newland.paas.paasservice.sysmgr.vo.InquireSysRolePageListVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.utils.ResponseUtils;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色实现类
 *
 * @author zhongqingjiang
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    private static final Log LOGGER = LogFactory.getLogger(SysRoleServiceImpl.class);

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuOperRoleService sysMenuOperRoleService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Override
    public ResultPageData<SysRole> getAllSysRoleForTenant(BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException {
        ResultPageData<SysRole> rePageData = new ResultPageData<>();
        if (input != null) {
            InquireSysRolePageListVo tempInput = input.getParams();
            String roleName = null;
            Long tenantId = null;
            if (tempInput != null) {
                roleName = input.getParams().getRoleName();
                tenantId = input.getParams().getTenantId();
            }
            PageInfo pageInfo = input.getPageInfo();
            Page<SysRole> page = PageHelper.startPage(input.getPageInfo().getCurrentPage(),
                    input.getPageInfo().getPageSize());
            SysRole record = new SysRole();
            if (tenantId != null) {
                if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
                    // 租户ID大于100为普通租户，查询时转换租户ID为101
                    tenantId = SysMgrConstants.TENANT_ID_NORMAL_MIN;
                }
                record.setTenantId(tenantId);
                record.setRoleType(SysMgrConstants.ROLE_TYPE_PLATFORM);
            }
            if (roleName != null) {
                record.setRoleName(roleName);
            }
            List<SysRole> reList = this.sysRoleMapper.selectForTenant(record);
            rePageData.setList(reList);
            pageInfo.setTotalRecord(page.getTotal());
            rePageData.setPageInfo(pageInfo);
        } else {
            throw new ApplicationException(ResponseUtils.EXCEPTION);
        }
        return rePageData;
    }

    @Override
    public ResultPageData<SysRole> getAllSysRoleForAdmin(BasicPageRequestContentVo<InquireSysRolePageListVo> input)
            throws ApplicationException {
        ResultPageData<SysRole> rePageData = new ResultPageData<>();
        if (input != null) {
            InquireSysRolePageListVo tempInput = input.getParams();
            String roleName = null;
            if (tempInput != null) {
                roleName = input.getParams().getRoleName();
            }
            PageInfo pageInfo = input.getPageInfo();
            Page<SysRole> page = PageHelper.startPage(input.getPageInfo().getCurrentPage(),
                    input.getPageInfo().getPageSize());
            SysRole record = new SysRole();
            if (roleName != null) {
                record.setRoleName(roleName);
            }
            List<SysRole> reList = this.sysRoleMapper.selectForAdmin(record);
            rePageData.setList(reList);
            pageInfo.setTotalRecord(page.getTotal());
            rePageData.setPageInfo(pageInfo);
        } else {
            throw new ApplicationException(ResponseUtils.EXCEPTION);
        }
        return rePageData;
    }

    @Override
    public List<SysRole> listSysRoleByUserId(Long userId) {
        return sysRoleMapper.listSysRoleByUserId(userId);
    }

    @Override
    public List<SysRole> listSysRole(Long tenantId, Long userId) {
        List<SysRole> sysRoleList = sysRoleMapper.listSysRole(tenantId, userId);
        return sysRoleList;
    }

    /********** 工组角色模板 **********/

    @Override
    public List<SysRole> listGroupRoleTemplate() throws ApplicationException {
        Long tenantId = RequestContext.getTenantId();
        SysRole record = new SysRole();
        if (tenantId != null) {
            if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
                // 租户ID大于100为普通租户，查询时转换租户ID为101
                tenantId = SysMgrConstants.TENANT_ID_NORMAL_MIN;
            }
            record.setTenantId(tenantId);
            record.setRoleType(SysMgrConstants.ROLE_TYPE_GROUP_TEMPLATE);
        }
        List<SysRole> sysRoleList = this.sysRoleMapper.selectBySelective(record);
        return sysRoleList;
    }

    @Override
    public SysRole getGroupRoleTemplate(String roleName) throws ApplicationException {
        if (roleName != null) {
            List<SysRole> groupRoleTemplates = this.listGroupRoleTemplate();
            for (SysRole sysRole : groupRoleTemplates) {
                if (sysRole.getRoleName().equals(roleName)) {
                    return sysRole;
                }
            }
        }
        return null;
    }

    @Override
    public Long getGroupAdminRoleId() {
        Long tenantId = RequestContext.getTenantId();
        if (tenantId != null) {
            if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNYIN)) {
                return null;
            } else if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNWEI)) {
                return SysMgrConstants.ROLE_ID_YW_GROUP_ADMIN;
            } else if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
                return SysMgrConstants.ROLE_ID_ZH_GROUP_ADMIN;
            }
        }
        return null;
    }

    @Override
    public Long getGroupOperatorRoleId() {
        Long tenantId = RequestContext.getTenantId();
        if (tenantId != null) {
            if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNYIN)) {
                return null;
            } else if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNWEI)) {
                return SysMgrConstants.ROLE_ID_YW_GROUP_OPERATOR;
            } else if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
                return SysMgrConstants.ROLE_ID_ZH_GROUP_OPERATOR;
            }
        }
        return null;
    }

    @Override
    public Long getGroupObserverRoleId() {
        Long tenantId = RequestContext.getTenantId();
        if (tenantId != null) {
            if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNYIN)) {
                return null;
            } else if (tenantId.equals(SysMgrConstants.TENANT_ID_YUNWEI)) {
                return SysMgrConstants.ROLE_ID_YW_GROUP_OBSERVER;
            } else if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
                return SysMgrConstants.ROLE_ID_ZH_GROUP_OBSERVER;
            }
        }
        return null;
    }

    /********** 工组角色 **********/

    @Override
    public void insertGroupRole(SysRole sysRole) throws ApplicationException {
        sysRole.setRoleType(SysMgrConstants.ROLE_TYPE_GROUP);
        sysRole.setTenantId(RequestContext.getTenantId());
        sysRole.setCreatorId(RequestContext.getUserId());
        this.sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public void insertGroupRole(SysRole sysRole, List<Long> menuIdList) throws ApplicationException {
        this.insertGroupRole(sysRole);
        if (menuIdList != null && menuIdList.size() > 0) {
            sysMenuOperRoleService.assignAndCorrectMenuPrivileges(sysRole.getRoleId(), menuIdList);
        }
    }

    @Override
    public void insertGroupRole(SysRole sysRole, Long templateRoleId) throws ApplicationException {
        this.insertGroupRole(sysRole);
        // 继承模板角色的菜单权限
        List<Long> templateMenuIdList = sysMenuOperRoleService.getSelectMenuIdList(templateRoleId);
        sysMenuOperRoleService.assignAndCorrectMenuPrivileges(sysRole.getRoleId(), templateMenuIdList);
    }

    @Override
    public void updateGroupRole(SysRole sysRole) throws ApplicationException {
        sysRole.setRoleType(SysMgrConstants.ROLE_TYPE_GROUP);
        sysRole.setTenantId(RequestContext.getTenantId());
        sysRole.setCreatorId(RequestContext.getUserId());
        this.sysRoleMapper.updateByPrimaryKey(sysRole);
    }

    @Override
    public void deleteGroupRole(Long roleId) throws ApplicationException {
        SysRole sysRole = new SysRole();
        sysRole.setRoleType(SysMgrConstants.ROLE_TYPE_GROUP);
        sysRole.setRoleId(roleId);
        int count = this.sysRoleMapper.deleteBySelective(sysRole);
        if (count > 0) {
            // 删除角色与菜单操作关系
            sysMenuOperRoleService.deleteByRoleId(roleId);
            // 删除角色成员
            sysRoleUserService.deleteByRoleId(roleId);
        }
    }

    @Override
    public SysRole getGroupRole(Long roleId) throws ApplicationException {
        SysRole sysRole = new SysRole();
        sysRole.setRoleType(SysMgrConstants.ROLE_TYPE_GROUP);
        sysRole.setRoleId(roleId);
        List<SysRole> sysRoleList = this.sysRoleMapper.selectBySelective(sysRole);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return null;
        } else {
            return sysRoleList.get(0);
        }
    }

    @Override
    public List<SysRole> selectByParentRoleId(Long parentRoleId) throws ApplicationException {
        List<SysRole> sysRoleList = new ArrayList<>();
        if (parentRoleId != null) {
            SysRole sysRole = new SysRole();
            sysRole.setParentRoleId(parentRoleId);
            List<SysRole> list = this.sysRoleMapper.selectBySelective(sysRole);
            if (list != null) {
                sysRoleList = list;
            }
        }
        return sysRoleList;
    }

    @Override
    public List<SysRole> selectByMenuOperId(Long menuOperId) throws ApplicationException {
        List<SysRole> sysRoleList = new ArrayList<>();
        if (menuOperId != null) {
            Long tenantId = RequestContext.getTenantId();
            List<SysRole> list = this.sysRoleMapper.selectByMenuOperId(tenantId, menuOperId);
            if (list != null && list.size() > 0) {
                sysRoleList = list;
            }
        }
        return sysRoleList;
    }

    @Override
    public List<SysRole> selectByMenuOperCode(String menuOperCode) throws ApplicationException {
        List<SysRole> sysRoleList = new ArrayList<>();
        if (!StringUtils.isBlank(menuOperCode)) {
            Long tenantId = RequestContext.getTenantId();
            List<SysRole> list = this.sysRoleMapper.selectByMenuOperCode(tenantId, menuOperCode);
            if (list != null && list.size() > 0) {
                sysRoleList = list;
            }
        }
        return sysRoleList;
    }
}
