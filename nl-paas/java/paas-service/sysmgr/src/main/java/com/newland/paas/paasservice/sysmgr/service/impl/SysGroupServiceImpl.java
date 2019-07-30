package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.common.util.UuidUtil;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysGroupError;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupForMemberRespVO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupOtherRespVO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupReqVO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupWithRoleVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("sysGroupService")
public class SysGroupServiceImpl implements SysGroupService {
    private static final Log LOG = LogFactory.getLogger(SysGroupServiceImpl.class);

    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysCategoryMapper sysCategoryMapper;
    @Autowired
    private SysGroupUserService sysGroupUserService;
    @Autowired
    private SysGroupRoleService sysGroupRoleService;
    @Autowired
    private SysCategoryGrantService sysCategoryGrantService;
    @Autowired
    private SysObjService sysObjService;
    
    private static final String GROUP =  "group_";

    @Value("${group.maxlevel}")
    private Integer maxlevel;
    private static final int DEFAULT_MAX_LEVEL = 3;

    @Override
    public int groupCount() throws ApplicationException {
        return sysGroupMapper.groupCount();
    }

    @Override
    public ResultPageData<SysGroup> page(SysGroupReqBo params, PageInfo pageInfo) throws ApplicationException {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysGroup> rrs = sysGroupMapper.selectBySelective(params);
        return new ResultPageData<>(rrs);
    }

    @Override
    public List<SysGroup> list(SysGroupReqBo params) throws ApplicationException {
        return sysGroupMapper.selectBySelective(params);
    }

    /**
     * 查我可见的工组
     *
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<SysGroupRespBo> listMyGroup() throws ApplicationException {
        SysGroupReqBo params = new SysGroupReqBo();
        Long tenantId = RequestContext.getTenantId();
        Long userId = RequestContext.getUserId();
        Boolean isTenantAdmin = RequestContext.getSession().getIsTenantAdmin();
        params.setTenantId(tenantId);
        List<SysGroupRespBo> sysGroupRespBoList = new ArrayList<>();
        if (isTenantAdmin) {
            // 租户管理员，可以看到所有的工组，且可以管理所有工组
            params.setParentGroupNames(null);
            sysGroupRespBoList = sysGroupMapper.selectGroupRespBoBySelective(params);
            for (SysGroupRespBo sysGroupRespBo : sysGroupRespBoList) {
                sysGroupRespBo.setbGroupAdmin(true);
            }
        } else {
            // 工组管理员，可以看到所有下级工组
            // 非工组管理员，只可以看到自己的所在工组
            // 获取我管理的工组
            List<Long> managedGroupIdList = new ArrayList<>();
            List<SysGroupRole> myGroupRoleList = sysGroupRoleService.listMyGroupRoleList(true);
            for (SysGroupRole sysGroupRole : myGroupRoleList) {
                if (SysMgrConstants.ROLE_NAME_GROUP_ADMIN.equals(sysGroupRole.getGroupRoleName())) {
                    Long groupId = sysGroupRole.getGroupId();
                    if (!managedGroupIdList.contains(groupId)) {
                        managedGroupIdList.add(groupId);
                    }
                }
            }
            if (managedGroupIdList.size() > 0) {
                List<SysGroupRespBo> managedGroupRespBoList = sysGroupMapper.selectGroupRespBoByGroupIds(managedGroupIdList);
                // 获取我管理的工组的子工组
                List<String> managedGroupNames = new ArrayList<>();
                for (SysGroupRespBo managedGroupRespBo : managedGroupRespBoList) {
                    managedGroupNames.add("/" + managedGroupRespBo.getGroupName() + "/");
                }
                params.setParentGroupNames(managedGroupNames);
                List<SysGroupRespBo> managedGroupAndSubGroupRespBoList = sysGroupMapper.selectGroupRespBoBySelective(params);
                for (SysGroupRespBo sysGroupRespBo : managedGroupAndSubGroupRespBoList) {
                    Long groupId = sysGroupRespBo.getGroupId();
                    if (!managedGroupIdList.contains(groupId)) {
                        managedGroupIdList.add(groupId);
                    }
                    sysGroupRespBo.setbGroupAdmin(true);
                    sysGroupRespBoList.add(sysGroupRespBo);
                }
            }
            // 获取我加入的但非我管理的工组
            List<Long> nonManagedGroupIdList = new ArrayList<>();
            SysGroupUserReqBo req = new SysGroupUserReqBo();
            req.setUserId(userId);
            req.setTenantId(tenantId);
            List<SysGroupUserRespBo> sysGroupUserRespBoList = sysGroupUserService.list(req);
            for (SysGroupUserRespBo sysGroupUserRespBo : sysGroupUserRespBoList) {
                Long groupId = sysGroupUserRespBo.getGroupId();
                if (!managedGroupIdList.contains(groupId)) {
                    nonManagedGroupIdList.add(groupId);
                }
            }
            if (nonManagedGroupIdList.size() > 0) {
                List<SysGroupRespBo> nonManagedGroupRespBoList = sysGroupMapper.selectGroupRespBoByGroupIds(nonManagedGroupIdList);
                for (SysGroupRespBo sysGroupRespBo : nonManagedGroupRespBoList) {
                    sysGroupRespBo.setbGroupAdmin(false);
                    sysGroupRespBoList.add(sysGroupRespBo);
                }
            }
        }
        return sysGroupRespBoList;
    }

    @Override
    public List<SysGroupWithRoleVo> listMyGroupWithRole() throws ApplicationException {
        List<SysGroupWithRoleVo> sysGroupWithRoleVoList = new ArrayList<>();
        Long userId = RequestContext.getUserId();
        // 获取我的工组
        List<SysGroupRespBo> myGroupList = this.listMyGroup();
        for (SysGroupRespBo sysGroupRespBo : myGroupList) {
            SysGroupWithRoleVo sysGroupWithRoleVo = new SysGroupWithRoleVo();
            BeanUtils.copyProperties(sysGroupRespBo, sysGroupWithRoleVo);
            // 获取我的工组角色
            List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listMyGroupRoleList(sysGroupRespBo.getGroupId());
            sysGroupWithRoleVo.setGroupRoleList(sysGroupRoleList);
            sysGroupWithRoleVoList.add(sysGroupWithRoleVo);
        }
        return sysGroupWithRoleVoList;
    }

    /**
     * 查询我管理的工组
     *
     * @return
     */
    @Override
    public List<SysGroupRespBo> listManagedGroup()  throws ApplicationException {
        List<SysGroupRespBo> managedSysGroupList = new ArrayList<>();
        List<SysGroupRespBo> sysGroupRespBoList = this.listMyGroup();
        for (SysGroupRespBo sysGroupRespBo : sysGroupRespBoList) {
            if (sysGroupRespBo.getbGroupAdmin()) {
                managedSysGroupList.add(sysGroupRespBo);
            }
        }
        return managedSysGroupList;
    }

