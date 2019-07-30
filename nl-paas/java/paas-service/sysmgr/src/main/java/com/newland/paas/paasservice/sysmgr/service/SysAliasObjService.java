package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysAliasObjVO;

import java.util.List;

/**
 * 别名关系管理
 *
 * @author WRP
 * @since 2019/1/11
 */
public interface SysAliasObjService {

    /**
     * 别名列表查询
     * 根据查询条件(类型、父级)获取别名列表
     *
     * @param aliasObjType 别名对象类型
     * @param objCodeP     父对象CODE
     */
    List<SysAliasObjVO> getAliasList(String aliasObjType, String objCodeP);

    /**
     * 别名翻译查询
     * 根据CODE获取引用对象CODE
     *
     * @param aliasObjType 别名对象类型
     * @param aliasObjCode 别名对象CODE
     */
    String getObjCode(String aliasObjType, String aliasObjCode);

}
