package com.newland.paas.sbcommon.upload.vo;

import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;

/**
 * @author WRP
 * @since 2018/10/15
 */
public class TimeoutInterfaceVo {

    private MicroservicesItemProperties microservices;

    private String interfaceUrl;

    private String param;

    public MicroservicesItemProperties getMicroservices() {
        return microservices;
    }

    public void setMicroservices(MicroservicesItemProperties microservices) {
        this.microservices = microservices;
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
