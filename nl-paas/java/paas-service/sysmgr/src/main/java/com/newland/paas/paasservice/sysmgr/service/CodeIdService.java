package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.CodeId;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.CodeIdVO;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * CODE与ID对应Service
 *
 * @author WRP
 * @since 2019/1/10
 */
public interface CodeIdService {

    /**
     * 定时同步全量信息
     */
    void syncCodeId();

    /**
     * 同步单个信息
     *
     * @param codeIdVO CodeId信息
     */
    void syncSingleCodeId(CodeIdVO codeIdVO);

    /**
     * 查询列表
     *
     * @param codeId
     * @param pageInfo
     * @return
     */
    ResultPageData<CodeIdVO> selectByParams(CodeId codeId, PageInfo pageInfo);

}
