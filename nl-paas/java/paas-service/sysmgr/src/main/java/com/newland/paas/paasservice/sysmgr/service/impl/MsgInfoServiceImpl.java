package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.MsgInfoMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.MsgNotifyMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.MsgTypeMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMemberMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgInfo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgNotify;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgType;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgNotifyBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MsgTypeUnreadCount;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.MsgInfoVO;
import com.newland.paas.paasservice.sysmgr.common.MsgDefaultTypeEnum;
import com.newland.paas.paasservice.sysmgr.common.MsgStatusEnum;
import com.newland.paas.paasservice.sysmgr.common.ReceiverRangeEnum;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.service.MsgInfoService;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author WRP
 * @since 2019/2/25
 */
@Service
public class MsgInfoServiceImpl implements MsgInfoService {

    private static final Log LOG = LogFactory.getLogger(MsgInfoServiceImpl.class);

    @Autowired
    private MsgTypeMapper msgTypeMapper;
    @Autowired
    private MsgInfoMapper msgInfoMapper;
    @Autowired
    private MsgNotifyMapper msgNotifyMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Autowired
    private SysGroupUserMapper sysGroupUserMapper;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Override
    public void createMsgType(MsgType msgType) {
        msgType.setCreatorId(RequestContext.getUserId());
        msgTypeMapper.insertSelective(msgType);
    }

    @Override
    public void deleteMsgType(Long msgTypeId) {
        if (msgTypeId == null || msgTypeMapper.selectByPrimaryKey(msgTypeId) == null) {
            throw new SystemException(new PaasError("21800103", "消息类别不存在！"));
        }
        checkMsgTypeOperator(msgTypeId, "不能删除，存在该类型的消息！");
        MsgType msgType = new MsgType();
        msgType.setMsgTypeId(msgTypeId);
        msgType.setDelFlag(1L);
        msgTypeMapper.updateByPrimaryKeySelective(msgType);
    }

    @Override
    public void updateMsgType(MsgType msgType) {
        checkMsgTypeOperator(msgType.getMsgTypeId(), "不能修改，存在该类型的消息！");
        msgTypeMapper.updateByPrimaryKeySelective(msgType);
    }

    @Override
    public ResultPageData<MsgType> msgTypePageByParams(MsgType msgType, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<MsgType> list = msgTypeMapper.selectBySelective(msgType);
        return new ResultPageData(list);
    }

    /**
     * 检查消息类别能否操作
     *
     * @param msgTypeId
     * @param errorMsg
     */
    private void checkMsgTypeOperator(Long msgTypeId, String errorMsg) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgTypeId(msgTypeId);
        List<MsgInfo> msgInfos = msgInfoMapper.selectBySelective(msgInfo);
        if (!CollectionUtils.isEmpty(msgInfos)) {
            throw new SystemException(new PaasError("21800104", errorMsg));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void createMsgInfo(MsgInfo msgInfo) {
        msgInfoMapper.insertSelective(msgInfo);
    }

    @Override
    public void deleteMsgInfo(Long msgId) {
        checkMsgInfoOperator(msgId, "不能删除，消息已发布！");
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgId(msgId);
        msgInfo.setDelFlag(1L);
        msgInfoMapper.deleteByPrimaryKey(msgId);
    }

    @Override
    public void updateMsgInfo(MsgInfo msgInfo) {
        checkMsgInfoOperator(msgInfo.getMsgId(), "不能修改，消息已发布！");
        msgInfoMapper.updateByPrimaryKeySelective(msgInfo);
    }

    @Override
    public void publishMsg(Long msgId) {
        MsgInfo msgInfo = checkMsgInfoOperator(msgId, "消息已发布，不能重复发布！");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "消息接收范围：" + msgInfo.getReceiverRange());
        if (Objects.equals(msgInfo.getReceiverRange(), ReceiverRangeEnum.ALL.getValue())) {
            publishMsgToAll(msgId);
        } else if (Objects.equals(msgInfo.getReceiverRange(), ReceiverRangeEnum.TENANT.getValue())) {
            publishMsgToTeanant(msgId, msgInfo.getReceivers());
        } else if (Objects.equals(msgInfo.getReceiverRange(), ReceiverRangeEnum.GROUP.getValue())) {
            publishMsgToGroup(msgId, msgInfo.getReceivers());
        } else if (Objects.equals(msgInfo.getReceiverRange(), ReceiverRangeEnum.USER.getValue())) {
            publishMsgToUser(msgId, msgInfo.getReceivers());
        }
        // 消息状态修改为已发布
        MsgInfo updateMsgInfo = new MsgInfo();
        updateMsgInfo.setMsgId(msgId);
        updateMsgInfo.setMsgStatus(MsgStatusEnum.PUBLISH.getValue());
        msgInfoMapper.updateByPrimaryKeySelective(updateMsgInfo);
    }

