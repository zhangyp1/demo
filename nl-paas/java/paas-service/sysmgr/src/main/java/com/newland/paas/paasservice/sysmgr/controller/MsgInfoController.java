package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgInfo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgType;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgNotifyBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgTypeUnreadCount;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.MsgInfoVO;
import com.newland.paas.paasservice.sysmgr.common.ReadFlagEnum;
import com.newland.paas.paasservice.sysmgr.common.ReceiverRangeEnum;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.service.MsgInfoService;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 消息管理
 *
 * @author WRP
 * @since 2019/2/22
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/msg")
@Validated
public class MsgInfoController {

    private static final Log LOG = LogFactory.getLogger(MsgInfoController.class);

    /**
     * 导航消息默认显示条数
     */
    private static final int MSG_DEFAULE_SIZE = 5;

    @Autowired
    private MsgInfoService msgInfoService;

    /**
     * 新增消息类别
     *
     * @param msgTypeVo
     */
    @AuditOperate(value = "createMsgType", name = "新增消息类别", desc = "新增消息类别")
    @PostMapping(value = "createMsgType", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void createMsgType(@Validated @RequestBody BasicRequestContentVo<MsgType> msgTypeVo) {
        msgInfoService.createMsgType(msgTypeVo.getParams());
    }

    /**
     * 删除消息类别
     *
     * @param msgTypeId
     */
    @AuditOperate(value = "deleteMsgType", name = "删除消息类别", desc = "删除消息类别")
    @GetMapping(value = "deleteMsgType", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteMsgType(Long msgTypeId) {
        msgInfoService.deleteMsgType(msgTypeId);
    }

    /**
     * 修改消息类别
     *
     * @param msgTypeVo
     */
    @AuditOperate(value = "updateMsgType", name = "修改消息类别", desc = "修改消息类别")
    @PostMapping(value = "updateMsgType", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateMsgType(@Validated @RequestBody BasicRequestContentVo<MsgType> msgTypeVo) {
        msgInfoService.updateMsgType(msgTypeVo.getParams());
    }

    /**
     * 消息类别-分页查询
     *
     * @param msgTypeVo
     */
    @AuditOperate(value = "msgTypePage", name = "消息类别列表", desc = "消息类别列表")
    @PostMapping(value = "msgTypePage", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<MsgType> msgTypePage(@RequestBody BasicPageRequestContentVo<MsgType> msgTypeVo) {
        return msgInfoService.msgTypePageByParams(msgTypeVo.getParams(), msgTypeVo.getPageInfo());
    }

    /**
     * 消息类别-所有消息类别
     * manualRelease=0 不允许手工发布
     * manualRelease=1 手工发布
     * manualRelease=null 所有
     *
     * @param manualRelease
     * @return
     */
    @GetMapping(value = "findAllMsgType", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<MsgType> findAllMsgType(Long manualRelease) {
        return msgInfoService.findAllMsgType(manualRelease);
    }

    /**
     * 所有接收范围
     *
     * @return
     */
    @GetMapping(value = "getReceiverRanges", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> getReceiverRanges() {
        boolean isYunYing = Objects.equals(SysMgrConstants.TENANT_ID_YUNYIN, RequestContext.getTenantId());
        if (isYunYing) {
            return ReceiverRangeEnum.getYunYingReceiverRanges();
        } else {
            return ReceiverRangeEnum.getTenantReceiverRanges();
        }
    }

    /**
     * 所有接收人列表
     *
     * @return
     */
    @GetMapping(value = "getReceivers/{receiverRange}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<GlbDict> getReceivers(@PathVariable(value = "receiverRange") String receiverRange) {
        return msgInfoService.getReceivers(receiverRange);
    }

    /**
     * 新增消息信息
     *
     * @param msgInfoVo
     */
    @AuditOperate(value = "createMsgInfo", name = "新增消息信息", desc = "新增消息信息")
    @PostMapping(value = "createMsgInfo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void createMsgInfo(@Validated @RequestBody BasicRequestContentVo<MsgInfo> msgInfoVo) {
        checkMsgInfo(msgInfoVo.getParams());
        MsgInfo msgInfo = msgInfoVo.getParams();
        msgInfo.setTenantId(RequestContext.getTenantId());
        msgInfo.setCreatorId(RequestContext.getUserId());
        msgInfoService.createMsgInfo(msgInfoVo.getParams());
    }

    /**
     * 删除消息信息
     *
     * @param msgId
     */
    @AuditOperate(value = "deleteMsgInfo", name = "删除消息信息", desc = "删除消息信息")
    @GetMapping(value = "deleteMsgInfo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteMsgInfo(Long msgId) {
        msgInfoService.deleteMsgInfo(msgId);
    }

    /**
     * 根据消息ID获取消息详情
     *
     * @param msgId
     * @return
     */
    @GetMapping(value = "getMsgInfoDetail/{msgId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public MsgInfoVO getMsgInfoDetail(@PathVariable(value = "msgId") Long msgId) {
        return msgInfoService.getMsgInfoDetail(msgId);
    }

    /**
     * 修改消息信息
     *
     * @param msgInfoVo
     */
    @AuditOperate(value = "updateMsgInfo", name = "修改消息信息", desc = "修改消息信息")
    @PostMapping(value = "updateMsgInfo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void updateMsgInfo(@Validated @RequestBody BasicRequestContentVo<MsgInfo> msgInfoVo) {
        checkMsgInfo(msgInfoVo.getParams());
        msgInfoService.updateMsgInfo(msgInfoVo.getParams());
    }

    /**
     * 发布消息
     *
     * @param msgId
     */
    @AuditOperate(value = "publishMsg", name = "发布消息", desc = "发布消息")
    @GetMapping(value = "publishMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void publishMsg(Long msgId) {
        msgInfoService.publishMsg(msgId);
    }

    /**
     * 消息信息-分页查询
     *
     * @param msgInfoVo
     */
    @AuditOperate(value = "msgInfoPage", name = "消息信息列表", desc = "消息信息列表")
    @PostMapping(value = "msgInfoPage", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<MsgInfo> msgInfoPage(@RequestBody BasicPageRequestContentVo<MsgInfo> msgInfoVo) {
        return msgInfoService.msgInfoPageByParams(msgInfoVo.getParams(), msgInfoVo.getPageInfo());
    }

    /**
     * 统计未读消息总条数
     * 导航栏未读条数
     */
    @GetMapping(value = "queryUnreadMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<MsgNotifyBO> queryUnreadMsg() {
        MsgNotifyBO msgNotifyBO = new MsgNotifyBO();
        msgNotifyBO.setReadFlag(ReadFlagEnum.UNREAD.getValue());
        PageInfo pageInfo = new PageInfo(1, MSG_DEFAULE_SIZE);
        return msgInfoService.selectMsgNotify(msgNotifyBO, pageInfo);
    }

    /**
     * 统计各个消息类型的未读消息总条数
     * 总概览未读条数
     */
    @GetMapping(value = "queryMsgTypeUnreadMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<MsgTypeUnreadCount> queryGroupUnreadMsg() {
        return msgInfoService.queryMsgTypeUnreadMsg();
    }

    /**
     * 根据通知ID获取消息详情
     *
     * @param notifyId
     * @return
     */
    @GetMapping(value = "getMsgDetail/{notifyId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public MsgNotifyBO getMsgDetail(@PathVariable(value = "notifyId") Long notifyId) {
        return msgInfoService.getMsgDetail(notifyId);
    }

    /**
     * 接收消息信息-分页查询
     *
     * @param msgInfoVo
     */
    @AuditOperate(value = "queryNotify", name = "接收消息信息列表", desc = "接收消息信息列表")
    @PostMapping(value = "queryNotify", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<MsgNotifyBO> queryNotify(@RequestBody BasicPageRequestContentVo<MsgNotifyBO> msgInfoVo) {
        return msgInfoService.selectMsgNotify(msgInfoVo.getParams(), msgInfoVo.getPageInfo());
    }

    /**
     * 单条消息标记为已读
     *
     * @param msgId
     */
    @PostMapping(value = "singleMarkRead/{msgId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void singleMarkRead(@PathVariable(value = "msgId") Long msgId) {
        msgInfoService.markMsgRead(Arrays.asList(msgId));
    }

    /**
     * 全部消息标记为已读
     *
     * @param msgIds
     */
    @PostMapping(value = "allMarkRead", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void allMarkRead(@RequestBody BasicPageRequestContentVo<List<Long>> msgIds) {
        if (CollectionUtils.isEmpty(msgIds.getParams())) {
            throw new SystemException(new PaasError("2-18-00053", "参数不全！"));
        }
        msgInfoService.markMsgRead(msgIds.getParams());
    }

    /**
     * 删除单条已读消息
     *
     * @param msgId
     */
    @PostMapping(value = "deleteSingleReadMsg/{msgId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteSingleReadMsg(@PathVariable(value = "msgId") Long msgId) {
        msgInfoService.deleteReadMsg(Arrays.asList(msgId));
    }

    /**
     * 刪除全部已读消息
     *
     * @param msgIds
     */
    @PostMapping(value = "deleteAllReadMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void deleteAllReadMsg(@RequestBody BasicPageRequestContentVo<List<Long>> msgIds) {
        if (CollectionUtils.isEmpty(msgIds.getParams())) {
            throw new SystemException(new PaasError("2-18-00053", "参数不全！"));
        }
        msgInfoService.deleteReadMsg(msgIds.getParams());
    }

    /**
     * 外部接口：接收工单消息
     *
     * @param msgInfoVo
     */
    @PostMapping(value = "receiveActivitiMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void receiveActivitiMsg(@Validated @RequestBody BasicRequestContentVo<MsgInfo> msgInfoVo) {
        checkMsgInfo(msgInfoVo.getParams());
        msgInfoService.receiveActivitiMsg(msgInfoVo.getParams());
    }

    /**
     * 外部接口：接收告警消息
     *
     * @param msgInfoVo
     */
    @PostMapping(value = "receiveAlarmMsg", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void receiveAlarmMsg(@Validated @RequestBody BasicRequestContentVo<MsgInfo> msgInfoVo) {
        checkMsgInfo(msgInfoVo.getParams());
        msgInfoService.receiveAlarmMsg(msgInfoVo.getParams());
    }

    /**
     * 检查消息参数
     *
     * @param msgInfo
     */
    private void checkMsgInfo(MsgInfo msgInfo) {
        boolean receiverRange = Objects.equals(ReceiverRangeEnum.TENANT.getValue(), msgInfo.getReceiverRange())
                || Objects.equals(ReceiverRangeEnum.GROUP.getValue(), msgInfo.getReceiverRange())
                || Objects.equals(ReceiverRangeEnum.USER.getValue(), msgInfo.getReceiverRange());
        if (!receiverRange && !Objects.equals(ReceiverRangeEnum.ALL.getValue(), msgInfo.getReceiverRange())) {
            throw new SystemException(new PaasError("21800101", "接收范围不能为空！"));
        }
        if (receiverRange && StringUtils.isEmpty(msgInfo.getReceivers())) {
            throw new SystemException(new PaasError("21800102", "接收对象不能为空！"));
        }
    }

}
