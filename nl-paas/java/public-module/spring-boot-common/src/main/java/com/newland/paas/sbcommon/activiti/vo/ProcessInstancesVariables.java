package com.newland.paas.sbcommon.activiti.vo;

/**
 * 流程实例变量
 *
 * @author WRP
 * @since 2019/7/4
 */
public class ProcessInstancesVariables {

    /**
     * 获得流程实例的一个变量
     */
    public static final String INTERFACE_URL = "runtime/process-instances/{0}/variables/{1}";

    private String name;

    private String type;

    private Object value;

    private String scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