    @Override
    public ResultPageData<MsgInfo> msgInfoPageByParams(MsgInfo msgInfo, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        msgInfo.setTenantId(RequestContext.getTenantId());
        List<MsgInfo> list = msgInfoMapper.selectBySelective(msgInfo);
        return new ResultPageData(list);
    }

    @Override
    public ResultPageData<MsgNotifyBO> selectMsgNotify(MsgNotifyBO msgNotifyBO, PageInfo pageInfo) {
        msgNotifyBO.setReceiverId(RequestContext.getUserId());
        msgNotifyBO.setTenantId(RequestContext.getTenantId());
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<MsgNotifyBO> list = msgNotifyMapper.selectMsgNotify(msgNotifyBO);
        return new ResultPageData(list);
    }

    @Override
    public MsgNotifyBO getMsgDetail(Long notifyId) {
        MsgNotifyBO msgNotifyBO = new MsgNotifyBO();
        msgNotifyBO.setReceiverId(RequestContext.getUserId());
        msgNotifyBO.setTenantId(RequestContext.getTenantId());
        msgNotifyBO.setNotifyId(notifyId);
        List<MsgNotifyBO> msgNotifyBOs = msgNotifyMapper.selectMsgNotify(msgNotifyBO);
        if (CollectionUtils.isEmpty(msgNotifyBOs)) {
            throw new SystemException(new PaasError("21800105", "消息不存在，请刷新后重试！"));
        }
        msgNotifyBO = msgNotifyBOs.get(0);
        Long msgId = msgNotifyBO.getMsgId();
        MsgInfo msgInfo = msgInfoMapper.selectByPrimaryKey(msgId);
        if (msgInfo == null) {
            throw new SystemException(new PaasError("21800105", "消息不存在，请刷新后重试！"));
        }
        msgNotifyBO.setMsgContent(msgInfo.getMsgContent());
        msgNotifyBO.setReceiverRangeDesc(ReceiverRangeEnum.getDescByValue(msgInfo.getReceiverRange()));
        // 标记消息为已读
        markMsgRead(Arrays.asList(msgId));
        return msgNotifyBO;
    }

    @Override
    public MsgInfoVO getMsgInfoDetail(Long msgId) {
        MsgInfo msgInfo = msgInfoMapper.selectByPrimaryKey(msgId);
        MsgInfoVO msgInfoVO = new MsgInfoVO();
        BeanUtils.copyProperties(msgInfo, msgInfoVO);
        MsgType msgType = msgTypeMapper.selectByPrimaryKey(msgInfo.getMsgTypeId());
        if (msgType != null) {
            msgInfoVO.setTypeName(msgType.getTypeName());
        }
        msgInfoVO.setReceiverRangeDesc(ReceiverRangeEnum.getDescByValue(msgInfo.getReceiverRange()));
        return msgInfoVO;
    }

    @Override
    public List<MsgTypeUnreadCount> queryMsgTypeUnreadMsg() {
        MsgNotifyBO msgNotifyBO = new MsgNotifyBO();
        msgNotifyBO.setReceiverId(RequestContext.getUserId());
        msgNotifyBO.setTenantId(RequestContext.getTenantId());
        return msgNotifyMapper.countMsgTypeUnreadNum(msgNotifyBO);
    }

    @Override
    public List<MsgType> findAllMsgType(Long manualRelease) {
        MsgType msgType = new MsgType();
        msgType.setManualRelease(manualRelease);
        return msgTypeMapper.selectBySelective(msgType);
    }

    @Override
    public List<GlbDict> getReceivers(String receiverRange) {
        Boolean isAdmin = RequestContext.getSession().getIsTenantAdmin();
        List<GlbDict> list = new ArrayList<>();
        if (!isAdmin) {
            return list;
        }
        boolean isYunYing = Objects.equals(SysMgrConstants.TENANT_ID_YUNYIN, RequestContext.getTenantId());
        if (Objects.equals(receiverRange, ReceiverRangeEnum.TENANT.getValue())) {
            // 运营管理员：所有租户；租户管理员：当前租户
            list = buildTenantList(isYunYing);
        } else if (Objects.equals(receiverRange, ReceiverRangeEnum.GROUP.getValue())) {
            // 运营管理员：所有工组；租户管理员：归属当前租户的所有工组
            list = buildGroupList(isYunYing);
        } else if (Objects.equals(receiverRange, ReceiverRangeEnum.USER.getValue())) {
            // 运营管理员：所有用户；租户管理员：归属当前租户的所有用户
            list = buildUserList(isYunYing);
        }
        return list;
    }

