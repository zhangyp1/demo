package com.newland.paas.sbcommon.activiti.vo;

/**
 * 历史流程变量-变量数据
 *
 * @author WRP
 * @since 2019/7/24
 */
public class HVariable {

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
