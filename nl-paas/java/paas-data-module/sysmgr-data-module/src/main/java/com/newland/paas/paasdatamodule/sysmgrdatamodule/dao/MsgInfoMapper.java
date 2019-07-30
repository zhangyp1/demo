package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgInfo;

import java.util.List;

public interface MsgInfoMapper {
    int countBySelective(MsgInfo record);

    int deleteBySelective(MsgInfo record);

    int deleteByPrimaryKey(Long msgId);

    int insert(MsgInfo record);

    int insertSelective(MsgInfo record);

    List<MsgInfo> selectBySelectiveWithBLOBs(MsgInfo record);

    List<MsgInfo> selectBySelective(MsgInfo record);

    MsgInfo selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(MsgInfo record);

    int updateByPrimaryKeyWithBLOBs(MsgInfo record);

    int updateByPrimaryKey(MsgInfo record);
}