    /**
     * 新增工组
     *
     * @param params
     * @return
     * @throws ApplicationException
     */
    @Override
    public void add(SysGroup params) throws ApplicationException {
        SysGroupReqBo nameCountQuery = new SysGroupReqBo();
        nameCountQuery.setGroupName(params.getGroupName());
        nameCountQuery.setTenantId(params.getTenantId());
        if (sysGroupMapper.countBySelective(nameCountQuery) > 0) {
            throw new ApplicationException(SysGroupError.DUPLICATE_SYS_GROUP_ERROR);
        }

        // 层级约束
        int curLevel = sysGroupMapper.getCurLevel(params.getParentGroupId());
        maxlevel = maxlevel > DEFAULT_MAX_LEVEL ? maxlevel : DEFAULT_MAX_LEVEL;
        if (curLevel > maxlevel) {
            SysGroupError.EXCEED_MAXLEVEL_ERROR.setMsgArguments(maxlevel);
            throw new ApplicationException(
                    new PaasError(SysGroupError.EXCEED_MAXLEVEL_ERROR.getCode(),
                            SysGroupError.EXCEED_MAXLEVEL_ERROR.getDescription(),
                            SysGroupError.EXCEED_MAXLEVEL_ERROR.getSolution()));
        }

        Long parentId = params.getParentGroupId();
        if (parentId != null && parentId > 0) {
            SysGroup parent = sysGroupMapper.selectByPrimaryKey(parentId);
            if (parent == null) {
                throw new ApplicationException(SysGroupError.PARENT_NOT_EXIST);
            }
            params.setWholePath(StringUtils.isBlank(parent.getWholePath()) ? "/" : parent.getWholePath()
                    + params.getGroupName() + "/");
        } else if (parentId != null && parentId.equals(SysMgrConstants.ROOT_GROUP_PARENT_ID)) {
            params.setWholePath("/" + params.getGroupName() + "/");
        }
        params.setTenantId(params.getTenantId());
        params.setCreatorId(RequestContext.getUserId());
        final String groupCode = UuidUtil.buildUuid12();
        params.setGroupCode(groupCode);
        sysGroupMapper.insertSelective(params);

        // 新增默认工组角色： 工组管理员 和 工组操作员 和 工组观察员
        SysGroupRole sysGroupAdminRole = new SysGroupRole();
        sysGroupAdminRole.setGroupId(params.getGroupId());
        sysGroupAdminRole.setGroupRoleName(SysMgrConstants.ROLE_NAME_GROUP_ADMIN);
        sysGroupAdminRole.setGroupRoleDesc(SysMgrConstants.ROLE_NAME_GROUP_ADMIN);
        SysGroupRole sysGroupOperatorRole = new SysGroupRole();
        sysGroupOperatorRole.setGroupId(params.getGroupId());
        sysGroupOperatorRole.setGroupRoleName(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR);
        sysGroupOperatorRole.setGroupRoleDesc(SysMgrConstants.ROLE_NAME_GROUP_OPERATOR);
        SysGroupRole sysGroupObserverRole = new SysGroupRole();
        sysGroupObserverRole.setGroupId(params.getGroupId());
        sysGroupObserverRole.setGroupRoleName(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER);
        sysGroupObserverRole.setGroupRoleDesc(SysMgrConstants.ROLE_NAME_GROUP_OBSERVER);
        sysGroupRoleService.add(sysGroupAdminRole);
        sysGroupRoleService.add(sysGroupOperatorRole);
        sysGroupRoleService.add(sysGroupObserverRole);
    }

