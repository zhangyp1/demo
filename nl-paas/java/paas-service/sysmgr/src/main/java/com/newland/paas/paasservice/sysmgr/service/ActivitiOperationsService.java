package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.sysmgr.vo.ActivitiRunProcessInput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskOutput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskStartOutput;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;
import java.util.Map;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.service.ActivitiOperationsService
 * @Date 2018/8/7
 */
public interface ActivitiOperationsService {
	/**
	 * activiti流程处理
	 *
	 * @param taskId  当前任务节点的任务ID
	 */
	void taskExecute(String taskId);
	/**
	 * activiti流程处理
	 *
	 * @param taskId  当前任务节点的任务ID
	 * @param groupId 提交给下一环节处理的组ID  可以为空，多个组用逗号隔开 【group_1,group_2,role_1】
	 */
	void taskExecute(String taskId, String groupId);

	/**
	 * activiti流程处理
	 * @param taskId  当前任务节点的任务ID
	 * @param properties  流程变量
	 */
	void taskExecute(String taskId, List<Map<String, String>> properties);

	/**
	 * activiti流程发起
	 *
	 * @param activitiRunProcessInput
	 * @return
	 */
	ActivitiTaskStartOutput runProcess(ActivitiRunProcessInput activitiRunProcessInput);

	/**
	 * 获取流程实例变量
	 *
	 * @param processInstanceId 流程实例ID
	 * @param key               变量KEY
	 * @param clazz
	 * @return
	 */
	<T> T getProcessInstanceVariable(String processInstanceId, String key, Class<T> clazz);

	/**
	 * 获取任务变量
	 *
	 * @param taskId 任务ID
	 * @param key    变量KEY
	 * @param clazz
	 * @return
	 */
	<T> T getTaskVariable(String taskId, String key, Class<T> clazz);

	/**
	 * 获取任务ID
	 *
	 * @param processInstanceId
	 * @return
	 */
	String getTaskId(String processInstanceId) throws ApplicationException;

	/**
	 * 获取任务信息
	 *
	 * @param taskId
	 * @return
	 */
	ActivitiTaskOutput getTask(String taskId);

	/**
	 *
	 * @param processInstanceId
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T getProcessInstanceVariableHistory(String processInstanceId, String key, Class<T> clazz);
}
