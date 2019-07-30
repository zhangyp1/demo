package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryEmpower;

import java.util.List;

/**
 * 系统分组赋权
 *
 * @author zhongqingjiang
 */
public interface SysCategoryEmpowerService {

    List<SysCategoryEmpower> list();

    List<SysCategoryEmpower> listByGroupRoleIds(List<Long> groupRoleIds);

    List<SysCategoryEmpower> selectBySelective(SysCategoryEmpower req);

    void insert(SysCategoryEmpower sysCategoryEmpower);

    void update(SysCategoryEmpower sysCategoryEmpower);

    void delete(Long categoryGrantId);

    void deleteBySelective(SysCategoryEmpower req);

    void deleteByGroupRoleId(Long groupRoleId);

    void deleteByGroupRoleIds(List<Long> groupRoleIds);

}
