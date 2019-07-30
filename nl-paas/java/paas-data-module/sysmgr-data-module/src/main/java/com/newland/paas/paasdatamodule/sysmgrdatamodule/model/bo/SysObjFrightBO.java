package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjFright;

import java.util.List;

/**
 * @author WRP
 * @since 2018/8/4
 */
public class SysObjFrightBO extends SysObjFright {

    private String groupName;

    private String groupRoleName;

    private List<String> operateNames;

    private String operateCNs;

    private List<String> operateCNNames;

    private Long userId;

    private Long groupRoleId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupRoleName() {
        return groupRoleName;
    }

    public void setGroupRoleName(String groupRoleName) {
        this.groupRoleName = groupRoleName;
    }

    public List<String> getOperateNames() {
        return operateNames;
    }

    public void setOperateNames(List<String> operateNames) {
        this.operateNames = operateNames;
    }

    public String getOperateCNs() {
        return operateCNs;
    }

    public void setOperateCNs(String operateCNs) {
        this.operateCNs = operateCNs;
    }

    public List<String> getOperateCNNames() {
        return operateCNNames;
    }

    public void setOperateCNNames(List<String> operateCNNames) {
        this.operateCNNames = operateCNNames;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Long getGroupRoleId() {
        return groupRoleId;
    }

    @Override
    public void setGroupRoleId(Long groupRoleId) {
        this.groupRoleId = groupRoleId;
    }
}
