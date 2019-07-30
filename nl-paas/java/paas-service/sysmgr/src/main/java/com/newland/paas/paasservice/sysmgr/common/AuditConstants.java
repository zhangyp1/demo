package com.newland.paas.paasservice.sysmgr.common;

import com.newland.paas.paasservice.sysmgr.vo.AuditInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:PanYang
 * Date:Created in 上午10:17 2018/8/2
 * Modified By:
 */
public class AuditConstants {
    //审计日志公共缓存
    public static List<AuditInfoVo> list = new ArrayList<>();
    public static final BlockingQueue<AuditInfoVo> queue = new LinkedBlockingQueue<AuditInfoVo>(1000000);
    public static boolean auditFlag = true;

}