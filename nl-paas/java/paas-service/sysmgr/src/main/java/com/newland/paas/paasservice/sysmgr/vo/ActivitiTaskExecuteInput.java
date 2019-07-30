package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;
import java.util.Map;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.vo.ActivitiTaskStartOutput
 * @Date 2018/8/6
 */
public class ActivitiTaskExecuteInput {

    private String taskId;
    private List<Map<String, String>> properties;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Map<String, String>> getProperties() {
        return properties;
    }

    public void setProperties(List<Map<String, String>> properties) {
        this.properties = properties;
    }

}
