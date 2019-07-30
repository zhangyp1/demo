package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 * @author WRP
 * @since 2018/9/7
 */
public class GroupBean {

    private List<Long> groupIds;

    private List<Long> adminGroupIds;

    public GroupBean() {
    }

    public GroupBean(List<Long> groupIds, List<Long> adminGroupIds) {
        this.groupIds = groupIds;
        this.adminGroupIds = adminGroupIds;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public List<Long> getAdminGroupIds() {
        return adminGroupIds;
    }

    public void setAdminGroupIds(List<Long> adminGroupIds) {
        this.adminGroupIds = adminGroupIds;
    }
}
