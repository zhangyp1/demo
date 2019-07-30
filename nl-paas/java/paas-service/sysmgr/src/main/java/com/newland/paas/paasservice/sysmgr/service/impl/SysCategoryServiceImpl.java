package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryQuotaMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryQuotaUsedMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysObjViewMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysCategoryError;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: paas-all
 * @description: Frown
 * @author: Frown
 * @create: 2018-07-30 14:52
 **/
@Service("sysCategoryService")
public class SysCategoryServiceImpl implements SysCategoryService {
    @Autowired
    private SysCategoryMapper sysCategoryMapper;
    @Autowired
    private SysObjViewMapper sysObjViewMapper;
    @Autowired
    private SysGroupUserService sysGroupUserService;
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysGroupRoleSysRoleService sysGroupRoleSysRoleService;
    @Autowired
    private SysUserGroupRoleService sysUserGroupRoleService;
    @Autowired
    private SysGroupRoleService sysGroupRoleService;
    @Autowired
    private SysCategoryQuotaService sysCategoryQuotaService;
    @Autowired
    private SysCategoryQuotaMapper sysCategoryQuotaMapper;
    @Autowired
    private SysCategoryGrantService sysCategoryGrantService;
    @Autowired
    private SysCategoryQuotaUsedMapper sysCategoryQuotaUsedMapper;

    @Value("${syscategory.maxlevel}")
    private Integer maxlevel;
    private static final int DEFAULT_MAX_LEVEL = 3;

    /**
     * 保存系统分组
     *
     * @param sysCategory
     * @throws ApplicationException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveSysCategory(SysCategoryBo sysCategory) throws ApplicationException {
        checkCategoryName(sysCategory);
        checkCategoryCode(sysCategory);
        checkBelongGroup(sysCategory);
        Long id = sysCategory.getSysCategoryId();
        if (id == null) {
            // 新增
            checkLevel(sysCategory.getSysCategoryPid());
            sysCategory.setCreatorId(RequestContext.getUserId());
            if (sysCategory.getTenantId() == null) {
                sysCategory.setTenantId(RequestContext.getTenantId());
            }
            sysCategoryMapper.insertSelective(sysCategory);
        } else {
            // 修改
            SysCategory sc = new SysCategory();
            sc.setSysCategoryId(id);
            sc.setSysCategoryName(sysCategory.getSysCategoryName());
            sc.setGroupId(sysCategory.getGroupId());
            sc.setSysCategoryCode(sysCategory.getSysCategoryCode());
            sysCategoryMapper.updateByPrimaryKeySelective(sc);
        }
        // 新增或者修改分组配额
        SysCategoryQuota quota = new SysCategoryQuota();
        quota.setSysCategoryId(sysCategory.getSysCategoryId());
        quota.setStorageQuota(sysCategory.getStorageQuota());
        quota.setMemoryQuota(sysCategory.getMemoryQuota());
        quota.setCpuQuota(sysCategory.getCpuQuota());
        quota.setTenantId(sysCategory.getTenantId());
        sysCategoryQuotaService.saveSysCategoryQuota(quota);
    }

    /**
     * 检查系统分组名称，名称必须租户内唯一
     * @param sysCategory
     * @throws ApplicationException
     */
    private void checkCategoryName(SysCategoryBo sysCategory) throws ApplicationException {
        // 名称不能为空
        if (StringUtils.isBlank(sysCategory.getSysCategoryName())) {
            throw new ApplicationException(SysCategoryError.NULL_NAME_ERROR);
        }
        Long id = sysCategory.getSysCategoryId();
        if (id == null) {
            // 新增
            SysCategory sysCategoryReq = new SysCategory();
            sysCategoryReq.setSysCategoryName(sysCategory.getSysCategoryName());
            sysCategoryReq.setTenantId(RequestContext.getTenantId());
            List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
            if (existsSysCategorys != null  && !existsSysCategorys.isEmpty()) {
                throw new ApplicationException(SysCategoryError.DUPLICATE_NAME_ERROR);
            }
        } else {
            // 修改
            SysCategory updateSysCategory = sysCategoryMapper.selectByPrimaryKey(sysCategory.getSysCategoryId());
            SysCategory sysCategoryReq = new SysCategory();
            sysCategoryReq.setSysCategoryName(sysCategory.getSysCategoryName());
            sysCategoryReq.setTenantId(RequestContext.getTenantId());
            List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
            if (existsSysCategorys != null  && !existsSysCategorys.isEmpty()) {
                for (SysCategory existsSysCategory : existsSysCategorys) {
                    if (existsSysCategory.getSysCategoryId().equals(updateSysCategory.getSysCategoryId())) {
                        continue;
                    }
                    throw new ApplicationException(SysCategoryError.DUPLICATE_NAME_ERROR);
                }
            }
        }
    }

