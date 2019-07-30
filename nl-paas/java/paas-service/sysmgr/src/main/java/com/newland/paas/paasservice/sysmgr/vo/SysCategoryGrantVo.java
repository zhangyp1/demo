package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryGrant;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统分组权限 授予的工组
 *
 * @author zhongqingjiang
 */
public class SysCategoryGrantVo extends SysCategoryGrant {

    public SysCategoryGrantVo() {
        this.operateList = new ArrayList<>();
        this.optionalOperateList = new ArrayList<>();
        this.groupRoleEmpowerList = new ArrayList<>();
    }

    private String groupName;

    // 是否是该系统分组的所属工组
    private Boolean bCategoryOwner;

    private List<ObjectOperateVo> operateList;

    private List<ObjectOperateVo> optionalOperateList;

    private List<SysCategoryEmpowerVo> groupRoleEmpowerList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getbCategoryOwner() {
        return bCategoryOwner;
    }

    public void setbCategoryOwner(Boolean bCategoryOwner) {
        this.bCategoryOwner = bCategoryOwner;
    }

    public List<ObjectOperateVo> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<ObjectOperateVo> operateList) {
        this.operateList = operateList;
    }

    public List<ObjectOperateVo> getOptionalOperateList() {
        return optionalOperateList;
    }

    public void setOptionalOperateList(List<ObjectOperateVo> optionalOperateList) {
        this.optionalOperateList = optionalOperateList;
    }

    public List<SysCategoryEmpowerVo> getGroupRoleEmpowerList() {
        return groupRoleEmpowerList;
    }

    public void setGroupRoleEmpowerList(List<SysCategoryEmpowerVo> groupRoleEmpowerList) {
        this.groupRoleEmpowerList = groupRoleEmpowerList;
    }
}
