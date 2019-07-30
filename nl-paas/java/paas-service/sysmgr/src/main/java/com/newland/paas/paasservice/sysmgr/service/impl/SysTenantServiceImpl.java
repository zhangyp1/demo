/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.datacommonmodule.common.GlbDictConstants;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.controllerapi.harbormgr.vo.RepoReqVo;
import com.newland.paas.paasservice.controllerapi.resmgr.vo.CountResByResTypeVo;
import com.newland.paas.paasservice.controllerapi.svrmgr.vo.SelectSubStaRO;
import com.newland.paas.paasservice.controllerapi.svrmgr.vo.SelectSubStaResVO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.*;
import com.newland.paas.paasservice.sysmgr.async.DepotHelperAsync;
import com.newland.paas.paasservice.sysmgr.common.ResLimitUnitConstants;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysTenantError;
import com.newland.paas.paasservice.sysmgr.error.SysTenantMemberError;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.ApproveBo;
import com.newland.paas.paasservice.sysmgr.vo.SysTenantWithRoleVo;
import com.newland.paas.sbcommon.activiti.ActEngine;
import com.newland.paas.sbcommon.activiti.vo.ExecTaskPropertie;
import com.newland.paas.sbcommon.activiti.vo.ExecTaskVo;
import com.newland.paas.sbcommon.activiti.vo.StartProcessVariable;
import com.newland.paas.sbcommon.activiti.vo.StartProcessVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月27日 下午2:15:29
 */
@Service
public class SysTenantServiceImpl implements SysTenantService {

    private static final Log LOG = LogFactory.getLogger(SysTenantServiceImpl.class);
    public static final String GROUP_ID = "groupId";
    public static final String TASK_ID = "taskId:";
    public static final String COMMENT = "comment";
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysTenantMemberService sysTenantMemberService;
    @Autowired
    private SysTenantOperateMapper sysTenantOperateMapper;
    @Autowired
    private DepotHelperAsync depotHelperAsync;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysTenantLimitMapper sysTenantLimitMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Autowired
    private SysCategoryService sysCategoryService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private MicroservicesProperties microservicesProperties;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private ActEngine actEngine;

    private static final Integer TIMEOUT = 60000;

    // temp
    @Value("${depot.ftp.username}")
    private String ftpUsername;
    @Value("${depot.ftp.password}")
    private String ftpPassword;
    @Value("${resource.image.host}")
    private String imageHost;
    @Value("${resource.ast.host}")
    private String astHost;
    @Value("${resource.ast.homePath}")
    private String astHomePath;



    private static final String VALUE = "value";

    /**
     * 描述
     *
     * @return
     * @author linkun
     * @created 2018年6月26日 下午4:29:20
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#tenantCount()
     */
    @Override
    public int tenantCount() {
        SysTenant params = new SysTenant();
        params.setDelFlag(SysMgrConstants.IS_DELETE_FALSE);
        params.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        return sysTenantMapper.countBySelective(params);
    }

    /**
     * 描述
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:24:02
     */
    @Transactional
    @Override
    public SysTenant addSysTenant(SysTenant sysTenant) throws ApplicationException {
        int count = sysTenantMapper.insert(sysTenant);
        if (count == 0) {
            throw new ApplicationException(SysTenantError.ADD_SYS_TENANT_ERROR);
        }
        return sysTenant;
    }

