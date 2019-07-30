package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleRespBo;

public interface SysUserGroupRoleMapper {
	int countBySelective(SysUserGroupRoleReqBo record);

	int deleteBySelective(SysUserGroupRoleReqBo record);

	List<SysUserGroupRoleRespBo> selectBySelective(SysUserGroupRoleReqBo record);

	SysUserGroupRoleRespBo selectByPrimaryKey(Long id);

	int deleteByPrimaryKey(Long id);

	int insert(SysUserGroupRole record);

	int insertSelective(SysUserGroupRole record);

	int updateByPrimaryKeySelective(SysUserGroupRole record);

	int updateByPrimaryKey(SysUserGroupRole record);

	List<SysUserGroupRoleRespBo> getUsersByGroupRole(@Param("groupRoleId") Long groupRoleId);

	List<SysUserGroupRoleRespBo> getGroupRolesByUser(@Param("userId") Long userId, @Param("groupId") Long groupId);

	List<SysUserGroupRoleRespBo> getAllUsersByGroup(@Param("groupId") Long tenantId,
			@Param("groupRoleId") Long groupRoleId, @Param("userNameLike") String userNameLike);

	List<SysUserGroupRoleRespBo> getAllRolesByGroup(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

	int deleteByParams(@Param("userId") Long userId, @Param("groupId") Long groupId);
}