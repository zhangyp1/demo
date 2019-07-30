package com.newland.paas.paasservice.activitiflow.service;

/**
 * 权限验证
 *
 * @author WRP
 * @since 2019/7/4
 */
public interface IDService {

    /**
     * 是否有权限（存在与组内）
     *
     * @param userId
     * @param groupId
     * @return
     */
    boolean isOwn(String userId, String groupId);

}
