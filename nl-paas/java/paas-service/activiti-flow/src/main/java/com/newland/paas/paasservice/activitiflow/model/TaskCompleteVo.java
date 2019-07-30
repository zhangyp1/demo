package com.newland.paas.paasservice.activitiflow.model;

import java.util.Map;

/**
 * 执行任务实体
 */
public class TaskCompleteVo {
    /**
     * 当前任务id
     */
    private String taskId;
    /**
     * 任务完成参数
     */
    private Map<String, Object> variables;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
