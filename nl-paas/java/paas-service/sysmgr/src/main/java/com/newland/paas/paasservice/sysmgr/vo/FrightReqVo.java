package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author WRP
 * @since 2018/8/9
 */
public class FrightReqVo implements Serializable {

    private static final long serialVersionUID = 7978280096925045767L;

    private Long roleId;

    private List<String> operates;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<String> getOperates() {
        return operates;
    }

    public void setOperates(List<String> operates) {
        this.operates = operates;
    }
}
