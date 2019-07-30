/*
 *
 */
package com.newland.paas.paasservice.sysmgr.vo;

/**
 * 描述
 * @author linkun
 * @created 2018年6月27日 下午5:50:28
 */
public class SysTenanMemberVo {

    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 用户名称
     */
    private String memberName;
    /**
     * 账户名
     */
    private String account;
    /**
     * 状态（1可用，0禁用）
     */
    private String status;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 所属部门
     */
    private String dept;
    /**
     * 用户描述
     */
    private String description;
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public Long getMemberId() {
        return memberId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param memberId
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getMemberName() {
        return memberName;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param memberName
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getAccount() {
        return account;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getStatus() {
        return status;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getEmail() {
        return email;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getDept() {
        return dept;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param dept
     */
    public void setDept(String dept) {
        this.dept = dept;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @return 
     */
    public String getDescription() {
        return description;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午5:54:43
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}

