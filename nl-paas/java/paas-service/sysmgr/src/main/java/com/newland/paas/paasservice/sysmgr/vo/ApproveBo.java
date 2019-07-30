package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantLimitVo;

import java.util.List;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-08-17 11:17
 **/
public class ApproveBo {

    private String taskId;
    private String isAgree;
    private String comment;
    private List<SysTenantLimitVo> limitList;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SysTenantLimitVo> getLimitList() {
        return limitList;
    }

    public void setLimitList(List<SysTenantLimitVo> limitList) {
        this.limitList = limitList;
    }
}
