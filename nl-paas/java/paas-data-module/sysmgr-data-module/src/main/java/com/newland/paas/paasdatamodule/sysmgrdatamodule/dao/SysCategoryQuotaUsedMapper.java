package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuotaUsed;
import java.util.List;

public interface SysCategoryQuotaUsedMapper {
    /**
     * countBySelective
     * @param record
     * @return
     */
    int countBySelective(SysCategoryQuotaUsed record);

    /**
     * deleteBySelective
     * @param record
     * @return
     */
    int deleteBySelective(SysCategoryQuotaUsed record);

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
    int insert(SysCategoryQuotaUsed record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(SysCategoryQuotaUsed record);

    /**
     * selectBySelective
     * @param record
     * @return
     */
    List<SysCategoryQuotaUsed> selectBySelective(SysCategoryQuotaUsed record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return
     */
    SysCategoryQuotaUsed selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysCategoryQuotaUsed record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysCategoryQuotaUsed record);

    /**
     * 依据系统分组统计配额
     * @param record
     * @return
     */
    SysCategoryQuotaUsed countQuotaUsedBySysCategory(SysCategoryQuotaUsed record);
}
