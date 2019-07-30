package com.newland.paas.paasservice.activitiflow.service.impl;

import com.newland.paas.paasdatamodule.activitiflowdatamodule.dao.ActIdMembershipMapper;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.ActIdMembership;
import com.newland.paas.paasservice.activitiflow.service.IDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WRP
 * @since 2019/7/4
 */
@Service
public class IDServiceImpl implements IDService {

    @Autowired
    private ActIdMembershipMapper actIdMembershipMapper;

    @Override
    public boolean isOwn(String userId, String groupId) {
        ActIdMembership actIdMembership = new ActIdMembership();
        actIdMembership.setUserId(userId);
        actIdMembership.setGroupId(groupId);
        return !actIdMembershipMapper.selectBySelective(actIdMembership).isEmpty();
    }

}
