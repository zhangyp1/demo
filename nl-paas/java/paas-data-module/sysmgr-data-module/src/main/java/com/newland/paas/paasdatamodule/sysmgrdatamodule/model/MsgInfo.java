package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

public class MsgInfo implements Serializable {
    private static final long serialVersionUID = 336945555308701370L;

    private Long msgId;

    @NotEmpty(message = "消息标题不能为空")
    private String msgTitle;

    private Long msgTypeId;

    private Long msgStatus;

    private String receiverRange;

    private String receivers;

    private Long tenantId;

    private Long creatorId;

    private Date createDate;

    private Date changeDate;

    private Long delFlag;

    private String msgContent;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle == null ? null : msgTitle.trim();
    }

    public Long getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Long msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public Long getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Long msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getReceiverRange() {
        return receiverRange;
    }

    public void setReceiverRange(String receiverRange) {
        this.receiverRange = receiverRange == null ? null : receiverRange.trim();
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers == null ? null : receivers.trim();
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }
}