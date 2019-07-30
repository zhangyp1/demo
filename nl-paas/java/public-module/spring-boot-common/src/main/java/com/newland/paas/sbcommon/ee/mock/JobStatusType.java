package com.newland.paas.sbcommon.ee.mock;

import org.apache.commons.lang.StringUtils;

/**
 * JobStatusType
 */
public enum JobStatusType {
    //开始
    START("start"),
    //就绪
    READY("ready"),
    //运行
    RUN("run"),
    //成功
    SUCC("succ"),
    //回滚
    ROLLBACK("rollback"),
    //失败
    FAILURE("failure"),
    //未知
    UNKNOWN("unknown");

    String value;

    JobStatusType(String value) {
        this.value = value;
    }

    /**
     * JobStatusType
     *
     * @param value
     * @return
     */
    public static JobStatusType convert(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            JobStatusType rt = valueOf(value.toUpperCase());
            return rt;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
