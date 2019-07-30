package com.newland.paas.sbcommon.activitiflow.Utils;

import org.springframework.util.Base64Utils;

/**
 * 工单引擎token工具
 *
 */
public class ActivitiUtils {


    public static final String BASIC = "Basic ";

 

    /**
     * 根据用户名构造token
     * 使用场景：微服务后台异步回调activiti服务时使用
     *
     * @return
     */
    public static String buildUserActivitiToken(String account_tenantId) {
        String namePwd = account_tenantId + ":" + account_tenantId;
        return BASIC + Base64Utils.encodeToString(namePwd.getBytes());
    }

    /**
     * 根据用户名构造token
     * 使用场景：微服务后台异步回调activiti服务时使用
     *
     * @return
     */
    public static String buildUserTenantIdActivitiToken(String account,String tenantId) {
//    	String account_tenantId=account+"_"+tenantId;
        String namePwd = account + ":" + account;
        return BASIC + Base64Utils.encodeToString(namePwd.getBytes());
    }
    

}
