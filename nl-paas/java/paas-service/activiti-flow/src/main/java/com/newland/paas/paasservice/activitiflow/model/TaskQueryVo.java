package com.newland.paas.paasservice.activitiflow.model;

/**
 * 任务查询
 *
 * @author WRP
 * @since 2019/1/30
 */
public class TaskQueryVo {
    //工单号
    private String id;
    //名称
    private String name;
    //流程定义的key
    private String processDifintionKey;
    //是否结束
    private Boolean isFinished;
    //参数名称
    private String variableName;
    //参数值
    private String variableValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessDifintionKey() {
        return processDifintionKey;
    }

    public void setProcessDifintionKey(String processDifintionKey) {
        this.processDifintionKey = processDifintionKey;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }
}
