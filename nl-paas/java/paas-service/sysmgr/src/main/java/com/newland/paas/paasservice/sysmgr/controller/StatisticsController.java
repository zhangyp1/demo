/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.paasservice.sysmgr.service.SysGroupService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantMemberService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantService;
import com.newland.paas.paasservice.sysmgr.service.SysUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午4:22:30
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1")
@Validated
public class StatisticsController {
    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysTenantMemberService sysTenantMemberService;
    @Autowired
    private SysUserService sysUserService;

    private static final int TWO = 2;

    private static final int NEGATIVEF_ONE = -1;

    private static final String TIME_PATTERN = "yyyy-MM-dd";
    /**
     * 描述   查询租户总数
     *
     * @return
     * @author linkun
     * @created 2018年6月26日 下午4:24:49
     */
    @GetMapping(value = "/tenantCount")
    public Map<String, Object> tenantCount() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("tenantCount", sysTenantService.tenantCount());
        return resultMap;
    }

    /**
     * 描述   查询工组总数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午4:43:59
     */
    @GetMapping(value = "/groupCount")
    public Map<String, Object> groupCount() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("groupCount", sysGroupService.groupCount());
        return resultMap;
    }

    /**
     * 描述  查询租户下的成员总数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午5:06:41
     */
    @GetMapping(value = "/memberCount")
    public Map<String, Object> memberCount() throws ApplicationException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberCount", sysTenantMemberService.memberCount(null));
        return resultMap;
    }

    /**
     * 描述  用户总数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午6:17:13
     */
    @GetMapping(value = "/userCount")
    public Map<String, Object> userCount() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userCount", sysUserService.userCount());

        return resultMap;
    }

    /**
     * 描述 在线用户统计数据
     *
     */
    @GetMapping(value = "/onlineUserCount")
    public Map<String, Object> onlineUserCount() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("onlineUserCount", sysUserService.onlineUserCount());

        return resultMap;
    }

    /**
     * 描述   今日新增用户数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午7:48:00
     */
    @GetMapping(value = "/userTodayCount")
    public Map<String, Object> userTodayCount() {
        Map<String, Object> resultMap = new HashMap<>();
        String beginDate = DateFormatUtils.format(new Date(), TIME_PATTERN);
        String endDate = DateFormatUtils.format(DateUtils.addDays(new Date(), 1), TIME_PATTERN);
        resultMap.put("userTodayCount", sysUserService.userCount(beginDate, endDate));

        return resultMap;
    }

    /**
     * 描述    本周新增用户数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午7:48:25
     */
    @GetMapping(value = "/userWeekCount")
    public Map<String, Object> userWeekCount() {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("userWeekCount", sysUserService.userCount(getThisMonday(), getNextMonday()));

        return resultMap;
    }

    /**
     * 描述    本月新增用户数
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午7:48:40
     */
    @GetMapping(value = "/userMonthCount")
    public Map<String, Object> userMonthCount() {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("userMonthCount", sysUserService.userCount(getThisMonthFirst(), getNextMonthFirst()));

        return resultMap;
    }

    /**
     * 描述               查询近24小时的用户登录统计数据
     *
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月26日 下午7:51:45
     */
    @GetMapping(value = "/userLoginInfo")
    public Object userLoginInfo() {
        return sysUserService.userLoginInfo();
    }


    /**
     * @return
     */
    private static String getThisMonday() {
        Calendar c = Calendar.getInstance();
        int weekDay = c.get(Calendar.DAY_OF_WEEK) - TWO;
        c.add(Calendar.DAY_OF_WEEK, NEGATIVEF_ONE * weekDay);
        return DateFormatUtils.format(c, TIME_PATTERN);
    }

    /**
     * @return
     */
    private static String getNextMonday() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int weekDay = c.get(Calendar.DAY_OF_WEEK) - TWO;
        c.add(Calendar.DAY_OF_WEEK, (Calendar.DAY_OF_WEEK - weekDay));
        return DateFormatUtils.format(c, TIME_PATTERN);
    }

    /**
     * @return
     */
    private static String getThisMonthFirst() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return DateFormatUtils.format(c, TIME_PATTERN);
    }

    /**
     * @return
     */
    private static String getNextMonthFirst() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return DateFormatUtils.format(c, TIME_PATTERN);
    }
}

