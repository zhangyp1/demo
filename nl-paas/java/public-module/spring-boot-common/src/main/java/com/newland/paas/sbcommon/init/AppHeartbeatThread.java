package com.newland.paas.sbcommon.init;

import java.util.Date;

import com.newland.paas.common.util.DateUtils;
import com.newland.paas.common.util.Json;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.utils.redis.RedisUtil;

/**
 * 应用心跳线程
 * @author Administrator
 *
 */
public class AppHeartbeatThread implements Runnable {
	
	private static final Log log = LogFactory.getLogger(AppHeartbeatThread.class);
	private static final String tag = "AppHeartbeatThread===";
	
	private String key;
	
	public AppHeartbeatThread(String key) {
		this.key = key;
	}

	@Override
	public void run() {
//		byte[] keyB = key.getBytes(Charset.forName("utf-8"));
		Date date = new Date();
		while(true) {
			try {
				date.setTime(System.currentTimeMillis());
				AppInfo.getIns().setHeartbeatTime(DateUtils.formatDate(date,DateUtils.DATE_FULL_FORMAT));
				RedisUtil.hset(key,AppInfo.getIns().getAppId(), AppInfo.getIns());
				log.debug(LogProperty.LOGTYPE_SYS, tag + "key=" + key + 
						" | field=" + AppInfo.getIns().getAppId() + 
						" | value=" + Json.toJson(AppInfo.getIns()));
//				 redisTemplate.execute(new RedisCallback<Object>() {
//
//					@Override
//					public Object doInRedis(RedisConnection connection) throws DataAccessException {
//						AppInfo.getIns().setHeartbeatTime(Long.valueOf(DateUtils.formatDate(new Date(),DateUtils.DATE_FORMAT)));
//						String field = AppInfo.getIns().getAppId();
//						String value = Json.toJson(AppInfo.getIns());
//						log.debug(LogProperty.LOGTYPE_SYS, tag + "key=" + key + " | field=" + field + " | value=" + value);
//						connection.hSet(keyB, field.getBytes(), value.getBytes());
//						return null;
//					}
//					 
//				 });
			} catch (Throwable e) {
				log.error(LogProperty.LOGTYPE_SYS, null, e, tag + " write heartbeat error");
			}
			
			try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                log.error(LogProperty.LOGTYPE_SYS, null, e, tag);
            }
		}
	}

}
