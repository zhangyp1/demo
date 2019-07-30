package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.service.SysObjService;
import com.newland.paas.paasservice.sysmgr.vo.FrightReqVo;
import com.newland.paas.paasservice.sysmgr.vo.GroupBean;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightReqVo;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightVo;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象权限
 *
 * @author WRP
 * @since 2018/7/31
 */
@Service
public class SysObjServiceImpl implements SysObjService {

    private static final Log LOG = LogFactory.getLogger(SysObjServiceImpl.class);

    private static final String OP = "_op";

    @Autowired
    private SysGroupObjMapper sysGroupObjMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Autowired
    private SysObjSrightMapper sysObjSrightMapper;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private GlbDictMapper glbDictMapper;
    @Autowired
    private SysObjFrightMapper sysObjFrightMapper;
    @Autowired
    private SysObjViewMapper sysObjViewMapper;
    @Autowired
    private ObjPermissionRulerServiceImpl objPermissionRulerService;

    /**
     * 分页查询工组对象列表，从v_sys_obj
     *
     * @param pageInfo
     * @param params
     * @return
     */
    @Override
    public ResultPageData<SysGroupObjBO> pageViewByParams(PageInfo pageInfo, SysGroupObjBO params) {
        GroupBean groupBean = buildGroupBean();
        // 用户无管理工组，无可操作对象
        if (CollectionUtils.isEmpty(groupBean.getGroupIds())) {
            return new ResultPageData(new ArrayList());
        }

        List<Long> filteredGroupIds = new ArrayList<>();
        Long byGroupId = params.getGroupId();
        for (Long groupId : groupBean.getGroupIds()) {
            if (byGroupId != null && !byGroupId.equals(groupId)) {
                continue;
            }
            filteredGroupIds.add(groupId);
        }

        if (CollectionUtils.isEmpty(filteredGroupIds)) {
            return new ResultPageData(new ArrayList());
        }

        List<String> canGrantObjTypeList = objPermissionRulerService.listCanGrantObjTypeCode();

        Page<SysGroupObjBO> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        // 查找objId不重复的数据
        List<SysGroupObjBO> groupObjBOs =
                sysGroupObjMapper.selectVSysObjDetailBySelective(params, filteredGroupIds, canGrantObjTypeList);
        pageInfo.setTotalRecord(page.getTotal());

        // 判断对象所属工组是否是管理员（界面操作按钮需要）
        groupObjBOs.forEach(v -> {
            if ("0".equals(v.getIsSright()) && groupBean.getAdminGroupIds().contains(v.getGroupId())) {
                v.setIsAdmin(true);
            }
        });
        // 获取对象权限列表
        buildGroupObjectPrivilege(groupObjBOs);
        return new ResultPageData(groupObjBOs, pageInfo);
    }

    /**
     * 当前用户是否是当前租户的管理员
     *
     * @return
     */
    private boolean isTenantAdmin() {
        SysTenantMember tenantMemberParams = new SysTenantMember();
        tenantMemberParams.setTenantId(RequestContext.getTenantId());
        tenantMemberParams.setUserId(RequestContext.getUserId());
        tenantMemberParams.setIsAdmin(Short.valueOf("1"));
        List<SysTenantMember> tenantMembers = sysTenantMemberMapper.selectBySelective(tenantMemberParams);
        return !tenantMembers.isEmpty();
    }

    /**
     * 租户管理员的所有工组
     *
     * @return
     */
    private List<Long> adminTenantGroup() {
        SysGroupReqBo groupParams = new SysGroupReqBo();
        groupParams.setTenantId(RequestContext.getTenantId());
        List<SysGroup> groups = sysGroupMapper.selectBySelective(groupParams);
        return groups.stream().map(SysGroup::getGroupId).collect(Collectors.toList());
    }

