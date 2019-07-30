package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;

/**
 * 别名管理服务接口
 */
public interface SysAliasService {

    /**
     * 获取别名管理左侧属性结构
     *
     * @return
     */
    List<BaseTreeDo> getSysAliasTree();

    /**
     * 新增别名
     *
     * @param sysAliasObj
     * @throws ApplicationException
     */
    SysAliasObj addSysAlias(SysAliasObj sysAliasObj) throws ApplicationException;

    /**
     * 更新别名
     *
     * @param sysAliasObj
     * @throws ApplicationException
     */
    SysAliasObj updateSysAlias(SysAliasObj sysAliasObj) throws ApplicationException;

    /**
     * 别名删除
     *
     * @param sysAliasObjCode
     */
    void deleteSysAliasObj(String sysAliasObjCode) throws ApplicationException;

    /**
     * 查询别名
     *
     * @param sysAliasObjCode
     * @return
     */
    SysAliasObj getSysAliasObj(String sysAliasObjCode) throws ApplicationException;

    /**
     * 根据sysAliasObjCode获取
     * @param sysAliasObjCode
     * @return
     */
    List<SysAliasObj> listSysAliasObjsBySysAliasObjCode(String sysAliasObjCode);


    /**
     * 根据AliasObjName获取
     * @param AliasObjName
     * @return
     * @throws ApplicationException
     */
    List<SysAliasObj> getSysAliasObjByAliasObjName(String aliasObjName) throws ApplicationException;
}
