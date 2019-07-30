package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.GlbLabelBo;

import java.util.List;

public interface GlbLabelMapper {
    int countBySelective(GlbLabel record);

    int deleteBySelective(GlbLabel record);

    int deleteByPrimaryKey(Long glbLabelId);

    int insert(GlbLabel record);

    int insertSelective(GlbLabel record);

    List<GlbLabel> selectBySelective(GlbLabel record);

    GlbLabel selectByPrimaryKey(Long glbLabelId);

    int updateByPrimaryKeySelective(GlbLabel record);

    int updateByPrimaryKey(GlbLabel record);

    /**
     * 查询租户的标签（包括标签值）
     * @param record
     * @return
     */
    List<GlbLabelBo> listGlbLabels(GlbLabelBo record);

    /**
     * 按照条件过滤标签
     * @param record
     * @return
     */
    List<GlbLabel> lisGlbLabel(GlbLabelBo record);
}