package com.newland.paas.paasdatamodule.activitiflowdatamodule.dao;

import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.ActIdMembership;

import java.util.List;

/**
 * @author WRP
 * @since 2019/7/4
 */
public interface ActIdMembershipMapper {

    List<ActIdMembership> selectBySelective(ActIdMembership record);

}