    /**
     * 检查系统分组标识，标识必须全平台唯一
     * @param sysCategory
     * @throws ApplicationException
     */
    private void checkCategoryCode(SysCategoryBo sysCategory) throws ApplicationException {
        // 标识不能为空
        if (StringUtils.isBlank(sysCategory.getSysCategoryCode())) {
            throw new ApplicationException(SysCategoryError.NULL_CODE_ERROR);
        }
        Long id = sysCategory.getSysCategoryId();
        if (id == null) {
            // 新增
            SysCategory sysCategoryReq = new SysCategory();
            sysCategoryReq.setSysCategoryCode(sysCategory.getSysCategoryCode());
            List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
            if (existsSysCategorys != null  && !existsSysCategorys.isEmpty()) {
                throw new ApplicationException(SysCategoryError.DUPLICATE_CODE_ERROR);
            }
        } else {
            // 修改
            SysCategory updateSysCategory = sysCategoryMapper.selectByPrimaryKey(sysCategory.getSysCategoryId());
            SysCategory sysCategoryReq = new SysCategory();
            sysCategoryReq.setSysCategoryCode(sysCategory.getSysCategoryCode());
            List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
            if (existsSysCategorys != null  && !existsSysCategorys.isEmpty()) {
                for (SysCategory existsSysCategory : existsSysCategorys) {
                    if (existsSysCategory.getSysCategoryId().equals(updateSysCategory.getSysCategoryId())) {
                        continue;
                    }
                    throw new ApplicationException(SysCategoryError.DUPLICATE_CODE_ERROR);
                }
            }
        }
    }

    /**
     * 检查系统分组的所属工组，ROOT工组下只能有一个系统分组
     * @param sysCategory
     * @throws ApplicationException
     */
    private void checkBelongGroup(SysCategoryBo sysCategory) throws ApplicationException {
        // 所属工组不能为空
        if (sysCategory.getGroupId() == null) {
            throw new ApplicationException(SysCategoryError.NULL_GROUP_ERROR);
        }
        SysGroup group = sysGroupService.get(sysCategory.getGroupId());
        if (group != null && SysMgrConstants.ROOT_GROUP_PARENT_ID.equals(group.getParentGroupId())) {
            Long id = sysCategory.getSysCategoryId();
            if (id == null) {
                // 新增
                SysCategory sysCategoryReq = new SysCategory();
                sysCategoryReq.setGroupId(sysCategory.getGroupId());
                List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
                if (existsSysCategorys != null && !existsSysCategorys.isEmpty()) {
                    throw new ApplicationException(SysCategoryError.DUPLICATE_ROOT_GROUP_ERROR);
                }
            } else {
                // 修改
                SysCategory updateSysCategory = sysCategoryMapper.selectByPrimaryKey(sysCategory.getSysCategoryId());
                SysCategory sysCategoryReq = new SysCategory();
                sysCategoryReq.setGroupId(sysCategory.getGroupId());
                List<SysCategory> existsSysCategorys = sysCategoryMapper.selectBySelective(sysCategoryReq);
                if (existsSysCategorys != null && !existsSysCategorys.isEmpty()) {
                    for (SysCategory existsSysCategory : existsSysCategorys) {
                        if (existsSysCategory.getSysCategoryId().equals(updateSysCategory.getSysCategoryId())) {
                            continue;
                        }
                        throw new ApplicationException(SysCategoryError.DUPLICATE_ROOT_GROUP_ERROR);
                    }
                }
            }
        }
    }

    /**
     * 检查层级
     * @param sysCategoryPid
     * @throws ApplicationException
     */
    private void checkLevel(Long sysCategoryPid) throws ApplicationException {
        int curLevel = sysCategoryMapper.getCurLevel(sysCategoryPid);
        maxlevel = maxlevel > DEFAULT_MAX_LEVEL ? maxlevel : DEFAULT_MAX_LEVEL;
        if (curLevel > maxlevel) {
            SysCategoryError.EXCEED_MAXLEVEL_ERROR.setMsgArguments(maxlevel);
            throw new ApplicationException(
                    new PaasError(SysCategoryError.EXCEED_MAXLEVEL_ERROR.getCode(),
                            SysCategoryError.EXCEED_MAXLEVEL_ERROR.getDescription(),
                            SysCategoryError.EXCEED_MAXLEVEL_ERROR.getSolution()));
        }
    }

