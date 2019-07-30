package com.newland.paas.sbcommon.codeid;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.CodeIdVO;
import com.newland.paas.sbcommon.utils.redis.RedisUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * code对应di工具类
 *
 * @author WRP
 * @since 2019/1/10
 */
public final class CodeIdUtil {

    private static final Log LOG = LogFactory.getLogger(CodeIdUtil.class);

    public static final String APP_CONFIG = "APP_CONFIG";   //应用配置
    public static final String APP_ENDPOINT = "APP_ENDPOINT";   //应用接入点
    public static final String APP_INFO = "APP_INFO";   //应用信息主表
    public static final String APP_LOAD_BALANCE = "APP_LOAD_BALANCE";   //应用负载
    public static final String APP_RELY = "APP_RELY";   //应用依赖
    public static final String APP_UNIT = "APP_UNIT";   //应用单元
    public static final String APP_UNIT_ALARM_C = "APP_UNIT_ALARM_C";   //应用单元告警配置
    public static final String APP_UNIT_LOG_C = "APP_UNIT_LOG_C";   //应用单元日志配置
    public static final String APP_UNIT_MODULE = "APP_UNIT_MODULE"; //应用单元模块
    public static final String AST_INFO = "AST_INFO";   //资产信息
    public static final String AST_UNIT = "AST_UNIT";   //资产单元
    public static final String CLU_INFO = "CLU_INFO";   //集群信息
    public static final String CLU_LOAD_BALANCE = "CLU_LOAD_BALANCE";   //集群LB
    public static final String CLU_ZONE = "CLU_ZONE";   //集群分区
    public static final String FRW_INFO = "FRW_INFO";   //框架信息
    public static final String RES_LOG_CLUSTER = "RES_LOG_CLUSTER"; //日志集群
    public static final String SVR_SUBS_INST = "SVR_SUBS_INST"; //服务订阅
    public static final String SYS_CATEGORY = "SYS_CATEGORY";   //系统组
    public static final String SYS_GROUP = "SYS_GROUP"; //工组
    public static final String SYS_TENANT = "SYS_TENANT";   //租户
    public static final String RES_RESOURCE_IP = "RES_RESOURCE_IP";   //资源IP

    private CodeIdUtil() {
    }

    /**
     * key
     */
    private static final String REDIS_PAAS_CODE_ID = "PAAS:CODE_ID:";

    /**
     * 将CodeId信息保存至redis
     *
     * @param codeIdMap key:tableName,value:map->{key:code,value:CodeIdVO}
     */
    public static void saveToRedis(Map<String, Map<String, Object>> codeIdMap) {
        if (!CollectionUtils.isEmpty(codeIdMap)) {
            for (Map.Entry<String, Map<String, Object>> v : codeIdMap.entrySet()) {
                RedisUtil.hset(REDIS_PAAS_CODE_ID + v.getKey().toLowerCase(), v.getValue());
            }
        }
    }

    /**
     * 将单个CodeId信息保存至redis
     *
     * @param codeIdVO CodeId信息
     */
    public static void saveSingleToRedis(CodeIdVO codeIdVO) {
        String tableName = codeIdVO.getTableName().toLowerCase();
        Map<String, Object> codeIdMap = RedisUtil.hgetAll(REDIS_PAAS_CODE_ID + tableName);
        if (CollectionUtils.isEmpty(codeIdMap)) {
            codeIdMap = new HashMap<>(1);
        }
        codeIdMap.put(codeIdVO.getCode(), new CodeIdVO(codeIdVO.getId(), codeIdVO.getName()));
        RedisUtil.hset(REDIS_PAAS_CODE_ID + tableName, codeIdMap);
    }

    /**
     * 根据tableName和code获取CodeId
     *
     * @param tableName 表名
     * @param code      code
     * @return
     */
    public static String getByCode(String tableName, String code) {
        CodeIdVO codeId = getCodeIdByCode(tableName, code);
        if (codeId != null) {
            return codeId.getId();
        }
        return null;
    }

    /**
     * 根据tableName和code获取CodeId信息
     *
     * @param tableName 表名
     * @param code      code
     * @return
     */
    public static CodeIdVO getCodeIdByCode(String tableName, String code) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("tableName:{0},code:{1}", tableName, code));
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(code)) {
            return null;
        }
        tableName = tableName.toLowerCase();
        Object result = RedisUtil.hget(REDIS_PAAS_CODE_ID + tableName, code);
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("result:{0}", JSON.toJSONString(result)));
        return result == null ? null : (CodeIdVO) result;
    }

}
