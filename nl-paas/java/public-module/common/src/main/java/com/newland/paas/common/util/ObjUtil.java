package com.newland.paas.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 对像工具
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class ObjUtil {

	/**
	 * 对象复制
	 * @param obj
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Object objClone(Object obj) throws InstantiationException, IllegalAccessException {
		Class c = obj.getClass();
		Object newObj = c.newInstance();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			field.set(newObj, field.get(obj));
		}
		return newObj;
	}
}
