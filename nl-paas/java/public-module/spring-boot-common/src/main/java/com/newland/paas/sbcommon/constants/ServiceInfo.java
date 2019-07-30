package com.newland.paas.sbcommon.constants;

import com.newland.paas.sbcommon.utils.MicrosevicesUtils;

/**
 * @author WRP
 * @since 2019/5/9
 */
public class ServiceInfo {

    private static String value;

    static {
        try {
            String containerHostName = MicrosevicesUtils.getHostName();
            String containerIp = MicrosevicesUtils.getHostIp();
            String physicalIp = MicrosevicesUtils.getPhysicalIp();

            StringBuilder sb = new StringBuilder();
            sb.append("[physicalIp: ").append(physicalIp)
                    .append(",containerIp: ").append(containerIp)
                    .append(",containerHostName: ").append(containerHostName)
                    .append("]");

            value = sb.toString();
        } catch (Exception e) {
        }
    }

    public static String getValue() {
        return value;
    }

}
