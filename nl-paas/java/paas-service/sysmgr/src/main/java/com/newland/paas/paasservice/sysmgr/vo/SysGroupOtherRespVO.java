package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 * @Description: 成员关联工组-工组列表查询返回参数VO
 * @Author: SongYJ
 * @Date: 2018/8/7
 */
public class SysGroupOtherRespVO {

    //工组列表
    private List<SysGroupForMemberRespVO> allGroups;

    //已属工组ID列表
    private List<Long> groupIds;

    public List<SysGroupForMemberRespVO> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<SysGroupForMemberRespVO> allGroups) {
        this.allGroups = allGroups;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }
}
