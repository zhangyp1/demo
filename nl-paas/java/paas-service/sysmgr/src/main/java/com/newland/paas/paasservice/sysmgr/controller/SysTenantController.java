/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantLimit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.controllerapi.harbormgr.vo.RepoReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.*;
import com.newland.paas.paasservice.sysmgr.async.DepotHelperAsync;
import com.newland.paas.paasservice.sysmgr.service.ActivitiOperationsService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantMemberService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantService;
import com.newland.paas.paasservice.sysmgr.vo.ApproveBo;
import com.newland.paas.paasservice.sysmgr.vo.SysTenantWithRoleVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicPageRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 描述 租户信息
 *
 * @author linkun
 * @created 2018年6月27日 下午2:13:54
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/tenantMgr")
@Validated
@AuditObject("租户管理")
public class SysTenantController {
    private static final Log LOGGER = LogFactory.getLogger(SysTenantController.class);

    @Autowired
    private SysTenantService sysTenantService;

    @Autowired
    private SysTenantMemberService sysTenantMemberService;

    @Autowired
    private ActivitiOperationsService activitiOperationsService;

    @Autowired
    private DepotHelperAsync depotHelperAsync;
    /**
     * 新增租户 描述
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping
    @AuditOperate(value = "addSysTenant", name = "新增租户")
    public SysTenantRespVo addSysTenant(@Validated @RequestBody BasicRequestContentVo<SysTenantReqBo> reqInfo)
            throws ApplicationException {
        SysTenant sysTenantInfo = sysTenantService.addSysTenantAndMembers(reqInfo.getParams());
        SysTenantRespVo vo = new SysTenantRespVo();
        vo.setTenantId(sysTenantInfo.getId());
        vo.setTenantName(sysTenantInfo.getTenantName());
        vo.setDescription(sysTenantInfo.getTenantDesc());
        return vo;
    }

    /**
     * 修改租户 描述
     *
     * @param sysTenantVo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午3:52:27
     */
    @PutMapping
    @AuditOperate(value = "updateSysTenant", name = "修改租户")
    public SysTenantRespVo updateSysTenant(@Validated @RequestBody BasicRequestContentVo<SysTenantReqBo> sysTenantVo)
            throws ApplicationException {
        SysTenant sysTenantInfo = sysTenantService.updateSysTenantAndMembers(sysTenantVo.getParams());
        return sysTenantService.getSysTenantAndAdmins(sysTenantInfo.getId());
    }

