package com.newland.paas.advice.request;

import java.util.Collections;
import java.util.List;

/**
 * 用户信息
 *
 * @author wrp
 * @since 2018/6/21
 */
public class SessionInfo {

    private Long userId;
    private String userName;
    private Long tenantId;
    private String tenantCode;
    private String account;
    private boolean isTenantAdmin;
    /**
     * 工组ID列表，与该用户直接关联的工组
     */
    private List<Long> groupIdList = Collections.emptyList();
    /**
     * 获取直接关联工组的子工组，即与该用户间接关联的工组
     */
    private List<Long> subGroupIdList = Collections.emptyList();
    /**
     * 
     * 角色ID列表
     */
    private List<Long> roleIdList = Collections.emptyList();
    /**
     * token签发时间
     */
    private Long iat;

    public SessionInfo() {}

    public SessionInfo(Long userId, String userName, Long tenantId) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<Long> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<Long> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public List<Long> getSubGroupIdList() {
        return subGroupIdList;
    }

    public void setSubGroupIdList(List<Long> subGroupIdList) {
        this.subGroupIdList = subGroupIdList;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getIsTenantAdmin() {
        return isTenantAdmin;
    }

    public void setIsTenantAdmin(boolean isTenantAdmin) {
        this.isTenantAdmin = isTenantAdmin;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
