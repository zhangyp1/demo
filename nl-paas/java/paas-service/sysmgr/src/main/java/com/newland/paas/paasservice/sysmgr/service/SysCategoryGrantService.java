package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryGrant;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 系统分组授权
 *
 * @author zhongqingjiang
 */
public interface SysCategoryGrantService {

    List<SysCategoryGrant> list();

    List<SysCategoryGrant> listByCategoryIds(List<Long> categoryIds);

    ResultPageData<Long> pageGrantedCategoryIds(Long byGroupId, String likeCategoryName, String objType, PageInfo pageInfo) throws ApplicationException;

    ResultPageData<Long> pageGrantedCategoryIds(PageInfo pageInfo) throws ApplicationException;

    List<Long> listGrantedCategoryIds(String objType) throws ApplicationException;

    void insert(SysCategoryGrant sysCategoryGrant);

    void update(SysCategoryGrant sysCategoryGrant);

    void delete(Long categoryGrantId);

    void deleteByCategoryId(Long categoryId);

    void deleteByGroupId(Long groupId) throws ApplicationException;

}
