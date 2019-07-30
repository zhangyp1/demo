package com.newland.paas.paasservice.sysmgr.common;

/**
 * 消息默认类型，必须与库表对应
 *
 * @author WRP
 * @since 2019/2/28
 */
public enum MsgDefaultTypeEnum {

    ALARM(1L, "告警消息"),
    ACTIVITI(2L, "工单消息"),
    NOTICE(3L, "通知消息");

    /**
     * 值
     */
    private final Long value;
    /**
     * 描述
     */
    private final String desc;

    MsgDefaultTypeEnum(Long value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Long getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
