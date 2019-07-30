package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupForCategoryRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;
import com.newland.paas.paasservice.sysmgr.service.SysGroupService;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupForMemberRespVO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupWithRoleVo;
import com.newland.paas.paasservice.sysmgr.vo.TreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/groupMgr")
@Validated
@AuditObject("工组管理")
public class SysGroupController {
    private static final Log LOG = LogFactory.getLogger(SysGroupController.class);

    @Autowired
    private SysGroupService sysGroupService;

    /**
     * 分页查询工组
     *
     * @param params
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    @GetMapping
    public ResultPageData<SysGroup> pagedQuery(SysGroupReqBo params, PageInfo pageInfo)
            throws ApplicationException {
        return sysGroupService.page(params, pageInfo);
    }

    /**
     * 查询我可见的工组列表
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/list")
    public List<SysGroupRespBo> listMyGroup() throws ApplicationException {
        List<SysGroupRespBo> list = sysGroupService.listMyGroup();
        return list;
    }

    @GetMapping("/list-my-group-with-role")
    public List<SysGroupWithRoleVo> listMyGroupWithRole() throws ApplicationException {
        List<SysGroupWithRoleVo> list = sysGroupService.listMyGroupWithRole();
        return list;
    }

    /**
     * 查询我管理的工组列表
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/listManagedGroup")
    public List<SysGroupRespBo> listManagedGroup() throws ApplicationException {
        List<SysGroupRespBo> list = sysGroupService.listManagedGroup();
        return list;
    }

    /**
     * 新增工组
     *
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PostMapping
    @AuditOperate(value = "add", name = "新增工组")
    public void add(@Validated(value = {ValidateAdd.class}) @RequestBody BasicRequestContentVo<SysGroup> reqInfo)
            throws ApplicationException {
        reqInfo.getParams().setTenantId(RequestContext.getTenantId());
        sysGroupService.add(reqInfo.getParams());
    }

    /**
     * 更新工组
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PutMapping
    @AuditOperate(value = "update", name = "更新工组")
    public void update(@Validated(value = {ValidateUpdate.class}) @RequestBody BasicRequestContentVo<SysGroup> reqInfo)
            throws ApplicationException {
        sysGroupService.update(reqInfo.getParams());
    }

    /**
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/{id}")
    public SysGroup get(@PathVariable("id") Long id) throws ApplicationException {
        return sysGroupService.get(id);
    }

    /**
     * 删除工组
     * @param id
     * @return
     * @throws ApplicationException
     */
    @DeleteMapping("/{id}")
    @AuditOperate(value = "delete", name = "删除工组")
    public void delete(@PathVariable("id") Long id) throws ApplicationException {
        sysGroupService.delete(id);
    }

    /**
     *
     * @param groupId
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/getCategorysByGroup/{groupId}")
    public List<SysGroupForCategoryRespBo> getCategorysByGroup(@PathVariable("groupId") Long groupId) {
        return sysGroupService.getCategorysByGroup(groupId);
    }

    /**
     * 成员所属工组列表查询
     *
     * @param userId 成员Id
     * @return List
     * @throws ApplicationException
     */
    @GetMapping(value = "/list/{userId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysGroupForMemberRespVO> queryList(@NotNull(message = "成员ID不能为空!")
                                                       @PathVariable("userId") Long userId)throws ApplicationException {
        return sysGroupService.queryListByUserId(userId);
    }

    /**
     * 根据工组ID获取工组树
     *
     * @param groupId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/sys-group-tree/{groupId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TreeNode<BaseTreeDo> getSysGroupTree(@PathVariable("groupId") Long groupId)
            throws ApplicationException {
        SysGroup sysGroup = new SysGroup();
        sysGroup.setGroupId(groupId);
        sysGroup.setTenantId(RequestContext.getTenantId());
        List<BaseTreeDo> treeData = sysGroupService.getSysGroupTree(sysGroup);
        TreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new TreeNode.TreeBuilder<>();

        for (BaseTreeDo data : treeData) {
            treeBuilder.addNode(data.getParentId(), data.getId(), data);
        }
        return treeBuilder.build();
    }

    /**
     * 根据工组ID获取子工组树列表
     *
     * @param groupId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/tree-list/{groupId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<BaseTreeDo> getTreeList(@PathVariable("groupId") Long groupId) throws ApplicationException {
        SysGroup sysGroup = new SysGroup();
        sysGroup.setGroupId(groupId);
        sysGroup.setTenantId(RequestContext.getTenantId());
        return sysGroupService.getSysGroupTree(sysGroup);
    }

    /**
     * 根据工组ID获取子工组列表
     *
     * @param groupId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/tree-sub-list/{groupId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<BaseTreeDo> listSubSysGroup(@PathVariable("groupId") Long groupId) throws ApplicationException {
        SysGroup sysGroup = new SysGroup();
        sysGroup.setGroupId(groupId);
        sysGroup.setTenantId(RequestContext.getTenantId());
        return sysGroupService.listSubSysGroup(sysGroup);
    }

   /**
     * 获取运营租户的根工组 create by wrp
     *
     * @return
     */
    @GetMapping(value = "/yyTenantRootGroup", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo yyTenantRootGroup() {
        LOG.info(LogProperty.LOGTYPE_CALL, "yyTenantRootGroup");
        return new BasicResponseContentVo(sysGroupService.yyTenantRootGroup());
    }

    /**
     * 获取当前用户的租户的根工组 create by wrp
     *
     * @return
     */
    @GetMapping(value = "/tenantRootGroup", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo tenantRootGroup() {
        LOG.info(LogProperty.LOGTYPE_CALL, "tenantRootGroup");
        return new BasicResponseContentVo(sysGroupService.tenantRootGroup());
    }

    /**
     * 获取当前租户用户的归属工组的父工组 create by wrp
     *
     * @return
     */
    @GetMapping(value = "/tenantParentGroup", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo tenantParentGroup() {
        LOG.info(LogProperty.LOGTYPE_CALL, "tenantParentGroup");
        return new BasicResponseContentVo(sysGroupService.tenantParentGroup());
    }

    /**
     * 获取运维租户的根工组 create by wrp
     *
     * @return
     */
    @GetMapping(value = "/ywTenantRootGroup", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo ywTenantRootGroup() {
        LOG.info(LogProperty.LOGTYPE_CALL, "ywTenantRootGroup");
        return new BasicResponseContentVo(sysGroupService.ywTenantRootGroup());
    }

}

