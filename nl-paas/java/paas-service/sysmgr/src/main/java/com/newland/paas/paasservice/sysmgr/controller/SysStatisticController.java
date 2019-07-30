package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.paasservice.sysmgr.service.SysStatisticService;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryListByTenantVo;
import com.newland.paas.paasservice.sysmgr.vo.SysCategoryStatisticVo;
import com.newland.paas.paasservice.sysmgr.vo.TenantStatisticVo;
import com.newland.paas.paasservice.sysmgr.vo.UserStatisticVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统统计接口
 *
 * @author zhongqingjiang
 */
@RestController
@RequestMapping("/sysmgr/v1/statistics")
public class SysStatisticController {

    @Autowired
    SysStatisticService sysStatisticService;

    @GetMapping(value = "/tenant", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TenantStatisticVo getTenantStatistic() {
        return sysStatisticService.getTenantStatistic();
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public UserStatisticVo getUserStatistic(@RequestParam(required = false) Integer top) {
        return sysStatisticService.getUserStatistic(top);
    }

    @GetMapping(value = "/sys-category-count", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SysCategoryStatisticVo getSysCategoryStatistic() {
        return sysStatisticService.getSysCategoryStatistic();
    }

    @GetMapping(value = "/sys-category-list", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysCategoryListByTenantVo> listSysCategory() throws ApplicationException {
        return sysStatisticService.listSysCategory();
    }

}
