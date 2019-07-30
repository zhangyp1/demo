package com.newland.paas.paasservice.activitiflow.listener;

import com.newland.paas.advice.request.ActivitiTokenUtils;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasservice.activitiflow.service.TaskService;
import com.newland.paas.paasservice.activitiflow.utils.SpringBeanUtil;
import com.newland.paas.paasservice.controllerapi.activiti.MonitorService;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.NanjinRestfulRequestUtil;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局事件监听
 * @author sunxm
 */
@Component
public class ComActivitiEventListener implements ActivitiEventListener, Serializable {

    private static final Log LOG = LogFactory.getLogger(ComActivitiEventListener.class);
    private static final int PAGESIZE = 100;

    @Value("${activiti.monitorUrlPrefix}")
    private String monitorUrlPrefix;
    @Autowired
    private TaskService taskService;


    /**
     * 1.判断是否任务创建
     * 2.判断是否方案创建流程
     * 3.获取用户任务操作工组
     * 4.根据工组查询用户列表（手机号、）
     * 5.发送消息
     * @param event
     */
    @Override
    public void onEvent(ActivitiEvent event) {

        if ("TASK_CREATED".equals(event.getType().name())) {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            ProcessDefinition def = engine.getRepositoryService().getProcessDefinition(event.getProcessDefinitionId());
            if (def != null && "方案创建流程".equals(def.getName())) {
                Object entity = ((ActivitiEntityEvent) event).getEntity();
                if (entity instanceof TaskEntity) {
                    String token = ActivitiTokenUtils.getPaasToken();
                    LOG.info("paastoken:::::::::::" + token);
                    /*SessionInfo sessionInfo = taskService.getSessionInfo();
                    TaskEntity task = (TaskEntity) entity;
                    Set<IdentityLink> groups = task.getCandidates();
                    List<String> list = getUsersByGroup(groups);
                    addReceiver(list);
                    LOG.info(task.getCandidates().toString());*/
                }
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * 根据工组查询用户列表（手机号）
     */
    public List<String> getUsersByGroup(Set<IdentityLink> groups) {
        List<String> emails = new ArrayList<>();
        MicroservicesProperties microservicesProperties = SpringBeanUtil.getBean(MicroservicesProperties.class);
        MicroservicesItemProperties env = microservicesProperties.getSysmgr();
        RestTemplateUtils restTemplateUtils = SpringBeanUtil.getBean(RestTemplateUtils.class);
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("pageSize", PAGESIZE);
        Set<String> set = new HashSet<>();
        List<Map<String, String>> userList = new ArrayList<>();
        for (IdentityLink group : groups) {
            String groupId = group.getGroupId().replaceAll("group_", "");
            String url = "/v1/groupUserMgr/getUsersByGroup/" + groupId;
            ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>(){};
            String token = ActivitiTokenUtils.getPaasToken();
            Object responseContent = restTemplateUtils.getForTokenEntity(env, url, token, params, responseType);
            if (responseContent != null) {
                Map<String, Object> content = (HashMap<String, Object>)
                        ((HashMap<String, Object>)responseContent).get("content");
                if (content != null) {
                    userList.addAll((List<Map<String, String>>)content.get("list"));
                }
            }
        }
        for (Map<String, String> map: userList) {
            if (map.get("isAdmin") != null
                    && "1".equals(String.valueOf(map.get("isAdmin")))) {
                set.add(map.get("userPhone"));
            }
        }
        emails.addAll(set);
        return emails;
    }

    /**
     * 创建告警发送方式
     * @param tos
     * @return
     */
    public Map<String, Object> addReceiver(List<String> tos) {
        Map<String, Object> receiver = new HashMap<>();
        receiver.put("name", "上线方案");
        List<Map<String, Object>> emailConfigs = new ArrayList<>();
        for (String to : tos) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Subject", "上线方案待办提醒");
            Map<String, Object> emailConfig = new HashMap<>();
            emailConfig.put("to", to);
            emailConfig.put("headers", headers);
            emailConfigs.add(emailConfig);
        }
        receiver.put("email_configs", emailConfigs);
        MonitorService monitorService =
                NanjinRestfulRequestUtil.getInstance(MonitorService.class, monitorUrlPrefix);
        Map<String, Object> response = monitorService.createReceiver(receiver);
        return receiver;
    }

    /**
     * 创建消息分组
     * @param groupBy
     */
    public void createGroup(List<String> groupBy) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "上线方案消息组");
        param.put("group_by", groupBy);
        List<String> noticeTypes = new ArrayList<>();
        noticeTypes.add("mobile");
        noticeTypes.add("email");
        param.put("notice_type", noticeTypes);
        MonitorService monitorService =
                NanjinRestfulRequestUtil.getInstance(MonitorService.class, monitorUrlPrefix);
        Map<String, Object> response = monitorService.createGroup(param);
    }

    /**
     * 修改消息分组
     * @param groupBy
     */
    public void updateGroup(List<String> groupBy) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", "");
        param.put("name", "上线方案消息组");
        param.put("group_by", groupBy);
        List<String> noticeTypes = new ArrayList<>();
        noticeTypes.add("mobile");
        noticeTypes.add("email");
        param.put("notice_type", noticeTypes);
        MonitorService monitorService =
                NanjinRestfulRequestUtil.getInstance(MonitorService.class, monitorUrlPrefix);
        Map<String, Object> response = monitorService.updateGroup(param);
    }




}
