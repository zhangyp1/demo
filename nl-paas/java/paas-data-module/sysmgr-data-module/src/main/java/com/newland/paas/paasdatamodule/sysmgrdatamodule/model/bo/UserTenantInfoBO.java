/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

/**
 * 描述   用户对应的租户信息
 * @author linkun
 * @created 2018年6月25日 下午6:22:13
 */
public class UserTenantInfoBO {    
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 租户姓名
     */
    private String tenantName;
    /**
     * 待办数
     */
    private Long tasks;
    /**
     * 消息数
     */
    private Long messages;
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @return 
     */
    public Long getTenantId() {
        return tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @param tenantId
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @return 
     */
    public String getTenantName() {
        return tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @param tenantName
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @return 
     */
    public Long getTasks() {
        return tasks;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @param tasks
     */
    public void setTasks(Long tasks) {
        this.tasks = tasks;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @return 
     */
    public Long getMessages() {
        return messages;
    }
    /**
     * @author linkun
     * @created 2018年7月5日 下午4:21:01
     * @param messages
     */
    public void setMessages(Long messages) {
        this.messages = messages;
    }
    
    
    
}

