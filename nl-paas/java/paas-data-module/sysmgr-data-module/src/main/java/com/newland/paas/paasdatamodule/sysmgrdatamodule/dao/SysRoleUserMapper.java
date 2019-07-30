package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;

public interface SysRoleUserMapper {
    int countBySelective(SysRoleUserReqBo record);

    int deleteBySelective(SysRoleUserReqBo record);
    
    List<SysRoleUserRespBo> selectBySelective(SysRoleUserReqBo record);
    
    SysRoleUserRespBo selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 批量新增
     *
     * @param sysRoleUserList 资源项详情列表
     * @param roleId  角色ID
     * @return
     */
    int batchInsert(@Param("list") List<SysRoleUser> sysRoleUserList, @Param("roleId") Long roleId);

	/**
	 * 描述
	 * @author linkun
	 * @created 2018年8月9日 上午10:58:58
	 * @param tenantId
	 * @return
	 */
	List<SysRoleUserRespBo> getRoleUsersByTenant(@Param("tenantId")Long tenantId);
}