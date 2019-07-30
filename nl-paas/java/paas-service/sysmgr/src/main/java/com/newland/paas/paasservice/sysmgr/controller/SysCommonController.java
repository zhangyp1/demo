package com.newland.paas.paasservice.sysmgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserTenantInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserWorkInfoBO;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysTenantError;
import com.newland.paas.paasservice.sysmgr.service.SysUserService;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 * 
 * 描述
 * 
 * @author linkun
 * @created 2018年6月25日 下午4:16:41
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/common/v1")
@Validated
public class SysCommonController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 
     * 描述 用户对应的租户列表
     * 
     * @author linkun
     * @created 2018年6月27日 上午9:57:59
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/identityList")
    public Map<String, Object> identities() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();

        Long userId = RequestContext.getUserId();
        List<UserTenantInfoBO> tenants = sysUserService.getTenantInfosByUserId(userId);
        List<UserWorkInfoBO> worklist = sysUserService.getWorkInfosByUserId(userId);

        resultMap.put("tenants", tenants);
        resultMap.put("worklist", worklist);
        return resultMap;
    }

    /**
     * 
     * 描 述 身份切换
     * 
     * @author linkun
     * @created 2018年6月26日 下午2:37:21
     * @param tenantId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/identity/{tenantId}")
    public Map<String, Object> identity(@PathVariable("tenantId") Long tenantId) throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        SysTenant tenant = sysUserService.getTenantById(tenantId);
        if (tenant == null) {
            throw new ApplicationException(SysTenantError.NOT_EXIST_SYS_TENANT_ERROR);
        }
        if (tenant.getDelFlag() == SysMgrConstants.IS_DELETE_TRUE) {
            throw new ApplicationException(SysTenantError.IS_DELETE_SYS_TENANT_ERROR);
        }
        return resultMap;
    }

    /**
     * 
     * 描述 重新获取登录信息
     * 
     * @author linkun
     * @created 2018年6月25日 下午4:16:52
     * @return
     */
    @GetMapping(value = "/reLogin")
    public Map<String, Object> reLogin() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("username", RequestContext.getSession().getUserName());
        resultMap.put("groups", sysUserService.getGroupsByUserId(RequestContext.getUserId()));
        return resultMap;
    }

    /**
     * 用户是否属于工组 描述
     * 
     * @author linkun
     * @created 2018年7月4日 下午1:52:54
     * @param groupId
     * @return
     * @throws ApplicationException
     */
    @GetMapping(value = "/isUserInGroup")
    public boolean isUserInGroup(@NotNull(message = "工组id不能为空") Long groupId) {
        return sysUserService.isUserInGroup(RequestContext.getUserId(), groupId);
    }

}
