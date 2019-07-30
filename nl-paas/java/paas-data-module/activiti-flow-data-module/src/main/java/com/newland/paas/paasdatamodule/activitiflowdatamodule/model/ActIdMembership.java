package com.newland.paas.paasdatamodule.activitiflowdatamodule.model;

/**
 * act_id_membership（用户与工组、角色关系，用户为此工组、角色的管理员）表
 *
 * @author WRP
 * @since 2019/7/4
 */
public class ActIdMembership {

    private String userId;

    private String groupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
