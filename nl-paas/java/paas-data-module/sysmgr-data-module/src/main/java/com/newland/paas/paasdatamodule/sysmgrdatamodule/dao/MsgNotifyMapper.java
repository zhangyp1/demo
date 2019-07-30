package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgNotify;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgNotifyBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgTypeUnreadCount;

import java.util.List;

public interface MsgNotifyMapper {
    int countBySelective(MsgNotify record);

    int deleteBySelective(MsgNotify record);

    int deleteByPrimaryKey(Long notifyId);

    int insert(MsgNotify record);

    int insertSelective(MsgNotify record);

    List<MsgNotify> selectBySelective(MsgNotify record);

    MsgNotify selectByPrimaryKey(Long notifyId);

    int updateByPrimaryKeySelective(MsgNotify record);

    int updateByPrimaryKey(MsgNotify record);

    List<MsgNotifyBO> selectMsgNotify(MsgNotifyBO msgNotifyBO);

    List<MsgTypeUnreadCount> countMsgTypeUnreadNum(MsgNotifyBO msgNotifyBO);

    int markMsgRead(MsgNotifyBO msgNotifyBO);

    int deleteReadMsg(MsgNotifyBO msgNotifyBO);

}