    /**
     * 更新工组
     *
     * @param params
     * @return
     */
    @Override
    public void update(SysGroup params) throws ApplicationException {
        SysGroupReqBo nameCountQuery = new SysGroupReqBo();
        nameCountQuery.setGroupName(params.getGroupName());
        nameCountQuery.setTenantId(params.getTenantId());
        nameCountQuery.setNeqId(params.getGroupId());
        if (sysGroupMapper.countBySelective(nameCountQuery) > 0) {
            throw new ApplicationException(SysGroupError.DUPLICATE_SYS_GROUP_ERROR);
        }

        Long parentId = params.getParentGroupId();

        if (parentId != null && parentId > 0) {
            SysGroup parent = sysGroupMapper.selectByPrimaryKey(parentId);
            params.setWholePath(StringUtils.isBlank(parent.getWholePath()) ? "/" : parent.getWholePath()
                    + params.getGroupName() + "/");

        } else if (parentId != null && parentId.equals(SysMgrConstants.ROOT_GROUP_PARENT_ID)) {
            params.setWholePath("/" + params.getGroupName() + "/");
        }

        sysGroupMapper.updateByPrimaryKeySelective(params);
    }

    /**
     * 删除工组
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(Long id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException(SysGroupError.GROUP_ID_NOT_NULL);
        }
        SysGroupReqBo childCountQuery = new SysGroupReqBo();
        childCountQuery.setParentGroupId(id);
        if (sysGroupMapper.countBySelective(childCountQuery) > 0) {
            throw new ApplicationException(SysGroupError.DEL_HAS_CHILD_GROUP);
        }
        SysCategory categoryGroupCountQuery = new SysCategory();
        categoryGroupCountQuery.setGroupId(id);
        categoryGroupCountQuery.setDelFlag(0L);
        if (sysCategoryMapper.countBySelective(categoryGroupCountQuery) > 0) {
            throw new ApplicationException(SysGroupError.HAS_CATEGORY_CANNOT_DELETE);
        }
        // 删除工组角色
        SysGroupRoleReqBo groupRoleParams = new SysGroupRoleReqBo();
        groupRoleParams.setGroupId(id);
        List<Long> groupRoleIds = sysGroupRoleService.list(groupRoleParams).stream()
                .map(SysGroupRoleRespBo::getGroupRoleId).collect(Collectors.toList());
        for (Long groupRoleId : groupRoleIds) {
            sysGroupRoleService.delete(groupRoleId, true);
        }
        // 删除工组成员
        SysGroupUserReqBo groupUserParams = new SysGroupUserReqBo();
        groupUserParams.setGroupId(id);
        List<Long> groupUserIds = sysGroupUserService.list(groupUserParams).stream()
                .map(SysGroupUserRespBo::getId).collect(Collectors.toList());
        for (Long groupUserId : groupUserIds) {
            sysGroupUserService.delete(groupUserId);
        }
        // 删除系统分组权限授权
        sysCategoryGrantService.deleteByGroupId(id);
        // 删除对象权限授权
        sysObjService.deleteSrightByGroupId(id);
        // 删除工组
        sysGroupMapper.deleteByPrimaryKey(id);

        LOG.info(LogProperty.LOGCONFIG_DEALID, "删除工组成功！");
    }

    @Override
    public SysGroup get(Long id) throws ApplicationException {
        return sysGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysGroupForCategoryRespBo> getCategorysByGroup(Long groupId) {
        return sysGroupMapper.getCategorysByGroup(groupId);
    }

    @Override
    public List<SysGroup> getSubGroupById(Long id) {
        return sysGroupMapper.getSubGroupById(id);
    }

    @Override
    public List<SysGroup> getSubGroupById(List<Long> ids) {
        List<SysGroup> result = new ArrayList<>();
        for (Long id : ids) {
            result.addAll(this.getSubGroupById(id));
        }
        return result;
    }

    @Override
    public List<SysGroupForMemberRespVO> queryListByUserId(Long userId) throws ApplicationException {
        Long tenantId = RequestContext.getTenantId();
        List<SysGroupForMemberRespVO> sgVOs = new ArrayList<>();
        List<SysGroupForMemberRespBO> sgBOs = sysGroupMapper.selectByUserIdAndTenantId(userId, tenantId);
        for (SysGroupForMemberRespBO bo : sgBOs) {
            Long groupId = bo.getGroupId();
            List<SysGroupRoleBO> sgrBOs = sysGroupMapper.selectRoleByParams(userId, groupId);
            bo.setSysGroupRoleBOs(sgrBOs);
            SysGroupForMemberRespVO vo = new SysGroupForMemberRespVO();
            BeanUtils.copyProperties(bo, vo);
            sgVOs.add(vo);
        }
        return sgVOs;
    }

    @Override
    public ResultPageData<SysGroupForMemberRespVO> pagedQueryListByUserId(
                            SysGroupPagingReqBO sysGroupPagingReqBO, PageInfo pageInfo)  throws ApplicationException {
        Page page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        Long userId = sysGroupPagingReqBO.getUserId();
        List<SysGroupForMemberRespVO> sgVOs = new ArrayList<>();
        List<SysGroupForMemberRespBO> sgBOs = sysGroupMapper.selectByUserId(userId);
        for (SysGroupForMemberRespBO bo : sgBOs) {
            Long groupId = bo.getGroupId();
            List<SysGroupRoleBO> sgrBOs = sysGroupMapper.selectRoleByParams(userId, groupId);
            bo.setSysGroupRoleBOs(sgrBOs);
            SysGroupForMemberRespVO vo = new SysGroupForMemberRespVO();
            BeanUtils.copyProperties(bo, vo);
            sgVOs.add(vo);
        }
        pageInfo.setTotalRecord(page.getTotal());
        return new ResultPageData(sgVOs, pageInfo);
    }

    @Override
    public void deleteGroupsByUserId(Long userId) throws ApplicationException {
        sysGroupMapper.deleteByUserId(userId);
    }

    @Override
    public void addGroups(SysGroupReqVO sysGroupReqVO) throws ApplicationException {
        int a = 0;
        short b = (short) a;
        Long[] groupIds = sysGroupReqVO.getGroupIds();
        if (groupIds != null) {
            for (Long groupId : groupIds) {
                SysGroupUser sysGroupUser = new SysGroupUser();
                sysGroupUser.setGroupId(groupId);
                sysGroupUser.setUserId(sysGroupReqVO.getUserId());
                sysGroupUser.setIsAdmin(b);
                sysGroupUser.setTenantId(RequestContext.getTenantId());
                sysGroupUser.setCreatorId(RequestContext.getUserId());
                sysGroupUser.setDelFlag(b);
                sysGroupUserMapper.insertSelective(sysGroupUser);
            }
        }
    }

    @Override
    public SysGroupOtherRespVO queryAllList(Long userId, Long tenantId) throws ApplicationException {
        SysGroupOtherRespVO sgoVO = new SysGroupOtherRespVO();
        List<SysGroupForMemberRespVO> sgVOs = new ArrayList<>();
        //查询所有工组列表
        List<SysGroupForMemberRespBO> sgBOs = sysGroupMapper.selectAll(tenantId);
        //根据成员ID查询所属工组ID列表
        List<Long> groupIds = sysGroupMapper.selectGroupId(userId, tenantId);
        for (SysGroupForMemberRespBO bo : sgBOs) {
            SysGroupForMemberRespVO vo = new SysGroupForMemberRespVO();
            BeanUtils.copyProperties(bo, vo);
            sgVOs.add(vo);
        }
        sgoVO.setAllGroups(sgVOs);
        sgoVO.setGroupIds(groupIds);
        return sgoVO;
    }

    /**
     * 获取工组树
     *
     * @return
     */
    @Override
    public List<BaseTreeDo> getSysGroupTree(SysGroup sysGroup) throws ApplicationException {
        Long groupId = sysGroup.getGroupId();
        if (groupId.equals(SysMgrConstants.ROOT_GROUP_PARENT_ID)) {
            SysGroupReqBo reqBo = new SysGroupReqBo();
            reqBo.setGroupName("ROOT");
            reqBo.setTenantId(sysGroup.getTenantId());
            List<SysGroup> list = sysGroupMapper.selectBySelective(reqBo);
            groupId = list.get(0).getGroupId();
        }
        sysGroup.setGroupId(groupId);
        return sysGroupMapper.getSysGroupTree(sysGroup);
    }

