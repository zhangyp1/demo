package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 系统分组权限请求Vo
 *
 * @author zhongqingjiang
 */
public class SysCategoryPermissionReqVo implements Serializable {

    private String likeCategoryName;

    private Long byGroupId;

    public String getLikeCategoryName() {
        return likeCategoryName;
    }

    public void setLikeCategoryName(String likeCategoryName) {
        this.likeCategoryName = likeCategoryName;
    }

    public Long getByGroupId() {
        return byGroupId;
    }

    public void setByGroupId(Long byGroupId) {
        this.byGroupId = byGroupId;
    }
}
