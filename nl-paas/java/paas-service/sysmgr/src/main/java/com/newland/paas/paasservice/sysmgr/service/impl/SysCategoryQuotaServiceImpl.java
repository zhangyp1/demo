package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryQuotaMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryQuotaUsedMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuota;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuotaUsed;
import com.newland.paas.paasservice.sysmgr.error.SysCategoryQuotaError;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryQuotaService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2019-06-17 14:38
 **/
@Service("sysCategoryQuotaService")
public class SysCategoryQuotaServiceImpl implements SysCategoryQuotaService {
    private static final Log LOG = LogFactory.getLogger(SysCategoryQuotaServiceImpl.class);

    @Autowired
    private SysCategoryQuotaMapper sysCategoryQuotaMapper;
    @Autowired
    private SysCategoryQuotaUsedMapper sysCategoryQuotaUsedMapper;

    /**
     * 保存配额
     *
     * @param quota
     * @throws ApplicationException
     */
    @Override
    @Transactional
    public void saveSysCategoryQuota(SysCategoryQuota quota) throws ApplicationException {
        SysCategoryQuota quotaReq = new SysCategoryQuota();
        quotaReq.setSysCategoryId(quota.getSysCategoryId());
        List<SysCategoryQuota> quotas = sysCategoryQuotaMapper.selectBySelective(quotaReq);
        SysCategoryQuota quotaDes = new SysCategoryQuota();
        if (quotas != null && !quotas.isEmpty()) {
            // 检查配额
            Boolean bValid = this.checkSysCategoryQuotaUsed(quota);
            if (bValid) {
                quotaDes.setId(quotas.get(0).getId());
                quotaDes.setCpuQuota(quota.getCpuQuota());
                quotaDes.setMemoryQuota(quota.getMemoryQuota());
                quotaDes.setStorageQuota(quota.getStorageQuota());
                sysCategoryQuotaMapper.updateByPrimaryKeySelective(quotaDes);
            }
        } else {
            quotaDes.setCpuQuota(quota.getCpuQuota());
            quotaDes.setMemoryQuota(quota.getMemoryQuota());
            quotaDes.setStorageQuota(quota.getStorageQuota());
            quotaDes.setSysCategoryId(quota.getSysCategoryId());
            if (quota.getTenantId() == null) {
                quotaDes.setTenantId(RequestContext.getTenantId());
            } else {
                quotaDes.setTenantId(quota.getTenantId());
            }
            quotaDes.setCreatorId(RequestContext.getUserId());
            sysCategoryQuotaMapper.insertSelective(quotaDes);
        }
    }


    /**
     * 检查已使用系统分组配额
     * @param quota
     * @return
     * @throws ApplicationException
     */
    private Boolean checkSysCategoryQuotaUsed(SysCategoryQuota quota) throws ApplicationException {
        SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
        quotaUsedReq.setCategoryId(quota.getSysCategoryId());
        SysCategoryQuotaUsed quotaUsed = sysCategoryQuotaUsedMapper.countQuotaUsedBySysCategory(quotaUsedReq);

        PaasError paasError = SysCategoryQuotaError.QUOTA_OVER_ERROR;
        PaasError error = new PaasError(paasError.getCode(), paasError.getDescription(), paasError.getSolution());
        if (quota.getCpuQuota() > 0 && quota.getCpuQuota() < quotaUsed.getCpuQuota()) {
            error.setMsgArguments("cpu");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }

        if (quota.getMemoryQuota() > 0 && quota.getMemoryQuota() < quotaUsed.getMemoryQuota()) {
            error.setMsgArguments("内存");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }

        if (quota.getStorageQuota() > 0 && quota.getStorageQuota() < quotaUsed.getStorageQuota()) {
            error.setMsgArguments("存储");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }

        return true;
    }

    /**
     * 删除配额
     *
     * @param quota
     * @throws ApplicationException
     */
    @Override
    @Transactional
    public void deleteSysCategoryQuota(SysCategoryQuota quota) {
        SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
        quotaUsedReq.setCategoryId(quota.getSysCategoryId());
        sysCategoryQuotaUsedMapper.deleteBySelective(quotaUsedReq);

        SysCategoryQuota quotaReq = new SysCategoryQuota();
        quotaReq.setSysCategoryId(quota.getSysCategoryId());
        sysCategoryQuotaMapper.deleteBySelective(quotaReq);
    }

