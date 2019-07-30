package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuota;

import java.util.List;

public interface SysCategoryQuotaMapper {
    /**
     * countBySelective
     * @param record
     * @return
     */
    int countBySelective(SysCategoryQuota record);

    /**
     * deleteBySelective
     * @param record
     * @return
     */
    int deleteBySelective(SysCategoryQuota record);

    /**
     * deleteByPrimaryKey
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     * @param record
     * @return
     */
    int insert(SysCategoryQuota record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(SysCategoryQuota record);

    /**
     * selectBySelective
     * @param record
     * @return
     */
    List<SysCategoryQuota> selectBySelective(SysCategoryQuota record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return
     */
    SysCategoryQuota selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysCategoryQuota record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysCategoryQuota record);
}
