package com.newland.paas.sbcommon.activiti.vo;

/**
 * 启动流程-参数
 *
 * @author WRP
 * @since 2019/7/1
 */
public class StartProcessVariable {

    public static final String GROUP_ID = "groupId";
    public static final String APPLY_USER_ID = "applyUserId";

    private String name;
    private Object value;

    public StartProcessVariable() {
    }

    public StartProcessVariable(String name, Object value) {
        this.name = name;
        this.value = value;
    }

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
}
