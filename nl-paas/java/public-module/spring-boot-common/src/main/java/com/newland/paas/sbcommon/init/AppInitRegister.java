package com.newland.paas.sbcommon.init;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.newland.paas.common.exception.NLCheckedException;
import com.newland.paas.common.util.AddressUtil;
import com.newland.paas.common.util.DateUtils;
import com.newland.paas.common.util.Json;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.utils.redis.RedisUtil;

/**
 * 注册当前运行服务信息
 * 
 * @author Administrator
 *
 */
@Service
public class AppInitRegister implements ApplicationListener<ContextRefreshedEvent> {

    private static final Log log = LogFactory.getLogger(AppInitRegister.class);
    private static final String tag = "AppInitRegister===";

    private static final String APP_HEARTBEAT_THREAD_NAME = "APP_HEARTBEAT_THREAD";

    // private RedisTemplate<String, String> redisTemplate =
    // SpringContextUtil.getContext().getBean("redisTemplate", RedisTemplate.class);

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        log.debug(LogProperty.LOGTYPE_SYS, tag + "应用初始化中...appName=" + appName);
        String appId = appName + "|" + UUID.randomUUID().toString();
        AppInfo.getIns().setAppId(appId);
        AppInfo.getIns().setStartTime(DateUtils.formatDate(new Date(), DateUtils.DATE_FULL_FORMAT));
        // 设置IP
        String ip = AddressUtil.getLocalIP();
        String nodeIp = System.getenv("PF_NODE_IP");
        if (nodeIp != null) {
            ip = nodeIp + " | " + ip;
        }
        AppInfo.getIns().setAddress(ip);
        AppInfo.getIns().setLocalPath(System.getProperty("user.dir"));
        log.info(LogProperty.LOGTYPE_DETAIL, tag + System.getProperty("user.dir"));
        AppInfo.getIns().setAppName(appName);
        // 启动心跳线程
        // RedisTemplate<String, Object> redisTemplate = arg0.getApplicationContext().getBean("redisTemplate",
        // RedisTemplate.class);
        String key = ApplicationContext.APP_HASHKEY + ":" + appName;
        Thread t = new Thread(new AppHeartbeatThread(key), APP_HEARTBEAT_THREAD_NAME);
        t.start();

        Date endDate = new Date();

        try {
            Map m = RedisUtil.hgetAll(key);
            if (m != null) {
                Iterator<String> it = m.keySet().iterator();
                while (it.hasNext()) {
                    String k = it.next();
                    AppInfo appInfo = (AppInfo)m.get(k);
                    Date startDate =
                        DateUtils.parseDate(String.valueOf(appInfo.getHeartbeatTime()), DateUtils.DATE_FULL_FORMAT);
                    if (DateUtils.isOverTime(startDate, endDate, ApplicationContext.HEART_OUTTIME)) {
                        log.debug(LogProperty.LOGTYPE_SYS, tag + "移除超时数据：k=" + k + " | v=" + Json.toJson(appInfo));
                        RedisUtil.hdel(key, k);
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag);
        }

        // 移除超时数据
        // Date endDate = new Date();
        // redisTemplate.execute(new RedisCallback<Object>() {
        //
        // @Override
        // public Object doInRedis(RedisConnection connection) throws DataAccessException {
        // byte[] keyB = key.getBytes(Charset.forName("utf-8"));
        // Map<byte[], byte[]> m = connection.hGetAll(keyB);
        // Iterator<byte[]> it = m.keySet().iterator();
        // while (it.hasNext()) {
        // byte[] kb = it.next();
        // String k = new String(kb);
        // String v = new String(m.get(kb));
        // try {
        // AppInfo appInfo = Json.toObject(v, AppInfo.class);
        // Date startDate = DateUtils.parseDate(String.valueOf(appInfo.getHeartbeatTime()),
        // DateUtils.DATE_FORMAT);
        // if(DateUtils.isOverTime(startDate, endDate, 120)) {
        // log.debug(LogProperty.LOGTYPE_SYS, tag + "移除超时数据：k=" + k + " | v=" + v);
        // connection.hDel(keyB, kb);
        // }
        // }catch (Exception e) {
        // log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "startException：k=" + k + " | v=" + v);
        // }
        //
        // }
        //
        // return null;
        // }
        //
        // });

        log.debug(LogProperty.LOGTYPE_SYS, tag + "应用初始化结束");
    }

    /**
     * 获取与本进程APPNAME一样的进程列表
     * 
     * @return
     * @throws NLCheckedException
     */
    public static List<AppInfo> getAppInfoList() {
        String key = ApplicationContext.APP_HASHKEY + ":" + AppInfo.getIns().getAppName();
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        Map m = RedisUtil.hgetAll(key);
        if (m != null) {
            Iterator<String> it = m.keySet().iterator();
            while (it.hasNext()) {
                String k = it.next();
                AppInfo appInfo = (AppInfo)m.get(k);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }
}
