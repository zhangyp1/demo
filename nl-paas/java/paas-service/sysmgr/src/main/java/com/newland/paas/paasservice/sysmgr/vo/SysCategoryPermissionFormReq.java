package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统分组权限生成表单请求
 *
 * @author zhongqingjiang
 */
public class SysCategoryPermissionFormReq implements Serializable {

    public SysCategoryPermissionFormReq() {
        this.groupIdList = new ArrayList<>();
    }

    private Long categoryId;

    private List<Long> groupIdList;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<Long> groupIdList) {
        this.groupIdList = groupIdList;
    }
}