    /**
     * 占用配额
     *
     * @param quota
     * @throws ApplicationException
     */
    @Override
    @Transactional
    public Map<Object, Object> useSysCategoryQuota(SysCategoryQuotaUsed quota) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("占用系统分组配额，入参: {0}",
                JSONObject.toJSONString(quota)));
        Map<Object, Object> resultMap = new HashMap<>();
        Boolean result = true;
        String message = "";
        try {
            SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
            quotaUsedReq.setObjId(quota.getObjId());
            List<SysCategoryQuotaUsed> quotaUseds = sysCategoryQuotaUsedMapper.selectBySelective(quotaUsedReq);
            if (quotaUseds != null && !quotaUseds.isEmpty()) {
                SysCategoryQuotaUsed curQuotaUsed = quotaUseds.get(0);
                sysCategoryQuotaUsedMapper.deleteByPrimaryKey(curQuotaUsed.getId());
                // 判断配额是否超出
                this.checkSysCategoryQuotaEnough(quota);
                curQuotaUsed.setCpuQuota(quota.getCpuQuota());
                curQuotaUsed.setMemoryQuota(quota.getMemoryQuota());
                curQuotaUsed.setStorageQuota(quota.getStorageQuota());
                sysCategoryQuotaUsedMapper.insertSelective(curQuotaUsed);
            } else {
                // 判断配额是否超出
                this.checkSysCategoryQuotaEnough(quota);
                SysCategoryQuotaUsed quotaUsed = new SysCategoryQuotaUsed();
                quotaUsed.setObjId(quota.getObjId());
                quotaUsed.setObjName(quota.getObjName());
                quotaUsed.setCpuQuota(quota.getCpuQuota());
                quotaUsed.setMemoryQuota(quota.getMemoryQuota());
                quotaUsed.setStorageQuota(quota.getStorageQuota());
                quotaUsed.setCategoryId(quota.getCategoryId());
                quotaUsed.setCreatorId(RequestContext.getUserId());
                quotaUsed.setTenantId(RequestContext.getTenantId());
                sysCategoryQuotaUsedMapper.insertSelective(quotaUsed);
            }
        } catch (ApplicationException e) {
            result = false;
            message = e.getError().getDescription();
            LOG.warn(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("占用系统分组配额失败，原因: {0}",
                    e.getError().getDescription()));
        }
        resultMap.put("result", result);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 检查系统分组配额是否够用
     * @param quota
     * @return
     */
    private void checkSysCategoryQuotaEnough(SysCategoryQuotaUsed quota) throws ApplicationException {
        // 获取原始配额
        SysCategoryQuota quotaReq = new SysCategoryQuota();
        quotaReq.setSysCategoryId(quota.getCategoryId());
        List<SysCategoryQuota> quotas = sysCategoryQuotaMapper.selectBySelective(quotaReq);
        SysCategoryQuota quotaRaw = quotas.get(0);

        // 获取已使用配额
        SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
        quotaUsedReq.setCategoryId(quota.getCategoryId());
        SysCategoryQuotaUsed quotaUsed = sysCategoryQuotaUsedMapper.countQuotaUsedBySysCategory(quotaUsedReq);

        PaasError paasError = SysCategoryQuotaError.QUOTA_NOT_ENOUGH_ERROR;
        PaasError error = new PaasError(paasError.getCode(), paasError.getDescription(), paasError.getSolution());
        if (quotaRaw.getCpuQuota() > 0
                && quota.getCpuQuota() != null
                && quota.getCpuQuota() > quotaRaw.getCpuQuota() - quotaUsed.getCpuQuota()) {
            error.setMsgArguments("cpu");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }

        if (quotaRaw.getMemoryQuota() > 0
                && quota.getMemoryQuota() != null
                && quota.getMemoryQuota() > quotaRaw.getMemoryQuota() - quotaUsed.getMemoryQuota()) {
            error.setMsgArguments("内存");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }

        if (quotaRaw.getStorageQuota() > 0
                && quota.getStorageQuota() != null
                && quota.getStorageQuota() > quotaRaw.getStorageQuota() - quotaUsed.getStorageQuota()) {
            error.setMsgArguments("存储");
            error.setDescription(error.getDescription());
            throw new ApplicationException(error);
        }
    }

    /**
     * 释放配额
     *
     * @param quota
     * @throws ApplicationException
     */
    @Override
    @Transactional
    public Map<Object, Object> freeSysCategoryQuota(SysCategoryQuotaUsed quota) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("释放系统分组配额，入参: {0}",
                JSONObject.toJSONString(quota)));
        Map<Object, Object> resultMap = new HashMap<>();
        Boolean result = true;
        String message = "";
        SysCategoryQuotaUsed quotaUsedReq = new SysCategoryQuotaUsed();
        quotaUsedReq.setCategoryId(quota.getCategoryId());
        quotaUsedReq.setObjId(quota.getObjId());
        sysCategoryQuotaUsedMapper.deleteBySelective(quotaUsedReq);
        resultMap.put("result", result);
        resultMap.put("message", message);
        return resultMap;
    }
}
