package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleEx;
import org.apache.ibatis.annotations.Param;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;

public interface SysGroupUserMapper {
	int countBySelective(SysGroupUserReqBo record);

	int deleteBySelective(SysGroupUserReqBo record);

	List<SysGroupUserRespBo> selectBySelective(SysGroupUserReqBo record);

	SysGroupUserRespBo selectByPrimaryKey(Long id);

	int deleteByPrimaryKey(Long id);

	int insert(SysGroupUser record);

	int insertSelective(SysGroupUser record);

	int updateByPrimaryKeySelective(SysGroupUser record);

	int updateByPrimaryKey(SysGroupUser record);

	/**
	 * 查询用户id关联的工组
	 * 
	 * @param userId
	 * @return
	 */
	List<SysGroupRoleEx> listSysGroupByUserId(@Param("userId") Long userId);

	/**
	 * 描述
	 * 
	 * @author linkun
	 * @created 2018年8月1日 下午3:21:01
	 * @param groupId
	 * @return
	 */
	List<SysGroupUserRespBo> getUsersByGroup(@Param("groupId") Long groupId);

	/**
	 * 描述
	 * 
	 * @author linkun
	 * @created 2018年8月1日 下午3:21:12
	 * @param userId
	 * @return
	 */
	List<SysGroupUserRespBo> getGroupsByUser(@Param("userId") Long userId);

	/**
	 * 描述
	 * 
	 * @author linkun
	 * @created 2018年8月1日 下午3:21:18
	 * @param tenantId
	 * @param groupId
	 * @return
	 */
	List<SysGroupUserRespBo> getAllUsersByTenant(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId,
			@Param("userNameLike") String userNameLike);

	/**
	 * 描述
	 * 
	 * @author linkun
	 * @created 2018年8月1日 下午3:21:25
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	List<SysGroupUserRespBo> getAllGroupsByTenant(@Param("tenantId") Long tenantId, @Param("userId") Long userId);


	/**
	 * 查询用户工组管理员数量
	 * @param groupIds
	 * @param userId
	 * @return
	 */
	List<SysGroupUserRespBo> getGroupAdmins(@Param("groupIds") List<Long> groupIds, @Param("userId") Long userId);

}