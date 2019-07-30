package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-08-27 16:22
 **/
public class SysTenantMemberReqEx extends SysTenantMemberReqBO {
    List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
