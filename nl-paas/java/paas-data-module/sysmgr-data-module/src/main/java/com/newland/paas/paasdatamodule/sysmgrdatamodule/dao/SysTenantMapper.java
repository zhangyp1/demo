package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;

public interface SysTenantMapper {
    int countBySelective(SysTenant record);

    int deleteBySelective(SysTenant record);

    int deleteByPrimaryKey(Long id);

    int insert(SysTenant record);

    int insertSelective(SysTenant record);

    List<SysTenant> selectBySelective(SysTenant record);

    SysTenant selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTenant record);

    int updateByPrimaryKey(SysTenant record);

    /**
     * 查找所有租户信息（ID, TENANT_NAME）
     * @return
     */
    List<SysTenant> findBaseAll();

    /**
     * 统计租户名称相同条数
     * 描述
     * @param tenantName
     * @param tenantId
     * @return
     */
    int countByTenantName(@Param("tenantName") String tenantName, @Param("tenantId") Long tenantId);

    /**
     * 根据ids获取对应的租户列表
     * 描述
     * @param ids
     * @return
     */
    List<SysTenant> getSysTenantsByIds(@Param("ids") String[] ids);

    /**
     * 获取租户列表
     * 描述
     * @return
     */
    List<SysTenantBo> selectTenantBo(@Param("tenantName") String tenantName,
                                    @Param("createDateB") String createDateB,
                                    @Param("createDateE") String createDateE,
                                    @Param("isAllTenants")Boolean isAllTenants,
                                    @Param("includePlatformMgr")Boolean includePlatformMgr);

    SysTenantBo selectTenantBoById(@Param("tenantId") Long tenantId);

    List<TenantUserBO> getSysTenantUsers(@Param("tenantId") Long tenantId, @Param("isAdmin") Short isAdmin, @Param("userName") String userName);

    List<TenantUserBO> getNoMemberUsers(@Param("tenantId") Long tenantId,@Param("userNameLike")String userNameLike);

	/**
	 * 描述
	 * @author linkun
	 * @created 2018年8月20日 上午9:57:56
	 * @param userId
	 */
	List<SysTenant> getAllTenantsCanJoin(@Param("userId")Long userId);

    /**
     * 获取用户添加的租户
     * @return
     */
    List<SysTenant> getUserAddSysTenants();

}