/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.advice.request.SessionDetail;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserAllInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserRO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserDeptInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserGroupInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserTenantInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserWorkInfoBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.paasservice.sysmgr.vo.UserLoginTimesVO;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月25日 下午4:48:43
 */
public interface SysUserService extends BaseService<SysUser> {
    /**
     * 描述 根据用户的登录名查询用户对象
     *
     * @param account=
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月25日 下午4:56:15
     */
    SysUser getUserByAccount(String account) throws ApplicationException;

    /**
     * 根据用户id获取租户列表 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月4日 上午11:16:02
     */
    List<UserTenantInfoBO> getTenantInfosByUserId(Long userId) throws ApplicationException;

    /**
     * 根据用户id获取租户工单列表 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月5日 下午4:36:04
     */
    List<UserWorkInfoBO> getWorkInfosByUserId(Long userId) throws ApplicationException;

    /**
     * 根据用户id获取用户工组列表 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月4日 上午11:16:19
     */
    List<UserGroupInfoBO> getGroupsByUserId(Long userId) throws ApplicationException;

    /**
     * 描述 根据id获取租户信息
     *
     * @param tenantId
     * @author linkun
     * @created 2018年6月25日 下午7:41:08
     */
    SysTenant getTenantById(Long tenantId);

    /**
     * 描述 查询总共用户数
     *
     * @return
     * @author linkun
     * @created 2018年6月26日 下午6:03:19
     */
    int userCount();

    /**
     * 描述 在线用户数
     *
     */
    int onlineUserCount();

    /**
     * 描述 查询一段时间创建的用户数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author linkun
     * @created 2018年6月26日 下午6:40:23
     */
    int userCount(String beginDate, String endDate);

    /**
     *
     * @return
     */
    List<UserLoginTimesVO> userLoginInfo();

    /**
     * 判断userId是否在groupId的工组中 描述
     *
     * @param userId
     * @param groupId
     * @return
     * @author linkun
     * @created 2018年7月4日 下午1:45:48
     */
    boolean isUserInGroup(Long userId, Long groupId);

    /**
     *
     * @param ids
     * @return
     */
    List<SysUser> getSysUsersByIds(String[] ids);

    /**
     *
     * @param user
     * @return
     */
    List<TenantUserBO> getSysUsers(TenantUserRO user);

    /**
     * 保存用户信息
     *
     * @param sysUser
     */
    void saveSysUser(SysUser sysUser, Integer isTongBu) throws ApplicationException;

    /**
     * 获取会话列表
     *
     * @return
     */
    List<SessionDetail> getSessionList();

    /**
     * 删除用户信息
     *
     * @param sysUser
     */
    void delSysUser(SysUser sysUser) throws ApplicationException;

    /**
     * 分页查询用户
     *
     * @param sysUser
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    ResultPageData pageQuerySysUser(SysUser sysUser, PageInfo pageInfo) throws ApplicationException;

    /**
     * 获取用户所有信息
     *
     * @param sysUser
     * @return
     */
    SysUserAllInfoBO getSysUserAllInfo(SysUser sysUser);

    /**
     * 查询用户所属的部门列表
     * 描述
     *
     * @return
     * @author caifeitong
     * @created 2018年7月23日 下午7:31:08
     */
    List<UserDeptInfoBO> getUserDeptList();

    /**
     * 获取所有用户信息(用户ID,用户名,部门)
     * 描述
     *
     * @return
     * @author caifeitong
     * @created 2018年7月23日 下午7:31:08
     */
    List<SysUser> getUnRoleSysUser(BasicRequestContentVo<SysUser> vo);

    /**
     * 查询具体已经分配某个角色的所有用户信息(用户ID，用户名，部门)
     *
     * @return
     * @author caifeitong
     * @created 2018年7月25日 下午3:04:08
     */
    List<SysUser> getSysRoleUser(Long roleId);

    /**
     * 根据用户账户和租户code查询用户信息
     *
     * @param account
     * @param tenantCode
     * @return
     */
    TenantUserVO getUserInfoByAccountAndTenantCode(String account, String tenantCode);
}
