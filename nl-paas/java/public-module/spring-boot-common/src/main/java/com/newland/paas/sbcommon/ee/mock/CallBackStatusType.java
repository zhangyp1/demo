package com.newland.paas.sbcommon.ee.mock;

/**
 * CallBackStatusType
 */
public enum CallBackStatusType {
    //回调状态 0 未回调，1 回调成功，-1 回调失败
    UN((short) 0),
    SUCCESS((short) 1),
    FAIL((short) -1);

    Short value;

    CallBackStatusType(Short value) {
        this.value = value;
    }

    /**
     * value
     *
     * @return
     */
    public Short value() {
        return value;
    }
}
