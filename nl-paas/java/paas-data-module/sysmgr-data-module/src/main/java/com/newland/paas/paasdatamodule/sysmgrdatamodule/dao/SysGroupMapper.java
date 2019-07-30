package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGroupMapper {

    int countBySelective(SysGroupReqBo record);

    int groupCount();

    int deleteBySelective(SysGroupReqBo record);

    List<SysGroup> selectBySelective(SysGroupReqBo record);

    List<SysGroupRespBo> selectGroupRespBoBySelective(SysGroupReqBo record);

    SysGroup selectByPrimaryKey(Long groupId);

    int deleteByPrimaryKey(Long groupId);

    int insert(SysGroup record);

    int insertSelective(SysGroup record);

    int updateByPrimaryKeySelective(SysGroup record);

    int updateByPrimaryKey(SysGroup record);

    /**
     * 查找所有工组信息（ID, NAME）
     * @return
     */
    List<SysGroup> findBaseAll();

    /**
     * 描述
     * @param groupId
     * @return
     */
    List<SysGroupForCategoryRespBo> getCategorysByGroup(@Param("groupId") Long groupId);

    /**
     * 根据工组ID，获取子工组
     * @param id
     * @return
     */
    List<SysGroup> getSubGroupById(Long id);

    /**
     * 根据成员ID查询他的所属工组列表
     * @param userId
     * @return List
     */
    List<SysGroupForMemberRespBO> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询不是某个成员所属工组列表数据
     * @param userId    成员Id
     * @param groupName 工组名称
     * @return List
     */
    List<SysGroupForMemberRespBO> selectOtherByUserId(@Param("userId") Long userId, @Param("groupName") String groupName);

    /**
     * 根据用户ID、工组ID查询用户的工组角色列表
     * @param userId  用户ID
     * @param groupId 工组ID
     * @return List
     */
    List<SysGroupRoleBO> selectRoleByParams(@Param("userId") Long userId, @Param("groupId") Long groupId);

    /**
     * 根据成员ID删除该成员所有所属工组
     * @param userId 成员ID
     * @return int
     */
    int deleteByUserId(Long userId);

    /**
     * 查询所有工组列表
     * @return List
     */
    List<SysGroupForMemberRespBO> selectAll(@Param("tenantId") Long tenantId);

    /**
     * 根据成员ID查询所属工组ID列表
     * @param userId
     * @return List
     */
    List<Long> selectGroupId(@Param("userId") Long userId,@Param("tenantId") Long tenantId);

    int updateRole(SysUserGroupRole sysUserGroupRole);

    List<BaseTreeDo> getSysGroupTree(SysGroup sysGroup);

    List<BaseTreeDo> listSubSysGroup(SysGroup sysGroup);

    List<SysGroup> selectGroupByGroupIds(List<Long> groupIds);

    List<SysGroupRespBo> selectGroupRespBoByGroupIds(@Param("groupIds") List<Long> groupIds);

    /**
     * 查询所有父工组
     * @param id
     * @return
     */
    List<SysGroup> getParentGroupById(Long id);

    /**
     * 查找所有工组信息（ID, NAME）,NAME=租户名_工组名
     * @return
     */
    List<SysGroup> findAllTenantGroup();

    /**
     * 获取当前层级
     * @param id
     * @return
     */
    int getCurLevel(Long id);

    /**
     * 根据用户和租户查询他的所属工组列表
     * @param userId
     * @return List
     */
    List<SysGroupForMemberRespBO> selectByUserIdAndTenantId(@Param("userId") Long userId,
                                                            @Param("tenantId") Long tenantId);

}