package com.newland.paas.paasservice.sysmgr.controller;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupObjBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysObjOperateVo;
import com.newland.paas.paasservice.sysmgr.service.ObjPermissionHubService;
import com.newland.paas.paasservice.sysmgr.service.SysObjService;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightReqVo;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * 系统管理-对象权限
 *
 * @author WRP
 * @since 2018/7/31
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/objMgr")
@Validated
@AuditObject("对象管理")
public class SysObjController {

    public static final Log LOG = LogFactory.getLogger(SysObjController.class);

    @Autowired
    private SysObjService sysObjService;
    @Autowired
    private ObjPermissionHubService objPermissionHubService;

    private static final String CODE = "2-18-00053";
    private static final String DESCRIPTION = "参数不全！";

    /**
     * 同步工组对象数据
     */
    @GetMapping(value = "syncGroupObjByObjId/{objId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void syncGroupObjByObjId(@PathVariable(value = "objId") Long objId) {
        // TODO:此接口已经弃用，目前不再需要同步对象权限数据，但为了兼容旧的前端代码，暂时保留
    }

    /**
     * 分页查询工组对象列表
     *
     * @param objContentVo
     * @return
     */
    @PostMapping(value = "page", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData<SysGroupObjBO> page(@RequestBody BasicPageRequestContentVo<SysGroupObjBO> objContentVo)
        throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, "page,objContentVo：", JSON.toJSONString(objContentVo));
        if (objContentVo == null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        return sysObjService.pageViewByParams(objContentVo.getPageInfo(), objContentVo.getParams());
    }

    /**
     * 获取对象详情
     *
     * @param objId
     * @return
     */
    @GetMapping(value = "getGroupObjDetail/{objId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysGroupObjBO getGroupObjDetail(@PathVariable(value = "objId") Long objId) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, "getGroupObjDetail,objId：" + objId);
        if (objId == null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        return sysObjService.getGroupObjDetail(objId);
    }

    /**
     * 根据objId和groupId列表获取对应的授权与赋权
     *
     * @param objId
     * @param groupIdVo
     * @return
     */
    @PostMapping(value = "getGroupAndGroupRole/{objId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SrightFrightVo getGroupAndGroupRole(@PathVariable(value = "objId") Long objId,
        @RequestBody BasicRequestContentVo<List<Long>> groupIdVo) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, MessageFormat.format("getGroupObjSrightFright,objId：{0},groupIdVo：{1}",
            objId, JSON.toJSONString(groupIdVo)));
        if (objId == null || groupIdVo == null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        return sysObjService.getGroupAndGroupRole(objId, groupIdVo.getParams());
    }

    /**
     * 对象授权&赋权
     *
     * @param objId
     * @param srightFrightReqVo
     * @return
     */
    @AuditOperate(value = "srightFright", name = "对象授权&赋权", desc = "对象授权&赋权")
    @PostMapping(value = "srightFright/{objId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void srightFright(@PathVariable(value = "objId") Long objId,
        @RequestBody BasicRequestContentVo<List<SrightFrightReqVo>> srightFrightReqVo) throws ApplicationException {
        LOG.info(
            LogProperty.LOGTYPE_CALL,
            MessageFormat.format("srightFright,objId：{0},srightFrightReqVo：{1}", objId,
                JSON.toJSONString(srightFrightReqVo)));
        if (objId == null || srightFrightReqVo == null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        sysObjService.srightFright(objId, srightFrightReqVo.getParams());
    }

    /**
     * 对象操作权限校验
     *
     * @param objOperateVo
     * @return
     */
    @PostMapping(value = "auth", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysObjOperateVo> auth(@RequestBody BasicRequestContentVo<List<SysObjOperateVo>> objOperateVo)
        throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL,
            MessageFormat.format("auth,objOperateVo：{0}", JSON.toJSONString(objOperateVo)));
        if (objOperateVo == null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        return objPermissionHubService.auth(objOperateVo.getParams());
    }

    /**
     * @描述: 获取对象修改是已授权信息
     * @方法名: getGroupObjDetaillist
     * @param objId
     * @param groupId
     * @return
     * @throws ApplicationException
     * @创建人 zhang
     * @创建时间 2019-05-26
     */
    @GetMapping(value = "getGroupObjDetaillist/{objId}/{groupId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public  List<SysGroupObjBO>  getGroupObjDetaillist(@PathVariable(value = "objId") Long objId,@PathVariable(value = "groupId") Long groupId) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_CALL, "getGroupObjDetaillist,objId：" + objId );
        LOG.info(LogProperty.LOGTYPE_CALL, "getGroupObjDetaillist,groupId：" + groupId );
        if (objId == null||groupId==null) {
            throw new ApplicationException(new PaasError(CODE, DESCRIPTION));
        }
        return sysObjService.getGroupObjDetaillist(objId,groupId);
    }

}