    /**
     * 构造工组对象（当前用户归属的工组，以及管理员工组）
     *
     * @return
     */
    private GroupBean buildGroupBean() {
        Set<Long> adminGroupIds = new TreeSet<>();
        Set<Long> groupIds = new TreeSet<>();
        if (isTenantAdmin()) {
            List<Long> ids = adminTenantGroup();
            groupIds.addAll(ids);
            adminGroupIds.addAll(ids);
        } else {
            // 查找当前用户归属的工组以及子工组（是管理员的情况查找子工组）
            SysGroupUserReqBo groupUserParams = new SysGroupUserReqBo();
            groupUserParams.setTenantId(RequestContext.getTenantId());
            groupUserParams.setUserId(RequestContext.getUserId());
            List<SysGroupUserRespBo> groupUserRespBos = sysGroupUserMapper.selectBySelective(groupUserParams);
            groupUserRespBos.forEach(v -> {
                // 1:管理员
                if (v.getIsAdmin().equals(Short.valueOf("1"))) {
                    List<Long> subGroupIds = new ArrayList<>();
                    subGroupIds.add(v.getGroupId());
                    buildGroupIds(v.getGroupId(), subGroupIds);
                    groupIds.addAll(subGroupIds);
                    adminGroupIds.addAll(subGroupIds);
                } else {
                    groupIds.add(v.getGroupId());
                }
            });
        }
        return new GroupBean(new ArrayList<>(groupIds), new ArrayList<>(adminGroupIds));
    }

    /**
     * 查询对象详情
     *
     * @param objId
     * @return
     */
    @Override
    public SysGroupObjBO getGroupObjDetail(Long objId) {
        SysGroupObjBO params = new SysGroupObjBO();
        params.setObjId(objId);
        List<SysGroupObjBO> groupObjBOs = sysGroupObjMapper.selectDetailBySelective(params, null);
        if (groupObjBOs.isEmpty()) {
            return null;
        } else {
            return groupObjBOs.get(0);
        }
    }

    /**
     * 根据objId和groupId列表获取对应的授权与赋权
     *
     * @param groupIds
     * @return
     */
    @Override
    public SrightFrightVo getGroupAndGroupRole(Long objId, List<Long> groupIds) {
        SrightFrightVo srightFrightVo = new SrightFrightVo();
        if (CollectionUtils.isEmpty(groupIds)) {
            return srightFrightVo;
        }
        // 查找所有操作，目前只查找字典表数据(只有管理员才有所有操作)
        // operates可能会根据具体对象新增
        List<GlbDict> operates = new ArrayList<>();
        List<GlbDict> filteredOperates = new ArrayList<>();
        SysGroupObjBO params = new SysGroupObjBO();
        params.setObjId(objId);
        List<SysGroupObjBO> groupObjBOs = sysGroupObjMapper.selectVSysObjDetailBySelective(params, null, null);
        String pCode = groupObjBOs.get(0).getObjType() + OP;
        String isSright = groupObjBOs.get(0).getIsSright();
        Long groupId = groupObjBOs.get(0).getGroupId();
        if (!groupObjBOs.isEmpty()) {
            operates.addAll(glbDictMapper.selectByDictPcode(pCode));

            List<String> canNotGrantObjOperateList = objPermissionRulerService.listCanNotGrantObjOperate(groupObjBOs.get(0).getObjType());
            for (GlbDict glbDict : operates) {
                if (canNotGrantObjOperateList != null && canNotGrantObjOperateList.contains(glbDict.getDictCode())) {
                    continue;
                }
                filteredOperates.add(glbDict);
            }

            srightFrightVo.setOperates(filteredOperates);
        }
        List<SysGroupForObjRespBo> groups = new ArrayList<>();
        List<SysGroup> sysGroupList = sysGroupMapper.selectGroupByGroupIds(groupIds);
        for (SysGroup sysGroup : sysGroupList) {
            SysGroupForObjRespBo sysGroupForObjRespBo = new SysGroupForObjRespBo();
            BeanUtils.copyProperties(sysGroup, sysGroupForObjRespBo);
            groups.add(sysGroupForObjRespBo);
        }

        groups.forEach(v -> {
            if (!"1".equals(isSright) && v.getGroupId().equals(groupId)) {
                buildAdmin(v, filteredOperates, objId, pCode);
            } else {
                buildNotAdmin(v, filteredOperates, objId, pCode);
            }
        });
        srightFrightVo.setGroups(groups);
        return srightFrightVo;
    }

