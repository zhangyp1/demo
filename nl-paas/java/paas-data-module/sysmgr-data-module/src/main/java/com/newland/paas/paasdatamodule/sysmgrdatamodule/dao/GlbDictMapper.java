package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GlbDictMapper {
    int countBySelective(GlbDict record);

    int deleteBySelective(GlbDict record);

    int insert(GlbDict record);

    int insertSelective(GlbDict record);

    int updateByDictCode(GlbDict record);

    int updateByDictId(GlbDict record);

    List<GlbDict> selectBySelective(GlbDict record);

    /**
     * 查找所有字典信息（DICT_CODE, DICT_NAME, DICT_PCODE）
     *
     * @return
     */
    List<GlbDict> findBaseAll();

    /**
     * 翻译code，未传code根据父code查所有子code
     *
     * @param dictPcode
     * @param dictCodes
     * @return
     */
    List<String> selectByDictCode(@Param(value = "dictPcode") String dictPcode,
                                  @Param(value = "dictCodes") List<String> dictCodes,
                                  @Param(value = "notInDictCodes") List<String> notInDictCodes);

    List<GlbDict> selectDetailByDictCode(@Param(value = "dictPcode") String dictPcode,
                                         @Param(value = "dictCodes") List<String> dictCodes,
                                         @Param(value = "notInDictCodes") List<String> notInDictCodes);

    List<GlbDict> selectByDictPcode(@Param(value = "dictPcode") String dictPcode);

}