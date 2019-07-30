package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantLimit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantLimitBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantLimitInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysTenantLimitMapper {
    int countBySelective(SysTenantLimit record);

    int deleteBySelective(SysTenantLimit record);

    int deleteByPrimaryKey(Long id);

    int insert(SysTenantLimit record);

    int insertSelective(SysTenantLimit record);

    List<SysTenantLimit> selectBySelective(SysTenantLimit record);

    SysTenantLimit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTenantLimit record);

    int updateByPrimaryKey(SysTenantLimit record);

    /**
     * 批量新增
     * @param limits
     * @param tenantId
     * @return
     */
    int batchInsert(@Param("list") List<SysTenantLimit> limits, @Param("tenantId") Long tenantId);

    /**
     * 批量更新
     * @param limits
     * @param tenantId
     * @return
     */
    int batchUpdate(@Param("list") List<SysTenantLimit> limits, @Param("tenantId") Long tenantId);
    
    


    /**
     * 统计租户的资源配额
     * @param tenantId
     * @return
     */
    SysTenantLimitInfoVo getLimitsByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 根据租户id查询配额
     * @param tenantId
     * @return
     */
    List<SysTenantLimitBo> listLimitsByTenantId(@Param("tenantId") Long tenantId);


    /**    
     * @描述: 批量删除配额
     * @param tenantId
     * @return         
     * @创建人：zyp
     * @创建时间：2019/05/23 
     */
    int deletbatch(@Param("tenantId") Long tenantId);

}