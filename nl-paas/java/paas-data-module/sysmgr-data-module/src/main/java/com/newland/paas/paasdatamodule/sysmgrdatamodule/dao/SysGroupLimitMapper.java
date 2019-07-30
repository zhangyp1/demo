package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupLimit;
import java.util.List;

public interface SysGroupLimitMapper {
    int countBySelective(SysGroupLimit record);

    int deleteBySelective(SysGroupLimit record);

    int deleteByPrimaryKey(Long id);

    int insert(SysGroupLimit record);

    int insertSelective(SysGroupLimit record);

    List<SysGroupLimit> selectBySelective(SysGroupLimit record);

    SysGroupLimit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysGroupLimit record);

    int updateByPrimaryKey(SysGroupLimit record);
}