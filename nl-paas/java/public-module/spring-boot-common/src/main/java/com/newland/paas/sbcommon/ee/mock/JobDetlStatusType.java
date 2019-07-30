package com.newland.paas.sbcommon.ee.mock;

import org.apache.commons.lang.StringUtils;

/**
 * 任务明细状态：等待、运行中、成功、失败、未知
 */
public enum JobDetlStatusType {
    //等待、运行中、成功、失败、未知
    WAIT("wait"), RUN("run"), SUCC("succ"), FAILURE("failure"), UNKNOWN("unknown");

    String value;

    JobDetlStatusType(String value) {
        this.value = value;
    }

    /**
     * JobDetlStatusType
     * @param statusTypeStr
     * @return
     */
    public static JobDetlStatusType convert(String statusTypeStr) {
        if (StringUtils.isBlank(statusTypeStr)) {
            return null;
        }
        JobDetlStatusType rt;
        try {
            rt = JobDetlStatusType.valueOf(statusTypeStr.toUpperCase());
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
