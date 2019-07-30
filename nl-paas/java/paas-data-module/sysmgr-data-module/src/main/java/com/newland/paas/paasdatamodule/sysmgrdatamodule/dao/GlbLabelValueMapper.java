package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabelValue;
import java.util.List;

/**
 * GlbLabelValueMapper
 */
public interface GlbLabelValueMapper {
    /**
     * countBySelective
     * @param record
     * @return
     */
    int countBySelective(GlbLabelValue record);

    /**
     * deleteBySelective
     * @param record
     * @return
     */
    int deleteBySelective(GlbLabelValue record);

    /**
     * deleteByPrimaryKey
     * @param glbLabelValueId
     * @return
     */
    int deleteByPrimaryKey(Long glbLabelValueId);

    /**
     * insert
     * @param record
     * @return
     */
    int insert(GlbLabelValue record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(GlbLabelValue record);

    /**
     * selectBySelective
     * @param record
     * @return
     */
    List<GlbLabelValue> selectBySelective(GlbLabelValue record);

    /**
     * selectByPrimaryKey
     * @param glbLabelValueId
     * @return
     */
    GlbLabelValue selectByPrimaryKey(Long glbLabelValueId);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(GlbLabelValue record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(GlbLabelValue record);
}
