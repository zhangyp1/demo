package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelQueryVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelRspVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;
import java.util.Map;

/**
 * @program: paas-all
 * @description: 标签项
 * @author: Frown
 * @create: 2019-05-30 17:00
 **/
public interface GlbLabelService {

    /**
     * 分页查询标签项
     * @param queryVo
     * @param pageInfo
     * @return
     */
    ResultPageData<GlbLabel> pageGlbLabel(GlbLabelQueryVo queryVo, PageInfo pageInfo) throws ApplicationException;

    /**
     * 保存标签项
     * @return
     */
    GlbLabel saveGlbLabel(GlbLabel glbLabel) throws ApplicationException;

    /**
     * 删除标签项
     * @param glbLabel
     */
    void deleteGlbLabel(GlbLabel glbLabel) throws ApplicationException;

    /**
     * 获取标签项
     * @param glbLabel
     * @return
     */
    GlbLabel getGlbLabel(GlbLabel glbLabel) throws ApplicationException;

    /**
     * 查询标签值分组
     * @param queryVo
     * @return
     */
    List<GlbLabelRspVo> listGroupGlbLabel(GlbLabelQueryVo queryVo) throws ApplicationException;

    /**
     *  检查标签编码
     * @param queryVo
     * @return
     */
    Map checkLabelCode(GlbLabelQueryVo queryVo);

    /**
     *  检查标签名称
     * @param queryVo
     * @return
     */
    Map checkLabelName(GlbLabelQueryVo queryVo);
}