    /**
     * 删除系统分组
     *
     * @param sysCategory
     * @throws ApplicationException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteSysCategory(SysCategory sysCategory) throws ApplicationException {
        if (sysCategory == null || sysCategory.getSysCategoryId() == null) {
            throw new ApplicationException(SysCategoryError.ID_NULL_ERROR);
        }
        SysCategory existsSysCategory =  sysCategoryMapper.selectByPrimaryKey(sysCategory.getSysCategoryId());
        if (existsSysCategory == null) {
            throw new ApplicationException(SysCategoryError.SYS_CATEGORY_NOT_FOUND);
        }
        if (existsSysCategory.getSysCategoryPid().equals(SysMgrConstants.ROOT_SYS_CATEGORY_PARENT_ID)) {
            throw new ApplicationException(SysCategoryError.CAN_NOT_DELETE_ROOT);
        }
        List<SysCategory> subSysCategoryList = this.listSubSysCategory(sysCategory);
        if (subSysCategoryList != null && !subSysCategoryList.isEmpty()) {
            throw new ApplicationException(SysCategoryError.HAS_SUB_ERROR);
        }
        SysObj sysObj = new SysObj();
        sysObj.setSysCategoryId(sysCategory.getSysCategoryId());
        int usedCount = sysObjViewMapper.countBySelectiveSysObj(sysObj);
        if (usedCount > 0) {
            throw new ApplicationException(SysCategoryError.HAS_OBJECT_ERROR);
        }
        // 删除系统分组
        sysCategoryMapper.delSubSysCategory(sysCategory);
        // 删除系统分组配额
        SysCategoryQuota quota = new SysCategoryQuota();
        quota.setSysCategoryId(sysCategory.getSysCategoryId());
        sysCategoryQuotaService.deleteSysCategoryQuota(quota);
        // 删除系统分组权限授权和赋权
        sysCategoryGrantService.deleteByCategoryId(sysCategory.getSysCategoryId());
    }

    /**
     * 查询系统分组列表
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<SysCategory> listSysCategory(SysCategory sysCategory) throws ApplicationException {
        return sysCategoryMapper.selectBySelective(sysCategory);
    }

    /**
     * 分页查询系统分组
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    @Override
    public ResultPageData pageQuerySysCategory(SysCategory sysCategory, PageInfo pageInfo) throws ApplicationException {
        Page<SysCategory> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysCategory> list = sysCategoryMapper.listSubSysCategory(sysCategory);
        pageInfo.setTotalRecord(page.getTotal());
        return new ResultPageData(list, pageInfo);
    }

    /**
     * 获取系统分组
     *
     * @param sysCategory
     * @return
     * @throws ApplicationException
     */
    @Override
    public SysCategoryBo getSysCategory(SysCategory sysCategory) throws ApplicationException {
        Long id = sysCategory.getSysCategoryId();
        SysCategory category = sysCategoryMapper.selectByPrimaryKey(id);
        SysCategoryBo sysCategoryBo = new SysCategoryBo();
        BeanUtils.copyProperties(category, sysCategoryBo);

        SysCategoryQuota quotaReq = new SysCategoryQuota();
        quotaReq.setSysCategoryId(id);
        List<SysCategoryQuota> quotas = sysCategoryQuotaMapper.selectBySelective(quotaReq);
        if (quotas != null && !quotas.isEmpty()) {
            SysCategoryQuota quota = quotas.get(0);
            sysCategoryBo.setCpuQuota(quota.getCpuQuota());
            sysCategoryBo.setMemoryQuota(quota.getMemoryQuota());
            sysCategoryBo.setStorageQuota(quota.getStorageQuota());
        }
        SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
        quotaUsedReq.setCategoryId(id);
        SysCategoryQuotaUsed quotaUsed = sysCategoryQuotaUsedMapper.countQuotaUsedBySysCategory(quotaUsedReq);
        sysCategoryBo.setCpuQuotaUsed(quotaUsed.getCpuQuota());
        sysCategoryBo.setMemoryQuotaUsed(quotaUsed.getMemoryQuota());
        sysCategoryBo.setStorageQuotaUsed(quotaUsed.getStorageQuota());

        return sysCategoryBo;

    }

