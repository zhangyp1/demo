package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabelValue;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelQueryVo;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;

/**
 * @program: paas-all
 * @description: 标签值
 * @author: Frown
 * @create: 2019-05-30 17:00
 **/
public interface GlbLabelValueService {

    /**
     * 查询标签值
     * @param queryVo
     * @return
     */
    List<GlbLabelValue> listGlbLabelValue(GlbLabelQueryVo queryVo);

    /**
     * 保存标签值
     * @return
     */
    GlbLabelValue saveGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException;

    /**
     * 获取标签值
     * @return
     */
    GlbLabelValue getGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException;

    /**
     * 删除标签值
     * @param glbLabelValue
     */
    void deleteGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException;

}
