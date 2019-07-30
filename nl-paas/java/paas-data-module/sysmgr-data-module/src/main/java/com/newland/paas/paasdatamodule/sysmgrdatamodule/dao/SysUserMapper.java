package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserRO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserDeptInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserGroupInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserTenantInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserWorkInfoBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int countBySelective(SysUser record);

    int deleteBySelective(SysUser record);

    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUser> selectBySelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByAccountSelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    
    /**
     * 获取用户对应的租户列表
     * 描述
     * @author linkun
     * @created 2018年6月25日 下午7:21:26
     * @param userId
     * @return
     */
    List<UserTenantInfoBO> getTenantInfosByUserId(Long userId);
    /**
     * 获取用户对应的租户工单列表
     * 描述
     * @author linkun
     * @created 2018年7月5日 下午4:34:47
     * @param userId
     * @return
     */
    List<UserWorkInfoBO> getWorkInfosByUserId(Long userId);
    /**
     * 获取用户对应的群组列表
     * 描述
     * @author linkun
     * @created 2018年7月4日 上午11:14:23
     * @param userId
     * @return
     */
    List<UserGroupInfoBO> getGroupsByUserId(Long userId);

    /**
     * 描述
     * @author linkun
     * @created 2018年6月26日 下午6:41:35
     * @param beginDate
     * @param endDate
     * @return
     */
    int userCount(@Param("beginDate")String beginDate,@Param("endDate") String endDate);
    
    /**
     * 查找所有用户信息（id，name）
     * @return
     */
    List<SysUser> findBaseAll();
    
    List<SysUser> getSysUsersByIds(String[] ids);
    
    List<TenantUserBO> getUsers(TenantUserRO user);

    Integer countDupSysUser(SysUser sysUser);

    List<SysUser> listSysUser(SysUser sysUser);

    /**
     * 查找所有用户归属的部门(部门ID, 部门名称)
     * @return
     */
    public List<UserDeptInfoBO> findDeptAll();

    /**
     * 查找未分配角色的用户列表
     * @return
     */
    public List<SysUser> getUnRoleSysUser(SysUser sysUser);

    /**
     * 查找具体某个分配角色的用户列表
     * @return
     */
    public List<SysUser> getSysRoleUser(Long roleId);


    List<SysUser> getUserByTenant(@Param(value = "tenantId") Long tenantId);
}