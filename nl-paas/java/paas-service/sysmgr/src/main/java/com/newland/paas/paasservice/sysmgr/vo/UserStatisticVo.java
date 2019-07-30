package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户统计
 *
 * @author zhongqingjiang
 */
public class UserStatisticVo implements Serializable {

    public UserStatisticVo(int userCount, int onlineUserCount) {
        this.userCount = userCount;
        this.onlineUserCount = onlineUserCount;
        userStatisticByTenant = new ArrayList<>();
    }

    private int userCount;
    private int onlineUserCount;
    private List<UserStatisticByTenantVo> userStatisticByTenant;
    private List<UserStatisticByTenantVo> userStatisticByTenantTop;

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

    public List<UserStatisticByTenantVo> getUserStatisticByTenant() {
        return userStatisticByTenant;
    }

    public void setUserStatisticByTenant(List<UserStatisticByTenantVo> userStatisticByTenant) {
        this.userStatisticByTenant = userStatisticByTenant;
    }

    public List<UserStatisticByTenantVo> getUserStatisticByTenantTop() {
        return userStatisticByTenantTop;
    }

    public void setUserStatisticByTenantTop(List<UserStatisticByTenantVo> userStatisticByTenantTop) {
        this.userStatisticByTenantTop = userStatisticByTenantTop;
    }

    public void addUserCount(int num) {
        this.userCount += num;
    }

    public void addOnlineUserCount(int num) {
        this.onlineUserCount += num;
    }

    public void merge(UserStatisticByTenantVo item, boolean addToTotal) {
        UserStatisticByTenantVo existsItem = null;
        for (UserStatisticByTenantVo user : userStatisticByTenant) {
            if (user.getTenantId().equals(item.getTenantId())) {
                existsItem = user;
                break;
            }
        }
        if (existsItem != null) {
            existsItem.addUserCount(item.getUserCount());
            existsItem.addOnlineUserCount(item.getOnlineUserCount());
        } else {
            this.userStatisticByTenant.add(item);
        }

        if (addToTotal) {
            this.addUserCount(item.getUserCount());
            this.addOnlineUserCount(item.getOnlineUserCount());
        }
    }

    public void top(Integer top) {
        if (top > userStatisticByTenant.size()) {
            top = userStatisticByTenant.size();
        }
        userStatisticByTenantTop = userStatisticByTenant.subList(0, top);
    }
}