    /**
     * 管理员权限构造
     *
     * @param v
     * @param operates
     * @param objId
     * @param pCode
     */
    private void buildAdmin(SysGroupForObjRespBo v, List<GlbDict> operates, Long objId, String pCode) {
        // 管理员
        v.setIsAdmin(true);
        // 可选操作
        v.setOperates(operates);
        // 已选操作
        v.setSelectOperates(operates);
        SysObjFrightBO objFrightBOParams = new SysObjFrightBO();
        objFrightBOParams.setObjId(objId);
        objFrightBOParams.setGroupId(v.getGroupId());
        List<SysObjFrightBO> objFrightBOs = sysObjFrightMapper.selectGroupRoleDetail(objFrightBOParams);
        List<SysGroupRoleBO> groupRoles = new ArrayList<>();
        objFrightBOs.forEach(r -> {
            SysGroupRoleBO sysGroupRoleBO = new SysGroupRoleBO();
            sysGroupRoleBO.setRoleId(r.getGroupRoleId());
            sysGroupRoleBO.setRoleName(r.getGroupRoleName());
            // 可选操作
            sysGroupRoleBO.setOperates(operates);
            String operatesStr = r.getOperates();
            if (StringUtils.isNotEmpty(operatesStr)) {
                String[] array = operatesStr.split("\\,");
                // 已选操作
                sysGroupRoleBO.setSelectOperates(glbDictMapper.selectDetailByDictCode(pCode, Arrays.asList(array),
                        null));
            } else {
                sysGroupRoleBO.setSelectOperates(new ArrayList<>());
            }
            groupRoles.add(sysGroupRoleBO);
        });
        v.setGroupRoles(groupRoles);
    }

    /**
     * 非管理员构造
     *
     * @param v
     * @param operates
     * @param objId
     * @param pCode
     */
    private void buildNotAdmin(SysGroupForObjRespBo v, List<GlbDict> operates, Long objId, String pCode) {
        // 不是管理员
        v.setIsAdmin(false);
        // 可选操作
        v.setOperates(operates);
        SysObjSrightBO objSrightBOParams = new SysObjSrightBO();
        objSrightBOParams.setObjId(objId);
        objSrightBOParams.setGroupId(v.getGroupId());
        List<SysObjSrightBO> objSrightBOs = sysObjSrightMapper.selectGroupDetail(objSrightBOParams);
        List<GlbDict> groupSelectOperates = new ArrayList<>();
        if (!objSrightBOs.isEmpty()) {
            String operatesStr = objSrightBOs.get(0).getOperates();
            if (StringUtils.isNotEmpty(operatesStr)) {
                String[] array = operatesStr.split("\\,");
                // 已选操作
                groupSelectOperates.addAll(glbDictMapper.selectDetailByDictCode(pCode, Arrays.asList(array), null));
                v.setSelectOperates(groupSelectOperates);
            }
        } else {
            v.setSelectOperates(new ArrayList<>());
        }
        SysObjFrightBO objFrightBOParams = new SysObjFrightBO();
        objFrightBOParams.setObjId(objId);
        objFrightBOParams.setGroupId(v.getGroupId());
        List<SysObjFrightBO> objFrightBOs = sysObjFrightMapper.selectGroupRoleDetail(objFrightBOParams);
        List<SysGroupRoleBO> groupRoles = new ArrayList<>();
        objFrightBOs.forEach(r -> {
            SysGroupRoleBO sysGroupRoleBO = new SysGroupRoleBO();
            sysGroupRoleBO.setRoleId(r.getGroupRoleId());
            sysGroupRoleBO.setRoleName(r.getGroupRoleName());
            // 可选操作
            sysGroupRoleBO.setOperates(groupSelectOperates);
            String operatesStr = r.getOperates();
            if (StringUtils.isNotEmpty(operatesStr)) {
                String[] array = operatesStr.split("\\,");
                // 已选操作
                sysGroupRoleBO.setSelectOperates(glbDictMapper.selectDetailByDictCode(pCode, Arrays.asList(array),
                        null));
            } else {
                sysGroupRoleBO.setSelectOperates(new ArrayList<>());
            }
            groupRoles.add(sysGroupRoleBO);
        });
        v.setGroupRoles(groupRoles);
    }

