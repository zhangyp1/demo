package com.newland.paas.sbcommon.vo;

import com.newland.paas.sbcommon.config.AppVersion;

import java.io.Serializable;

/**
 * 应答body结构
 *
 * @param <T>
 * @author wrp
 */
public class BasicResponseContentVo<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8748287509576459772L;

    /**
     * 返回数据
     */
    private T content;

    /**
     * 错误返回对象
     */
    private PaasError error;

    /**
     * 返回消息对象
     */
    private PaasMsg msg;

    private AppVersion appVersion;

    private String traceId;

    public BasicResponseContentVo() {}

    /**
     * 默认成功
     * 
     * @param content
     */
    public BasicResponseContentVo(T content) {
        this.content = content;
    }

    /**
     * 构造异常
     * 
     * @param error
     */
    public BasicResponseContentVo(PaasError error) {
        this.msg = new PaasMsg("2", error.getDescription());
        this.error = error;
    }

    public BasicResponseContentVo(PaasMsg msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public PaasError getError() {
        return error;
    }

    public void setError(PaasError error) {
        this.error = error;
    }

    public PaasMsg getMsg() {
        return msg;
    }

    public void setMsg(PaasMsg msg) {
        this.msg = msg;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
