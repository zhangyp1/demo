package com.newland.paas.common.util.range;

import com.newland.paas.common.util.StringUtils;

public class VersionRangeUtils {

    /**
     * 根据版本范围返回处理的版本号
     * 
     * @param versionRange 版本范围 
     * 1-特定某个版本，如：7.0 等于
     * 2-版本区间范围，如：4.0-5.5 大于等于并且小于
     * 3-某个版本以上（包括该版本），如：6.0~ 大于等于
     * @author cjw
     * @date 2018年12月21日
     * @return 返回版本信息 rangeType类型 up:>= ,between:>= 并且 < ,equals:=
     */
    public static VersionRange getVersionRangeResult(String versionRange) {
        VersionRange versionRangeObject = new VersionRange();
        if(StringUtils.isNotEmpty(versionRange)){
            // 资产版本范围
            if (versionRange.indexOf("~") > -1) {
                // 版本之上
                StringBuilder sbd = new StringBuilder();
                String[] versionUp = versionRange.replace("~", "").split("\\.");
                if (null != versionUp && versionUp.length > 0) {
                    for (int i = 0; i < versionUp.length; i++) {
                        // 0 代表前面补充0
                        // 5 代表长度为5
                        // d 代表参数为正数型
                        sbd.append(String.format("%05d", Integer.valueOf(versionUp[i])));
                    }
                }
                versionRangeObject.setRangeType("up");
                versionRangeObject.setVersionRangeStart((sbd.toString()));
            } else if (versionRange.indexOf("-") > -1) {
                // 版本之间
                String[] version = versionRange.split("-");
                // 开始版本
                StringBuilder sbdStart = new StringBuilder();
                String[] versionRangeStart = version[0].split("\\.");
                if (null != versionRangeStart && versionRangeStart.length > 0) {
                    for (int i = 0; i < versionRangeStart.length; i++) {
                        sbdStart.append(String.format("%05d", Integer.valueOf(versionRangeStart[i])));
                    }
                }
                versionRangeObject.setVersionRangeStart(sbdStart.toString());
                // 结束版本
                StringBuilder sbdEnd = new StringBuilder();
                String[] versionRangeEnd = version[1].split("\\.");
                if (null != versionRangeEnd && versionRangeEnd.length > 0) {
                    for (int i = 0; i < versionRangeEnd.length; i++) {
                        sbdEnd.append(String.format("%05d", Integer.valueOf(versionRangeEnd[i])));
                    }
                }
                versionRangeObject.setRangeType("between");
                versionRangeObject.setVersionRangeEnd(sbdEnd.toString());
            } else {
                // 版本等于
                StringBuilder sbd = new StringBuilder();
                String[] version = versionRange.split("\\.");
                if (null != version && version.length > 0) {
                    for (int i = 0; i < version.length; i++) {
                        sbd.append(String.format("%05d", Integer.valueOf(version[i])));
                    }
                }
                versionRangeObject.setRangeType("equals");
                versionRangeObject.setVersionRangeStart(sbd.toString());
            }
        }
        return versionRangeObject;
    }
}
