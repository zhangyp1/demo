package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysObjViewMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysObjOperateVo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.controller.ObjPermissionHubController;
import com.newland.paas.paasservice.sysmgr.error.ObjPermissionHubError;
import com.newland.paas.paasservice.sysmgr.service.ObjPermissionHubService;
import com.newland.paas.paasservice.sysmgr.service.ObjPermissionRulerService;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryGrantService;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryService;
import com.newland.paas.paasservice.sysmgr.vo.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象权限集中管理，集中管理对象权限和系统分组权限
 *
 * @author zhongqingjiang
 */
@Service
public class ObjPermissionHubServiceImpl implements ObjPermissionHubService {

    public static final Log LOG = LogFactory.getLogger(ObjPermissionHubServiceImpl.class);

    @Autowired
    SysObjViewMapper sysObjViewMapper;
    @Autowired
    SysCategoryService sysCategoryService;
    @Autowired
    SysCategoryGrantService sysCategoryGrantService;
    @Autowired
    ObjPermissionRulerService objPermissionRulerService;

    @Override
    public TreeNode<BaseTreeDo> getMySysCategoryTree() throws ApplicationException {
        /**
         * 我的系统分组：
         * 1. 系统分组属于我管理的或我加入的工组
         */
        return sysCategoryService.getSysCategoryTree();
    }

    @Override
    public TreeNode<BaseTreeDo> getGrantedSysCategoryTree() throws ApplicationException {
        /**
         * 授权系统分组：
         * 1. 系统分组不属于我管理的或我加入的工组
         * 2. 系统分组是通过“对象权限功能”授权给我加入的工组角色
         * 3. 系统分组是通过“系统分组权限功能”授权给我加入的工组角色（包括系统分组下无对象）
         */
        TreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new TreeNode.TreeBuilder<>();
        List<SysCategory> grantedSysCategoryList = this.listGrantedSysCategory(null);
        for (SysCategory sysCategory : grantedSysCategoryList) {
            BaseTreeDo baseTreeDo = new BaseTreeDo();
            baseTreeDo.setParentId(sysCategory.getSysCategoryPid().toString());
            baseTreeDo.setId(sysCategory.getSysCategoryId().toString());
            baseTreeDo.setName(sysCategory.getSysCategoryName());
            treeBuilder.addNode(baseTreeDo.getParentId(), baseTreeDo.getId(), baseTreeDo);
        }
        return treeBuilder.build();
    }

    @Override
    public List<SysCategory> listGrantedSysCategory(String objType) throws ApplicationException {
        List<SysCategory> grantedSysCategoryList = new ArrayList<>();
        // 获取视图中授权给我的系统分组列表
        Long userId = RequestContext.getUserId();
        Long tenantId = RequestContext.getTenantId();
        List<SysCategoryVo> sysCategoryVoList = sysCategoryService.listMySysCategory();
        List<Long> notInSysCategoryIds = sysCategoryVoList.stream().map(SysCategory::getSysCategoryId).collect(Collectors.toList());
        List<Long> sysCategoryIds1 = sysObjViewMapper.selectUserGrantObjCategoryId(userId, tenantId, notInSysCategoryIds, objType);
        // 获取系统分组权限功能中，来自授权的系统分组（主要是为了获取到不包含对象的系统分组）
        List<Long> sysCategoryIds2 = sysCategoryGrantService.listGrantedCategoryIds(objType);
        // 去重
        List<Long> grantedSysCategoryIdList = new ArrayList<>();
        List<Long> sysCategoryIds = new ArrayList<>();
        sysCategoryIds.addAll(sysCategoryIds1);
        sysCategoryIds.addAll(sysCategoryIds2);
        for (Long sysCategoryId : sysCategoryIds) {
            if (!grantedSysCategoryIdList.contains(sysCategoryId)) {
                grantedSysCategoryIdList.add(sysCategoryId);
            }
        }
        if (grantedSysCategoryIdList.size() > 0) {
            grantedSysCategoryList = sysCategoryService.listSysCategoryByIds(grantedSysCategoryIdList);
        }
        return grantedSysCategoryList;
    }

