package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 按租户统计用户
 *
 * @author zhongqingjiang
 */
public class UserStatisticByTenantVo implements Serializable, Comparable<UserStatisticByTenantVo> {

    public UserStatisticByTenantVo(Long tenantId, int userCount, int onlineUserCount) {
        this.tenantId = tenantId;
        this.userCount = userCount;
        this.onlineUserCount = onlineUserCount;
    }

    private Long tenantId;
    private int userCount;
    private int onlineUserCount;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getOnlineUserCount() {
        return onlineUserCount;
    }

    public void setOnlineUserCount(int onlineUserCount) {
        this.onlineUserCount = onlineUserCount;
    }

    @Override
    public int compareTo(UserStatisticByTenantVo o) {
        return o.getOnlineUserCount() - this.getOnlineUserCount();
    }

    public void addUserCount(int num) {
        this.userCount += num;
    }

    public void addOnlineUserCount(int num) {
        this.onlineUserCount += num;
    }
}
