package com.newland.paas.paasservice.sysmgr.vo;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.vo.ActivitiVariable
 * @Date 2018/8/7
 */
public class ActivitiVariable {

    private String name;
    private Object value;
    private String type;
    private String scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