    /**
     * 获取系统分组树
     *
     * @return
     */
    @Override
    public TreeNode<BaseTreeDo> getSysCategoryTree() {
        TreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new TreeNode.TreeBuilder<>();
        Long tenantId = RequestContext.getTenantId();
        List<Long> groupIds = RequestContext.getSession().getGroupIdList();
        List<Long> subGroupIds = RequestContext.getSession().getSubGroupIdList();

        List<Long> groupIdList = new ArrayList<>();
        groupIdList.addAll(groupIds);
        groupIdList.addAll(subGroupIds);
        if (groupIdList != null && !groupIdList.isEmpty()) {
            List<BaseTreeDo> sysCategoryTreeDoList = sysCategoryMapper.getSysCategoryTree(tenantId, groupIdList);
            // 找出孤儿系统分组，嫁接到根节点上
            for (BaseTreeDo baseTreeDo1 : sysCategoryTreeDoList) {
                if ("none".equals(baseTreeDo1.getParentId())
                        || "-1".equals(baseTreeDo1.getParentId())) {
                    continue;
                }
                Boolean isParentFound = false;
                for (BaseTreeDo baseTreeDo2 : sysCategoryTreeDoList) {
                    if (baseTreeDo1.getParentId().equals(baseTreeDo2.getId())) {
                        isParentFound = true;
                        break;
                    }
                }
                if (!isParentFound) {
                    baseTreeDo1.setParentId("-1");
                }
            }
            for (BaseTreeDo data : sysCategoryTreeDoList) {
                treeBuilder.addNode(data.getParentId(), data.getId(), data);
            }
        }
        return treeBuilder.build();
    }

    @Override
    public List<SysCategoryVO> listSysCategoryByGroupId(List<Long> groupIdList) throws ApplicationException {
        List<SysCategoryVO> sysCategoryVOList = new ArrayList<>();
        if (groupIdList != null && !groupIdList.isEmpty()) {
            List<Long> groupIdListWithSub = new ArrayList<>();
            groupIdListWithSub.addAll(groupIdList);
            // 获取子工组列表
            List<Long> subGroupIdList = null;
            List<SysGroup> subGroupList = sysGroupService.getSubGroupById(groupIdList);
            if (subGroupList != null && !subGroupList.isEmpty()) {
                for (SysGroup subGroup : subGroupList) {
                    Long subGroupId = subGroup.getGroupId();
                    if (!groupIdListWithSub.contains(subGroupId)) {
                        groupIdListWithSub.add(subGroupId);
                    }
                }
            }
            // 获取系统分组列表
            SysCategoryBo sysCategoryBo = new SysCategoryBo();
            sysCategoryBo.setGroupIds(groupIdListWithSub);
            List<SysCategory> list = sysCategoryMapper.listCurSysCategory(sysCategoryBo);
            if (list != null && !list.isEmpty()) {
                List<SysCategory> sysCategories = sysCategoryMapper.listSubsSysCategory(list);
                sysCategoryVOList = sysCategories.stream().map(item ->
                        new SysCategoryVO(item.getSysCategoryId(),
                                item.getSysCategoryName(),
                                item.getSysCategoryPid(),
                                item.getSysCategoryCode()))
                        .collect(Collectors.toList());
            }
        }
        return sysCategoryVOList;
    }

    @Override
    public List<SysCategoryVO> listSysCategory() throws ApplicationException {
        List<Long> groupIdList = new ArrayList<>();
        SessionInfo session = RequestContext.getSession();
        Long tenantId = session.getTenantId();
        Long userId = session.getUserId();
        List<SysGroupUserRespBo> groupList = sysGroupUserService.getGroupsByTenantAndUser(tenantId, userId);
        groupIdList = groupList.stream().map(SysGroupUserRespBo::getGroupId).collect(Collectors.toList());
        return listSysCategoryByGroupId(groupIdList);
    }

