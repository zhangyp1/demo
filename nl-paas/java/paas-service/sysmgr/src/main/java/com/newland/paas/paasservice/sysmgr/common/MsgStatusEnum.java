package com.newland.paas.paasservice.sysmgr.common;

/**
 * 消息状态 0 保存，1 发布
 *
 * @author WRP
 * @since 2019/2/28
 */
public enum MsgStatusEnum {

    ADD(0L, "保存"),
    PUBLISH(1L, "发布");

    /**
     * 值
     */
    private final Long value;
    /**
     * 描述
     */
    private final String desc;

    MsgStatusEnum(Long value, String desc) {
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
