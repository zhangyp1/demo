/*
 *
 */
package com.newland.paas.paasservice.sysmgr.vo;

/**
 * 描述
 * @author linkun
 * @created 2018年7月9日 下午2:59:06
 */
public class MemberVo {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 帐号
     */
    private String account;
    /**
     * 部门
     */
    private String dept;
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @return 
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @return 
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @return 
     */
    public String getAccount() {
        return account;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @return 
     */
    public String getDept() {
        return dept;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午3:00:54
     * @param dept
     */
    public void setDept(String dept) {
        this.dept = dept;
    }
    
    
}

