package com.newland.paas.common.util;

import java.lang.reflect.Method;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
/**
 * 主要用于把表model类 复制到 表modelHis类中
 * （只适用于两个类一致的情况下，且有属性都有getter  setter 方法）
 * @author wujw
 *
 */
public class ModelCopyUtils {
	private static Log logger = LogFactory.getLogger(ModelCopyUtils.class);
	public static <T, R> T modelToHisModel(Class<T> destination, R source){
		try {
			T t = destination.newInstance();
			Method[] d_menthods = destination.getDeclaredMethods();
			for(Method method : d_menthods){
				String mName = method.getName();
				if(mName.startsWith("set")){
					try {
						String sMethodName = "g" + mName.substring(1);
						Method smethod = source.getClass().getDeclaredMethod(sMethodName);
						method.invoke(t, smethod.invoke(source));
					} catch (Exception e) {
						//方法赋值失败
						logger.error("", "", e, e.getMessage());
					}
				}
			}
			return t;
		} catch (IllegalAccessException e) {
			logger.error("", "", e, e.getMessage());
		} catch (InstantiationException e) {
			logger.error("", "", e, e.getMessage());
		}
		return null;
	}
}