    /**
     * 获取子工组
     *
     * @param sysGroup
     * @return
     */
    @Override
    public List<BaseTreeDo> listSubSysGroup(SysGroup sysGroup) throws ApplicationException {

        return sysGroupMapper.listSubSysGroup(sysGroup);
    }

    /**
     * 获取当前用户的租户的根工组 create by wrp
     *
     * @return
     */
    @Override
    public String tenantRootGroup() {
        SysGroupReqBo sysGroup = new SysGroupReqBo();
        sysGroup.setTenantId(RequestContext.getTenantId());
        sysGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
        List<SysGroup> list = sysGroupMapper.selectBySelective(sysGroup);
        LOG.info(LogProperty.LOGCONFIG_DEALID, "获取当前用户的租户的根工组：" + JSON.toJSONString(list));
        if (!list.isEmpty()) {
            return GROUP + list.get(0).getGroupId();
        }
        return null;
    }

    /**
     * 获取当前租户用户的归属工组的父工组 create by wrp
     *
     * @return
     */
    @Override
    public String tenantParentGroup() {
        // 1.查询当前用户登录的租户下的归属工组
        SysGroupUserReqBo groupUserParams = new SysGroupUserReqBo();
        groupUserParams.setTenantId(RequestContext.getTenantId());
        groupUserParams.setUserId(RequestContext.getUserId());
        List<SysGroupUserRespBo> groupUserRespBos = sysGroupUserMapper.selectBySelective(groupUserParams);
        LOG.info(LogProperty.LOGCONFIG_DEALID, "1.查询当前用户登录的租户下的归属工组：" + JSON.toJSONString(groupUserRespBos));
        if (!groupUserRespBos.isEmpty()) {
            // 2.查询此工组的父工组
            SysGroupReqBo sysGroup = new SysGroupReqBo();
            sysGroup.setTenantId(RequestContext.getTenantId());
            sysGroup.setGroupId(groupUserRespBos.get(0).getGroupId());
            List<SysGroup> list = sysGroupMapper.selectBySelective(sysGroup);
            LOG.info(LogProperty.LOGCONFIG_DEALID, "1.查询此工组的父工组：" + JSON.toJSONString(list));
            if (!list.isEmpty()) {
                return GROUP + list.get(0).getParentGroupId();
            }
        }
        return null;
    }

