package com.newland.paas.sbcommon.activitiflow;

import java.util.List;
import java.util.Map;

import com.newland.paas.sbcommon.activitiflow.vo.ActivitiVariable;
import com.newland.paas.sbcommon.common.ApplicationException;

/**   
*      
* @类名称：ActivitiflowService   
* @类描述：   
* @创建人：zyp
* @创建时间：2019/06/12  
*    
*/
public interface ActivitiflowService {

	/**
	 * @描述: 启动工作流
	 * @param processDefinitionKey——>pnmb名称key
	 * @param activitiName  ——>流程名称
	 * @param variables --> com.newland.paas.sbcommon.activitiflow.vo.variables 变量信息列表
	 * @param account 用户
	 * @param tenantId 租户id
	 * @return 流程实例processInstanceId 
	 * @throws ApplicationException
	 * @创建人：zyp 
	 * @创建时间：2019/06/07
	 */
	public String startRunProcess(String processDefinitionKey, String activitiName, List<ActivitiVariable> variables,
			String account,String tenantId) throws ApplicationException;

	/**
	 * @描述: 根据processInstanceId启动执行下个流程
	 * @param processInstanceId 流程id
	 * @param parameter 流程参数
	 * @param account 用户
	 * @param tenantId 租户id
	 * @throws ApplicationException
	 * @创建人：zyp 
	 * @创建时间：2019/06/07
	 */
	public void taskExecuteProcessId(String processInstanceId, Map<String, String> parameter,
			String account,String tenantId) throws ApplicationException;

	/**
	 * @描述: 根据taskId启动执行下个流程
	 * @param taskId
	 * @param parameter
	 * @param account 用户
	 * @param tenantId 租户id
	 * @创建人：zyp 
	 * @创建时间：2019/06/07
	 */
	public void taskExecuteTaskId(String taskId, Map<String, String> parameter, String account,String tenantId);
	
	/**
	 * @描述: 获取当前用户GroupId
	 * @param passToken不是 activitiToken
	 * @return GroupId
	 * @创建人：zyp 
	 * @创建时间：2019/06/08
	 */
	public String getGroupId(String passToken) throws ApplicationException;

	/**
	 * @描述: 获取运营GroupId
	 * @param paastoken不是activitiToken
	 * @return GroupId
	 * @创建人：zyp 
	 * @创建时间：2019/06/08
	 */
	public String getYYGroupId(String passToken) throws ApplicationException;
	
	/**    
	 * @描述: 根据processInstanceId回退到当前用户
	 * @param processInstanceId
	 * @param account 用户
	 * @param tenantId 租户id
	 * @throws ApplicationException         
	 * @创建人：zyp
	 * @创建时间：2019/06/12 
	 */
	public void rollBackToAssignWorkFlow(String processInstanceId,String account,String tenantId) throws ApplicationException;
	
	/**    
	 * @描述:  根据processInstanceId回退到当前工组
	 * @param processInstanceId
	 * @param account 用户
	 * @param tenantId 租户id
	 * @param groupId
	 * @throws ApplicationException         
	 * @创建人：zyp
	 * @创建时间：2019/06/12 
	 */
	public void rollBackToGroupWorkFlow(String processInstanceId,String account,String tenantId,String groupId) throws ApplicationException;
	
	/**    
	 * @描述: 流程作废
	 * @param processInstanceId
	 * @param account 用户
	 * @param tenantId 租户id
	 * @throws ApplicationException         
	 * @创建人：zyp
	 * @创建时间：2019/06/12 
	 */
	public void deleteTaskWorkFlow(String processInstanceId,String account,String tenantId) throws ApplicationException;
	
	/**    
	 * @描述: 任务认领
	 * @param taskId
	 * @param account 用户
	 * @param tenantId 租户id        
	 * @创建人：zyp
	 * @创建时间：2019/06/15 
	 */
	public void getTaskClaim(String taskId, String account,String tenantId) ;
}
