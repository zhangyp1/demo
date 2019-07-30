package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryGrantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryEmpower;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryGrant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryEmpowerService;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryGrantService;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryService;
import com.newland.paas.paasservice.sysmgr.service.SysGroupRoleService;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统分组授权实现类
 *
 * @author zhongqingjiang
 */
@Service
public class SysCategoryGrantServiceImpl implements SysCategoryGrantService {

    @Autowired
    SysCategoryGrantMapper sysCategoryGrantMapper;
    @Autowired
    SysGroupRoleService sysGroupRoleService;
    @Autowired
    SysCategoryService sysCategoryService;
    @Autowired
    SysCategoryEmpowerService sysCategoryEmpowerService;

    @Override
    public List<SysCategoryGrant> list() {
        List<SysCategoryGrant> sysCategoryGrantList;
        sysCategoryGrantList = sysCategoryGrantMapper.selectBySelective(null);
        return sysCategoryGrantList;
    }

    @Override
    public List<SysCategoryGrant> listByCategoryIds(List<Long> categoryIds) {
        List<SysCategoryGrant> sysCategoryGrantList;
        sysCategoryGrantList = sysCategoryGrantMapper.selectByCategoryIds(categoryIds);
        return sysCategoryGrantList;
    }

    @Override
    public ResultPageData<Long> pageGrantedCategoryIds(Long byGroupId, String likeCategoryName, String objType, PageInfo pageInfo) throws ApplicationException {
        List<Long> grantedCategoryIds = new ArrayList<>();
        // 获取我的工组角色列表
        List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listMyGroupRoleList(true);
        // 获取赋权给我的系统分组列表
        List<Long> groupRoleIdList = sysGroupRoleList.stream().map(SysGroupRole::getGroupRoleId).collect(Collectors.toList());
        if (groupRoleIdList.size() > 0) {
            List<SysCategoryEmpower> sysCategoryEmpowerList = sysCategoryEmpowerService.listByGroupRoleIds(groupRoleIdList);
            if (sysCategoryEmpowerList.size() > 0) {
                List<Long> sysCategoryIdList = sysCategoryEmpowerList.stream().map(SysCategoryEmpower::getCategoryId).collect(Collectors.toList());
                // 去重
                List<Long> grantedSysCategoryIdList = new ArrayList<>();
                for (Long sysCategoryId : sysCategoryIdList) {
                    if (!grantedSysCategoryIdList.contains(sysCategoryId)) {
                        grantedSysCategoryIdList.add(sysCategoryId);
                    }
                }
                // 获取我管理的系统分组
                List<SysCategoryVo> managedSysCategoryList = sysCategoryService.listManagedSysCategory();
                List<Long> managedSysCategoryIds = managedSysCategoryList.stream().map(SysCategory::getSysCategoryId).collect(Collectors.toList());
                List<Long> groupIds = new ArrayList<>();
                if (byGroupId != null) {
                    groupIds.add(byGroupId);
                }
                if (pageInfo != null) {
                    // 分页
                    Page<SysCategoryGrant> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
                    grantedCategoryIds = sysCategoryGrantMapper.selectGrantedCategoryIds2(likeCategoryName, objType,
                            grantedSysCategoryIdList, managedSysCategoryIds, groupIds);
                    pageInfo.setTotalRecord(page.getTotal());
                } else {
                    // 不分页
                    grantedCategoryIds = sysCategoryGrantMapper.selectGrantedCategoryIds2(likeCategoryName, objType,
                            grantedSysCategoryIdList, managedSysCategoryIds, groupIds);
                }
            }
        }
        ResultPageData<Long> resultPageData = new ResultPageData(grantedCategoryIds, pageInfo);
        return resultPageData;
    }

    @Override
    public ResultPageData<Long> pageGrantedCategoryIds(PageInfo pageInfo) throws ApplicationException {
        ResultPageData<Long> resultPageData = this.pageGrantedCategoryIds(null, null, null, pageInfo);
        return resultPageData;
    }

    @Override
    public List<Long> listGrantedCategoryIds(String objType) throws ApplicationException {
        ResultPageData<Long> resultPageData = this.pageGrantedCategoryIds(null, null, objType, null);
        return resultPageData.getList();
    }

    @Override
    public void insert(SysCategoryGrant sysCategoryGrant) {
        sysCategoryGrant.setTenantId(RequestContext.getTenantId());
        sysCategoryGrant.setCreatorId(RequestContext.getUserId());
        sysCategoryGrantMapper.insertSelective(sysCategoryGrant);
    }

    @Override
    public void update(SysCategoryGrant sysCategoryGrant) {
        sysCategoryGrantMapper.updateByPrimaryKey(sysCategoryGrant);
    }

    @Override
    public void delete(Long categoryGrantId) {
        sysCategoryGrantMapper.deleteByPrimaryKey(categoryGrantId);
    }

    @Override
    public void deleteByCategoryId(Long categoryId) {
        SysCategoryGrant sysCategoryGrant = new SysCategoryGrant();
        sysCategoryGrant.setCategoryId(categoryId);
        sysCategoryGrantMapper.deleteBySelective(sysCategoryGrant);

        SysCategoryEmpower sysCategoryEmpower = new SysCategoryEmpower();
        sysCategoryEmpower.setCategoryId(categoryId);
        sysCategoryEmpowerService.deleteBySelective(sysCategoryEmpower);
    }

    @Override
    public void deleteByGroupId(Long groupId) throws ApplicationException {
        SysCategoryGrant sysCategoryGrant = new SysCategoryGrant();
        sysCategoryGrant.setGroupId(groupId);
        sysCategoryGrantMapper.deleteBySelective(sysCategoryGrant);

        List<Long> groupIds = new ArrayList<>();
        groupIds.add(groupId);
        List<SysGroupRole> sysGroupRoleList = sysGroupRoleService.listByGroupIds(groupIds);
        for (SysGroupRole sysGroupRole : sysGroupRoleList) {
            sysCategoryEmpowerService.deleteByGroupRoleId(sysGroupRole.getGroupRoleId());
        }
    }
}
