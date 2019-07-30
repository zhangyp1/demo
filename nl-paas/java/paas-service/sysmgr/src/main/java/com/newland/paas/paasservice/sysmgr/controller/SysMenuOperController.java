/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.OperBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuOperReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateAdd;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.validate.ValidateUpdate;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午2:55:20
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/menu")
@Validated
@AuditObject("菜单操作管理")
public class SysMenuOperController {
    @Autowired
    private SysMenuOperService sysMenuOperService;

    /**
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/list")
    public Map<String, Object> getMenuList() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        Long userId = RequestContext.getUserId();
        Long tenantId = RequestContext.getTenantId();
        List<MenuBO> menuList = sysMenuOperService.getMenusByTenantUserId(userId, tenantId);
        List<OperBO> operateList = sysMenuOperService.getOpersByTenantUserId(userId, tenantId);
        resultMap.put("menuList", menuList);
        resultMap.put("operateList", operateList);
        return resultMap;
    }

    @GetMapping(value = "/listMyMenuId")
    public List<Long> getMenuIdList() throws ApplicationException {
        List<Long> menuIdList = new ArrayList<>();
        Long userId = RequestContext.getUserId();
        Long tenantId = RequestContext.getTenantId();
        List<MenuBO> menuList = sysMenuOperService.getMenusByTenantUserId(userId, tenantId);
        List<OperBO> operateList = sysMenuOperService.getOpersByTenantUserId(userId, tenantId);
        for (MenuBO menuBO : menuList) {
            menuIdList.add(menuBO.getMenuId());
        }
        for (OperBO operBO : operateList) {
            menuIdList.add(operBO.getOperateId());
        }
        return menuIdList;
    }

    /**
     * 查询所有菜单
     * @param reqBo
     * @return
     * @throws ApplicationException
     */
    @GetMapping
    public List<SysMenuOper> list(SysMenuOperReqBo reqBo) {
        return sysMenuOperService.getMenus(reqBo);
    }

    /**
     * 新增菜单
     * 描述
     *
     * @param paramReqVo
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月19日 下午7:35:42
     */
    @PostMapping
    @AuditOperate(value = "add", name = "新增菜单")
    public int add(@Validated(value = {ValidateAdd.class})
                       @RequestBody BasicRequestContentVo<SysMenuOperReqBo> paramReqVo) throws ApplicationException {
        return sysMenuOperService.add(paramReqVo.getParams());

    }

    /**
     *
     * @param menuCode
     * @return
     * @throws ApplicationException
     */

    @AuditOperate(value ="isExistMenuCode", name = "判断操作代码是否存在")
    @PostMapping("/isExistMenuCode")
    public Boolean isExistMenuCode(@RequestBody BasicRequestContentVo<SysMenuOperReqBo> paramReqVo)  throws ApplicationException{
      int count = sysMenuOperService.countByMenuCode(paramReqVo.getParams());
      return count > 0;
    }



    /**
     * 修改菜单
     * @param reqInfo
     * @return
     * @throws ApplicationException
     */
    @PutMapping
    @AuditOperate(value = "update", name = "修改菜单")
    public int update(@Validated(value = {ValidateUpdate.class})
                          @RequestBody BasicRequestContentVo<SysMenuOperReqBo> reqInfo) throws ApplicationException {
        return sysMenuOperService.update(reqInfo.getParams());
    }

    /**
     * 删除菜单
     * 描述
     *
     * @param id
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年7月19日 下午7:35:57
     */
    @DeleteMapping(value = "/{id}")
    @AuditOperate(value = "delete", name = "删除菜单")
    public int delete(@NotNull(message = "id不能为空") @PathVariable("id") Long id) throws ApplicationException {
        return sysMenuOperService.delete(id);
    }

    /**
     *
     * @param id
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/{id}")
    public SysMenuOper getSysTenant(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return sysMenuOperService.get(id);
    }

    /**
     *
     * @param reqBo
     * @param pageInfo
     * @return
     * @throws ApplicationException
     */
    @GetMapping("/pageQuery")
    public ResultPageData<SysMenuOper> pageQuery(SysMenuOperReqBo reqBo, PageInfo pageInfo) {
        return sysMenuOperService.pageQuery(reqBo, pageInfo);
    }

    /**
     *
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/allList")
    public Map<String, Object> getAllMenuList() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        List<MenuBO> menuList = sysMenuOperService.getMenusList();
        resultMap.put("menuList", menuList);

        return resultMap;
    }
}

