package com.newland.paas.paasservice.activitiflow.model;

/**
 * 流程定义
 *
 * @author WRP
 * @since 2019/1/30
 */
public class ProcessDefinetionVo {
    /**
     * 模板的key
     */
    private String key;
    /**
     * 模板名称
     */
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
