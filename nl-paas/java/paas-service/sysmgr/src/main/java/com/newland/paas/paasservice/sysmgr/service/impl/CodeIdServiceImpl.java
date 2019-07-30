package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.CodeIdMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.CodeId;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.CodeIdVO;
import com.newland.paas.paasservice.sysmgr.service.CodeIdService;
import com.newland.paas.sbcommon.codeid.CodeIdUtil;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WRP
 * @since 2019/1/10
 */
@Service
public class CodeIdServiceImpl implements CodeIdService {

    public static final Log LOG = LogFactory.getLogger(CodeIdServiceImpl.class);

    @Autowired
    private CodeIdMapper codeIdMapper;

    @Override
    public void syncCodeId() {
        List<CodeId> codeIds = codeIdMapper.selectBySelective(null);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "codeIds:" + JSON.toJSONString(codeIds));
        if (!codeIds.isEmpty()) {
            Map<String, Map<String, Object>> codeIdMap = new HashMap<>();
            codeIds.forEach(v -> {
                if (StringUtils.isNotEmpty(v.getCode()) && StringUtils.isNotEmpty(v.getId())) {
                    if (!codeIdMap.containsKey(v.getTableName())) {
                        codeIdMap.put(v.getTableName(), new HashMap<>());
                    }
                    codeIdMap.get(v.getTableName()).put(v.getCode(), new CodeIdVO(v.getId(), v.getName()));
                }
            });
            CodeIdUtil.saveToRedis(codeIdMap);
        }
    }

    @Override
    public void syncSingleCodeId(CodeIdVO codeIdVO) {
        CodeIdUtil.saveSingleToRedis(codeIdVO);
    }

    @Override
    public ResultPageData<CodeIdVO> selectByParams(CodeId codeId, PageInfo pageInfo) {
        Page<CodeIdVO> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        codeId.setCodeNotNull(true);
        List<CodeId> codeIds = codeIdMapper.selectBySelective(codeId);
        pageInfo.setTotalRecord(page.getTotal());
        return new ResultPageData(codeIds.stream().map(this::toVo).collect(Collectors.toList()), pageInfo);
    }

    /**
     * CodeId转CodeIdVO
     *
     * @param codeId
     * @return
     */
    private CodeIdVO toVo(CodeId codeId) {
        CodeIdVO codeIdVO = new CodeIdVO();
        codeIdVO.setTableName(codeId.getTableName());
        codeIdVO.setCode(codeId.getCode());
        codeIdVO.setId(codeId.getId());
        codeIdVO.setName(codeId.getName());
        codeIdVO.setType(codeId.getType());
        return codeIdVO;
    }

}
