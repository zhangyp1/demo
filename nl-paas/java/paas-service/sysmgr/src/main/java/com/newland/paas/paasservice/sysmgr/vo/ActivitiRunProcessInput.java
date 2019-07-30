package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.vo.ActivitiRunProcessInput
 * @Date 2018/8/7
 */
public class ActivitiRunProcessInput {

    private String processDefinitionKey;
    private String businessKey;
    private List<ActivitiVariable> variables;

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

    public List<ActivitiVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<ActivitiVariable> variables) {
        this.variables = variables;
    }
}
