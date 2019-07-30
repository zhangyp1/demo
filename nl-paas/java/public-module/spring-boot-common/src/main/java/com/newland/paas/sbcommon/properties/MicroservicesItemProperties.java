package com.newland.paas.sbcommon.properties;

/**
 * 微服务属性配置
 *
 * @author wrp
 */
public class MicroservicesItemProperties {

    /**
     * 服务名
     */
    private String serviceId;
    /**
     * 应用上下文
     */
    private String contentPath;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }
}
