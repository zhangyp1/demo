package com.newland.paas.paasservice.sysmgr.common;

import org.apache.commons.lang.StringUtils;

/**
 * 资源配额单位
 *
 *  author: Frown
 */
public enum ResLimitUnitConstants {
    CPUUNIT("cpu", "个"), MACHINEUNIT("machine", "台"), MEMORYUNIT("memory", "Gb"), STORAGEUNIT("storage", "Gb");
    private final String code;
    private final String value;

    ResLimitUnitConstants(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取单元
     * @param code
     * @return
     */
    public static String getUnit(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ResLimitUnitConstants resLimitUnitConstants : ResLimitUnitConstants.values()) {
            if (resLimitUnitConstants.getCode().equals(code)) {
                return resLimitUnitConstants.getValue();
            }
        }
        return null;
    }

}
