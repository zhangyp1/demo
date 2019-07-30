package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author WRP
 * @since 2018/8/7
 */
public class SrightFrightReqVo implements Serializable {

    private static final long serialVersionUID = -3003035846649215417L;

    private Long groupId;

    private Boolean isAdmin;

    private List<String> operates;

    private List<FrightReqVo> groupRoles;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<String> getOperates() {
        return operates;
    }

    public void setOperates(List<String> operates) {
        this.operates = operates;
    }

    public List<FrightReqVo> getGroupRoles() {
        return groupRoles;
    }

    public void setGroupRoles(List<FrightReqVo> groupRoles) {
        this.groupRoles = groupRoles;
    }
}