    /**
     * 根据操作代码查询有权限的系统分组列表
     *
     * @param menuOperCode
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<SysCategoryVO> listSysCategoryByMenuOperCode(String menuOperCode) throws ApplicationException {
        List<SysCategoryVO> sysCategoryVOList = new ArrayList<>();
        Boolean isTenantAdmin = RequestContext.getSession().getIsTenantAdmin();
        if (isTenantAdmin) {
            // 租户管理员默认拥有所有系统分组权限
            sysCategoryVOList = this.listSysCategory();
        } else {
            Long userId = RequestContext.getUserId();
            Long tenantId = RequestContext.getTenantId();
            // 通过操作代码查询有菜单权限的工组角色（系统角色）
            List<SysRole> sysRoleList = this.sysRoleService.selectByMenuOperCode(menuOperCode);
            // 通过有菜单权限的工组角色（系统角色）查询有菜单权限的工组角色ID
            List<Long> authedGroupRoleIdList = new ArrayList<>();
            for (SysRole sysRole : sysRoleList) {
                SysGroupRoleSysRole sysGroupRoleSysRole = sysGroupRoleSysRoleService.getByRoleId(sysRole.getRoleId());
                if (sysGroupRoleSysRole != null) {
                    authedGroupRoleIdList.add(sysGroupRoleSysRole.getGroupRoleId());
                }
            }

            // 获取该用户关联的工组角色ID列表
            SysUserGroupRoleReqBo userGroupRoleReqBo = new SysUserGroupRoleReqBo();
            userGroupRoleReqBo.setUserId(userId);
            userGroupRoleReqBo.setTenantId(tenantId);
            List<SysUserGroupRoleRespBo> sysUserGroupRoleRespBoList = sysUserGroupRoleService.list(userGroupRoleReqBo);
            // 通过有权限工组角色ID过滤工组角色ID列表
            List<Long> groupRoleIdList = new ArrayList<>();
            for (SysUserGroupRoleRespBo userGroupRoleRespBo : sysUserGroupRoleRespBoList) {
                Long groupRoleId = userGroupRoleRespBo.getGroupRoleId();
                if (authedGroupRoleIdList.contains(groupRoleId)) {
                    groupRoleIdList.add(groupRoleId);
                }
            }
            // 通过工组角色ID列表查询工组ID列表
            List<Long> authedGroupIdList = new ArrayList<>();
            for (Long groupRoleId : groupRoleIdList) {
                SysGroupRoleRespBo sysGroupRoleRespBo = sysGroupRoleService.get(groupRoleId);
                if (sysGroupRoleRespBo != null) {
                    authedGroupIdList.add(sysGroupRoleRespBo.getGroupId());
                }
            }
            // 通过工组ID查询系统分组
            sysCategoryVOList = listSysCategoryByGroupId(authedGroupIdList);
        }
        return sysCategoryVOList;
    }

    /**
     * 查询子系统分组
     *
     * @param record
     * @return
     */
    @Override
    public List<SysCategory> listSubSysCategory(SysCategory record) {
        return sysCategoryMapper.listSubSysCategory(record);
    }

    /**
     * 查询子系统分组（包括根节点）
     *
     * @param record
     * @return
     */
    @Override
    public List<SysCategory> listSubAndCurSysCategory(SysCategory record) {
        return sysCategoryMapper.listSubAndCurSysCategory(record);
    }

    /**
     * 查询父系统分组（包括根节点）
     *
     * @param record
     * @return
     */
    @Override
    public List<SysCategory> listParentAndCurSysCategory(SysCategory record) {
        return sysCategoryMapper.listParentAndCurSysCategory(record);
    }

    /**
     * 查询我管理的系统分组
     *
     * @return
     */
    @Override
    public List<SysCategoryVo> listManagedSysCategory() throws ApplicationException {
        List<SysGroupRespBo> sysGroupList = sysGroupService.listManagedGroup();
        return this.listSysCategoryVo(sysGroupList);
    }

    /**
     * 查询我的系统分组
     *
     * @return
     */
    @Override
    public List<SysCategoryVo> listMySysCategory() throws ApplicationException {
        List<SysGroupRespBo> sysGroupList = sysGroupService.listMyGroup();
        return this.listSysCategoryVo(sysGroupList);
    }