    /**
     * 获取运营租户的根工组 create by wrp
     *
     * @return
     */
    @Override
    public String yyTenantRootGroup() {
        SysGroupReqBo sysGroup = new SysGroupReqBo();
        sysGroup.setTenantId(SysMgrConstants.TENANT_ID_YUNYIN);
        sysGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
        List<SysGroup> list = sysGroupMapper.selectBySelective(sysGroup);
        LOG.info(LogProperty.LOGCONFIG_DEALID, "获取运营租户的根工组：" + JSON.toJSONString(list));
        if (!list.isEmpty()) {
            return GROUP + list.get(0).getGroupId();
        }
        return null;
    }

    /**
     * 获取运维租户的根工组 create by wrp
     *
     * @return
     */
    @Override
    public String ywTenantRootGroup() {
        SysGroupReqBo sysGroup = new SysGroupReqBo();
        sysGroup.setTenantId(SysMgrConstants.TENANT_ID_YUNWEI);
        sysGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
        List<SysGroup> list = sysGroupMapper.selectBySelective(sysGroup);
        LOG.info(LogProperty.LOGCONFIG_DEALID, "获取运维租户的根工组：" + JSON.toJSONString(list));
        if (!list.isEmpty()) {
            return GROUP + list.get(0).getGroupId();
        }
        return null;
    }

    /**
     * 查询所有父工组
     *
     * @param id
     * @return
     */
    @Override
    public List<SysGroup> getParentGroupById(Long id) {
        return sysGroupMapper.getParentGroupById(id);
    }
}