    @Override
    public ResultPageData<SysUserObj> pageObjectBySysCategory(SysCategoryObjReqVo req, PageInfo pageInfo)
            throws ApplicationException {
        if (req == null || req.getSysCategoryId() == null) {
            throw new ApplicationException(ObjPermissionHubError.SYS_CATEGORY_ID_IS_NULL);
        }
        List<SysUserObj> sysUserObjList = new ArrayList<>();
        Page<SysUserObj> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        if (!req.getbGrant()) {
            // 我的系统分组
            List<String> canGrantObjTypeCodes = objPermissionRulerService.listCanGrantObjTypeCode();
            if (RequestContext.getSession().getIsTenantAdmin()) {
                // TODO：因为租户管理员默认拥有租户下所有对象的权限，考虑是否直接查V_SYS_OBJ，提高查询效率
                // 租户管理员
                sysUserObjList = sysObjViewMapper.selectUserObjByCategoryAndUser_Merged(req.getSysCategoryId(),
                        RequestContext.getUserId(), req.getLikeObjName(), req.getObjType(), canGrantObjTypeCodes);
            } else {
                // 非租户管理员
                sysUserObjList = sysObjViewMapper.selectUserObjByCategoryAndUser_Merged(req.getSysCategoryId(),
                        RequestContext.getUserId(), req.getLikeObjName(), req.getObjType(), canGrantObjTypeCodes);
            }
        } else {
            // 授权系统分组
            sysUserObjList = sysObjViewMapper.selectUserGrantObjByCategoryAndUser_Merged(req.getSysCategoryId(),
                    RequestContext.getUserId(), req.getLikeObjName(), req.getObjType());
        }
        List<SysUserObj> processedSysUserObjList = this.processSysUserObjForMerged(sysUserObjList);
        pageInfo.setTotalRecord(page.getTotal());
        ResultPageData<SysUserObj> resultPageData = new ResultPageData(processedSysUserObjList, pageInfo);
        return resultPageData;
    }

