package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObjKey;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;

import java.util.List;

public interface SysAliasObjMapper {
    int countBySelective(SysAliasObj record);

    int deleteBySelective(SysAliasObj record);

    int deleteByPrimaryKey(SysAliasObjKey key);

    int insert(SysAliasObj record);

    int insertSelective(SysAliasObj record);

    List<SysAliasObj> selectBySelective(SysAliasObj record);

    SysAliasObj selectByPrimaryKey(SysAliasObjKey key);

    int updateByPrimaryKeySelective(SysAliasObj record);

    int updateByPrimaryKey(SysAliasObj record);

    List<BaseTreeDo> getSysAliasTree(Long tenantId);

    List<SysAliasObj> listSubAliasObj(SysAliasObj sysAliasObj);
}