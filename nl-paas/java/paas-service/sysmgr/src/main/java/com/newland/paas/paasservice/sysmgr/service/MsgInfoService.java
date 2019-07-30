package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgInfo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgType;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgNotifyBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgTypeUnreadCount;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.MsgInfoVO;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 消息管理
 *
 * @author WRP
 * @since 2019/2/22
 */
public interface MsgInfoService {

    /**
     * 新增消息类别
     *
     * @param msgType
     */
    void createMsgType(MsgType msgType);

    /**
     * 删除消息类别
     *
     * @param msgTypeId
     */
    void deleteMsgType(Long msgTypeId);

    /**
     * 修改消息类别
     *
     * @param msgType
     */
    void updateMsgType(MsgType msgType);

    /**
     * 消息类别-分页查询
     *
     * @param msgType
     * @param pageInfo
     * @return
     */
    ResultPageData<MsgType> msgTypePageByParams(MsgType msgType, PageInfo pageInfo);

    /**
     * 新增消息信息
     *
     * @param msgInfo
     */
    void createMsgInfo(MsgInfo msgInfo);

    /**
     * 删除消息信息
     *
     * @param msgId
     */
    void deleteMsgInfo(Long msgId);

    /**
     * 修改消息信息
     *
     * @param msgInfo
     */
    void updateMsgInfo(MsgInfo msgInfo);

    /**
     * 发布消息
     *
     * @param msgId
     */
    void publishMsg(Long msgId);

    /**
     * 消息信息-分页查询
     *
     * @param msgInfo
     * @param pageInfo
     * @return
     */
    ResultPageData<MsgInfo> msgInfoPageByParams(MsgInfo msgInfo, PageInfo pageInfo);

    /**
     * 获取接收消息
     *
     * @return
     */
    ResultPageData<MsgNotifyBO> selectMsgNotify(MsgNotifyBO msgNotifyBO, PageInfo pageInfo);

    /**
     * 根据通知ID获取消息详情
     *
     * @param notifyId
     * @return
     */
    MsgNotifyBO getMsgDetail(Long notifyId);

    /**
     * 根据消息ID获取消息详情
     *
     * @param msgId
     * @return
     */
    MsgInfoVO getMsgInfoDetail(Long msgId);

    /**
     * 统计各个消息类型的未读消息总条数
     *
     * @return
     */
    List<MsgTypeUnreadCount> queryMsgTypeUnreadMsg();

    /**
     * 根据manualRelease查找消息类别
     *
     * @param manualRelease
     * @return
     */
    List<MsgType> findAllMsgType(Long manualRelease);

    /**
     * 根据接收范围构造接收人列表
     *
     * @param receiverRange
     * @return
     */
    List<GlbDict> getReceivers(String receiverRange);

    /**
     * 消息标记为已读
     *
     * @param msgIds
     */
    void markMsgRead(List<Long> msgIds);

    /**
     * 刪除已读消息
     *
     * @param msgIds
     */
    void deleteReadMsg(List<Long> msgIds);

    /**
     * 接收工单消息
     *
     * @param msgInfo
     */
    void receiveActivitiMsg(MsgInfo msgInfo);

    /**
     * 接收告警消息
     *
     * @param msgInfo
     */
    void receiveAlarmMsg(MsgInfo msgInfo);
}
