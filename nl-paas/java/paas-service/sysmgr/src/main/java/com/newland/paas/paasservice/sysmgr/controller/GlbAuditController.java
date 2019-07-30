package com.newland.paas.paasservice.sysmgr.controller;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.AuditLog;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAudit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAuditLogAttr;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupObjBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.AuditVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbAuditLogAttrVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbAuditVo;
import com.newland.paas.paasservice.sysmgr.service.GlbAuditService;
import com.newland.paas.paasservice.sysmgr.service.SysObjService;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 审计日志查询
 * Author:PanYang
 * Date:Created in 下午2:41 2018/7/30
 * Modified By:
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "common/v1/glbAudit")
@Validated
public class GlbAuditController {
    private static final Log LOGGER = LogFactory.getLogger(GlbAuditController.class);
    @Autowired
    private GlbAuditService glbAuditService;
    @Autowired
    private SysObjService sysObjService;
    @Value("${4AFTP.ftpName}")
    private static String ftpName;
    @Value("${4AFTP.ftpPassword}")
    private static String ftpPassword;
    @Value("${4AFTP.ftpPath}")
    private static String ftpPath;
    @Value("${4AFTP.ftpAddress}")
    private static String ftpAddress;

    /**
     * 审计日志写入接口
     */
    @PutMapping(value = "/save-audit",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void saveAuditLog(@RequestBody BasicRequestContentVo<AuditVo> reqInfo) {
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("GlbAuditController saveAuditLogs start reqInfo is [{0}]",
                        JSONObject.toJSONString(reqInfo)));
        AuditVo auditVo = reqInfo.getParams();
        //用户账号
        String account = "";
        String objectType = "";
        String objectId = "";
        String operateCode = "";
        String operateName = "";
        // 对象名称
        String objName = null;
        List<GlbAuditLogAttr> glbAuditLogAttrList = new ArrayList<>();
        List<GlbAuditLogAttrVo> glbAuditLogAttrVoList = auditVo.getGlbAuditLogAttr();
        for (GlbAuditLogAttrVo glbAuditLogAttrVo : glbAuditLogAttrVoList) {
            GlbAuditLogAttr glbAuditLogAttr = new GlbAuditLogAttr();
            glbAuditLogAttr.setAttrKey(glbAuditLogAttrVo.getAttrKey() == null ? " " : glbAuditLogAttrVo.getAttrKey());
            glbAuditLogAttr.setAttrValue(glbAuditLogAttrVo.getAttrValue()
                    == null ? " " : glbAuditLogAttrVo.getAttrValue());
            glbAuditLogAttrList.add(glbAuditLogAttr);
            if (glbAuditLogAttr.getAttrKey().equals("operate_account")) {
                account = glbAuditLogAttr.getAttrValue();
            }
            if (glbAuditLogAttr.getAttrKey().equals("object_type")) {
                objectType = glbAuditLogAttr.getAttrValue();
            }
            if (glbAuditLogAttr.getAttrKey().equals("object_id")) {
                objectId = glbAuditLogAttr.getAttrValue();
                SysGroupObjBO sysGroupObjBO = sysObjService.getGroupObjDetail(Long.parseLong(objectId));
                if (sysGroupObjBO != null && sysGroupObjBO.getObjName() != null) {
                    objName = sysGroupObjBO.getObjName();
                }
            }
            if (glbAuditLogAttr.getAttrKey().equals("operate_code")) {
                operateCode = glbAuditLogAttr.getAttrValue();
            }
            if (glbAuditLogAttr.getAttrKey().equals("operate_name")) {
                operateName = glbAuditLogAttr.getAttrValue();
            }
        }
        GlbAudit glbAudit = getGlbAudit(auditVo, operateName, objName, glbAuditLogAttrList);
        AuditLog al = new AuditLog();
        al.setGlbAuditLogAttr(glbAuditLogAttrList);
        al.setGlbAudit(glbAudit);
        //入库
        glbAuditService.putAudit(al);
        //写入xml
        glbAuditService.writeAuditDate(al, account, objectType, objectId, operateCode, operateName);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL, "GlbAuditController saveAuditLogs end");
    }

    /**
     * getGlbAudit
     *
     * @param auditVo
     * @param operateName
     * @param objName
     * @param glbAuditLogAttrList
     * @return
     */
    private GlbAudit getGlbAudit(AuditVo auditVo, String operateName,
                                 String objName, List<GlbAuditLogAttr> glbAuditLogAttrList) {
        // 对象操作，重新构造描述
        GlbAuditVo glbAuditVo = auditVo.getGlbAudit();
        GlbAudit glbAudit = buildGlbAudit(glbAuditVo);
        if (!StringUtils.isEmpty(objName)) {
            GlbAuditLogAttr objNameAttr = new GlbAuditLogAttr();
            objNameAttr.setAttrKey("object_name");
            objNameAttr.setAttrValue(objName);
            glbAuditLogAttrList.add(objNameAttr);
            glbAudit.setAuditDesc("对象【" + objName + "】-" + operateName);
        }
        return glbAudit;
    }

    /**
     * 构建GlbAudit
     *
     * @param glbAuditVo
     * @return
     */
    private GlbAudit buildGlbAudit(GlbAuditVo glbAuditVo) {
        GlbAudit glbAudit = new GlbAudit();
        glbAudit.setAuditCategory(glbAuditVo.getCategory() == null ? " " : glbAuditVo.getCategory());
        glbAudit.setAuditDesc(glbAuditVo.getDesc() == null ? " " : glbAuditVo.getDesc());
        glbAudit.setIp(glbAuditVo.getIp() == null ? " " : glbAuditVo.getIp());
        glbAudit.setAuditModule(glbAuditVo.getModule() == null ? " " : glbAuditVo.getModule());
        glbAudit.setAuditVersion(glbAuditVo.getVersion() == null ? " " : glbAuditVo.getVersion());
        glbAudit.setCreateTime(new Date());
        return glbAudit;
    }

    /**
     * 查询审计日志
     *
     * @param
     * @return
     */

    @PostMapping(value = "/getAudit")
    public ResultPageData<GlbAudit> pageQueryAudit(@RequestBody BasicRequestContentVo<GlbAuditVo> vo) {
        GlbAudit glbAudit = new GlbAudit();
        PageInfo pageInfo = new PageInfo();
        glbAudit.setAuditCategory(vo.getParams().getCategory());
        glbAudit.setAuditDesc(vo.getParams().getDesc());
        glbAudit.setIp(vo.getParams().getIp());
        glbAudit.setAuditModule(vo.getParams().getModule());
        glbAudit.setAuditVersion(vo.getParams().getVersion());
        String startTime = vo.getParams().getStartTime();
        String endTime = vo.getParams().getEndTime();
        if (StringUtils.isNotEmpty(startTime)) {
            String[] stTime = startTime.split("@");
            glbAudit.setStartTime(stTime[0] + " " + stTime[1]);
        }

        if (StringUtils.isNotEmpty(endTime)) {
            String[] enTime = endTime.split("@");
            glbAudit.setEndTime(enTime[0] + " " + enTime[1]);
        }
        glbAudit.setObject_name(vo.getParams().getObject_name());
        glbAudit.setOperate_account(vo.getParams().getOperate_account());
        glbAudit.setOperate_name(vo.getParams().getOperate_name());
        pageInfo.setCurrentPage(Integer.parseInt(vo.getParams().getCurrentPage()));
        pageInfo.setPageSize(Integer.parseInt(vo.getParams().getPageSize()));
        return glbAuditService.getAllGlbAudit(glbAudit, pageInfo);
    }

    /**
     * 查询审计日志关联对象
     *
     * @param
     * @return
     */

    @GetMapping(value = "/{id}")
    public List<GlbAuditLogAttr> queryAuditOperate(@PathVariable("id") Long id) {
        return glbAuditService.getAllGlbAuditOperate(id);
    }

}
