package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.CreateTenantDetail;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.DetailInfosBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.StatusInfosBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysTenantOperateMapper {
    int countBySelective(SysTenantOperate record);

    int deleteBySelective(SysTenantOperate record);

    int deleteByPrimaryKey(Long id);

    int insert(SysTenantOperate record);

    int insertSelective(SysTenantOperate record);

    List<SysTenantOperate> selectBySelective(SysTenantOperate record);

    SysTenantOperate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTenantOperate record);

    int updateByPrimaryKey(SysTenantOperate record);

    int updateStatusSuccess(SysTenantOperate record);

    int updateStatusFailure(SysTenantOperate record);

    /**
     * 获取租户创建信息
     * @param tenantId
     * @return
     */
    CreateTenantDetail getCreateTenantDetailInfo(@Param("tenantId") Long tenantId);

    /**
     * 获取主操作
     * @param map
     * @return
     */
    List<StatusInfosBo> listMainOps(Map map);

    /**
     * 获取子操作
     * @param map
     * @return
     */
    List<DetailInfosBo> listSubOps(Map map);

}