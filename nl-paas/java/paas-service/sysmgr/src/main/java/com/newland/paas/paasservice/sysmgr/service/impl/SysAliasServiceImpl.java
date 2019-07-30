package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysAliasObjMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.error.SysAliasError;
import com.newland.paas.paasservice.sysmgr.service.SysAliasService;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 别名管理实现类
 */

@Service("sysAliasService")
public class SysAliasServiceImpl implements SysAliasService {

    private static final Log LOG = LogFactory.getLogger(SysAliasServiceImpl.class);


    @Autowired
    private SysAliasObjMapper sysAliasObjMapper;

    private static final String PARAMS_SYS_ALIAS_OBJ_CODE = "sysAliasObjCode:";

    @Override
    public List<BaseTreeDo> getSysAliasTree() {
        return sysAliasObjMapper.getSysAliasTree(RequestContext.getSession().getTenantId());
    }

    @Override
    public SysAliasObj addSysAlias(SysAliasObj sysAliasObj) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "sysAliasObj:" + sysAliasObj);
        // 标识不允许重复
        SysAliasObj sysAliasObjReq = new SysAliasObj();
        sysAliasObjReq.setAliasObjCode(sysAliasObj.getAliasObjCode());
        List<SysAliasObj> list = sysAliasObjMapper.selectBySelective(sysAliasObjReq);
        if (list != null && !list.isEmpty()) {
            throw new ApplicationException(SysAliasError.DUPLICATE_ALIAS_CODE_ERROR);
        }
        // 名称不允许重复
        List<SysAliasObj> list2 = this.getSysAliasObjByAliasObjName(sysAliasObj.getAliasObjName());
        if (list2 != null && !list2.isEmpty()) {
            throw new ApplicationException(SysAliasError.DUPLICATE_ALIAS_NAME_ERROR);
        }

        sysAliasObj.setTenantId(RequestContext.getTenantId());
        sysAliasObj.setCreator(RequestContext.getUserId());
        if (null == sysAliasObj.getObjCodeP() || "".equals(sysAliasObj.getObjCodeP())) {
            sysAliasObj.setObjCodeP("-1");
        }
        sysAliasObjMapper.insertSelective(sysAliasObj);
        return sysAliasObj;
    }

    @Override
    public SysAliasObj updateSysAlias(SysAliasObj sysAliasObj) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "sysAliasObj:" + JSON.toJSONString(sysAliasObj));
        // aliasObjCode不能为空
        if (sysAliasObj.getAliasObjCode() == null || "".equals(sysAliasObj.getAliasObjCode())) {
            throw new ApplicationException(SysAliasError.ALIAS_OBJ_CODE_NULL_ERROR);
        }

        // 标识不允许重复
        SysAliasObj sysAliasObjReq = new SysAliasObj();
        sysAliasObjReq.setAliasObjCode(sysAliasObj.getAliasObjCode());
        List<SysAliasObj> list = sysAliasObjMapper.selectBySelective(sysAliasObjReq);
        if (list != null && !list.isEmpty()
                && !Objects.equals(list.get(0).getAliasObjCode(), sysAliasObj.getAliasObjCode())) {
            throw new ApplicationException(SysAliasError.DUPLICATE_ALIAS_CODE_ERROR);
        }
        // 名称不允许重复
        List<SysAliasObj> list2 = this.getSysAliasObjByAliasObjName(sysAliasObj.getAliasObjName());
        if (list2 != null && !list2.isEmpty()
                && !Objects.equals(list2.get(0).getAliasObjName(), sysAliasObj.getAliasObjName())) {
            throw new ApplicationException(SysAliasError.DUPLICATE_ALIAS_NAME_ERROR);
        }

        sysAliasObjMapper.updateByPrimaryKeySelective(sysAliasObj);
        return sysAliasObj;
    }

    @Override
    public void deleteSysAliasObj(String sysAliasObjCode) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, PARAMS_SYS_ALIAS_OBJ_CODE + sysAliasObjCode);
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP(sysAliasObjCode);
        List<SysAliasObj> list = sysAliasObjMapper.listSubAliasObj(sysAliasObj);
        if (list != null && !list.isEmpty()) {
            throw new ApplicationException(SysAliasError.HAS_SUB_ERROR);
        } else {
            sysAliasObj.setObjCodeP(null);
            sysAliasObj.setAliasObjCode(sysAliasObjCode);
        }
        sysAliasObjMapper.deleteBySelective(sysAliasObj);
    }

    @Override
    public SysAliasObj getSysAliasObj(String sysAliasObjCode) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, PARAMS_SYS_ALIAS_OBJ_CODE + sysAliasObjCode);
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjCode(sysAliasObjCode);
        List<SysAliasObj> list = sysAliasObjMapper.selectBySelective(sysAliasObj);
        if (list.size() != 1) {
            throw new ApplicationException(SysAliasError.DUPLICATE_ALIAS_CODE_ERROR);
        }
        return list.get(0);
    }

    @Override
    public List<SysAliasObj> listSysAliasObjsBySysAliasObjCode(String sysAliasObjCode) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, PARAMS_SYS_ALIAS_OBJ_CODE + sysAliasObjCode);
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjCode(sysAliasObjCode);
        return sysAliasObjMapper.selectBySelective(sysAliasObj);
    }

    /**
     * 根据AliasObjName获取
     *
     * @param aliasObjName@return
     * @throws ApplicationException
     */
    @Override
    public List<SysAliasObj> getSysAliasObjByAliasObjName(String aliasObjName) throws ApplicationException {
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjName(aliasObjName);
        return sysAliasObjMapper.selectBySelective(sysAliasObj);
    }
}
