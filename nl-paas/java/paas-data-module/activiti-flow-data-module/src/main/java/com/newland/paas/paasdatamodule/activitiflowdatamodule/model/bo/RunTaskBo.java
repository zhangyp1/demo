package com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo;

import java.util.List;

/**
 * @author WRP
 * @since 2019/7/12
 */
public class RunTaskBo {

    /**
     * 工单号（流程实例ID）
     */
    private String processInstanceId;
    /**
     * 流程定义的key
     */
    private String processDifintionKey;
    /**
     * 流程定义名称
     */
    private String processDefinitionName;
    /**
     * 流程KEY（工单标题）
     */
    private String businessKey;

    /**
     * 接收人
     */
    private List<String> users;
    /**
     * 接收组
     */
    private List<String> groups;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDifintionKey() {
        return processDifintionKey;
    }

    public void setProcessDifintionKey(String processDifintionKey) {
        this.processDifintionKey = processDifintionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
