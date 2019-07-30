package com.newland.paas.advice.request;

import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 工单引擎token工具
 *
 * @author WRP
 * @since 2018/8/1
 */
public class ActivitiTokenUtils {

    public static final String ACTIVITI_TOKEN = "Authorization";
    public static final String PAAS_TOKEN = "PAAS-Authorization";
    // 数据库默认用户
    public static final String BASE_SERVICE = "baseservice:baseservice";
    public static final String BASIC = "Basic ";

    /**
     * 获取activi服务token
     * 在activiti-flow项目delegate中才会使用到
     * @return
     */
    public static String getActivitiToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(ACTIVITI_TOKEN);
    }

    /**
     * 获取activiti用户名
     *
     * @return
     */
    public static String getActivitiUserName() {
        String token = getActivitiToken();
        String tokenDecode = new String(Base64Utils.decodeFromString(token));
        tokenDecode = tokenDecode.replaceAll(BASIC, "");
        String[] userInfos = tokenDecode.split("\\:");
        return userInfos[0];
    }

    /**
     * 获取PAAS服务token
     * 使用场景：activiti服务调用需要token的微服务后台接口
     *
     * @return
     */
    public static String getPaasToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(PAAS_TOKEN);
    }

    /**
     * 根据用户名构造token
     * 使用场景：微服务后台异步回调activiti服务时使用
     *
     * @return
     */
    public static String buildUserActivitiToken(String userName) {
        String namePwd = userName + ":" + userName;
        return BASIC + Base64Utils.encodeToString(namePwd.getBytes());
    }

    /**
     * 构造异步回调的token
     * 使用场景：微服务后台异步回调activiti服务时使用
     *
     * @return
     */
    public static String buildBaseServiceActivitiToken() {
        return BASIC + Base64Utils.encodeToString(BASE_SERVICE.getBytes());
    }
    
    
   	/**    
   	 * @描述: 判断自审批
   	 * @param groupid
   	 * @return         
   	 * @创建人：zyp
   	 * @创建时间：2019/06/18 
   	 */
   	public static boolean getApproveState(String groupid) {
   		SessionInfo sessionInfo = RequestContext.getSession();
   		List<Long> groupIdList = sessionInfo.getGroupIdList();
   		String groupidtmp=groupid;
   		if(groupid.indexOf("_")>0){
   			groupidtmp= groupid.substring(groupid.indexOf("_")+1);
   		}
   		return ifInclude(groupIdList, groupidtmp);
   	}

   	/**    
   	 * @描述: 判断自审批
   	 * @param list
   	 * @param str
   	 * @return         
   	 * @创建人：zyp
   	 * @创建时间：2019/06/18 
   	 */
   	public static boolean ifInclude(List<Long> list, String str) {
   		for (int i = 0; i < list.size(); i++) {
   			if (list.get(i).toString().equals(str)) {
   				return true;
   			}
   		}
   		return false;
   	}
   	
    /**
     * 根据用户名构造token 使用场景：微服务后台异步回调activiti服务时使用
     *
     * @return
     */
    public static String buildUserTenantIdActivitiToken(String account, String tenantId) {
        String account_tenantId = account + "_" + tenantId;
        String namePwd = account_tenantId + ":" + account_tenantId;
        return BASIC + Base64Utils.encodeToString(namePwd.getBytes());
    }


}
