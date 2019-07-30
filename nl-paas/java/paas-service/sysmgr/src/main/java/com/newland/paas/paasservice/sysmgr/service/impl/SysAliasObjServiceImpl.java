package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysAliasObjMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysAliasObjVO;
import com.newland.paas.paasservice.sysmgr.service.SysAliasObjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WRP
 * @since 2019/1/11
 */
@Service
public class SysAliasObjServiceImpl implements SysAliasObjService {

    public static final Log LOG = LogFactory.getLogger(SysAliasObjServiceImpl.class);

    private static final String PARAMS_NO_PARENT_CODE = "0";

    @Autowired
    private SysAliasObjMapper sysAliasObjMapper;

    @Override
    public List<SysAliasObjVO> getAliasList(String aliasObjType, String objCodeP) {
        if (PARAMS_NO_PARENT_CODE.equals(objCodeP)) {
            objCodeP = null;
        }
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjType(aliasObjType);
        sysAliasObj.setObjCodeP(objCodeP);
        List<SysAliasObj> sysAliasObjs = sysAliasObjMapper.selectBySelective(sysAliasObj);
        return sysAliasObjs.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public String getObjCode(String aliasObjType, String aliasObjCode) {
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjType(aliasObjType);
        sysAliasObj.setAliasObjCode(aliasObjCode);
        List<SysAliasObj> sysAliasObjs = sysAliasObjMapper.selectBySelective(sysAliasObj);
        return sysAliasObjs.isEmpty() ? null : sysAliasObjs.get(0).getObjCode();
    }

    /**
     * SysAliasObj转SysAliasObjVO
     *
     * @param sysAliasObj
     * @return
     */
    private SysAliasObjVO toVO(SysAliasObj sysAliasObj) {
        SysAliasObjVO tmp = new SysAliasObjVO();
        tmp.setAliasObjCode(sysAliasObj.getAliasObjCode());
        tmp.setAliasObjType(sysAliasObj.getAliasObjType());
        tmp.setObjCode(sysAliasObj.getObjCode());
        tmp.setObjCodeP(sysAliasObj.getObjCodeP());
        tmp.setAliasObjName(sysAliasObj.getAliasObjName());
        tmp.setObjName(sysAliasObj.getObjName());
        return tmp;
    }

}