    @Override
    public void markMsgRead(List<Long> msgIds) {
        MsgNotifyBO msgNotifyBO = new MsgNotifyBO();
        msgNotifyBO.setMsgIds(msgIds);
        msgNotifyBO.setReceiverId(RequestContext.getUserId());
        msgNotifyBO.setTenantId(RequestContext.getTenantId());
        msgNotifyMapper.markMsgRead(msgNotifyBO);
    }

    @Override
    public void deleteReadMsg(List<Long> msgIds) {
        MsgNotifyBO msgNotifyBO = new MsgNotifyBO();
        msgNotifyBO.setMsgIds(msgIds);
        msgNotifyBO.setReceiverId(RequestContext.getUserId());
        msgNotifyBO.setTenantId(RequestContext.getTenantId());
        msgNotifyMapper.deleteReadMsg(msgNotifyBO);
    }

    @Override
    public void receiveActivitiMsg(MsgInfo msgInfo) {
        msgInfo.setMsgTypeId(MsgDefaultTypeEnum.ACTIVITI.getValue());
        msgInfo.setCreatorId(SysMgrConstants.USER_ID_ADMIN);
        msgInfo.setTenantId(SysMgrConstants.TENANT_ID_ADMIN);
        // 保存消息
        ((MsgInfoServiceImpl) AopContext.currentProxy()).createMsgInfo(msgInfo);
        // 发布消息
        publishMsg(msgInfo.getMsgId());
    }

    @Override
    public void receiveAlarmMsg(MsgInfo msgInfo) {
        msgInfo.setMsgTypeId(MsgDefaultTypeEnum.ALARM.getValue());
        msgInfo.setCreatorId(SysMgrConstants.USER_ID_ADMIN);
        msgInfo.setTenantId(SysMgrConstants.TENANT_ID_ADMIN);
        // 保存消息
        ((MsgInfoServiceImpl) AopContext.currentProxy()).createMsgInfo(msgInfo);
        // 发布消息
        publishMsg(msgInfo.getMsgId());
    }

    /**
     * 运营管理员：所有租户；租户管理员：当前租户
     *
     * @param isYunYing
     * @return
     */
    private List<GlbDict> buildTenantList(boolean isYunYing) {
        List<SysTenant> tenants;
        if (!isYunYing) {
            SysTenant sysTenant = sysTenantMapper.selectByPrimaryKey(RequestContext.getTenantId());
            tenants = new ArrayList<>();
            tenants.add(sysTenant);
        } else {
            tenants = sysTenantMapper.selectBySelective(new SysTenant());
        }
        List<GlbDict> list = tenants.stream().map(v ->
                new GlbDict(String.valueOf(v.getId()), v.getTenantName())
        ).collect(Collectors.toList());
        return list;
    }