    /**
     * 对象授权&赋权
     * 1.校验
     * 2.操作
     *
     * @param objId
     * @param srightFrights
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void srightFright(Long objId, List<SrightFrightReqVo> srightFrights) {
        if (CollectionUtils.isEmpty(srightFrights)) {
            throw new SystemException(new PaasError("2-18-00050", "需要授权的工组列表为空！"));
        }
        SysGroupObjBO params = new SysGroupObjBO();
        params.setObjId(objId);
        List<SysGroupObjBO> groupObjBOs = sysGroupObjMapper.selectDetailBySelective(params, null);
        if (groupObjBOs.isEmpty()) {
            throw new SystemException(new PaasError("2-18-00051", "对象不存在！"));
        } else {
            srightFright(objId, srightFrights, groupObjBOs.get(0));
        }
    }

    /**
     * 对象授权&赋权
     *
     * @param objId
     * @param srightFrights
     * @param groupObj
     */
    private void srightFright(Long objId, List<SrightFrightReqVo> srightFrights, SysGroupObjBO groupObj) {
        SysObjSright delObjSright = new SysObjSright();
        delObjSright.setObjId(objId);
        delObjSright.setObjType(groupObj.getObjType());
        delObjSright.setTenantId(RequestContext.getTenantId());
        sysObjSrightMapper.deleteBySelective(delObjSright);
        SysObjFright delObjFright = new SysObjFright();
        delObjFright.setObjId(objId);
        delObjFright.setObjType(groupObj.getObjType());
        delObjFright.setTenantId(RequestContext.getTenantId());
        sysObjFrightMapper.deleteBySelective(delObjFright);
        srightFrights.forEach(g -> {
            // 部署授权赋权
            if (g.getIsAdmin() == null || !g.getIsAdmin()) {
                SysObjSright sysObjSright = new SysObjSright();
                sysObjSright.setObjId(objId);
                sysObjSright.setObjType(groupObj.getObjType());
                sysObjSright.setTenantId(RequestContext.getTenantId());
                sysObjSright.setGroupId(g.getGroupId());
                if (!CollectionUtils.isEmpty(g.getOperates())) {
                    sysObjSright.setCreatorId(RequestContext.getUserId());
                    sysObjSright.setTenantId(RequestContext.getTenantId());
                    sysObjSright.setOperates(StringUtils.join(g.getOperates(), ","));
                    sysObjSrightMapper.insertSelective(sysObjSright);
                }
            }
            if (!CollectionUtils.isEmpty(g.getGroupRoles())) {
                List<FrightReqVo> groupRoles = g.getGroupRoles();
                SysObjFright sysObjFright = new SysObjFright();
                sysObjFright.setObjId(objId);
                sysObjFright.setObjType(groupObj.getObjType());
                sysObjFright.setGroupId(g.getGroupId());
                sysObjFright.setTenantId(RequestContext.getTenantId());
                groupRoles.forEach(r -> {
                    if (!CollectionUtils.isEmpty(r.getOperates())) {
                        sysObjFright.setObjFrightId(null);
                        sysObjFright.setGroupRoleId(r.getRoleId());
                        sysObjFright.setCreatorId(RequestContext.getUserId());
                        sysObjFright.setTenantId(RequestContext.getTenantId());
                        sysObjFright.setOperates(StringUtils.join(r.getOperates(), ","));
                        sysObjFrightMapper.insertSelective(sysObjFright);
                    }
                });
            }
        });
    }

    /**
     * 构造工组的对象权限列表
     *
     * @param groupObjBOs
     */
    private void buildGroupObjectPrivilege(List<SysGroupObjBO> groupObjBOs) {
        groupObjBOs.stream().forEach(v -> {
            if ("1".equals(v.getIsSright())) {
                // 构造授权表的权限列表
                buildSright(v);
            } else {
                // 构造不是授权表的权限列表
                buildSright(v);
                buildNoSright(v);
            }
        });
    }

    /**
     * 构造不是授权表的权限列表
     *
     * @param v
     */
    private void buildNoSright(SysGroupObjBO v) {
        SysGroup group = sysGroupMapper.selectByPrimaryKey(v.getGroupId());
        if (group == null) {
            return;
        }
        SysObjSrightBO objSrightBO = new SysObjSrightBO();
        objSrightBO.setObjId(v.getObjId());
        objSrightBO.setGroupId(group.getGroupId());
        objSrightBO.setGroupName(group.getGroupName());
        List<String> dicts = glbDictMapper.selectByDictCode(v.getObjType() + OP, null,
                objPermissionRulerService.listCanNotGrantObjOperate(v.getObjType()));
        objSrightBO.setOperateCNNames(dicts);
        objSrightBO.setOperateCNs(StringUtils.join(dicts, ","));
        objSrightBO.setObjType(v.getObjType());
        // 构造对象赋权列表
        buildFright(objSrightBO);
        v.getSysObjSrightBOs().add(0, objSrightBO);
    }

