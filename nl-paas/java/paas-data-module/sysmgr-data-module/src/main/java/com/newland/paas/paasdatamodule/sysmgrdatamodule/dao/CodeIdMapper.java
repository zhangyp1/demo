package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.CodeId;

import java.util.List;

public interface CodeIdMapper {

    List<CodeId> selectBySelective(CodeId codeId);

}