package com.newland.paas.sbcommon.activiti.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动流程
 *
 * @author WRP
 * @since 2019/7/1
 */
public class StartProcessVo {
    /**
     * 接口地址
     */
    public static final String INTERFACE_URL = "runtime/process-instances";

    /**
     * 持久卷绑定集群申请流程
     */
    public static final String BIND_CLUSTER_VOL_APPLY = "BindClusterVolApply";

    /**
     * 流程定义key，require
     */
    private String processDefinitionKey;

    /**
     * 工单标题，require
     */
    private String businessKey;

    /**
     * 流程实例变量，（group_id变量必传）
     */
    private List<StartProcessVariable> variables;

    /**
     * StartProcessVo
     *
     * @param processDefinitionKey
     * @param businessKey
     */
    public StartProcessVo(String processDefinitionKey, String businessKey) {
        this.processDefinitionKey = processDefinitionKey;
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public List<StartProcessVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<StartProcessVariable> variables) {
        this.variables = variables;
    }

    /**
     * 添加变量
     *
     * @param variable
     */
    public StartProcessVo addVariables(StartProcessVariable variable) {
        if (variables == null) {
            variables = new ArrayList<>();
        }
        variables.add(variable);
        return this;
    }

    /**
     * 添加变量
     *
     * @param name
     * @param value
     */
    public StartProcessVo addVariables(String name, Object value) {
        StartProcessVariable variable = new StartProcessVariable(name, value);
        if (variables == null) {
            variables = new ArrayList<>();
        }
        variables.add(variable);
        return this;
    }

}
