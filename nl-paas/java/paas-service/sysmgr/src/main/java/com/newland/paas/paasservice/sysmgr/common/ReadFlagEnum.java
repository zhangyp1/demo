package com.newland.paas.paasservice.sysmgr.common;

/**
 * 读取标志：0 未读，1 已读
 *
 * @author WRP
 * @since 2019/2/28
 */
public enum ReadFlagEnum {

    UNREAD(0L, "未读"),
    READ(1L, "已读");

    /**
     * 值
     */
    private final Long value;
    /**
     * 描述
     */
    private final String desc;

    ReadFlagEnum(Long value, String desc) {
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
