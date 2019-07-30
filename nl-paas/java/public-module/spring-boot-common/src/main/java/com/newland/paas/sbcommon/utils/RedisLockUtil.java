package com.newland.paas.sbcommon.utils;

import com.newland.paas.common.util.AddressUtil;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author laiCheng
 * @date 2018/8/11 15:04
 */
public class RedisLockUtil {
	
	private static final Log log = LogFactory.getLogger(RedisLockUtil.class);
	private static final String tag = "RedisLockUtil===";

	private String key;
	private byte[] keyByteData;

	private int timeout;

	private RedisTemplate<String, String> redisTemplate = SpringContextUtil.getContext().getBean("redisTemplate", RedisTemplate.class);

	public boolean executeWhenGetLock(Runnable runnable) {
		String getToken = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				String ext = UUID.randomUUID().toString() + "|" + AddressUtil.getLocalIP() + "|" + String.valueOf(System.currentTimeMillis() + timeout);
				if (connection.setNX(key.getBytes(Charset.forName("utf-8")), ext.getBytes())) {
					log.debug(LogProperty.LOGTYPE_DETAIL, tag + "锁定成功：" + ext);
					return ext;
				}
				byte[] tmb = connection.get(key.getBytes(Charset.forName("utf-8")));
				if (tmb != null) {
					String tmv = new String(tmb);
					long time = Long.parseLong(tmv.replaceAll(".*\\|(\\d+)$", "$1"));
					if (System.currentTimeMillis() > time) {
						byte[] cvb = connection.getSet(key.getBytes(Charset.forName("utf-8")), ext.getBytes());
						if (cvb == null || tmv.equals(new String(cvb))) {
							log.debug(LogProperty.LOGTYPE_DETAIL, tag + "锁定成功：" + ext);
							return ext;
						}
					}
					log.debug(LogProperty.LOGTYPE_DETAIL, tag + "锁定失败：" + tmv);
				}
				return null;
			}
		});
		if (getToken == null) {
			return false;
		}
		try {
			runnable.run();
			return true;
		} catch (Throwable ex) {
			throw ex;
		} finally {
			redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.del(new byte[][]{key.getBytes(Charset.forName("utf-8"))});
					return null;
				}
			});
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getKeyByteData() {
		return this.key.getBytes(Charset.forName("utf-8"));
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
