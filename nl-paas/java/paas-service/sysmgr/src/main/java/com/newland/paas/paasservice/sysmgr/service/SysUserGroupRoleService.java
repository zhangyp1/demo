package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午4:42:06
 */
public interface SysUserGroupRoleService {

    int groupCount() throws ApplicationException;

    ResultPageData<SysUserGroupRoleRespBo> page(SysUserGroupRoleReqBo params, PageInfo pageInfo)
            throws ApplicationException;

    List<SysUserGroupRoleRespBo> list(SysUserGroupRoleReqBo params) throws ApplicationException;

    int delete(Long id) throws ApplicationException;

    /**
     * 给角色添加用户列表
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月31日 下午3:20:20
     */
    int removeAllAndAddUsers(SysUserGroupRoleReqBo params) throws ApplicationException;

    void addUser(Long groupRoleId, Long userId) throws ApplicationException;

    void removeAllUser(Long groupRoleId) throws ApplicationException;

    void removeUser(Long groupRoleId, Long userId) throws ApplicationException;

    /**
     * 通过工组角色获取用户列表
     * 描述
     *
     * @param groupRoleId
     * @return
     * @author linkun
     * @created 2018年7月31日 下午3:44:49
     */
    List<SysUserGroupRoleRespBo> getUsersByGroupRole(Long groupRoleId) throws ApplicationException;

    /**
     * 通过用户获取角色列表
     * 描述
     *
     * @param userId
     * @return
     * @author linkun
     * @created 2018年7月31日 下午3:45:14
     */
    List<SysUserGroupRoleRespBo> getGroupRolesByUser(Long userId, Long groupId) throws ApplicationException;

    List<SysUserGroupRoleRespBo> getAllUsersByGroup(SysUserGroupRoleReqBo params) throws ApplicationException;

    int deleteBySelective(SysUserGroupRoleReqBo userGroupRoleParams) throws ApplicationException;

}