    @Override
    public List<SysObjOperateVo> auth(List<SysObjOperateVo> objOperates) {
        List<SysObjOperateVo> objOperateData = new ArrayList<>();
        // 无参数返回空列表
        if (CollectionUtils.isEmpty(objOperates)) {
            return objOperateData;
        }
        Long userId = RequestContext.getUserId();
        Long tenantId = RequestContext.getTenantId();
        Boolean isTenantAdmin = RequestContext.getSession().getIsTenantAdmin();
        objOperates.forEach(v -> {
            SysObjOperateVo sysObjOperateVo = new SysObjOperateVo();
            sysObjOperateVo.setObjId(v.getObjId());
            sysObjOperateVo.setOperates(new ArrayList<>());
            List<String> checkOperates = v.getOperates();
            if (checkOperates != null) {
                SysObj sysObj = new SysObj();
                sysObj.setObjId(sysObjOperateVo.getObjId());
                List<SysObj> sysObjList = sysObjViewMapper.selectObjBySelective(sysObj);
                if (CollectionUtils.isEmpty(sysObjList)) {
                    // 查不到对象的，默认有权限，主要作用于网关对某些URL的放行，如LB
                    sysObjOperateVo.setOperates(checkOperates);
                }
                else if (SysMgrConstants.TENANT_ID_YUNYIN.equals(tenantId) && isTenantAdmin) {
                    // 运营管理员默认拥有所有对象权限
                    sysObjOperateVo.setOperates(checkOperates);
                } else if (isTenantAdmin) {
                    // 租户管理员默认拥有本租户下所有对象权限
                    SysUserObj req = new SysUserObj();
                    req.setObjId(sysObjOperateVo.getObjId());
                    req.setTenantId(tenantId);
                    List<SysUserObj> sysUserObjList = sysObjViewMapper.selectUserObjBySelective(req);
                    if (!CollectionUtils.isEmpty(sysUserObjList)) {
                        sysObjOperateVo.setOperates(checkOperates);
                    }
                } else {
                    SysUserObj req = new SysUserObj();
                    req.setUserId(userId);
                    req.setObjId(sysObjOperateVo.getObjId());
                    req.setTenantId(tenantId);
                    List<SysUserObj> sysUserObjList = sysObjViewMapper.selectUserObjBySelective(req);
                    if (!CollectionUtils.isEmpty(sysUserObjList)) {
                        for (String checkOperate : checkOperates) {
                            for (SysUserObj sysUserObj : sysUserObjList) {
                                if (!StringUtils.isBlank(sysUserObj.getOperates())) {
                                    Boolean isFound = false;
                                    if ("*".equals(sysUserObj.getOperates())) {
                                        isFound = true;
                                    } else {
                                        List<String> hasOperates = Arrays.asList(sysUserObj.getOperates().split(","));
                                        if (hasOperates.contains(checkOperate)) {
                                            isFound = true;
                                        }
                                    }
                                    if (isFound && !sysObjOperateVo.getOperates().contains(checkOperate)) {
                                        sysObjOperateVo.getOperates().add(checkOperate);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            objOperateData.add(sysObjOperateVo);
        });
        return objOperateData;
    }

    @Override
    public Boolean auth(Long objId, String operateCode) throws ApplicationException {
        if (objId == null) {
            throw new ApplicationException(ObjPermissionHubError.OBJ_ID_IS_NULL);
        }
        if (operateCode == null) {
            throw new ApplicationException(ObjPermissionHubError.OBJ_OPERATE_IS_NULL);
        }
        if (operateCode.toLowerCase().equals("detailinfo") || operateCode.toLowerCase().equals("pf_query")) {
            // TODO：集群的DetailInfo和应用的pf_query操作已弃用，现在字典的详情操作CODE为pf_detail，在集群和应用改接口地址之前，暂时放行
            return true;
        }
        List<SysObjOperateVo> req = new ArrayList<>();
        SysObjOperateVo sysObjOperateVo = new SysObjOperateVo();
        sysObjOperateVo.setObjId(objId);
        List<String> operates = new ArrayList<>();
        operates.add(operateCode);
        sysObjOperateVo.setOperates(operates);
        req.add(sysObjOperateVo);
        List<SysObjOperateVo> sysObjOperateVoList = this.auth(req);
        return !sysObjOperateVoList.isEmpty() && !sysObjOperateVoList.get(0).getOperates().isEmpty();
    }

    private List<SysUserObj> processSysUserObj(List<SysUserObj> rawSysUserObjList) {
        List<SysUserObj> processedSysUserObjList = new ArrayList<>();
        if (rawSysUserObjList != null) {
            HashMap<Long, List<SysUserObj>> userId_SysUserObjList_Map = new HashMap<>();
            Map<String, ObjectTypeVo> canGrantObjTypeMap = null;
            for (SysUserObj sysUserObj : rawSysUserObjList) {
                // 去除操作权限为空的记录
                if (StringUtils.isBlank(sysUserObj.getOperates())) {
                    continue;
                }
                // 检查是否包含通配符，有则获取对象操作替换通配符
                if ("*".equals(sysUserObj.getOperates())) {
                    if (canGrantObjTypeMap == null) {
                        canGrantObjTypeMap = objPermissionRulerService.listCanGrantObjTypeMap(true);
                    }
                    List<String> operateList = canGrantObjTypeMap.get(sysUserObj.getObjType()).getOperates().stream().map(GlbDict::getDictCode).collect(Collectors.toList());
                    String operates = String.join(",", operateList);
                    sysUserObj.setOperates(operates);
                }
                Long userId = sysUserObj.getUserId();
                if (!userId_SysUserObjList_Map.containsKey(userId)) {
                    userId_SysUserObjList_Map.put(userId, new ArrayList<>());
                }
                userId_SysUserObjList_Map.get(userId).add(sysUserObj);
            }
            // 合并用户相同且对象相同的操作权限
            for (Long userId : userId_SysUserObjList_Map.keySet()) {
                List<SysUserObj> sysUserObjList = userId_SysUserObjList_Map.get(userId);
                HashMap<Long, SysUserObj> objId_MergedSysUserObj_Map = new HashMap();
                for (SysUserObj sysUserObj : sysUserObjList) {
                    Long objId = sysUserObj.getObjId();
                    if (!objId_MergedSysUserObj_Map.containsKey(objId)) {
                        objId_MergedSysUserObj_Map.put(objId, sysUserObj);
                    } else {
                        SysUserObj mergedSysUserObj = objId_MergedSysUserObj_Map.get(objId);
                        String[] operates = sysUserObj.getOperates().split(",");
                        List<String> mergedOperateList = Arrays.asList(mergedSysUserObj.getOperates().split(","));
                        for (String operate : operates) {
                            if (!mergedOperateList.contains(operate)) {
                                mergedOperateList.add(operate);
                            }
                        }
                        mergedSysUserObj.setOperates(String.join(",", mergedOperateList));
                    }
                }
                processedSysUserObjList.addAll(objId_MergedSysUserObj_Map.values());
            }
        }
        return  processedSysUserObjList;
    }

    private List<SysUserObj> processSysUserObjForMerged(List<SysUserObj> rawSysUserObjList) {
        List<SysUserObj> processedSysUserObjList = new ArrayList<>();
        if (rawSysUserObjList != null) {
            Map<String, ObjectTypeVo> canGrantObjTypeMap = null;
            for (SysUserObj sysUserObj : rawSysUserObjList) {
                List<String> processedOperateList = new ArrayList<>();
                if (!StringUtils.isBlank(sysUserObj.getOperates())) {
                    if (sysUserObj.getOperates().contains("*")) {
                        // 包含通配符则获取对象操作替换
                        if (canGrantObjTypeMap == null) {
                            canGrantObjTypeMap = objPermissionRulerService.listCanGrantObjTypeMap(true);
                        }
                        processedOperateList = canGrantObjTypeMap.get(sysUserObj.getObjType()).getOperates().stream().map(GlbDict::getDictCode).collect(Collectors.toList());
                    } else {
                        // 不包含通配符则去重
                        String[] operates = sysUserObj.getOperates().split(",");
                        for (String operate : operates) {
                            if (!processedOperateList.contains(operate)) {
                                processedOperateList.add(operate);
                            }
                        }
                    }
                }
                String operates = String.join(",", processedOperateList);
                sysUserObj.setOperates(operates);
                processedSysUserObjList.add(sysUserObj);
            }
        }
        return  processedSysUserObjList;
    }
}
