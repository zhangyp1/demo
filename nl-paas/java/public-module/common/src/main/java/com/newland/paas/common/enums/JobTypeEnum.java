package com.newland.paas.common.enums;

public enum JobTypeEnum {

    BEAN("JavaBean任务"), K8S_JOB("k8s任务"), SCRIPT_JOB("脚本任务"), JOB_FLOW("任务流");
    private String desc;

    private JobTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
