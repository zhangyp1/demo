package com.newland.paas.sbcommon.vo;

/**
 * 返回消息对象
 *
 * @author wrp
 * @since 2018/6/20
 */
public class PaasMsg {

    public PaasMsg() {
    }

    public PaasMsg(String msgType, String content) {
        this.msgType = msgType;
        this.content = content;
    }

    /**
     * 消息显示方式
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
