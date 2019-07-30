package com.newland.paas.common.util.range;

import java.io.Serializable;

public class VersionRange implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3043188116879230553L;

    private String rangeType;
    private String versionRangeStart;
    private String versionRangeEnd;

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public String getVersionRangeStart() {
        return versionRangeStart;
    }

    public void setVersionRangeStart(String versionRangeStart) {
        this.versionRangeStart = versionRangeStart;
    }

    public String getVersionRangeEnd() {
        return versionRangeEnd;
    }

    public void setVersionRangeEnd(String versionRangeEnd) {
        this.versionRangeEnd = versionRangeEnd;
    }
}
