package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysCategoryPermissionError;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统分组权限实现类
 *
 * @author zhongqingjiang
 */
@Service
public class SysCategoryPermissionServiceImpl implements SysCategoryPermissionService {

    @Autowired
    SysGroupService sysGroupService;
    @Autowired
    SysGroupRoleService sysGroupRoleService;
    @Autowired
    SysGroupUserService sysGroupUserService;
    @Autowired
    SysCategoryService sysCategoryService;
    @Autowired
    SysCategoryGrantService sysCategoryGrantService;
    @Autowired
    SysCategoryEmpowerService sysCategoryEmpowerService;
    @Autowired
    ObjPermissionRulerService objPermissionRulerService;

    @Override
    public List<SysCategoryPermissionVo> listManagedSysCategory() throws ApplicationException {
        // 查询我管理的系统分组列表
        List<SysCategoryVo> sysCategoryVoList = sysCategoryService.listManagedSysCategory();
        List<SysCategoryPermissionVo> sysCategoryPermissionVoList = buildSysCategoryPermissionList(sysCategoryVoList,
                false);
        return sysCategoryPermissionVoList;
    }

    @Override
    public ResultPageData<SysCategoryPermissionVo> pageManagedSysCategory(Long byGroupId, String likeCategoryName,
                                                                          PageInfo pageInfo) throws ApplicationException {
        // 查询我管理的系统分组列表 - 分页
        ResultPageData<SysCategoryVo> resultPageDataTemp = sysCategoryService.pageManagedSysCategory(byGroupId,
                likeCategoryName, pageInfo);
        List<SysCategoryVo> sysCategoryVoList = resultPageDataTemp.getList();
        List<SysCategoryPermissionVo> sysCategoryPermissionVoList = buildSysCategoryPermissionList(sysCategoryVoList,
                false);
        pageInfo.setTotalRecord(resultPageDataTemp.getPageInfo().getTotalRecord());
        ResultPageData<SysCategoryPermissionVo> resultPageData = new ResultPageData(sysCategoryPermissionVoList,
                pageInfo);
        return resultPageData;
    }

