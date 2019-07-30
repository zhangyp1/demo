package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupObj;

import java.util.List;

/**
 * 对象详情
 * @author WRP
 * @since 2018/8/2
 */
public class SysGroupObjBO extends SysGroupObj {

    private String userName;

    private String objTypeName;

    private String sysCategoryName;

    private List<SysObjSrightBO> sysObjSrightBOs;

    private Boolean isAdmin;

    private String wholePath;

    private String groupName;

    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getWholePath() {
		return wholePath;
	}

	public void setWholePath(String wholePath) {
		this.wholePath = wholePath;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getObjTypeName() {
        return objTypeName;
    }

    public void setObjTypeName(String objTypeName) {
        this.objTypeName = objTypeName;
    }

    public String getSysCategoryName() {
        return sysCategoryName;
    }

    public void setSysCategoryName(String sysCategoryName) {
        this.sysCategoryName = sysCategoryName;
    }

    public List<SysObjSrightBO> getSysObjSrightBOs() {
        return sysObjSrightBOs;
    }

    public void setSysObjSrightBOs(List<SysObjSrightBO> sysObjSrightBOs) {
        this.sysObjSrightBOs = sysObjSrightBOs;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