    /**
     * 描述
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:37:10
     */
    @Transactional
    @Override
    public SysTenant updateSysTenant(SysTenant sysTenant) throws ApplicationException {
        int count = sysTenantMapper.updateByPrimaryKeySelective(sysTenant);
        if (count == 0) {
            throw new ApplicationException(SysTenantError.UPDATE_SYS_TENANT_ERROR);
        }
        return sysTenant;
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午2:55:46
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#deleteSysTenant(java.lang.Long)
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int deleteSysTenant(Long id) throws ApplicationException {
        if (id.equals(SysMgrConstants.TENANT_ID_ADMIN)
                || id.equals(SysMgrConstants.TENANT_ID_YUNYIN)
                || id.equals(SysMgrConstants.TENANT_ID_YUNWEI)) {
            throw new ApplicationException(SysTenantError.CAN_NOT_DELETE_SPECIAL_TENANT);
        }

        // 判断该租户下是否有工组
        SysGroupReqBo sysGroupReq = new SysGroupReqBo();
        sysGroupReq.setTenantId(id);
        List<SysGroup> sysGroups = sysGroupMapper.selectBySelective(sysGroupReq);
        if (!CollectionUtils.isEmpty(sysGroups)) {
            // 判断该租户下是否只有根工组，则允许删除
            if (!(sysGroups.size() == 1
                    && SysMgrConstants.ROOT_GROUP_PARENT_ID.equals(sysGroups.get(0).getParentGroupId()))) {
                SysTenantError.CAN_NOT_DELETE_SYS_TENANT.setMsgArguments("工组");
                throw new ApplicationException(SysTenantError.CAN_NOT_DELETE_SYS_TENANT);
            }
        }

        // 判断该租户下是否有成员
        List<TenantUserVO> membersByTenant = sysTenantMemberService.getMembersByTenant(id, null);
        if (!CollectionUtils.isEmpty(membersByTenant)) {
            // 判断该租户成员是否只有当前用户，则允许删除
            if (membersByTenant.size() > 1) {
                SysTenantError.CAN_NOT_DELETE_SYS_TENANT.setMsgArguments("成员");
                throw new ApplicationException(SysTenantError.CAN_NOT_DELETE_SYS_TENANT);
            }
        }

        // 判断该租户下是否有资源
        Map<String, Object> params1 = new HashMap<>();
        params1.put("tenantId", id);
        BasicResponseContentVo<List<CountResByResTypeVo>> resources = restTemplateUtils
                .getForTokenEntity(microservicesProperties.getResmgr(), "v1/resources/statistic/countByResType",
                        RequestContext.getToken(), params1,new ParameterizedTypeReference<BasicResponseContentVo<List<CountResByResTypeVo>>>(){
                        });
        if(!CollectionUtils.isEmpty(resources.getContent())) {
            SysTenantError.CAN_NOT_DELETE_SYS_TENANT.setMsgArguments("资源");
            throw new ApplicationException(SysTenantError.CAN_NOT_DELETE_SYS_TENANT);
        }

        // 判断该租户下是否有订阅服务
        BasicPageRequestContentVo<SelectSubStaRO> params2 = new BasicPageRequestContentVo<>();
        SelectSubStaRO selectSubStaRO = new SelectSubStaRO();
        selectSubStaRO.setTenantId(id);
        params2.setParams(selectSubStaRO);
        BasicResponseContentVo<Map<String, List<SelectSubStaResVO>>> result = restTemplateUtils
                .postForTokenEntity(microservicesProperties.getSvrmgr(), "v1/tenant/service_sub_statistics",
                        RequestContext.getToken(),
                        params2,new ParameterizedTypeReference<BasicResponseContentVo<Map<String, List<SelectSubStaResVO>>>>(){
                        });
        Map<String, List<SelectSubStaResVO>> serviceSubs = result.getContent();
        List<SelectSubStaResVO> selectSubStaResVOS = serviceSubs.get("list");
        if(!CollectionUtils.isEmpty(selectSubStaResVOS)) {
            for (SelectSubStaResVO selectSubStaResVO: selectSubStaResVOS) {
                // 订阅中次数
                Integer subscribingNum = (Integer) selectSubStaResVO.getSubscribes().get("subscribing");
                // 已订阅次数
                Integer subscribedNum = (Integer) selectSubStaResVO.getSubscribes().get("subscribed");
                if (subscribingNum != 0 || subscribedNum != 0) {
                    SysTenantError.CAN_NOT_DELETE_SYS_TENANT.setMsgArguments("已订阅的服务");
                    throw new ApplicationException(SysTenantError.CAN_NOT_DELETE_SYS_TENANT);
                }
            }
        }

        // 1.删除用户角色关系
        SysRoleUserReqBo sysRoleUserReqBo = new SysRoleUserReqBo();
        sysRoleUserReqBo.setTenantId(id);
        sysRoleUserMapper.deleteBySelective(sysRoleUserReqBo);
        // 2.删除租户成员关系
        SysTenantMember sysTenantMember = new SysTenantMember();
        sysTenantMember.setTenantId(id);
        sysTenantMemberMapper.deleteBySelective(sysTenantMember);
        // 3.删除工组用户关系
        SysGroupUserReqBo sysGroupUserReqBo = new SysGroupUserReqBo();
        sysGroupUserReqBo.setTenantId(id);
        sysGroupUserMapper.deleteBySelective(sysGroupUserReqBo);
        // 4.删除工组
        SysGroupReqBo sysGroupReqBo = new SysGroupReqBo();
        sysGroupReqBo.setTenantId(id);
        sysGroupMapper.deleteBySelective(sysGroupReqBo);
        // 5.释放资源配额
        SysTenantLimit sysTenantLimit = new SysTenantLimit();
        sysTenantLimit.setTenantId(id);
        sysTenantLimitMapper.deleteBySelective(sysTenantLimit);
        // 6.删除租户
        SysTenant sysTenant = sysTenantMapper.selectByPrimaryKey(id);
        sysTenant.setDelFlag(SysMgrConstants.IS_DELETE_TRUE);
        int count = sysTenantMapper.updateByPrimaryKeySelective(sysTenant);
        if (count == 0) {
            throw new ApplicationException(SysTenantError.DELETE_SYS_TENANT_ERROR);
        }
        // 7.删除租户仓库
        depotHelperAsync.clearSysTenantDepot(sysTenant, RequestContext.getToken());

        return count;
    }

    /**
     * 描述
     *
     * @param sysTenantVo
     * @param pageInfo
     * @return
     * @author linkun
     * @created 2018年6月27日 下午3:01:34
     */
    @Override
    public ResultPageData<SysTenant> querySysTenants(SysTenant sysTenantVo, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysTenant> rrs = sysTenantMapper.selectBySelective(sysTenantVo);
        return new ResultPageData<>(rrs);
    }

    /**
     * 描述
     *
     * @param tenantName
     * @param tenantId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月28日 下午4:23:21
     */
    @Override
    public int countByTenantName(String tenantName, Long tenantId) throws ApplicationException {
        return sysTenantMapper.countByTenantName(tenantName, tenantId);
    }

    /**
     * 描述
     *
     * @param ids
     * @return
     * @author linkun
     * @created 2018年7月5日 上午10:33:07
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#getSysTenantsByIds(java.util.List)
     */
    @Override
    public List<SysTenant> getSysTenantsByIds(String[] ids) {
        return sysTenantMapper.getSysTenantsByIds(ids);
    }

    /**
     * 描述
     *
     * @return
     * @author linkun
     * @created 2018年7月5日 下午2:58:33
     */
    @Override
    public ResultPageData<SysTenantBo> getSysTenantsByPage(SysTenantReqVo params, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysTenantBo> rrs = sysTenantMapper.selectTenantBo(params.getTenantName(), params.getCreateDateB(),
                params.getCreateDateE(), params.getIsAllTenants(), params.getIncludePlatformMgr());
        return new ResultPageData<>(rrs);
    }

    @Override
    public List<SysTenantBo> listSysTenantBo(SysTenantReqVo params) {
        return sysTenantMapper.selectTenantBo(params.getTenantName(), params.getCreateDateB(),
                params.getCreateDateE(), params.getIsAllTenants(), params.getIncludePlatformMgr());
    }

    @Override
    public SysTenantBo getSysTenantBo(Long tenantId) {
        return sysTenantMapper.selectTenantBoById(tenantId);
    }

    @Override
    public SysTenantWithRoleVo getSysTenantWithRoleVo(Long tenantId, Long userId) {
        SysTenantWithRoleVo sysTenantWithRoleVo = null;
        SysTenantBo sysTenantBo = this.getSysTenantBo(tenantId);
        if (sysTenantBo != null) {
            sysTenantWithRoleVo = new SysTenantWithRoleVo();
            BeanUtils.copyProperties(sysTenantBo, sysTenantWithRoleVo);
            List<SysRole> sysRoleList = sysRoleService.listSysRole(tenantId, userId);
            sysTenantWithRoleVo.setSysRoleList(sysRoleList);
        }
        return sysTenantWithRoleVo;
    }

    /**
     *  添加租户和成员信息
     * 描述
     *
     * @param sysTenantReqBo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 上午11:25:02
     */
    @Transactional
    @Override
    public SysTenant addSysTenantAndMembers(SysTenantReqBo sysTenantReqBo) throws ApplicationException {
        // 新增租户
        SysTenant sysTenantInfo = this.insertSysTenant(sysTenantReqBo);
        // 新增根工组
        SysGroup rootGroup = new SysGroup();
        rootGroup.setGroupName("ROOT");
        rootGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
        rootGroup.setTenantId(sysTenantInfo.getId());
        rootGroup.setDescription("根工组");
        rootGroup.setCreatorId(RequestContext.getUserId());
        rootGroup.setWholePath("/ROOT/");
        sysGroupMapper.insertSelective(rootGroup);
        // 新增根系统分组
        SysCategoryBo rootSysCategoryBo = new SysCategoryBo();
        rootSysCategoryBo.setSysCategoryName(sysTenantReqBo.getTenantName() + "系统");
        rootSysCategoryBo.setSysCategoryPid(-1L);
        rootSysCategoryBo.setSysCategoryCode(UUID.randomUUID().toString().replace('-', '0').toLowerCase().substring(0, 12));
        rootSysCategoryBo.setGroupId(rootGroup.getGroupId());
        rootSysCategoryBo.setCpuQuota(0F);
        rootSysCategoryBo.setMemoryQuota(0F);
        rootSysCategoryBo.setStorageQuota(0F);
        rootSysCategoryBo.setTenantId(sysTenantInfo.getId());
        sysCategoryService.saveSysCategory(rootSysCategoryBo);
        // 新增工组用户关系
        SysGroupUser sysGroupUser = new SysGroupUser();
        sysGroupUser.setUserId(RequestContext.getUserId());
        sysGroupUser.setTenantId(sysTenantInfo.getId());
        sysGroupUser.setGroupId(rootGroup.getGroupId());
        sysGroupUser.setDelFlag(SysMgrConstants.IS_DELETE_FALSE);
        sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
        sysGroupUserMapper.insertSelective(sysGroupUser);
        // 添加用户角色关系
        RoleUserReqBo[] roleUsers = sysTenantReqBo.getRoleUsers();
        SysRoleUserReqBo roleUserBo = new SysRoleUserReqBo();
        roleUserBo.setRoleUsers(roleUsers);
        roleUserBo.setTenantId(sysTenantInfo.getId());
        sysTenantMemberService.addMembers(roleUserBo);
        // 添加租户资源配额项
        List<SysTenantLimit> limit = sysTenantReqBo.getTenantLimits();
        if (limit != null && !limit.isEmpty()) {
            List<SysTenantLimit> sysTenantLimits = limit.stream()
                    .filter(
                            item -> item.getQuotaValue() != null)
                    .collect(Collectors.toList());

            if (sysTenantLimits != null && !sysTenantLimits.isEmpty()) {
                sysTenantLimitMapper.batchInsert(sysTenantLimits, sysTenantInfo.getId());
            }
        }
        // 初始化仓库
        depotHelperAsync.initSysTenantDepot(sysTenantInfo, RequestContext.getToken());

        return sysTenantInfo;
    }

    /**
     * 新增sysTenant
     * @param sysTenantReqBo
     * @return
     * @throws ApplicationException
     */
    private SysTenant insertSysTenant(SysTenantReqBo sysTenantReqBo) throws ApplicationException {
        SysTenant sysTenant = new SysTenant();
        sysTenant.setTenantName(sysTenantReqBo.getTenantName());
        sysTenant.setTenantDesc(sysTenantReqBo.getDescription());
        if (this.countByTenantName(sysTenant.getTenantName(), sysTenant.getId()) > 0) {
            throw new ApplicationException(SysTenantError.DUPLICATE_SYS_TENANT_ERROR);
        }
        sysTenant.setCreatorId(RequestContext.getUserId());
        sysTenant.setDelFlag(SysMgrConstants.IS_DELETE_FALSE);
        sysTenant.setStatus(sysTenantReqBo.getStatus());
        sysTenant.setAstAddress(sysTenantReqBo.getAstAddress());
        sysTenant.setAstUsername(ftpUsername);
        sysTenant.setAstPassword(ftpPassword);
        sysTenant.setImageProject(sysTenantReqBo.getImageProject());
        sysTenant.setImageUsername(sysTenantReqBo.getImageUsername());
        sysTenant.setImagePassword(sysTenantReqBo.getImagePassword());
        sysTenant.setTenantCode(sysTenantReqBo.getTenantCode());
        sysTenantMapper.insert(sysTenant);

        return sysTenant;
    }

    /**
     * 描述
     *
     * @param sysTenantReqBo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午2:24:13
     */
    @Transactional
    @Override
    public SysTenant updateSysTenantAndMembers(SysTenantReqBo sysTenantReqBo) throws ApplicationException {
        // 1.更新租户
        SysTenant sysTenant = new SysTenant();
        sysTenant.setTenantName(sysTenantReqBo.getTenantName());
        sysTenant.setId(sysTenantReqBo.getTenantId());
        sysTenant.setTenantDesc(sysTenantReqBo.getDescription());
        sysTenant.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
        sysTenant.setAstUsername(sysTenantReqBo.getAstUsername());
        sysTenant.setAstPassword(sysTenantReqBo.getAstPassword());
        sysTenant.setImageUsername(sysTenantReqBo.getImageUsername());
        sysTenant.setImagePassword(sysTenantReqBo.getImagePassword());
        if (this.countByTenantName(sysTenant.getTenantName(), sysTenant.getId()) > 0) {
            throw new ApplicationException(SysTenantError.DUPLICATE_SYS_TENANT_ERROR);
        }
        sysTenantMapper.updateByPrimaryKeySelective(sysTenant);

        // 2.添加租户成员
        SysRoleUserReqBo roleUserBo = new SysRoleUserReqBo();
        roleUserBo.setRoleUsers(sysTenantReqBo.getRoleUsers());
        roleUserBo.setTenantId(sysTenant.getId());
        sysTenantMemberService.addMembers(roleUserBo);

        // 3.更新租户资源配额项
        List<SysTenantLimit> limit = sysTenantReqBo.getTenantLimits();
        if (limit != null && !limit.isEmpty()) {
            List<SysTenantLimit> sysTenantLimits = limit.stream()
                    .filter(
                            item -> item.getQuotaValue() != null)
                    .collect(Collectors.toList());
            if (sysTenantLimits != null && !sysTenantLimits.isEmpty()) {
                sysTenantLimitMapper.batchUpdate(sysTenantLimits, sysTenantReqBo.getTenantId());
            }
        }else{
        	//如果页面值为空->删除全部配额值
        	 sysTenantLimitMapper.deletbatch(sysTenantReqBo.getTenantId());
        }

        return sysTenant;
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#selectBySelective(java.lang.Object)
     */
    @Override
    public List<SysTenant> selectBySelective(SysTenant params) {
        return sysTenantMapper.selectBySelective(params);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#selectByPrimaryKey(java.lang.Long)
     */
    @Override
    public SysTenant selectByPrimaryKey(Long id) {
        return sysTenantMapper.selectByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#deleteByPrimaryKey(java.lang.Long)
     */
    @Transactional
    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysTenantMapper.deleteByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#deleteBySelective(java.lang.Object)
     */
    @Transactional
    @Override
    public int deleteBySelective(SysTenant params) {
        return sysTenantMapper.deleteBySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#insert(java.lang.Object)
     */
    @Transactional
    @Override
    public int insert(SysTenant params) {
        return sysTenantMapper.insert(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#insertSelective(java.lang.Object)
     */
    @Override
    public int insertSelective(SysTenant params) {
        return sysTenantMapper.insertSelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#countBySelective(java.lang.Object)
     */
    @Override
    public int countBySelective(SysTenant params) {
        return sysTenantMapper.countBySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#updateByPrimaryKeySelective(java.lang.Object)
     */
    @Transactional
    @Override
    public int updateByPrimaryKeySelective(SysTenant params) {
        return sysTenantMapper.updateByPrimaryKeySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年7月9日 下午2:41:33
     * @see com.newland.paas.paasservice.sysmgr.service.BaseService#updateByPrimaryKey(java.lang.Object)
     */
    @Transactional
    @Override
    public int updateByPrimaryKey(SysTenant params) {
        return sysTenantMapper.updateByPrimaryKey(params);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午3:37:25
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#getSysTenantAndAdmins(java.lang.Long)
     */
    @Override
    public SysTenantRespVo getSysTenantAndAdmins(Long id) throws ApplicationException {
        SysTenant info = this.selectByPrimaryKey(id);
        if (info == null) {
            throw new ApplicationException(SysTenantError.NOT_EXIST_SYS_TENANT_ERROR);
        }
        List<TenantUserBO> adminUsers = sysTenantMapper.getSysTenantUsers(id, SysMgrConstants.IS_ADMIN_YES, null);
        List<TenantUserVO> tenantUserVOs = new ArrayList<>();
        if (!adminUsers.isEmpty()) {
            adminUsers.stream().forEach(bo -> {
                TenantUserVO vo = new TenantUserVO();
                BeanUtils.copyProperties(bo, vo);
                tenantUserVOs.add(vo);
            });
        }
        SysTenantRespVo vo = new SysTenantRespVo();
        vo.setTenantId(info.getId());
        vo.setTenantName(info.getTenantName());
        vo.setDescription(info.getTenantDesc());
        vo.setAstAddress(this.astHost + ":" + astHomePath + info.getAstAddress());
        vo.setAstUsername(info.getAstUsername());
        vo.setAstPassword(info.getAstPassword());
        vo.setImageProject(this.imageHost + "/" + info.getImageProject());
        vo.setImageUsername(info.getImageUsername());
        vo.setImagePassword(info.getImagePassword());
        vo.setImageProjectDir(info.getImageProject());
        vo.setAdminUsers(adminUsers.isEmpty() ? new ArrayList<>() : tenantUserVOs);
        vo.setImageHost(this.imageHost);
        return vo;
    }

    /**
     * 获取创建租户详情
     *
     * @param tenantId
     * @return
     */
    @Override
    public CreateTenantDetail getCreateTenantDetail(Long tenantId) {
        return sysTenantOperateMapper.getCreateTenantDetailInfo(tenantId);
    }


    /**
     * 页面获取租户并且包括管理员列表
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午3:36:52
     */
    @Override
    public SysTenantRespVo getPageSysTenantAndAdmins(Long id) throws ApplicationException {
        SysTenant info = this.selectByPrimaryKey(id);
        if (info == null) {
            throw new ApplicationException(SysTenantError.NOT_EXIST_SYS_TENANT_ERROR);
        }
        List<TenantUserBO> adminUsers = sysTenantMapper.getSysTenantUsers(id, SysMgrConstants.IS_ADMIN_YES, null);
        List<TenantUserVO> tenantUserVOs = new ArrayList<>();
        if (!adminUsers.isEmpty()) {
            adminUsers.stream().forEach(bo -> {
                TenantUserVO vo = new TenantUserVO();
                BeanUtils.copyProperties(bo, vo);
                tenantUserVOs.add(vo);
            });
        }
        SysTenantRespVo vo = new SysTenantRespVo();
        vo.setTenantId(info.getId());
        vo.setTenantName(info.getTenantName());
        vo.setDescription(info.getTenantDesc());
        vo.setAstAddress(info.getAstAddress());
        vo.setAstUsername(info.getAstUsername());
        vo.setAstPassword(info.getAstPassword());
        vo.setImageProject(info.getImageProject());
        vo.setImageUsername(info.getImageUsername());
        vo.setImagePassword(info.getImagePassword());
        vo.setTenantCode(info.getTenantCode());
        vo.setAdminUsers(adminUsers.isEmpty() ? new ArrayList<>() : tenantUserVOs);
        return vo;
    }

    /**
     * 描述
     *
     * @param params
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 上午10:20:09
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void joinTenantSubmitProcess(SysTenantFlowReqBo params) throws ApplicationException {

        SysTenantMember countTenantMember = new SysTenantMember();
        countTenantMember.setUserId(params.getUserId());
        countTenantMember.setTenantId(params.getTenantId());
        countTenantMember.setStatus("1");
        if (sysTenantMemberService.countBySelective(countTenantMember) > 0) {
            throw new ApplicationException(SysTenantMemberError.IS_MEMBER_OR_IS_JOINING);
        }
        countTenantMember.setStatus("0");
        if (sysTenantMemberService.countBySelective(countTenantMember) > 0) {
            throw new ApplicationException(SysTenantMemberError.IS_MEMBER_OR_IS_JOINING);
        }
        //插入数据
        SysRoleUser roleUser = new SysRoleUser();
        roleUser.setRoleId(params.getRoleId());
        roleUser.setUserId(params.getUserId());
        roleUser.setTenantId(params.getTenantId());
        roleUser.setStatus(SysMgrConstants.EXAMTYPE_PREEXAM);
        roleUser.setCreatorId(params.getCreatorId());
        sysRoleUserService.add(roleUser);

        SysTenantMember tenantMember = new SysTenantMember();
        tenantMember.setUserId(params.getUserId());
        tenantMember.setTenantId(params.getTenantId());
        // 角色是管理员的则设置为租户管理员
        int index = Arrays.binarySearch(SysMgrConstants.ROLE_ID_LIST_OF_ADMIN, roleUser.getRoleId());
        if (index >= 0) {
            tenantMember.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
        }
        tenantMember.setWorkType("加入租户");
        tenantMember.setStatus(SysMgrConstants.EXAMTYPE_PREEXAM);
        tenantMember.setCreatorId(params.getCreatorId());
        sysTenantMemberService.insertSelective(tenantMember);

        params.setRoleUserId(roleUser.getId());
        params.setTenantMemberId(tenantMember.getId());

        // 发起流程并执行流程草拟
        StartProcessVo startProcessVo = new StartProcessVo(SysMgrConstants.ACTIVITI_TENANT_JOIN_ORDERID,
                "关于" + params.getUserName() + "加入" + params.getTenantName() + "的申请");
        List<StartProcessVariable> variables = new ArrayList<>();
        startProcessVo.setVariables(variables);
        StartProcessVariable groupId = new StartProcessVariable();
        groupId.setName(GROUP_ID);
        groupId.setValue(actEngine.getYyTenantRootGroup());
        variables.add(groupId);
        StartProcessVariable reqInfo = new StartProcessVariable();
        reqInfo.setName("reqInfo");
        reqInfo.setValue(JSONObject.toJSONString(params));
        variables.add(reqInfo);
        actEngine.startProcessAndExecProcessDraft(startProcessVo,
                RequestContext.getTenantId(), RequestContext.getSession().getAccount());

    }

    /**
     * 描述
     *
     * @param proinstanceId
     * @return
     * @author linkun
     * @created 2018年8月17日 上午11:21:23
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#joinTenantGetTaskVar(java.lang.Long)
     */
    @Override
    public SysTenantFlowReqBo joinTenantGetTaskVar(Long proinstanceId) {
        String result = actEngine.getProcessInstanceVariableHistory(proinstanceId,
                "reqInfo", RequestContext.getTenantId(), RequestContext.getSession().getAccount());
        return JSONObject.parseObject(result, SysTenantFlowReqBo.class);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018年8月17日 上午11:26:43
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean joinTenantNextProcess(SysTenantFlowReqBo params) {
        ExecTaskVo execTaskVo = new ExecTaskVo();
        execTaskVo.setTaskId(Long.parseLong(params.getTaskId()));
        List<ExecTaskPropertie> properties = new ArrayList<>();
        execTaskVo.setProperties(properties);
        ExecTaskPropertie propertie = null;
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.IS_AGREE);
        propertie.setValue(params.getOpinion());
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.REMARK);
        propertie.setValue(params.getAuditDesc());
        actEngine.execTask(execTaskVo, RequestContext.getTenantId(), RequestContext.getSession().getAccount());
        return true;
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 下午1:17:31
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean joninTenantApprove(SysTenantFlowReqBo params) throws ApplicationException {
        SysRoleUser roleUser = new SysRoleUser();
        roleUser.setId(params.getRoleUserId());
        SysTenantMember tenantMember = new SysTenantMember();
        tenantMember.setId(params.getTenantMemberId());
        if (Objects.equals("1", params.getIsAgree())) {
            // 用户角色通过审批
            roleUser.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            sysRoleUserService.update(roleUser);
            // 租户成员通过审批
            tenantMember.setStatus(SysMgrConstants.EXAMTYPE_PASSED);
            tenantMember.setWorkType("");
            sysTenantMemberService.updateByPrimaryKeySelective(tenantMember);
            // 如果新增的用户是租户管理员，则加入根工组
            int index = Arrays.binarySearch(SysMgrConstants.ROLE_ID_LIST_OF_ADMIN, params.getRoleId());
            if (index >= 0) {
                // 获取根工组信息
                Long tenantId = params.getTenantId();
                SysGroupReqBo sysGroup = new SysGroupReqBo();
                sysGroup.setTenantId(tenantId);
                sysGroup.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
                List<SysGroup> rootGroupInfoList = sysGroupMapper.selectBySelective(sysGroup);
                Long rootGroupId = rootGroupInfoList.get(0).getGroupId();
                // 新增用户至根工组，并设置为工组管理员
                SysGroupUser sysGroupUser = new SysGroupUser();
                sysGroupUser.setGroupId(rootGroupId);
                sysGroupUser.setUserId(params.getUserId());
                sysGroupUser.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
                sysGroupUser.setTenantId(tenantId);
                sysGroupUser.setCreatorId(RequestContext.getUserId());
                sysGroupUserMapper.insertSelective(sysGroupUser);
            }
        } else {
            roleUser.setStatus(SysMgrConstants.EXAMTYPE_NOPASS);
            sysRoleUserService.update(roleUser);

            tenantMember.setStatus(SysMgrConstants.EXAMTYPE_NOPASS);
            tenantMember.setWorkType("");
            sysTenantMemberService.updateByPrimaryKeySelective(tenantMember);
        }
        return true;
    }

    /**
     * 描述
     *
     * @param userId
     * @return
     * @author linkun
     * @created 2018年8月20日 上午9:55:50
     * @see com.newland.paas.paasservice.sysmgr.service.SysTenantService#getAllTenantsCanJoin(java.lang.Long)
     */
    @Override
    public List<SysTenant> getAllTenantsCanJoin(Long userId) {
        return sysTenantMapper.getAllTenantsCanJoin(userId);
    }

    /**
     * 通过仓库查找租户
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     */
    @Override
    public int countTenantByDepot(SysTenant sysTenant) throws ApplicationException {
        return sysTenantMapper.countBySelective(sysTenant);
    }

    /**
     * 根据租户id查询配额
     *
     * @param tenantId
     * @return
     */
    @Override
    public com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantLimitStaVo getLimitsByTenantId(Long tenantId) {
        SysTenantLimitInfoVo sysTenantLimitInfoVo = sysTenantLimitMapper.getLimitsByTenantId(tenantId);
        com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantLimitStaVo sysTenantLimitVo =
                new com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantLimitStaVo();
        BeanUtils.copyProperties(sysTenantLimitInfoVo, sysTenantLimitVo);
        return sysTenantLimitVo;
    }


    /**
     * 获取租户资源配额
     *
     * @param tenantId
     * @return
     */
    @Override
    public List<SysTenantLimitBo> listSysTenantLimits(Long tenantId) {
        List<SysTenantLimitBo> limits = sysTenantLimitMapper.listLimitsByTenantId(tenantId);
        limits.forEach(item -> item.setResLimitUnit(ResLimitUnitConstants.getUnit(item.getQuotaItem())));
        return limits;
    }

    /**
     * 添加租户资源配额
     *
     * @param limitList 资源配额列表
     * @param tenantId  租户id
     * @return
     */
    @Override
    public int addSysTenantLimits(List<SysTenantLimitVo> limitList, Long tenantId) {
        List<SysTenantLimit> limits = null;
        if (limitList != null && !limitList.isEmpty()) {
            limits = limitList.stream()
                    .filter(
                            item -> item.getQuotaValue() != null)
                    .map(item -> {
                        SysTenantLimit limit = new SysTenantLimit();
                        BeanUtils.copyProperties(item, limit);
                        return limit;
                    }).collect(Collectors.toList());
            // 添加租户资源配额项
            if (limits != null && !limits.isEmpty()) {
                return sysTenantLimitMapper.batchInsert(limits, tenantId);
            }
        }
        return 1;
    }

    /**
     * 获取租户信息
     *
     * @param sysTenant
     * @return
     * @throws ApplicationException
     */
    @Override
    public SysTenant getSysTenant(SysTenant sysTenant) throws ApplicationException {
        SysTenant sysTenantVo = new SysTenant();
        sysTenantVo.setId(sysTenant.getId());
        sysTenantVo.setTenantName(sysTenant.getTenantName());
        sysTenantVo.setDelFlag((short) 0);
        List<SysTenant> sysTenants = sysTenantMapper.selectBySelective(sysTenantVo);
        return sysTenants != null && !sysTenants.isEmpty() ? sysTenants.get(0) : null;
    }

    /**
     * 获取租户根据租户code
     *
     * @param tenantCode
     * @return
     */
    @Override
    public List<SysTenant> listTenantByTenantCode(String tenantCode) {
        SysTenant sysTenant = new SysTenant();
        sysTenant.setTenantCode(tenantCode);
        return sysTenantMapper.selectBySelective(sysTenant);
    }

    /**
     * 租户申请
     *
     * @param reqInfo
     */
    @Transactional
    @Override
    public void tenantApply(BasicRequestContentVo<SysTenantApplyVo> reqInfo) throws ApplicationException {
        SysTenantApplyVo sysTenantVo = reqInfo.getParams();
        SysTenantReqBo sysTenantReqBo = this.getSysTenantReqBo(sysTenantVo);

        SysTenant tenant = this.addSysTenantAndMembers(sysTenantReqBo);
        sysTenantVo.setId(tenant.getId());

        // 发起流程并执行流程草拟
        StartProcessVo startProcessVo = new StartProcessVo(SysMgrConstants.ACTIVITI_TENANT_CREATE_ORDERID,
                SysMgrConstants.ACTIVITI_TENANT_CREATE_ORDERKEY + "-" + sysTenantVo.getTenantName());
        List<StartProcessVariable> variables = new ArrayList<>();
        StartProcessVariable groupId = new StartProcessVariable();
        groupId.setName("groupId");
        groupId.setValue(actEngine.getYyTenantRootGroup());
        variables.add(groupId);
        StartProcessVariable tenantInfo = new StartProcessVariable();
        tenantInfo.setName("tenantInfo");
        tenantInfo.setValue(JSONObject.toJSONString(sysTenantVo));
        variables.add(tenantInfo);
        StartProcessVariable preemptedTenantId = new StartProcessVariable();
        preemptedTenantId.setName("preemptedTenantId");
        preemptedTenantId.setValue(tenant.getId());
        variables.add(preemptedTenantId);
        startProcessVo.setVariables(variables);
        actEngine.startProcessAndExecProcessDraft(startProcessVo,
                RequestContext.getTenantId(), RequestContext.getSession().getAccount());
    }

    /**
     *
     * @param sysTenantVo
     * @return
     */
    private SysTenantReqBo getSysTenantReqBo(SysTenantApplyVo sysTenantVo) {
        // 插入租户
        // 1. 租户基本信息
        SysTenantReqBo sysTenantReqBo = new SysTenantReqBo();
        BeanUtils.copyProperties(sysTenantVo, sysTenantReqBo);
        sysTenantReqBo.setStatus("0");
        sysTenantReqBo.setDescription(sysTenantVo.getTenantDesc());
        // 2.当前用户设置为租户管理员
        RoleUserReqBo[] roleUsers = new RoleUserReqBo[]{};
        List<RoleUserReqBo> roleUserList = new ArrayList<>();
        RoleUserReqBo roleUserReqBo = new RoleUserReqBo();
        roleUserReqBo.setUserId(RequestContext.getUserId());
        roleUserReqBo.setRoleId(SysMgrConstants.ROLE_ID_ZH_ADMIN);
        roleUserList.add(roleUserReqBo);
        sysTenantReqBo.setRoleUsers(roleUserList.toArray(roleUsers));

        return sysTenantReqBo;
    }

    /**
     * 提供方申请流程提交
     *
     * @param applyVo
     * @throws ApplicationException
     */
    @Override
    public void providerTenantSubmitProcess(SysTenantProviderApplyVo applyVo) throws ApplicationException {
        Long id = applyVo.getId();
        SysTenant sysTenant = sysTenantMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(sysTenant, applyVo);

        // 修改租户提供方状态--审批中
        SysTenant sysTenantReq = new SysTenant();
        sysTenantReq.setId(id);
        sysTenantReq.setSupplierStatus(GlbDictConstants.APPROVE_STATUS_APPROVE.dictCode);
        sysTenantMapper.updateByPrimaryKeySelective(sysTenantReq);

        // 发起流程并执行流程草拟
        StartProcessVo startProcessVo = new StartProcessVo(SysMgrConstants.ACTIVITI_TENANT_PROVIDER_ORDERID,
                SysMgrConstants.ACTIVITI_TENANT_PROVIDER_ORDERKEY + "-" + sysTenant.getTenantName());
        List<StartProcessVariable> variables = new ArrayList<>();
        StartProcessVariable groupId = new StartProcessVariable();
        groupId.setName("groupId");
        groupId.setValue(actEngine.getYyTenantRootGroup());
        variables.add(groupId);
        StartProcessVariable tenantInfo = new StartProcessVariable();
        tenantInfo.setName("tenantInfo");
        tenantInfo.setValue(JSONObject.toJSONString(applyVo));
        variables.add(tenantInfo);
        StartProcessVariable preemptedTenantId = new StartProcessVariable();
        preemptedTenantId.setName("preemptedTenantId");
        preemptedTenantId.setValue(sysTenant.getId());
        variables.add(preemptedTenantId);
        startProcessVo.setVariables(variables);
        actEngine.startProcessAndExecProcessDraft(startProcessVo,
                RequestContext.getTenantId(), RequestContext.getSession().getAccount());
    }

    @Override
    public ResultPageData queryTenantImageRepo(RepoReqVo repoReqVo, PageInfo pageInfo) throws ApplicationException {
        Long tenantId = RequestContext.getTenantId();
        SysTenant sysTenant = sysTenantMapper.selectByPrimaryKey(tenantId);
        if (repoReqVo == null) {
            repoReqVo = new RepoReqVo();
        }
        repoReqVo.setProject(sysTenant.getImageProject());
        repoReqVo.setHarborUser(sysTenant.getImageUsername());
        repoReqVo.setHarborPwd(sysTenant.getImagePassword());
        String reqToken = RequestContext.getToken();
        String interfaceUrl = "/v1/repositories/list";
        BasicPageRequestContentVo<RepoReqVo> params = new BasicPageRequestContentVo<>();
        params.setParams(repoReqVo);
        params.setPageInfo(pageInfo);
        BasicResponseContentVo<ResultPageData> result = restTemplateUtils
                .postForTokenEntity(microservicesProperties.getHarbormgr(), interfaceUrl, reqToken, params,
                        new ParameterizedTypeReference<BasicResponseContentVo<ResultPageData>>(){
                        });
        ResultPageData resultPageData = null;
        if (result.getError() != null){
            PaasError paasError = result.getError();
            throw new ApplicationException(paasError);
        }
        if (result.getContent() != null) {
            resultPageData = result.getContent();
        }
        return resultPageData;
    }

    /**
     * 审批申请
     *
     * @param approveBo
     */
    @Override
    public void approveSysTenant(ApproveBo approveBo) {
        String isAgree = approveBo.getIsAgree();
        ExecTaskVo execTaskVo = new ExecTaskVo();
        execTaskVo.setTaskId(Long.parseLong(approveBo.getTaskId()));
        List<ExecTaskPropertie> properties = new ArrayList<>();
        execTaskVo.setProperties(properties);
        ExecTaskPropertie propertie = null;
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.IS_AGREE);
        propertie.setValue(isAgree);
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.REMARK);
        propertie.setValue(approveBo.getComment());
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId("limit");
        propertie.setValue(JSONObject.toJSONString(approveBo.getLimitList()));
        actEngine.execTask(execTaskVo, RequestContext.getTenantId(), RequestContext.getSession().getAccount());
    }

    /**
     * 同意新增租户
     *
     * @param sysTenantApplyVo
     */
    @Transactional
    @Override
    public void approveNewSysTenant(SysTenantApplyVo sysTenantApplyVo) throws ApplicationException {
        Long tenantId = sysTenantApplyVo.getId();
        LOG.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("新建租户操作，参数: {0}", JSONObject.toJSONString(sysTenantApplyVo)));
        SysTenant sysTenant = new SysTenant();
        sysTenant.setId(tenantId);
        if (Objects.equals("1", sysTenantApplyVo.getIsAgree())) {
            sysTenant.setStatus("1");
            // 添加租户资源配额项
            this.addSysTenantLimits(sysTenantApplyVo.getLimitList(), tenantId);
            // 查询租户详情
            long start = System.currentTimeMillis();
            while (true) {
                CreateTenantDetail detail = this.getCreateTenantDetail(tenantId);
                int activeStep = detail.getActiveStep();
                List<StatusInfosBo> statusInfos = detail.getStatusInfos();
                if (statusInfos != null && !statusInfos.isEmpty() && statusInfos.size() - 1 == activeStep) {
                    if (Objects.equals(statusInfos.get(activeStep).getProgress(), "成功")) {
                        break;
                    } else if (Objects.equals(statusInfos.get(activeStep).getProgress(), "失败")) {
                        throw new SystemException(new PaasError(SysTenantError.ADD_SYS_TENANT_ERROR.getCode(),
                                statusInfos.get(activeStep).getDescription()));
                    }
                }
                long end = System.currentTimeMillis();
                if ((end - start) > TIMEOUT) {
                    throw new SystemException(SysTenantError.ADD_SYS_TENANT_TIMEOUT_ERROR);
                }
            }
        } else {
            sysTenant.setStatus("-1");
            this.deleteSysTenant(tenantId);
        }
        this.updateSysTenant(sysTenant);
    }

    /**
     * 审批租户提供方申请
     *
     * @param approveBo
     */
    @Override
    public void approveSysTenantProvider(ApproveBo approveBo) {
        String isAgree = approveBo.getIsAgree();

        ExecTaskVo execTaskVo = new ExecTaskVo();
        execTaskVo.setTaskId(Long.parseLong(approveBo.getTaskId()));
        List<ExecTaskPropertie> properties = new ArrayList<>();
        execTaskVo.setProperties(properties);
        ExecTaskPropertie propertie = null;
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.APPROVE);
        propertie.setValue(Objects.equals(isAgree, "1") + "");
        propertie = new ExecTaskPropertie();
        properties.add(propertie);
        propertie.setId(ExecTaskPropertie.REMARK);
        propertie.setValue(approveBo.getComment());
        actEngine.execTask(execTaskVo, RequestContext.getTenantId(), RequestContext.getSession().getAccount());
    }

    /**
     * 租户提供方审批回调
     *
     * @param applyVo
     */
    @Transactional
    @Override
    public void approveStatusSysTenantProvider(SysTenantProviderApplyVo applyVo) throws ApplicationException {
        SysTenant sysTenant = new SysTenant();
        sysTenant.setId(applyVo.getId());
        sysTenant.setSupplierStatus(applyVo.getSupplierStatus());
        this.updateSysTenant(sysTenant);
    }

    /**
     * 获取申请租户工单详情
     *
     * @param proinstanceId
     * @return
     */
    @Transactional
    @Override
    public SysTenantApplyBo applySysTenantDetail(Long proinstanceId) {
        String result = actEngine.getProcessInstanceVariableHistory(proinstanceId,
                "tenantInfo", RequestContext.getTenantId(), RequestContext.getSession().getAccount());
        return JSONObject.parseObject(result, SysTenantApplyBo.class);
    }

    /**
     * 获取提供方申请流程详情
     *
     * @param proinstanceId
     * @return
     */
    @Override
    public SysTenantProviderApplyVo getSysTenantProviderDetail(Long proinstanceId) {
        String result = actEngine.getProcessInstanceVariableHistory(proinstanceId,
                "tenantInfo", RequestContext.getTenantId(), RequestContext.getSession().getAccount());
        return JSONObject.parseObject(result, SysTenantProviderApplyVo.class);
    }
}