    @Override
    public SysCategoryPermissionVo getManagedSysCategory(Long categoryId) throws ApplicationException {
        // 根据系统分组ID查询
        if (categoryId == null) {
            return null;
        }
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);
        List<SysCategoryVo> sysCategoryVoList = sysCategoryService.listSysCategoryVoByIds(categoryIds);
        if (sysCategoryVoList == null || sysCategoryVoList.size() == 0) {
            return null;
        }
        List<SysCategoryPermissionVo> sysCategoryPermissionVoList = buildSysCategoryPermissionList(sysCategoryVoList,
                false);
        return sysCategoryPermissionVoList.get(0);
    }

    @Override
    public void updateManagedSysCategory(SysCategoryPermissionVo sysCategoryPermissionVo) throws ApplicationException {
        // 删除原有数据
        sysCategoryGrantService.deleteByCategoryId(sysCategoryPermissionVo.getSysCategoryId());

        // 插入新数据
        if (sysCategoryPermissionVo.getObjectTypeGrantList() != null) {
            for (ObjectTypeGrantVo objectTypeGrantVo : sysCategoryPermissionVo.getObjectTypeGrantList()) {
                List<SysCategoryGrantVo> sysCategoryGrantVoList = objectTypeGrantVo.getSysCategoryGrantList();
                if (sysCategoryGrantVoList != null) {
                    for (SysCategoryGrantVo sysCategoryGrantVo : sysCategoryGrantVoList) {
                        this.insertSysCateGoryGrant(sysCategoryGrantVo);
                    }
                }
            }
        }
    }

    private void insertSysCateGoryGrant(SysCategoryGrantVo sysCategoryGrantVo) {
        if (sysCategoryGrantVo.getGroupRoleEmpowerList() != null) {
            for (SysCategoryEmpowerVo sysCategoryEmpowerVo : sysCategoryGrantVo.getGroupRoleEmpowerList()) {
                if (sysCategoryEmpowerVo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                    // 工组管理员权限 即 工组权限，操作集合为空的不插入
                    if (sysCategoryEmpowerVo.getOperateList() != null && sysCategoryEmpowerVo.getOperateList().size() > 0) {
                        List<String> grantOperateList =
                                sysCategoryEmpowerVo.getOperateList().stream().map(ObjectOperateVo::getOperateCode).collect(Collectors.toList());
                        String grantOperates = String.join(",", grantOperateList);
                        sysCategoryGrantVo.setOperates(grantOperates);
                        sysCategoryGrantService.insert(sysCategoryGrantVo);
                    }
                }
                // 工组角色，操作集合为空的不插入
                if (sysCategoryEmpowerVo.getOperateList() != null && sysCategoryEmpowerVo.getOperateList().size() > 0) {
                    List<String> empowerOperateList =
                            sysCategoryEmpowerVo.getOperateList().stream().map(ObjectOperateVo::getOperateCode).collect(Collectors.toList());
                    String empowerOperates = String.join(",", empowerOperateList);
                    sysCategoryEmpowerVo.setOperates(empowerOperates);
                    sysCategoryEmpowerService.insert(sysCategoryEmpowerVo);
                }
            }
        }
    }

    @Override
    public ResultPageData<SysCategoryPermissionVo> pageGrantedSysCategory(Long byGroupId, String likeCategoryName,
                                                                          PageInfo pageInfo) throws ApplicationException {
        // 查询授权我的系统分组列表 - 分页
        ResultPageData<Long> resultPageDataTemp = sysCategoryGrantService.pageGrantedCategoryIds(byGroupId,
                likeCategoryName, null, pageInfo);
        List<Long> grantedCategoryIds = resultPageDataTemp.getList();
        List<SysCategoryVo> sysCategoryVoList = sysCategoryService.listSysCategoryVoByIds(grantedCategoryIds);
        List<SysCategoryPermissionVo> sysCategoryPermissionVoList = buildSysCategoryPermissionList(sysCategoryVoList,
                true);
        pageInfo.setTotalRecord(resultPageDataTemp.getPageInfo().getTotalRecord());
        ResultPageData<SysCategoryPermissionVo> resultPageData = new ResultPageData(sysCategoryPermissionVoList,
                pageInfo);
        return resultPageData;
    }

    private List<SysCategoryPermissionVo> buildSysCategoryPermissionList(List<SysCategoryVo> sysCategoryVoList,
                                                                         Boolean isOnlyManagedGroup)
            throws ApplicationException {
        List<SysCategoryPermissionVo> sysCategoryPermissionVoList = new ArrayList<>();
        List<ObjectTypeVo> canGrantObjTypeVoList = objPermissionRulerService.listCanGrantObjType(true);
        HashMap<String, List<String>> objOptionalOperateMap = this.getObjOptionalOperateMap(canGrantObjTypeVoList);
        HashMap<String, String> objOperateNameMap = this.getObjOperateNameMap(canGrantObjTypeVoList);
        if (sysCategoryVoList != null && sysCategoryVoList.size() > 0) {
            for (SysCategoryVo sysCategoryVo : sysCategoryVoList) {
                // 构建sysCategoryPermissionVo
                SysCategoryPermissionVo sysCategoryPermissionVo = new SysCategoryPermissionVo();
                BeanUtils.copyProperties(sysCategoryVo, sysCategoryPermissionVo);
                // 查询系统分组列表的授权情况
                Long categoryId = sysCategoryVo.getSysCategoryId();
                List<Long> categoryIds = new ArrayList<>();
                categoryIds.add(categoryId);

                HashMap<Long, SysGroup> managedGroupMap = new HashMap<>();
                if (isOnlyManagedGroup) {
                    List<SysGroupRespBo> managedGroupList = sysGroupService.listManagedGroup();
                    if (managedGroupList != null && managedGroupList.size() > 0) {
                        for (SysGroup sysGroup : managedGroupList) {
                            managedGroupMap.put(sysGroup.getGroupId(), sysGroup);
                        }
                    }
                }

                List<SysCategoryGrant> sysCategoryGrantList = sysCategoryGrantService.listByCategoryIds(categoryIds);
                for (SysCategoryGrant sysCategoryGrant : sysCategoryGrantList) {
                    if (isOnlyManagedGroup && !managedGroupMap.containsKey(sysCategoryGrant.getGroupId())) {
                        continue;
                    }
                    SysCategoryGrantVo sysCategoryGrantVo = buildSysCategoryGrantVo(sysCategoryGrant,
                            sysCategoryPermissionVo.getGroupId(), objOptionalOperateMap, objOperateNameMap);
                    for (ObjectTypeVo objectTypeVo : canGrantObjTypeVoList) {
                        if (sysCategoryGrantVo.getObjType().equals(objectTypeVo.getObjTypeCode())) {
                            ObjectTypeGrantVo oldObjectTypeGrantVo =
                                    sysCategoryPermissionVo.getObjectTypeGrant(sysCategoryGrantVo.getObjType());
                            if (oldObjectTypeGrantVo == null) {
                                ObjectTypeGrantVo newObjectTypeGrantVo = new ObjectTypeGrantVo();
                                newObjectTypeGrantVo.setObjTypeCode(objectTypeVo.getObjTypeCode());
                                newObjectTypeGrantVo.setObjTypeName(objectTypeVo.getObjTypeName());
                                newObjectTypeGrantVo.getSysCategoryGrantList().add(sysCategoryGrantVo);
                                sysCategoryPermissionVo.getObjectTypeGrantList().add(newObjectTypeGrantVo);
                            } else {
                                oldObjectTypeGrantVo.getSysCategoryGrantList().add(sysCategoryGrantVo);
                            }
                        }
                    }
                }
                sysCategoryPermissionVoList.add(sysCategoryPermissionVo);
            }
        }
        return sysCategoryPermissionVoList;
    }

    private SysCategoryGrantVo buildSysCategoryGrantVo(SysCategoryGrant sysCategoryGrant, Long groupIdOfCategory,
                                                       HashMap<String, List<String>> objOptionalOperateMap,
                                                       HashMap<String, String> objOperateNameMap)
            throws ApplicationException {
        // 构建sysCategoryGrantVo
        SysCategoryGrantVo sysCategoryGrantVo = new SysCategoryGrantVo();
        BeanUtils.copyProperties(sysCategoryGrant, sysCategoryGrantVo);
        sysCategoryGrantVo.setGroupName(sysGroupService.get(sysCategoryGrantVo.getGroupId()).getGroupName());
        String[] grantOperateCodes = sysCategoryGrant.getOperates().split(",");
        sysCategoryGrantVo.setOperateList(toObjOperateVoList(grantOperateCodes, objOperateNameMap));
        // 查询是否是该系统分组的所属工组
        if (sysCategoryGrantVo.getGroupId().equals(groupIdOfCategory)) {
            sysCategoryGrantVo.setbCategoryOwner(true);
        } else {
            sysCategoryGrantVo.setbCategoryOwner(false);
        }
        // 可选操作
        String[] grantOptionalOperates = new String[0];
        grantOptionalOperates =
                objOptionalOperateMap.get(sysCategoryGrantVo.getObjType()).toArray(grantOptionalOperates);
        sysCategoryGrantVo.setOptionalOperateList(toObjOperateVoList(grantOptionalOperates, objOperateNameMap));
        // 查询授予工组的赋权情况
        Long groupId = sysCategoryGrantVo.getGroupId();
        List<Long> groupIds = new ArrayList<>();
        groupIds.add(groupId);

        List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listByGroupIds(groupIds);
        for (SysGroupRole sysGroupRole : sysGroupRoleList) {
            // 查询授予工组角色的权限
            // 没有授权的工组角色也列出，赋予默认值
            // 构建sysCategoryEmpowerVo
            SysCategoryEmpowerVo sysCategoryEmpowerVo = new SysCategoryEmpowerVo();
            Long groupRoleId = sysGroupRole.getGroupRoleId();
            sysCategoryEmpowerVo.setGroupRoleId(groupRoleId);
            sysCategoryEmpowerVo.setGroupRoleName(sysGroupRole.getGroupRoleName());
            sysCategoryEmpowerVo.setCategoryId(sysCategoryGrantVo.getCategoryId());
            sysCategoryEmpowerVo.setObjType(sysCategoryGrantVo.getObjType());
            sysCategoryEmpowerVo.setOperates("");
            sysCategoryEmpowerVo.setTenantId(sysCategoryGrant.getTenantId());
            // 可选操作
            String[] empowerOptionalOperates = new String[0];
            empowerOptionalOperates =
                    objOptionalOperateMap.get(sysCategoryEmpowerVo.getObjType()).toArray(empowerOptionalOperates);
            sysCategoryEmpowerVo.setOptionalOperateList(toObjOperateVoList(empowerOptionalOperates, objOperateNameMap));
            // 获取赋权数据
            if (sysCategoryEmpowerVo.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                // 工组管理员
                // 工组管理员的权限 即 工组的权限
                sysCategoryEmpowerVo.setOperates(sysCategoryGrantVo.getOperates());
            } else {
                // 其他工组角色
                SysCategoryEmpower sysCategoryEmpower = null;
                SysCategoryEmpower req = new SysCategoryEmpower();
                req.setCategoryId(sysCategoryGrantVo.getCategoryId());
                req.setObjType(sysCategoryGrantVo.getObjType());
                req.setGroupRoleId(groupRoleId);
                List<SysCategoryEmpower> sysCategoryEmpowerList
                        = sysCategoryEmpowerService.selectBySelective(req);
                if (sysCategoryEmpowerList != null && sysCategoryEmpowerList.size() > 0) {
                    sysCategoryEmpower = sysCategoryEmpowerList.get(0);
                }
                if (sysCategoryEmpower != null) {
                    BeanUtils.copyProperties(sysCategoryEmpower, sysCategoryEmpowerVo);
                }
            }

            String[] empowerOperateCodes = sysCategoryEmpowerVo.getOperates().split(",");
            sysCategoryEmpowerVo.setOperateList(toObjOperateVoList(empowerOperateCodes, objOperateNameMap));
            sysCategoryGrantVo.getGroupRoleEmpowerList().add(sysCategoryEmpowerVo);
        }
        return sysCategoryGrantVo;
    }

    @Override
    public List<SysGroupRespVo2> listGroup(Long categoryId) throws ApplicationException {
        List<SysGroupRespVo2> sysGroupRespVo2List = new ArrayList<>();
        SysCategoryVo sysCategoryVo = getCategoryVo(categoryId);
        SysGroupReqBo req = new SysGroupReqBo();
        req.setTenantId(RequestContext.getTenantId());
        List<SysGroup> sysGroupList = sysGroupService.list(req);
        for (SysGroup sysGroup : sysGroupList) {
            SysGroupRespVo2 sysGroupRespVo2 = new SysGroupRespVo2();
            BeanUtils.copyProperties(sysGroup, sysGroupRespVo2);
            if (sysGroup.getGroupId().equals(sysCategoryVo.getGroupId())) {
                sysGroupRespVo2.setbCategoryOwner(true);
            } else {
                sysGroupRespVo2.setbCategoryOwner(false);
            }
            sysGroupRespVo2List.add(sysGroupRespVo2);
        }
        return sysGroupRespVo2List;
    }

    private HashMap<String, List<String>> getObjOptionalOperateMap(List<ObjectTypeVo> canGrantObjTypeVoList) {
        HashMap<String, List<String>> objOptionalOperateMap = new HashMap<>();
        for (ObjectTypeVo objectTypeVo : canGrantObjTypeVoList) {
            String objTypeCode = objectTypeVo.getObjTypeCode();
            if (!objOptionalOperateMap.containsKey(objTypeCode)) {
                objOptionalOperateMap.put(objectTypeVo.getObjTypeCode(), new ArrayList<>());
            }
            for (GlbDict glbDict : objectTypeVo.getOperates()) {
                String objOperateCode = glbDict.getDictCode();
                objOptionalOperateMap.get(objTypeCode).add(objOperateCode);
            }
        }
        return objOptionalOperateMap;
    }

    private HashMap<String, String> getObjOperateNameMap(List<ObjectTypeVo> canGrantObjTypeVoList) {
        HashMap<String, String> objOperateNameMap = new HashMap<>();
        for (ObjectTypeVo objectTypeVo : canGrantObjTypeVoList) {
            for (GlbDict glbDict : objectTypeVo.getOperates()) {
                String objOperateCode = glbDict.getDictCode();
                String objOperateName = glbDict.getDictName();
                objOperateNameMap.put(objOperateCode, objOperateName);
            }
        }
        return objOperateNameMap;
    }

    private List<ObjectOperateVo> toObjOperateVoList(String[] operateCodes, HashMap<String, String> objOperateNameMap) {
        List<ObjectOperateVo> objOperateVoList = new ArrayList<>();
        for (String operateCode : operateCodes) {
            if (operateCode != null && operateCode.trim() != "") {
                objOperateVoList.add(new ObjectOperateVo(operateCode, objOperateNameMap.get(operateCode)));
            }
        }
        return objOperateVoList;
    }

    @Override
    public SysCategoryPermissionVo generateForm(SysCategoryPermissionFormReq req) throws ApplicationException {
        SysCategoryPermissionVo resp = new SysCategoryPermissionVo();
        Long categoryId = req.getCategoryId();
        resp.setSysCategoryId(categoryId);
        SysCategoryVo sysCategoryVo = getCategoryVo(categoryId);
        BeanUtils.copyProperties(sysCategoryVo, resp);
        List<ObjectTypeVo> canGrantObjTypeVoList = objPermissionRulerService.listCanGrantObjType(true);
        HashMap<String, List<String>> objOptionalOperateMap = this.getObjOptionalOperateMap(canGrantObjTypeVoList);
        HashMap<String, String> objOperateNameMap = this.getObjOperateNameMap(canGrantObjTypeVoList);
        SysCategoryPermissionVo sysCategoryPermissionVo = this.getManagedSysCategory(categoryId);
        for (Long groupId : req.getGroupIdList()) {
            for (ObjectTypeVo objectTypeVo : canGrantObjTypeVoList) {
                SysCategoryGrantVo grant = generateSysCategoryGrant(categoryId, objectTypeVo,
                        groupId, sysCategoryPermissionVo, objOptionalOperateMap, objOperateNameMap);

                ObjectTypeGrantVo oldObjectTypeGrantVo = resp.getObjectTypeGrant(objectTypeVo.getObjTypeCode());
                if (oldObjectTypeGrantVo == null) {
                    ObjectTypeGrantVo newObjectTypeGrantVo = new ObjectTypeGrantVo();
                    newObjectTypeGrantVo.setObjTypeCode(objectTypeVo.getObjTypeCode());
                    newObjectTypeGrantVo.setObjTypeName(objectTypeVo.getObjTypeName());
                    newObjectTypeGrantVo.getSysCategoryGrantList().add(grant);
                    resp.getObjectTypeGrantList().add(newObjectTypeGrantVo);
                } else {
                    oldObjectTypeGrantVo.getSysCategoryGrantList().add(grant);
                }

            }
        }
        return resp;
    }

    private SysCategoryVo getCategoryVo(Long categoryId) throws ApplicationException {
        // 根据系统分组ID查询系统分组
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);
        List<SysCategoryVo> sysCategoryVoList = sysCategoryService.listSysCategoryVoByIds(categoryIds);
        if (sysCategoryVoList == null || sysCategoryVoList.size() == 0) {
            throw new ApplicationException(SysCategoryPermissionError.CATEGORY_NOT_FOUND);
        }
        SysCategoryVo sysCategoryVo = sysCategoryVoList.get(0);
        return sysCategoryVo;
    }

    private SysCategoryGrantVo generateSysCategoryGrant(Long categoryId, ObjectTypeVo objectTypeVo, Long groupId,
                                                        SysCategoryPermissionVo sysCategoryPermissionVo,
                                                        HashMap<String, List<String>> objOptionalOperateMap,
                                                        HashMap<String, String> objOperateNameMap) throws ApplicationException {
        SysCategoryGrantVo grant = new SysCategoryGrantVo();
        /**
         * -- 工组
         * 系统分组ID
         * 对象类型
         * 工组ID
         * 工组名称
         * 是否是该系统分组的所属分组
         * 可选操作
         * 已选操作 2个
         *
         * -- 工组角色
         * 系统分组ID
         * 对象类型
         * 工组角色ID
         * 工组角色名称
         * 可选操作
         * 已选操作 2个
         */
        grant.setCategoryId(categoryId);
        grant.setObjType(objectTypeVo.getObjTypeCode());
        grant.setGroupId(groupId);
        SysGroup group = sysGroupService.get(groupId);
        grant.setGroupName(group.getGroupName());
        if (grant.getGroupId().equals(sysCategoryPermissionVo.getGroupId())) {
            grant.setbCategoryOwner(true);
        } else {
            grant.setbCategoryOwner(false);
        }
        String[] grantOptionalOperates = new String[0];
        grantOptionalOperates = objOptionalOperateMap.get(grant.getObjType()).toArray(grantOptionalOperates);
        grant.setOptionalOperateList(toObjOperateVoList(grantOptionalOperates, objOperateNameMap));
        grant.setOperates("");
        grant.setOperateList(new ArrayList<>());

        List<Long> groupIds = new ArrayList<>();
        groupIds.add(groupId);
        List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listByGroupIds(groupIds);
        for (SysGroupRole sysGroupRole : sysGroupRoleList) {
            SysCategoryEmpowerVo empower = new SysCategoryEmpowerVo();
            Long groupRoleId = sysGroupRole.getGroupRoleId();
            empower.setCategoryId(grant.getCategoryId());
            empower.setObjType(grant.getObjType());
            empower.setGroupRoleId(groupRoleId);
            empower.setGroupRoleName(sysGroupRole.getGroupRoleName());
            empower.setOptionalOperateList(grant.getOptionalOperateList());
            empower.setOperates("");
            empower.setOperateList(new ArrayList<>());
            grant.getGroupRoleEmpowerList().add(empower);
        }

        for (ObjectTypeGrantVo objectTypeGrantVo : sysCategoryPermissionVo.getObjectTypeGrantList()) {
            if (objectTypeVo.getObjTypeCode().equals(objectTypeGrantVo.getObjTypeCode())) {
                for (SysCategoryGrantVo grantHasData : objectTypeGrantVo.getSysCategoryGrantList()) {
                    if (grantHasData.getGroupId().equals(groupId)) {
                        // 有数据，则替换
                        grant.setOperates(grantHasData.getOperates());
                        grant.setOperateList(grantHasData.getOperateList());
                        for (SysCategoryEmpowerVo empower : grant.getGroupRoleEmpowerList()) {
                            if (empower.getGroupRoleName().equals(SysMgrConstants.ROLE_NAME_GROUP_ADMIN)) {
                                // 工组管理员权限 即 工组权限
                                empower.setOperates(grantHasData.getOperates());
                                empower.setOperateList(grantHasData.getOperateList());
                            } else {
                                // 其他工组角色
                                for (SysCategoryEmpowerVo empowerHasData : grantHasData.getGroupRoleEmpowerList()) {
                                    if (empowerHasData.getGroupRoleId().equals(empower.getGroupRoleId())) {
                                        empower.setOperates(empowerHasData.getOperates());
                                        empower.setOperateList(empowerHasData.getOperateList());
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

        return grant;
    }
}
