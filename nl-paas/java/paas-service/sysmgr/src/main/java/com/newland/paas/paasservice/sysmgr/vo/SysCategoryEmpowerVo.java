package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryEmpower;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统分组权限 授予的角色
 *
 * @author zhongqingjiang
 */
public class SysCategoryEmpowerVo extends SysCategoryEmpower {

    public SysCategoryEmpowerVo() {
        this.operateList = new ArrayList<>();
        this.optionalOperateList = new ArrayList<>();
    }

    private String groupRoleName;

    private List<ObjectOperateVo> operateList;

    private List<ObjectOperateVo> optionalOperateList;

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName;
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
}