    /**
     * 构造授权表的权限列表
     *
     * @param v
     */
    private void buildSright(SysGroupObjBO v) {
        // 对象授权 begin
        SysObjSrightBO objSrightParams = new SysObjSrightBO();
        objSrightParams.setObjId(v.getObjId());
        List<SysObjSrightBO> objSrights = sysObjSrightMapper.selectGroupDetail(objSrightParams);
        objSrights.forEach(s -> {
            String operates = s.getOperates();
            if (StringUtils.isNotEmpty(operates)) {
                List<String> dicts = null;
                if ("*".equals(operates)) {
                    dicts = glbDictMapper.selectByDictCode(v.getObjType() + OP, null,
                            objPermissionRulerService.listCanNotGrantObjOperate(v.getObjType()));
                } else {
                    String[] array = operates.split("\\,");
                    dicts = glbDictMapper.selectByDictCode(v.getObjType() + OP, Arrays.asList(array),
                            objPermissionRulerService.listCanNotGrantObjOperate(v.getObjType()));
                }
                s.setOperateCNNames(dicts);
                s.setOperateCNs(StringUtils.join(dicts, ","));
            }
            // 构造对象赋权列表
            s.setObjType(v.getObjType());
            buildFright(s);
        });
        // 对象授权 end
        v.setSysObjSrightBOs(objSrights);
    }

    /**
     * 构造对象赋权列表
     *
     * @param s
     */
    private void buildFright(SysObjSrightBO s) {
        // 对象赋权 begin
        // 用groupId查找工组角色表，根据groupId,OBJ_ID,GROUP_ROLE_ID查找对应的操作列表
        SysObjFrightBO objFrightBOParams = new SysObjFrightBO();
        objFrightBOParams.setGroupId(s.getGroupId());
        objFrightBOParams.setObjId(s.getObjId());
        List<SysObjFrightBO> objFrightBOs = sysObjFrightMapper.selectGroupRoleDetail(objFrightBOParams);
        objFrightBOs.forEach(f -> {
            String fOperates = f.getOperates();
            if (StringUtils.isNotEmpty(fOperates)) {
                List<String> dicts = null;
                if ("*".equals(fOperates)) {
                    dicts = s.getOperateCNNames();
                } else {
                    String[] array = fOperates.split("\\,");
                    dicts = glbDictMapper.selectByDictCode(s.getObjType() + OP, Arrays.asList(array),
                            objPermissionRulerService.listCanNotGrantObjOperate(f.getObjType()));
                }
                f.setOperateCNNames(dicts);
                f.setOperateCNs(StringUtils.join(dicts, ","));
            }
        });
        // 对象赋权 end
        s.setSysObjFrightBOs(objFrightBOs);
    }

    /**
     * 递归查找工组ID
     *
     * @param groupId
     * @param groupIds
     */
    private void buildGroupIds(Long groupId, List<Long> groupIds) {
        SysGroupReqBo groupParams = new SysGroupReqBo();
        groupParams.setParentGroupId(groupId);
        groupParams.setTenantId(RequestContext.getTenantId());
        List<SysGroup> groups = sysGroupMapper.selectBySelective(groupParams);
        for (SysGroup group : groups) {
            groupIds.add(group.getGroupId());
            buildGroupIds(group.getGroupId(), groupIds);
        }
    }


    /**
     *  * @描述:获取对象修改是已授权信息
     *  * @方法名: getGroupObjDetaillist
     *  * @param objId
     *  * @param groupId
     *  * @return
     *  * @创建人：zhang
     *  * @创建时间：2019年5月26日上午11:27:25
     *  * @修改备注：
     *  * @throws
     *  
     */
    @Override
    public List<SysGroupObjBO> getGroupObjDetaillist(Long objId, Long groupId) {
        if (groupId != null && groupId.equals(Long.valueOf("-1"))) {
            // TODO: 临时处理应用点击赋权按钮，跳转页面时groupId只传-1的情况
            SysObj sysObj = new SysObj();
            sysObj.setObjId(objId);
            List<SysObj> sysObjList = sysObjViewMapper.selectObjBySelective(sysObj);
            if (sysObjList != null && sysObjList.size() > 0) {
                groupId = sysObjList.get(0).getGroupId();
            }
        }
        return sysGroupObjMapper.getGroupObjDetaillist(objId, groupId);
    }

    @Override
    public void deleteSrightByGroupId(Long groupId) {
        sysObjSrightMapper.deleteByGroupId(groupId);
    }

    @Override
    public void deleteFrightByGroupRoleId(Long groupRoleId) {
        sysObjFrightMapper.deleteByGroupRoleId(groupRoleId);
    }
}
