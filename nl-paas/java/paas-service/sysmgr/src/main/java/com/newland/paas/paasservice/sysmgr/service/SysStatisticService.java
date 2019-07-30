package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.sysmgr.vo.SysCategoryListByTenantVo;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryStatisticVo;
import com.newland.paas.paasservice.sysmgr.vo.TenantStatisticVo;
import com.newland.paas.paasservice.sysmgr.vo.UserStatisticVo;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;

/**
 * 系统统计
 *
 * @author zhongqingjiang
 */
public interface SysStatisticService {

    TenantStatisticVo getTenantStatistic();

    UserStatisticVo getUserStatistic(Integer top);

    SysCategoryStatisticVo getSysCategoryStatistic();

    List<SysCategoryListByTenantVo> listSysCategory() throws ApplicationException;

}
