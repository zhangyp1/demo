package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MsgNotify implements Serializable, Comparable<MsgNotify> {
    private static final long serialVersionUID = 755030349993933578L;

    private Long notifyId;

    private Long msgId;

    private Long receiverId;

    private Long readFlag;

    private Long tenantId;

    private Long creatorId;

    private Date createDate;

    private Date changeDate;

    private Long delFlag;

    public MsgNotify() {
    }

    public MsgNotify(Long msgId, Long receiverId, Long tenantId, Long creatorId) {
        this.msgId = msgId;
        this.receiverId = receiverId;
        this.tenantId = tenantId;
        this.creatorId = creatorId;
    }

    public Long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Long readFlag) {
        this.readFlag = readFlag;
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

    @Override
    public int compareTo(MsgNotify o) {
        return (Objects.equals(msgId, o.getMsgId())
                && Objects.equals(receiverId, o.getReceiverId())
                && Objects.equals(tenantId, o.getTenantId())) ? 0 : 1;
    }
}