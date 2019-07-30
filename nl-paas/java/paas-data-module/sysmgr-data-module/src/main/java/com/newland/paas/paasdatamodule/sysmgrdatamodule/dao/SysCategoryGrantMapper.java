package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryGrant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统分组授权
 *
 * @author zhongqingjiang
 */
public interface SysCategoryGrantMapper {
    int countBySelective(SysCategoryGrant record);

    int deleteBySelective(SysCategoryGrant record);

    int deleteByPrimaryKey(Long categoryGrantId);

    int insert(SysCategoryGrant record);

    int insertSelective(SysCategoryGrant record);

    List<SysCategoryGrant> selectBySelective(SysCategoryGrant record);

    SysCategoryGrant selectByPrimaryKey(Long categoryGrantId);

    List<SysCategoryGrant> selectByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    List<Long> selectGrantedCategoryIds1(@Param("likeCategoryName") String likeCategoryName,
                                        @Param("managedCategoryIds") List<Long> managedCategoryIds,
                                        @Param("managedGroupIds") List<Long> managedGroupIds);

    List<Long> selectGrantedCategoryIds2(@Param("likeCategoryName") String likeCategoryName,
                                        @Param("objType") String objType,
                                        @Param("inCategoryIds") List<Long> inCategoryIds,
                                        @Param("notInCategoryIds") List<Long> notInCategoryIds,
                                        @Param("inGroupIds") List<Long> inGroupIds);

    int updateByPrimaryKeySelective(SysCategoryGrant record);

    int updateByPrimaryKey(SysCategoryGrant record);
}