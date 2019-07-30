package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenanMemberBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantRoleBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysTenantMemberMapper {
    int countBySelective(SysTenantMember record);

    int deleteBySelective(SysTenantMember record);

    int deleteByPrimaryKey(Long id);

    int insert(SysTenantMember record);

    int insertSelective(SysTenantMember record);

    List<SysTenantMember> selectBySelective(SysTenantMember record);

    SysTenantMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTenantMember record);

    int updateByPrimaryKey(SysTenantMember record);

    List<SysTenanMemberBO> getMembersByTenant(@Param("tenantId") Long tenantId, @Param("memberName") String memberName);

    /**
     * 查询用户id关联的租户
     * @param userId
     * @return
     */
    List<SysTenantRoleBo> listSysTenantByUserId(@Param("userId") Long userId);

    TenantUserBO selectMemberById(Long id);

    int deleteByTenantId(Long tenantId);
}