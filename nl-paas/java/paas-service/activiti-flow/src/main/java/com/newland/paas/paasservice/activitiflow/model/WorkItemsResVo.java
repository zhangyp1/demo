package com.newland.paas.paasservice.activitiflow.model;

/**
 * @Description: 工作项返回参数实体
 * @Author: Sunxm
 * @Date: 2019/1/7
 */
public class WorkItemsResVo {
    //工作项id
    private String id;
    //工作项名称
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
