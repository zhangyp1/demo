package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDictQuota;

import java.util.List;

public interface GlbDictQuotaMapper {
    int countBySelective(GlbDictQuota record);

    int deleteBySelective(GlbDictQuota record);

    int insert(GlbDictQuota record);

    int insertSelective(GlbDictQuota record);

    List<GlbDictQuota> selectBySelective(GlbDictQuota record);
}