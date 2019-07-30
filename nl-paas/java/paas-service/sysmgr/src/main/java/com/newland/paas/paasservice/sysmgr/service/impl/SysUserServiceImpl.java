/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.advice.request.SessionDetail;
import com.newland.paas.common.util.Json;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleEx;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantRoleBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserAllInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserRO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserDeptInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserGroupInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserTenantInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserWorkInfoBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.TenantUserVO;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.common.SysUserStatusConsts;
import com.newland.paas.paasservice.sysmgr.error.SysUserError;
import com.newland.paas.paasservice.sysmgr.service.SysUserService;
import com.newland.paas.paasservice.sysmgr.vo.UserLoginTimesVO;
import com.newland.paas.sbcommon.codeid.CodeIdUtil;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.constants.RedisKeyConstants;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述 用户管理
 *
 * @author linkun
 * @created 2018年6月25日 下午4:23:06
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    private static final Log LOGGER = LogFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Integer AMOUNT = 24;

    private static final Integer BOUND = 500;

    private Random random = new Random();

    /**
     * 描述
     *
     * @param account
     * @return
     * @author linkun
     * @created 2018年6月25日 下午4:23:33
     */
    @Override
    public SysUser getUserByAccount(String account) {
        SysUser user = null;
        SysUser condition = new SysUser();
        condition.setAccount(account);
        List<SysUser> users = sysUserMapper.selectBySelective(condition);
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    /**
     * 描述 根据用户id获取用户对应的租户列表
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月25日 下午7:03:27
     */
    @Override
    public List<UserTenantInfoBO> getTenantInfosByUserId(Long userId) {
        return sysUserMapper.getTenantInfosByUserId(userId);
    }

    /**
     * 描述 根据id获取租户信息
     *
     * @param tenantId
     * @return
     * @author linkun
     * @created 2018年6月25日 下午7:42:41
     */
    @Override
    public SysTenant getTenantById(Long tenantId) {
        return sysTenantMapper.selectByPrimaryKey(tenantId);
    }

    /**
     * 描述 查询用户总数
     *
     * @return
     * @author linkun
     * @created 2018年6月26日 下午6:03:51
     * @see com.newland.paas.paasservice.sysmgr.service.SysUserService#userCount()
     */
    @Override
    public int userCount() {
        return sysUserMapper.countBySelective(null);
    }

    /**
     * 描述 在线用户数
     *
     */
    @Override
    public int onlineUserCount() {
        return getSessionList().size();
    }

    /**
     * 描述 一段时间创建的用户数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author linkun
     * @created 2018年6月26日 下午6:40:54
     */
    @Override
    public int userCount(String beginDate, String endDate) {
        return sysUserMapper.userCount(beginDate, endDate);
    }

    @Override
    public List<SessionDetail> getSessionList() {
        List<SessionDetail> result = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(RedisKeyConstants.REDIS_PAAS_SESSION + "*");
        for (String string : keys) {
            String json = redisTemplate.opsForValue().get(string);
            SessionDetail sessionDetail = Json.toObject(json, SessionDetail.class);
            result.add(sessionDetail);
        }
        return result;
    }

    @Override
    public List<UserLoginTimesVO> userLoginInfo() {
        List<UserLoginTimesVO> userLoginTimesVOList = new LinkedList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -AMOUNT);
        int index = 0;
        while (index < AMOUNT) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String time = simpleDateFormat.format(calendar.getTime());
            Integer userCount = random.nextInt(BOUND);
            UserLoginTimesVO userLoginTimesVO = new UserLoginTimesVO(time, userCount);
            userLoginTimesVOList.add(userLoginTimesVO);
            index++;
        }
        return userLoginTimesVOList;
    }

    /**
     * 描述
     *
     * @param userId
     * @return
     * @author linkun
     * @created 2018年7月4日 上午11:17:35
     * @see com.newland.paas.paasservice.sysmgr.service.SysUserService#getGroupsByUserId(java.lang.Long)
     */
    @Override
    public List<UserGroupInfoBO> getGroupsByUserId(Long userId) {
        return sysUserMapper.getGroupsByUserId(userId);
    }

    /**
     * 描述
     *
     * @param userId
     * @param groupId
     * @return
     * @author linkun
     * @created 2018年7月4日 下午1:46:40
     * @see com.newland.paas.paasservice.sysmgr.service.SysUserService#isUserInGroup(java.lang.Long, java.lang.Long)
     */
    @Override
    public boolean isUserInGroup(Long userId, Long groupId) {
        SysGroupUserReqBo params = new SysGroupUserReqBo();
        params.setUserId(userId);
        params.setGroupId(groupId);
        params.setDelFlag(SysMgrConstants.IS_DELETE_FALSE);
        return sysGroupUserMapper.countBySelective(params) > 0;
    }

    /**
     * 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月5日 下午4:36:33
     */
    @Override
    public List<UserWorkInfoBO> getWorkInfosByUserId(Long userId) {
        return sysUserMapper.getWorkInfosByUserId(userId);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#selectBySelective(java.lang.Object)
     */
    @Override
    public List<SysUser> selectBySelective(SysUser params) {
        return sysUserMapper.selectBySelective(params);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#selectByPrimaryKey(java.lang.Long)
     */
    @Override
    public SysUser selectByPrimaryKey(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#deleteByPrimaryKey(java.lang.Long)
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysUserMapper.deleteByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#deleteBySelective(java.lang.Object)
     */
    @Override
    public int deleteBySelective(SysUser params) {
        return sysUserMapper.deleteBySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#insert(java.lang.Object)
     */
    @Override
    public int insert(SysUser params) {
        return sysUserMapper.insert(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#insertSelective(java.lang.Object)
     */
    @Override
    public int insertSelective(SysUser params) {
        return sysUserMapper.insertSelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#countBySelective(java.lang.Object)
     */
    @Override
    public int countBySelective(SysUser params) {
        return sysUserMapper.countBySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#updateByPrimaryKeySelective(java.lang.Object)
     */
    @Override
    public int updateByPrimaryKeySelective(SysUser params) {
        return sysUserMapper.updateByPrimaryKeySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:45:25
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#updateByPrimaryKey(java.lang.Object)
     */
    @Override
    public int updateByPrimaryKey(SysUser params) {
        return sysUserMapper.updateByPrimaryKey(params);
    }

    /**
     * 描述
     *
     * @param ids
     * @return
     * @author linkun
     * @created 2018年7月9日 下午3:02:47
     * @see com.newland.paas.paasservice.sysmgr.service.SysUserService#getSysUsersByIds(java.lang.String[])
     */
    @Override
    public List<SysUser> getSysUsersByIds(String[] ids) {
        return sysUserMapper.getSysUsersByIds(ids);
    }

    /**
     * 描述
     *
     * @param user
     * @return
     * @author linkun
     * @created 2018年7月9日 下午4:21:32
     */
    @Override
    public List<TenantUserBO> getSysUsers(TenantUserRO user) {
        return sysUserMapper.getUsers(user);
    }

    /**
     * 保存用户信息
     *
     * @param sysUser
     * @param isTongBu 是否是后台同步用户信息
     */
    @Override
    public void saveSysUser(SysUser sysUser, Integer isTongBu) throws ApplicationException {
        Long userId = sysUser.getUserId();
        String account = sysUser.getAccount();

        if (StringUtils.isNotEmpty(account)) {
            // 账号不能重复
            SysUser user = new SysUser();
            user.setUserId(sysUser.getUserId());
            user.setAccount(sysUser.getAccount());
            Integer count = sysUserMapper.countDupSysUser(user);
            if (count != null && count.intValue() > 0) {
                throw new ApplicationException(SysUserError.DUP_SYSUSER_ACCOUNT_ERROR);
            }
        }

        if (userId == null) {
            if (isTongBu != null) {
                SysUser user = null;
                SysUser condition = new SysUser();
                condition.setAccount("yyadmin");
                List<SysUser> users = sysUserMapper.selectBySelective(condition);
                if (users != null && !users.isEmpty()) {
                    user = users.get(0);
                    sysUser.setCreatorId(user.getUserId());
                }
            } else {
                sysUser.setCreatorId(RequestContext.getUserId());
            }
            sysUserMapper.insertSelective(sysUser);
        } else {
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
        }
    }

    /**
     * 删除用户信息
     *
     * @param sysUser
     */
    @Override
    public void delSysUser(SysUser sysUser) {
        sysUser.setDelFlag((short) 1);
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 分页查询用户
     *
     * @param sysUser
     * @param pageInfo
     * @return
     */
    @Override
    public ResultPageData pageQuerySysUser(SysUser sysUser, PageInfo pageInfo) {
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######pageQuerySysUser sysUser info is {0}, page info is {1}######",
                        JSONObject.toJSON(sysUser), JSONObject.toJSON(pageInfo)));
        Page<SysUser> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysUser> list = sysUserMapper.listSysUser(sysUser);
        pageInfo.setTotalRecord(page.getTotal());
        ResultPageData resultPageData = new ResultPageData(list, pageInfo);
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######pageQuerySysUser result is {0}######", JSONObject.toJSON(resultPageData)));
        return resultPageData;
    }

    /**
     * 获取用户所有信息
     *
     * @param sysUser
     * @return
     */
    @Override
    public SysUserAllInfoBO getSysUserAllInfo(SysUser sysUser) {
        Long userId = sysUser.getUserId();
        SysUserAllInfoBO allInfoBO = new SysUserAllInfoBO();

        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        BeanUtils.copyProperties(user, allInfoBO);

        List<SysTenantRoleBo> sysTenants = sysTenantMemberMapper.listSysTenantByUserId(userId);
        allInfoBO.setSysTenantList(sysTenants);

        List<SysGroupRoleEx> sysGroups = sysGroupUserMapper.listSysGroupByUserId(userId);
        allInfoBO.setSysGroupList(sysGroups);

        List<SysRole> sysRoles = sysRoleMapper.listSysRoleByUserId(userId);
        allInfoBO.setSysRoleList(sysRoles);

        return allInfoBO;
    }

    /**
     * 描述
     *
     * @return
     * @author caifeitong
     * @created 2018年7月23日 下午8:13:32
     */
    @Override
    public List<UserDeptInfoBO> getUserDeptList() {
        return sysUserMapper.findDeptAll();
    }

    /**
     * 获取待分配角色的用户信息(用户ID,用户名,部门) 描述
     *
     * @return
     * @author caifeitong
     * @created 2018年7月25日 下午7:31:08
     */
    @Override
    public List<SysUser> getUnRoleSysUser(BasicRequestContentVo<SysUser> vo) {
        SysUser sysUser = new SysUser();
        if (vo != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LogProperty.LOGCONFIG_DEALID, "params = " + vo);
            }
            if (vo.getParams() != null) {
                sysUser.setUsername(vo.getParams().getUsername());
                sysUser.setDept(vo.getParams().getDept());
            }
        }
        return sysUserMapper.getUnRoleSysUser(sysUser);
    }

    /**
     * 查询具体已经分配某个角色的所有用户信息(用户ID，用户名，部门)
     *
     * @return
     * @author caifeitong
     * @created 2018年7月25日 下午3:04:08
     */
    @Override
    public List<SysUser> getSysRoleUser(Long roleId) {
        return sysUserMapper.getSysRoleUser(roleId);
    }

    @Override
    public TenantUserVO getUserInfoByAccountAndTenantCode(String account, String tenantCode) {
        // 查询用户信息
        SysUser sysUserParam = new SysUser();
        sysUserParam.setAccount(account);
        List<SysUser> sysUsers = sysUserMapper.selectBySelective(sysUserParam);
        if (sysUsers.isEmpty()) {
            throw new SystemException(new PaasError("2-18-00101", "用户不存在"));
        }
        SysUser sysUser = sysUsers.get(0);
        if (StringUtils.isNotEmpty(sysUser.getStatus())) {
            if (!sysUser.getStatus().equalsIgnoreCase(SysUserStatusConsts.NOTLOGIN.getValue())
                    && !sysUser.getStatus().equalsIgnoreCase(SysUserStatusConsts.ENABLED.getValue())) {
                throw new SystemException(new PaasError("2-18-00104", "用户不可登录"));
            }
        }
        TenantUserVO user = new TenantUserVO();
        user.setAccount(sysUser.getAccount());
        user.setUserName(sysUser.getUsername());
        user.setUserId(sysUser.getUserId());

        String tenantId = CodeIdUtil.getByCode(CodeIdUtil.SYS_TENANT, tenantCode);
        if (StringUtils.isEmpty(tenantId)) {
            throw new SystemException(new PaasError("2-18-00102", "租户code不存在对应的租户!"));
        }

        SysTenantMember sysTenantMemberParam = new SysTenantMember();
        sysTenantMemberParam.setUserId(sysUser.getUserId());
        sysTenantMemberParam.setTenantId(Long.valueOf(tenantId));
        List<SysTenantMember> sysTenantMembers = sysTenantMemberMapper.selectBySelective(sysTenantMemberParam);
        if (sysTenantMembers.isEmpty()) {
            throw new SystemException(new PaasError("2-18-00103", "用户不归属于此租户!"));
        }
        user.setTenantId(sysTenantMembers.get(0).getTenantId());
        return user;
    }
}
