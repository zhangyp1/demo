package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgType;

/**
 * 消息类型未读消息统计
 *
 * @author WRP
 * @since 2019/2/27
 */
public class MsgTypeUnreadCount extends MsgType {

    private static final long serialVersionUID = 2763207737420718102L;

    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
