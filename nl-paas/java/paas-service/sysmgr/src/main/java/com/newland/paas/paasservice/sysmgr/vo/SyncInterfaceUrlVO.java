package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 同步接口返回值
 *
 * @author WRP
 * @since 2019/1/19
 */
public class SyncInterfaceUrlVO implements Serializable {

    private static final long serialVersionUID = -4769032950022093213L;
    private String serviceId;
    private Boolean success;
    private String msg;

    public SyncInterfaceUrlVO(String serviceId, Boolean success) {
        this.serviceId = serviceId;
        this.success = success;
    }

    public SyncInterfaceUrlVO(String serviceId, Boolean success, String msg) {
        this.serviceId = serviceId;
        this.success = success;
        this.msg = msg;
    }

    public SyncInterfaceUrlVO() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
