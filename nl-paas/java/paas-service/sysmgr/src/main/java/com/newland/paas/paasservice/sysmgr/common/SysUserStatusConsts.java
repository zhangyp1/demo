package com.newland.paas.paasservice.sysmgr.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户状态常量
 * 
 * @author Frown
 * @date 2018/07/23
 */
public enum SysUserStatusConsts {
    NOTLOGIN("notLogin"), DISABLED("disabled"), ENABLED("enabled");
    private final String value;

    private static final Integer INITIALCAPACITY = 8;

    SysUserStatusConsts(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取值
     * 
     * @return
     */
    public static List<String> getValues() {
        List<String> values = new ArrayList<>(INITIALCAPACITY);
        for (SysUserStatusConsts resourceStatus : SysUserStatusConsts.values()) {
            values.add(resourceStatus.getValue());
        }
        return values;
    }

    /**
     * 判断是否包含这个value
     * 
     * @param value
     * @return
     */
    public static boolean includes(String value) {
        return SysUserStatusConsts.getValues().contains(value);
    }

}
