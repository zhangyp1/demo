package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.MsgType;

import java.util.List;

public interface MsgTypeMapper {
    int countBySelective(MsgType record);

    int deleteBySelective(MsgType record);

    int deleteByPrimaryKey(Long msgTypeId);

    int insert(MsgType record);

    int insertSelective(MsgType record);

    List<MsgType> selectBySelective(MsgType record);

    MsgType selectByPrimaryKey(Long msgTypeId);

    int updateByPrimaryKeySelective(MsgType record);

    int updateByPrimaryKey(MsgType record);
}