    /**
     * 删除租户
     * @param id
     * @throws ApplicationException
     */
    @DeleteMapping(value = "/{id}")
    @AuditOperate(value = "deleteSysTenant", name = "删除租户")
    public void deleteSysTenant(@PathVariable("id") Long id) throws ApplicationException {
        sysTenantService.deleteSysTenant(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/{id}")
    public SysTenantRespVo getSysTenant(@PathVariable("id") Long id) throws ApplicationException {
        return sysTenantService.getSysTenantAndAdmins(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/tenant/{id}")
    public SysTenant getTenantInfo(@PathVariable("id") Long id) {
        return sysTenantService.selectByPrimaryKey(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/page/{id}")
    public SysTenantRespVo getPageSysTenant(@PathVariable("id") Long id) throws ApplicationException {
        return sysTenantService.getPageSysTenantAndAdmins(id);
    }

    /**
     * 获取所有租户列表 - 分页
     *
     * @param vo
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    @GetMapping
    public ResultPageData<SysTenantBo> pagedQuery(SysTenantReqVo vo, PageInfo pageInfo) {
        return sysTenantService.getSysTenantsByPage(vo, pageInfo);
    }

    /**
     * 获取所有租户列表
     *
     * @param vo
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/list")
    public List<SysTenantBo> list(SysTenantReqVo vo) {
        return sysTenantService.listSysTenantBo(vo);
    }

    /**
     * 获取当前租户信息
     *
     * @return
     */
    @GetMapping(value = "/tenant-info", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysTenantBo getSysTenantBo() {
        Long tenantId = RequestContext.getTenantId();
        return sysTenantService.getSysTenantBo(tenantId);
    }

    /**
     * 获取当前租户信息和平台角色
     *
     * @return
     */
    @GetMapping(value = "/tenant-info-with-role", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysTenantWithRoleVo getSysTenantWithRoleVo() {
        Long tenantId = RequestContext.getTenantId();
        Long userId = RequestContext.getUserId();
        return sysTenantService.getSysTenantWithRoleVo(tenantId, userId);
    }

    /**
     * 根据ids获取租户列表 描述
     *
     * @param ids
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月5日 上午10:38:17
     */
    @GetMapping("/getSysTenantsByIds")
    public List<SysTenant> getSysTenantsByIds(@NotNull(message = "ids不允许为空") String ids) {
        String[] ids1 = ids.split(",");
        return sysTenantService.getSysTenantsByIds(ids1);
    }

    /**
     * 根据租户ID列表获取租户详情列表 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月9日 下午5:18:17
     */
    @PostMapping("/tenantDetails")
    public List<SysTenantRespVo> tenantDetails(@RequestBody BasicRequestContentVo<SysTenantReqVo> reqInfo)
            throws ApplicationException {
        List<SysTenantRespVo> list = new ArrayList<>();
        Long[] ids = reqInfo.getParams().getTenants();
        for (Long id : ids) {
            list.add(sysTenantService.getSysTenantAndAdmins(id));
        }

        return list;
    }

    /**
     *
     * @param sysTenantReqVo
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/createDetail")
    public CreateTenantDetail createDetail(SysTenantReqVo sysTenantReqVo) throws ApplicationException {
        return sysTenantService.getCreateTenantDetail(sysTenantReqVo.getTenantId());
    }

    /**
     * 是否租户管理员 描述
     *
     * @return
     * @author linkun
     * @created 2018年8月13日 下午1:56:26
     */
    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        boolean isAdmin = false;

        Long tenantId = RequestContext.getTenantId();
        Long userId = RequestContext.getUserId();
        SysTenantMember tenantUserParam = new SysTenantMember();
        tenantUserParam.setTenantId(tenantId);
        tenantUserParam.setUserId(userId);
        List<SysTenantMember> list = sysTenantMemberService.selectBySelective(tenantUserParam);
        if (list != null && !list.isEmpty()) {
            SysTenantMember info = list.get(0);
            if (info.getIsAdmin().shortValue() == 1) {
                isAdmin = true;
            }
        }

        return isAdmin;
    }

    /**
     *
     * @param reqInfo
     * @throws ApplicationException
     */
    @PostMapping("/tenant-apply")
    public void applySysTenant(@Validated @RequestBody BasicRequestContentVo<SysTenantApplyVo> reqInfo)
            throws ApplicationException {
        sysTenantService.tenantApply(reqInfo);

    }

    /**
     *
     * @param proinstanceId
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/tenant-apply-detail/{proinstanceId}")
    public SysTenantApplyBo applySysTenantDetail(@PathVariable("proinstanceId") Long proinstanceId)
            throws ApplicationException {
        return sysTenantService.applySysTenantDetail(proinstanceId);
    }

    /**
     *
     * @param reqInfo
     * @throws ApplicationException
     */
    @PostMapping("/tenant-apply-approve")
    public void approveSysTenant(@RequestBody BasicRequestContentVo<ApproveBo> reqInfo) {
        ApproveBo approveBo = reqInfo.getParams();
        sysTenantService.approveSysTenant(approveBo);
    }

    /**
     *
     * @param reqInfo
     * @throws ApplicationException
     */
    @PostMapping("/tenant-new")
    public void newSysTenant(@RequestBody BasicRequestContentVo<SysTenantApplyVo> reqInfo) throws ApplicationException {
        SysTenantApplyVo sysTenantApplyVo = reqInfo.getParams();
        sysTenantService.approveNewSysTenant(sysTenantApplyVo);
    }

    /**
     * 加入租户 描述
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 上午10:07:39
     */
    @PostMapping("/joinTenant/submitProcess")
    public void joinTenantSubmitProcess(
            @RequestBody BasicRequestContentVo<SysTenantFlowReqBo> reqInfo) throws ApplicationException {
        SysTenantFlowReqBo reqBo = reqInfo.getParams();
        reqBo.setUserId(RequestContext.getUserId());
        reqBo.setUserName(RequestContext.getSession().getUserName());
        reqBo.setCreatorId(RequestContext.getUserId());

        sysTenantService.joinTenantSubmitProcess(reqBo);
    }

    /**获取加入租户的参数 描述
     *
     */
    @GetMapping("/joinTenant/getTaskVar/{proinstanceId}")
    public SysTenantFlowReqBo joinTenantGetTaskVar(@PathVariable("proinstanceId") Long proinstanceId) {
        return sysTenantService.joinTenantGetTaskVar(proinstanceId);
    }

    /**
     * 加入租户审批 描述
     *
     * @param reqInfo
     * @return
     * @author linkun
     * @created 2018年8月17日 下午1:23:35
     */
    @PostMapping("/joinTenant/nextProcess")
    public boolean joinTenantNextProcess(@RequestBody BasicRequestContentVo<SysTenantFlowReqBo> reqInfo) {
        return sysTenantService.joinTenantNextProcess(reqInfo.getParams());
    }

    /**
     * 加入租户审批通过 描述
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 下午1:23:44
     */
    @PostMapping("/joinTenant/approve")
    public boolean joninTenantApprove(@RequestBody BasicRequestContentVo<SysTenantFlowReqBo> reqInfo)
            throws ApplicationException {
        return sysTenantService.joninTenantApprove(reqInfo.getParams());
    }

    /**
     * 获取所有可以加入的租户 描述
     *
     * @return
     * @author linkun
     * @created 2018年8月20日 下午2:08:29
     */
    @GetMapping("/joinTenant/getAllTenantsCanJoin")
    public List<SysTenant> getAllTenantsCanJoin() {
        Long userId = RequestContext.getUserId();
        return sysTenantService.getAllTenantsCanJoin(userId);
    }

    /**
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/isExistTenantName")
    public boolean isExistTenantName(@RequestBody BasicRequestContentVo<SysTenant> reqInfo)
            throws ApplicationException {
        int count =
                sysTenantService.countByTenantName(reqInfo.getParams().getTenantName(),
                        reqInfo.getParams().getId());
        return count > 0;
    }

    /**
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/isExistAstAddr")
    public boolean isExistAstAddr(@RequestBody BasicRequestContentVo<SysTenant> reqInfo)
            throws ApplicationException {
        SysTenant sysTenant = reqInfo.getParams();
        Long id = sysTenant.getId();
        if (id == null) {
            int count = sysTenantService.countTenantByDepot(sysTenant);
            return count > 0;
        } else {
            SysTenant tenantReq = new SysTenant();
            tenantReq.setAstAddress(sysTenant.getAstAddress());
            List<SysTenant> sysTenants = sysTenantService.selectBySelective(tenantReq);
            return sysTenants != null && !sysTenants.isEmpty()
                    && !Objects.equals(sysTenants.get(0).getId(), id);
        }
    }

    /**
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/isExistImgAddr")
    public boolean isExistImgAddr(@RequestBody BasicRequestContentVo<SysTenant> reqInfo)
            throws ApplicationException {
        SysTenant sysTenant = reqInfo.getParams();
        Long id = sysTenant.getId();
        if (id == null) {
            int count = sysTenantService.countTenantByDepot(sysTenant);
            return count > 0;
        } else {
            SysTenant tenantReq = new SysTenant();
            tenantReq.setImageProject(sysTenant.getImageProject());
            List<SysTenant> sysTenants = sysTenantService.selectBySelective(tenantReq);
            return sysTenants != null && !sysTenants.isEmpty()
                    && !Objects.equals(sysTenants.get(0).getId(), id);
        }
    }

    /**
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/isExistImgUsr")
    public boolean isExistImgUsr(@RequestBody BasicRequestContentVo<SysTenant> reqInfo)
            throws ApplicationException {
        SysTenant sysTenant = reqInfo.getParams();
        Long id = sysTenant.getId();
        if (id == null) {
            int count = sysTenantService.countTenantByDepot(sysTenant);
            return count > 0;
        } else {
            SysTenant tenantReq = new SysTenant();
            tenantReq.setImageUsername(sysTenant.getImageUsername());
            List<SysTenant> sysTenants = sysTenantService.selectBySelective(tenantReq);
            return sysTenants != null && !sysTenants.isEmpty()
                    && !Objects.equals(sysTenants.get(0).getId(), id);
        }
    }

    /**
     *
     * @param tenantId
     * @return
     */
    @GetMapping("/countLimitsByTenantId/{tenantId}")
    public SysTenantLimitStaVo getLimitsByTenantId(@PathVariable("tenantId") Long tenantId) {
        return sysTenantService.getLimitsByTenantId(tenantId);
    }

    /**
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/listSysTenantLimits",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysTenantLimitBo> listSysTenantLimits(
            @RequestBody BasicPageRequestContentVo<SysTenantLimit> params) {
        SysTenantLimit sysTenantLimit = params.getParams();
        Long tenantId = sysTenantLimit.getTenantId();

        return sysTenantService.listSysTenantLimits(tenantId);
    }

    /**
     *
     * @param requestContentVo
     * @throws Exception
     */
    @PostMapping(value = "/refreshSysTenantDepot",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void refreshSysTenantDepot(@RequestBody BasicRequestContentVo<Map> requestContentVo) {
        Map<String, Object> depotMap = requestContentVo.getParams();
        depotHelperAsync.refreshSysTenantDepot(depotMap, RequestContext.getToken());
    }

    /**
     * 校验租户code是否重复
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping("/isExistTenantCode")
    public boolean isExistAppCode(@RequestBody BasicRequestContentVo<SysTenant> reqInfo) {

        SysTenant sysTenant = reqInfo.getParams();
        Long id = sysTenant.getId();
        if (id == null) {
            List<SysTenant> sysTenantList = sysTenantService.listTenantByTenantCode(sysTenant.getTenantCode());
            return sysTenantList != null && !sysTenantList.isEmpty();
        } else {
            SysTenant tenantReq = new SysTenant();
            tenantReq.setTenantCode(sysTenant.getTenantCode());
            List<SysTenant> sysTenants = sysTenantService.selectBySelective(tenantReq);
            return sysTenants != null && !sysTenants.isEmpty()
                    && !Objects.equals(sysTenants.get(0).getId(), id);
        }
    }

    /**
     * 提供方申请流程提交
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月17日 上午10:07:39
     */
    @PostMapping("/providerTenant/submitProcess")
    public void providerTenantSubmitProcess(
            @RequestBody BasicRequestContentVo<SysTenantProviderApplyVo> reqInfo)
            throws ApplicationException {
        SysTenantProviderApplyVo reqBo = reqInfo.getParams();
        reqBo.setCreatorId(RequestContext.getUserId());

        sysTenantService.providerTenantSubmitProcess(reqBo);
    }

    /**
     * 提供方申请流程详情
     * @param proinstanceId
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/tenant-provider-detail/{proinstanceId}")
    public SysTenantProviderApplyVo getSysTenantProviderDetail(
            @PathVariable("proinstanceId") Long proinstanceId) {
        return sysTenantService.getSysTenantProviderDetail(proinstanceId);
    }

    /**
     * 提供方申请流程同意
     * @param reqInfo
     * @throws ApplicationException
     */
    @PostMapping("/tenant-provider-apply-approve")
    public void approveSysTenantProvider(@RequestBody BasicRequestContentVo<ApproveBo> reqInfo) {
        ApproveBo approveBo = reqInfo.getParams();
        sysTenantService.approveSysTenantProvider(approveBo);
    }

    /**
     * 提供方租户状态变更
     * @param reqInfo
     * @throws ApplicationException
     */
    @PostMapping("/tenant-provider-status")
    public void sysTenantStatus(@RequestBody BasicRequestContentVo<SysTenantProviderApplyVo> reqInfo)
            throws ApplicationException {
        SysTenantProviderApplyVo applyVo = reqInfo.getParams();
        sysTenantService.approveStatusSysTenantProvider(applyVo);
    }

    @AuditOperate(value = "queryTenantImageRepo", name = "查询租户镜像列表")
    @PostMapping(value = "/queryTenantImageRepo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultPageData queryTenantImageRepo(@RequestBody BasicPageRequestContentVo<RepoReqVo> reqInfo)
            throws ApplicationException {
        return sysTenantService.queryTenantImageRepo(reqInfo.getParams(), reqInfo.getPageInfo());
    }

}
