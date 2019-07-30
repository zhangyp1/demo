package com.newland.paas.sbcommon.vo;

import java.text.MessageFormat;

public class PaasError {

    /**
     * 错误序列
     */
    private String seq;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误的概要描述
     */
    private String description;
    /**
     * 解决方案
     */
    private String solution;

    private Object[] msgArguments;

    private PaasError() {}

    public PaasError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public PaasError(String code, String description, String solution) {
        super();
        this.code = code;
        this.description = description;
        this.solution = solution;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        if (this.msgArguments != null) {
            return MessageFormat.format(this.description, this.msgArguments);
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMsgArguments(Object... arguments) {
        this.msgArguments = arguments;
        // MessageFormat.format(this.description, arguments);
    }

    public Object[] getMsgArguments() {
        return msgArguments;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {

        this.solution = solution;
    }

}
