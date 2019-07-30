package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuota;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuotaUsed;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.Map;

/**
 * @program: paas-all
 * @description: 系统分组配额服务
 * @author: Frown
 * @create: 2019-06-17 14:33
 **/
public interface SysCategoryQuotaService {

    /**
     * 保存配额
     * @param quota
     * @throws ApplicationException
     */
    void saveSysCategoryQuota(SysCategoryQuota quota) throws ApplicationException;

    /**
     * 删除配额
     * @param quota
     * @throws ApplicationException
     */
    void deleteSysCategoryQuota(SysCategoryQuota quota) throws ApplicationException;

    /**
     * 占用配额
     * @param quota
     * @throws ApplicationException
     */
    Map<Object, Object> useSysCategoryQuota(SysCategoryQuotaUsed quota) throws ApplicationException;

    /**
     * 释放配额
     * @param quota
     * @throws ApplicationException
     */
    Map<Object, Object> freeSysCategoryQuota(SysCategoryQuotaUsed quota) throws ApplicationException;
}
