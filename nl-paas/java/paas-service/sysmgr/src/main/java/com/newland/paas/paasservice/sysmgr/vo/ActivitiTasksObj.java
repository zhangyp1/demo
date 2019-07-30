package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.vo.ActivitiTasksOutput
 * @Date 2018/8/6
 */
public class ActivitiTasksObj implements Serializable {

    private String id;
    private String url;
    private String name;
    private String tenantId;
    private String executionId;
    private String executionUrl;
    private String processInstanceId;
    private String processInstanceUrl;
    private String taskDefinitionKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getExecutionUrl() {
        return executionUrl;
    }

    public void setExecutionUrl(String executionUrl) {
        this.executionUrl = executionUrl;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceUrl() {
        return processInstanceUrl;
    }

    public void setProcessInstanceUrl(String processInstanceUrl) {
        this.processInstanceUrl = processInstanceUrl;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Override
    public String toString() {
        return "ActivitiTasksOutput{"
                + "id='" + id + '\''
                + ", url='" + url + '\''
                + ", name='" + name + '\''
                + ", tenantId='" + tenantId + '\''
                + ", executionId='" + executionId + '\''
                + ", executionUrl='" + executionUrl + '\''
                + ", processInstanceId='" + processInstanceId + '\''
                + ", processInstanceUrl='" + processInstanceUrl + '\''
                + '}';
    }
}
