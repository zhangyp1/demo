package com.newland.paas.sbcommon.activiti.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行处理
 *
 * @author WRP
 * @since 2019/7/1
 */
public class ExecTaskVo {
    /**
     * 认领任务
     */
    public static final String CLAIM_URL = "runtime/tasks/{0}";
    /**
     * 审批
     */
    public static final String APPROVAL_URL = "form/form-data";

    /**
     * 任务ID，require
     */
    private Long taskId;

    private String processInstanceId;

    /**
     * 任务变量，（approve 是否同意【true,false】必传，remark 审批备注必传）
     *
     */
    private List<ExecTaskPropertie> properties;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public List<ExecTaskPropertie> getProperties() {
        return properties;
    }

    public void setProperties(List<ExecTaskPropertie> properties) {
        this.properties = properties;
    }

    /**
     * 添加变量
     *
     * @param prop
     */
    public ExecTaskVo addVariables(ExecTaskPropertie prop) {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(prop);
        return this;
    }

    /**
     * 添加变量
     *
     * @param id
     * @param value
     */
    public ExecTaskVo addVariables(String id, String value) {
        ExecTaskPropertie prop = new ExecTaskPropertie(id, value);
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(prop);
        return this;
    }

}
