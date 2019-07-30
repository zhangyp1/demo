package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgNotify;

import java.util.List;

/**
 * @author WRP
 * @since 2019/2/27
 */
public class MsgNotifyBO extends MsgNotify {

    private static final long serialVersionUID = 4110468061091243312L;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息类型
     */
    private Long msgTypeId;

    /**
     * 接收范围
     */
    private String receiverRange;

    /**
     * 接收范围详细ID
     */
    private String receivers;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息类型名称
     */
    private String typeName;

    /**
     * 消息描述
     */
    private String typeDesc;

    /**
     * 接收范围
     */
    private String receiverRangeDesc;

    /**
     * 消息ID列表
     */
    private List<Long> msgIds;

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public Long getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Long msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public String getReceiverRange() {
        return receiverRange;
    }

    public void setReceiverRange(String receiverRange) {
        this.receiverRange = receiverRange;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getReceiverRangeDesc() {
        return receiverRangeDesc;
    }

    public void setReceiverRangeDesc(String receiverRangeDesc) {
        this.receiverRangeDesc = receiverRangeDesc;
    }

    public List<Long> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<Long> msgIds) {
        this.msgIds = msgIds;
    }
}
