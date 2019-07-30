package com.newland.paas.paasservice.sysmgr.vo;

/**
 * @Description: 工组新增移除成员所属工组接口请求参数VO
 * @Author: SongYJ
 * @Date: 2018/8/7
 */
public class SysGroupReqVO {

    private Long userId;

    private Long[] groupIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long[] getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Long[] groupIds) {
        this.groupIds = groupIds;
    }
}
