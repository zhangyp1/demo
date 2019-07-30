package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.SessionDetail;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;
import com.newland.paas.paasservice.sysmgr.service.*;
import com.newland.paas.paasservice.sysmgr.vo.*;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 系统统计实现类
 *
 * @author zhongqingjiang
 */
@Service
public class SysStatisticServiceImpl implements SysStatisticService {

    private static final Log log = LogFactory.getLogger(SysStatisticServiceImpl.class);

    @Autowired
    SysTenantService sysTenantService;
    @Autowired
    SysTenantMapper sysTenantMapper;
    @Autowired
    SysTenantMemberService sysTenantMemberService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysGroupService sysGroupService;
    @Autowired
    SysCategoryService sysCategoryService;

    @Override
    public TenantStatisticVo getTenantStatistic() {
        TenantStatisticVo tenantStatisticVo = new TenantStatisticVo();
        int tenantCount = sysTenantService.tenantCount();
        tenantStatisticVo.setTenantCount(tenantCount);
        return tenantStatisticVo;
    }

    @Override
    public UserStatisticVo getUserStatistic(Integer top) {
        UserStatisticVo userStatisticVo = new UserStatisticVo(0, 0);

        // 获取当前所有用户，包含没有关联租户的、未审批通过的、审批不通过的用户
        SysUser sysUserReq = new SysUser();
        List<SysUser> sysUserList = sysUserMapper.listSysUser(sysUserReq);
        if (sysUserList == null) {
            sysUserList = new ArrayList<>();
        }
        // 获取当前会话列表
        List<SessionDetail> sessionDetailList = sysUserService.getSessionList();
        if (sessionDetailList == null) {
            sessionDetailList = new ArrayList<>();
        }
        HashSet<Long> onlineUserSet = new HashSet<>();
        for (SessionDetail sessionDetail : sessionDetailList) {
            Long userId = sessionDetail.getUserId();
            if (!onlineUserSet.contains(userId)) {
                onlineUserSet.add(userId);
            }
        }
        // 获取当前所有租户的成员列表
        SysTenantMember sysTenantMemberReq = new SysTenantMember();
        List<SysTenantMember> sysTenantMemberList = sysTenantMemberService.selectBySelective(sysTenantMemberReq);

        // 统计系统用户数和在线用户数
        userStatisticVo.setUserCount(sysUserList.size());
        userStatisticVo.setOnlineUserCount(sessionDetailList.size());
        for (SysTenantMember sysTenantMember : sysTenantMemberList) {
            // 统计租户用户数
            Long tenantId = sysTenantMember.getTenantId();
            Long userId = sysTenantMember.getUserId();
            String status = sysTenantMember.getStatus();
            // TODO: 使用了魔数 用户ID 14 15
            if (userId.equals(14L) || userId.equals(15L)) {
                // 过滤内部用户 umonitor 和 uplan
                continue;
            }
            // TODO: 使用了魔数 租户成员状态 1
            if (!status.equals("1")) {
                // 过滤未审批通过的用户
                continue;
            }
            UserStatisticByTenantVo userStatisticByTenantVo = new UserStatisticByTenantVo(tenantId, 1, 0);
            // 统计租户在线用户数
            if (onlineUserSet.contains(userId)) {
                userStatisticByTenantVo.setOnlineUserCount(1);
            }
            userStatisticVo.merge(userStatisticByTenantVo, false);
        }

        // 倒序排序
        userStatisticVo.getUserStatisticByTenant().sort(UserStatisticByTenantVo::compareTo);

        if (top != null) {
            userStatisticVo.top(top);
        }

        return userStatisticVo;
    }

    @Override
    public SysCategoryStatisticVo getSysCategoryStatistic() {
        SysCategoryStatisticVo sysCategoryStatisticVo = new SysCategoryStatisticVo();
        sysCategoryStatisticVo.setSysCategoryCount(sysCategoryService.countSysCategory());
        return sysCategoryStatisticVo;
    }

    @Override
    public List<SysCategoryListByTenantVo> listSysCategory() throws ApplicationException {
        List<SysCategoryListByTenantVo> sysCategoryListByTenantVoList = new ArrayList<>();
        // 获取所有租户的工组
        SysGroupReqBo sysGroupReqBo = new SysGroupReqBo();
        List<SysGroup> allTenantGroupList = sysGroupService.list(sysGroupReqBo);
        // 按租户分组
        HashMap<Long, List<Long>> tenantId_groupIdList_map = new HashMap<>();
        if (allTenantGroupList != null && allTenantGroupList.size() > 0) {
            for (SysGroup sysGroup : allTenantGroupList) {
                Long tenantId = sysGroup.getTenantId();
                if (!tenantId_groupIdList_map.containsKey(tenantId)) {
                    tenantId_groupIdList_map.put(tenantId, new ArrayList<>());
                }
                tenantId_groupIdList_map.get(tenantId).add(sysGroup.getGroupId());
            }
        }
        // 根据工组获取系统分组列表
        for (Long tenantId : tenantId_groupIdList_map.keySet()) {
            List<Long> groupIdList = tenantId_groupIdList_map.get(tenantId);
            List<SysCategoryVO> sysCategoryVOList = sysCategoryService.listSysCategoryByGroupId(groupIdList);
            SysCategoryListByTenantVo sysCategoryListByTenantVo = new SysCategoryListByTenantVo();
            sysCategoryListByTenantVo.setTenantId(tenantId);
            sysCategoryListByTenantVo.setSysCategoryList(sysCategoryVOList);
            sysCategoryListByTenantVoList.add(sysCategoryListByTenantVo);
        }

        return sysCategoryListByTenantVoList;
    }
}
