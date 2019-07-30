package com.newland.paas.paasservice.sysmgr.controller;

import com.alibaba.fastjson.JSON;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.CodeId;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.CodeIdVO;
import com.newland.paas.paasservice.sysmgr.service.CodeIdService;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CODE与ID对应
 *
 * @author WRP
 * @since 2019/1/10
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/codeid")
@Validated
public class CodeIdController {

    private static final Log LOG = LogFactory.getLogger(CodeIdController.class);

    @Autowired
    private CodeIdService codeIdService;

    /**
     * 同步全量信息
     */
    @GetMapping(value = "syncCodeId", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void syncCodeId() {
        LOG.info(LogProperty.LOGTYPE_CALL, "syncCodeId");
        codeIdService.syncCodeId();
    }

    /**
     * 同步单个信息
     *
     * @param codeIdVO CodeId信息
     */
    @PostMapping(value = "syncSingleCodeId", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void syncSingleCodeId(@Validated @RequestBody BasicRequestContentVo<CodeIdVO> codeIdVO) {
        LOG.info(LogProperty.LOGTYPE_CALL, "syncSingleCodeId:" + JSON.toJSONString(codeIdVO));
        CodeIdVO codeId = codeIdVO.getParams();
        if (StringUtils.isEmpty(codeId.getTableName()) || StringUtils.isEmpty(codeId.getId())
                || StringUtils.isEmpty(codeId.getCode())) {
            throw new SystemException(new PaasError("2-18-00053", "参数不全！"));
        }
        codeIdService.syncSingleCodeId(codeId);
    }

    /**
     * 根据别名类型查找
     *
     * @param type    别名类型
     * @param reuqest 参数
     * @return
     */
    @PostMapping(value = "selectCodeIdByType/{type}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<CodeIdVO> selectCodeIdByType(@PathVariable("type") String type,
                                                       @RequestBody BasicPageRequestContentVo<String> reuqest) {
        LOG.info(LogProperty.LOGTYPE_CALL, "selectCodeIdByType,type:" + type);
        CodeId codeId = new CodeId();
        codeId.setType(type);
        codeId.setCode(reuqest.getParams());
        return codeIdService.selectByParams(codeId, reuqest.getPageInfo());
    }

}
