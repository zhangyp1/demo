package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;

/**
 * 系统分组
 *
 * @author zhongqingjiang
 */
public class SysCategoryVo extends SysCategory {

    public String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
