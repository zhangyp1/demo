package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryEmpowerMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryEmpower;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryEmpowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 系统分组赋权实现类
 *
 * @author zhongqingjiang
 */
@Service
public class SysCategoryEmpowerServiceImpl implements SysCategoryEmpowerService {

    @Autowired
    SysCategoryEmpowerMapper sysCategoryEmpowerMapper;

    @Override
    public List<SysCategoryEmpower> list() {
        List<SysCategoryEmpower> sysCategoryEmpowerList;
        sysCategoryEmpowerList = sysCategoryEmpowerMapper.selectBySelective(null);
        return sysCategoryEmpowerList;
    }

    @Override
    public List<SysCategoryEmpower> listByGroupRoleIds(List<Long> groupRoleIds) {
        List<SysCategoryEmpower> sysCategoryEmpowerList;
        sysCategoryEmpowerList = sysCategoryEmpowerMapper.selectByGroupRoleIds(groupRoleIds);
        return sysCategoryEmpowerList;
    }

    @Override
    public List<SysCategoryEmpower> selectBySelective(SysCategoryEmpower req) {
        List<SysCategoryEmpower> sysCategoryEmpowerList = sysCategoryEmpowerMapper.selectBySelective(req);
        return sysCategoryEmpowerList;
    }

    @Override
    public void insert(SysCategoryEmpower sysCategoryEmpower) {
        sysCategoryEmpower.setTenantId(RequestContext.getTenantId());
        sysCategoryEmpower.setCreatorId(RequestContext.getUserId());
        sysCategoryEmpowerMapper.insertSelective(sysCategoryEmpower);
    }

    @Override
    public void update(SysCategoryEmpower sysCategoryEmpower) {
        sysCategoryEmpowerMapper.updateByPrimaryKey(sysCategoryEmpower);
    }

    @Override
    public void delete(Long categoryGrantId) {
        sysCategoryEmpowerMapper.deleteByPrimaryKey(categoryGrantId);
    }

    @Override
    public void deleteBySelective(SysCategoryEmpower req) {
        sysCategoryEmpowerMapper.deleteBySelective(req);
    }

    @Override
    public void deleteByGroupRoleId(Long groupRoleId) {
        sysCategoryEmpowerMapper.deleteByGroupRoleId(groupRoleId);
    }

    @Override
    public void deleteByGroupRoleIds(List<Long> groupRoleIds) {
        if (!CollectionUtils.isEmpty(groupRoleIds)) {
            sysCategoryEmpowerMapper.deleteByGroupRoleIds(groupRoleIds);
        }
    }
}