    private List<SysCategoryVo> listSysCategoryVo(List<SysGroupRespBo> sysGroupList) {
        List<SysCategoryVo> sysCategoryVoList = new ArrayList<>();
        HashMap<Long, String> groupNameMap = new HashMap<>();
        for (SysGroup sysGroup : sysGroupList) {
            if (!groupNameMap.containsKey(sysGroup.getGroupId())) {
                groupNameMap.put(sysGroup.getGroupId(), sysGroup.getGroupName());
            }
        }
        List<Long> groupIds = sysGroupList.stream().map(SysGroup::getGroupId).collect(Collectors.toList());
        List<SysCategory> sysCategoryList = new ArrayList<>();
        if (groupIds != null && groupIds.size() > 0) {
            sysCategoryList = sysCategoryMapper.selectByGroupIds(null, groupIds);
        }
        for (SysCategory sysCategory : sysCategoryList) {
            SysCategoryVo sysCategoryVo = new SysCategoryVo();
            BeanUtils.copyProperties(sysCategory, sysCategoryVo);
            sysCategoryVo.setGroupName(groupNameMap.get(sysCategory.getGroupId()));
            sysCategoryVoList.add(sysCategoryVo);
        }
        return sysCategoryVoList;
    }

    /**
     * 查询我管理的系统分组 - 分页
     *
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    @Override
    public ResultPageData<SysCategoryVo> pageManagedSysCategory(Long byGroupId, String likeCategoryName, PageInfo pageInfo) throws ApplicationException {
        List<SysCategoryVo> sysCategoryVoList = new ArrayList<>();
        List<SysGroupRespBo> sysGroupList = sysGroupService.listManagedGroup();
        HashMap<Long, String> groupNameMap = new HashMap<>();
        for (SysGroup sysGroup : sysGroupList) {
            if (!groupNameMap.containsKey(sysGroup.getGroupId())) {
                groupNameMap.put(sysGroup.getGroupId(), sysGroup.getGroupName());
            }
        }
        List<Long> groupIds = new ArrayList<>();
        List<Long> managedGroupIds = sysGroupList.stream().map(SysGroup::getGroupId).collect(Collectors.toList());
        if (managedGroupIds != null && managedGroupIds.size() > 0) {
            for (Long groupId : managedGroupIds) {
                if (byGroupId != null && !byGroupId.equals(groupId)) {
                    continue;
                }
                groupIds.add(groupId);
            }
        }
        List<SysCategory> sysCategoryList = new ArrayList<>();
        if (groupIds != null && groupIds.size() > 0) {
            Page<SysCategory> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
            sysCategoryList = sysCategoryMapper.selectByGroupIds(likeCategoryName, groupIds);
            pageInfo.setTotalRecord(page.getTotal());
        }
        for (SysCategory sysCategory : sysCategoryList) {
            SysCategoryVo sysCategoryVo = new SysCategoryVo();
            BeanUtils.copyProperties(sysCategory, sysCategoryVo);
            sysCategoryVo.setGroupName(groupNameMap.get(sysCategory.getGroupId()));
            sysCategoryVoList.add(sysCategoryVo);
        }
        ResultPageData<SysCategoryVo> resultPageData = new ResultPageData(sysCategoryVoList, pageInfo);
        return resultPageData;
    }

    @Override
    public List<SysCategory> listSysCategoryByIds(List<Long> sysCategoryIds) throws ApplicationException {
        List<SysCategory> sysCategoryList = new ArrayList<>();
        if (sysCategoryIds != null && sysCategoryIds.size() > 0) {
            List<SysCategory> list = sysCategoryMapper.selectByCategoryIds(sysCategoryIds);
            if (list != null && list.size() > 0) {
                sysCategoryList = list;
            }
        }
        return sysCategoryList;
    }

    /**
     * 根据系统分组ID查询
     *
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<SysCategoryVo> listSysCategoryVoByIds(List<Long> sysCategoryIds) throws ApplicationException {
        List<SysCategoryVo> sysCategoryVoList = new ArrayList<>();
        List<SysCategory> sysCategoryList = this.listSysCategoryByIds(sysCategoryIds);
        for (SysCategory sysCategory : sysCategoryList) {
            SysCategoryVo sysCategoryVo = new SysCategoryVo();
            BeanUtils.copyProperties(sysCategory, sysCategoryVo);
            SysGroup sysGroup = sysGroupService.get(sysCategory.getGroupId());
            sysCategoryVo.setGroupName(sysGroup.getGroupName());
            sysCategoryVoList.add(sysCategoryVo);
        }
        return sysCategoryVoList;
    }

    /**
     * 按层级统计系统分组数量
     *
     * @return
     */
    @Override
    public List<SysCategoryCount> countSysCategory() {
        Long tenantId = RequestContext.getTenantId();
        List<SysCategoryCount> sysCategoryCountList = sysCategoryMapper.countSysCategoryByTenantId(tenantId);
        return sysCategoryCountList;
    }
}