    /**
     * 运营管理员：所有工组；租户管理员：归属当前租户的所有工组
     *
     * @param isYunYing
     * @return
     */
    private List<GlbDict> buildGroupList(boolean isYunYing) {
        List<GlbDict> list;
        if (isYunYing) {
            List<SysGroup> groups = sysGroupMapper.findAllTenantGroup();
            list = groups.stream().map(v ->
                    new GlbDict(String.valueOf(v.getGroupId()), v.getGroupName())
            ).collect(Collectors.toList());
        } else {
            SysGroupReqBo sysGroupReqBo = new SysGroupReqBo();
            sysGroupReqBo.setTenantId(RequestContext.getTenantId());
            List<SysGroup> sysGroupList = sysGroupMapper.selectBySelective(sysGroupReqBo);
            list = sysGroupList.stream().map(v ->
                    new GlbDict(String.valueOf(v.getGroupId()), v.getGroupName())
            ).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 运营管理员：所有用户；租户管理员：归属当前租户的所有用户
     *
     * @param isYunYing
     * @return
     */
    private List<GlbDict> buildUserList(boolean isYunYing) {
        List<GlbDict> list;
        if (isYunYing) {
            List<SysUser> users = sysUserMapper.findBaseAll();
            list = users.stream().map(v ->
                    new GlbDict(String.valueOf(v.getUserId()), v.getUsername())
            ).collect(Collectors.toList());
        } else {
            List<TenantUserBO> tenantUserBOS = sysTenantMapper.getSysTenantUsers(
                    RequestContext.getTenantId(), null, null);
            list = tenantUserBOS.stream().map(v ->
                    new GlbDict(String.valueOf(v.getUserId()), v.getUserName())
            ).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 检查消息信息能否操作
     *
     * @param msgId
     * @param errorMsg
     * @return
     */
    private MsgInfo checkMsgInfoOperator(Long msgId, String errorMsg) {
        MsgInfo msgInfo = msgInfoMapper.selectByPrimaryKey(msgId);
        if (null == msgInfo) {
            throw new SystemException(new PaasError("21800106", "消息不存在！"));
        } else if (Objects.equals(msgInfo.getMsgStatus(), 1L)) {
            throw new SystemException(new PaasError("21800107", errorMsg));
        }
        return msgInfo;
    }

    /**
     * 发布消息给所有用户
     * 1.运营租户管理员，发送给所有用户
     * 2.其他租户管理员，发送给本租户下的所有用户
     */
    private void publishMsgToAll(Long msgId) {
        boolean isYunYing = Objects.equals(SysMgrConstants.TENANT_ID_YUNYIN, RequestContext.getTenantId());
        List<SysUser> users;
        Long tenantId = null;
        if (isYunYing) {
            users = sysUserMapper.selectBySelective(null);
        } else {
            tenantId = RequestContext.getTenantId();
            users = sysUserMapper.getUserByTenant(tenantId);
        }
        final Long tId = tenantId;
        List<Long> userIds = users.stream().map(v -> v.getUserId()).collect(Collectors.toList());
        List<MsgNotify> msgNotifyList = userIds.stream().map(v ->
                new MsgNotify(msgId, v, tId, RequestContext.getUserId())
        ).collect(Collectors.toList());
        publishMsgToUsers(new ArrayList<>(msgNotifyList));
    }

    /**
     * 发布消息给租户列表
     *
     * @param receivers
     */
    private void publishMsgToTeanant(Long msgId, String receivers) {
        List<Long> tenantIds = stringToLongList(receivers);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "消息接收范围-租户列表：" + JSON.toJSONString(tenantIds));
        if (!CollectionUtils.isEmpty(tenantIds)) {
            Set<MsgNotify> msgNotifyList = new TreeSet<>();
            tenantIds.forEach(v -> {
                SysTenantMember sysTenantMember = new SysTenantMember();
                sysTenantMember.setTenantId(v);
                List<SysTenantMember> tenantMembers = sysTenantMemberMapper.selectBySelective(sysTenantMember);
                List<MsgNotify> msgNotifyListTmp = tenantMembers.stream().map(i ->
                        new MsgNotify(msgId, i.getUserId(), i.getTenantId(), RequestContext.getUserId())
                ).collect(Collectors.toList());
                msgNotifyList.addAll(msgNotifyListTmp);
            });
            publishMsgToUsers(new ArrayList<>(msgNotifyList));
        }
    }

    /**
     * 发布消息给工组列表
     *
     * @param receivers
     */
    private void publishMsgToGroup(Long msgId, String receivers) {
        List<Long> groupIds = stringToLongList(receivers);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "消息接收范围-工组列表：" + JSON.toJSONString(groupIds));
        if (!CollectionUtils.isEmpty(groupIds)) {
            Set<MsgNotify> msgNotifyList = new TreeSet<>();
            groupIds.forEach(v -> {
                SysGroupUserReqBo groupUserReqBo = new SysGroupUserReqBo();
                groupUserReqBo.setGroupId(v);
                List<SysGroupUserRespBo> groupUserRespBos = sysGroupUserMapper.selectBySelective(groupUserReqBo);
                List<MsgNotify> msgNotifyListTmp = groupUserRespBos.stream().map(i ->
                        new MsgNotify(msgId, i.getUserId(), i.getTenantId(), RequestContext.getUserId())
                ).collect(Collectors.toList());
                msgNotifyList.addAll(msgNotifyListTmp);
            });
            publishMsgToUsers(new ArrayList<>(msgNotifyList));
        }
    }

    /**
     * 发布消息给用户列表
     *
     * @param msgId
     * @param receivers
     */
    private void publishMsgToUser(Long msgId, String receivers) {
        List<Long> userIds = stringToLongList(receivers);
        if (!CollectionUtils.isEmpty(userIds)) {
            List<MsgNotify> msgNotifyList = userIds.stream().map(v ->
                    new MsgNotify(msgId, v, null, RequestContext.getUserId())
            ).collect(Collectors.toList());
            publishMsgToUsers(msgNotifyList);
        }

    }

    /**
     * 字符串数组转List<Long>
     *
     * @param strArr
     * @return
     */
    private List<Long> stringToLongList(String strArr) {
        if (!StringUtils.isEmpty(strArr)) {
            return Arrays.stream(strArr.split(","))
                    .map(v -> Long.parseLong(v.trim()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 发布消息
     *
     * @param msgNotifyList
     */
    @Async("publishMsgToUsers")
    public void publishMsgToUsers(List<MsgNotify> msgNotifyList) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "发布消息：" + JSON.toJSONString(msgNotifyList));
        if (!CollectionUtils.isEmpty(msgNotifyList)) {
            msgNotifyList.forEach(v -> msgNotifyMapper.insertSelective(v));
        }
    }

}
