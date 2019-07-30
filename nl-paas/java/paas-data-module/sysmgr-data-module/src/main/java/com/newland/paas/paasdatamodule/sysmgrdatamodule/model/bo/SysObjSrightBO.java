package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjSright;

import java.util.List;

/**
 * 对象授权表+关联的详细信息
 * @author WRP
 * @since 2018/8/2
 */
public class SysObjSrightBO extends SysObjSright {

    private String objName;

    private String groupName;

    private List<String> operateNames;

    private String operateCNs;

    private List<String> operateCNNames;

    private List<SysObjFrightBO> sysObjFrightBOs;

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public List<SysObjFrightBO> getSysObjFrightBOs() {
        return sysObjFrightBOs;
    }

    public void setSysObjFrightBOs(List<SysObjFrightBO> sysObjFrightBOs) {
        this.sysObjFrightBOs = sysObjFrightBOs;
    }
}
