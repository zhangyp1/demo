package com.newland.paas.common.util.objcopy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.newland.paas.common.util.Json;

public class ObjCopy {

	public static void main(String[] args) {
		try {
			AppBase1 app1 = new AppBase1();
			app1.setAppInfoId(111222L);
			app1.setAstVer("df555");
			app1.setAppName("appName");
			app1.setSysCategoryId(111L);
			AppBase2 app2 = new AppBase2();
			System.out.println("app1=" + Json.toJson(app1));
			System.out.println("app2=" + Json.toJson(app2));
//			ObjCopy.copyProperties(app1, app2);
//			System.out.println("app1=" + Json.toJson(app1));
//			System.out.println("app2=" + Json.toJson(app2));
			System.out.println("-------------------------------------------------------");
			ObjCopy.copyProperties(app1, app2, new String[][] { { "astVer", "astVer1" } });
			System.out.println("app2=" + Json.toJson(app2));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//类对应属性
	public static Map<Class, Map<String, Field>> cm = new HashMap<Class, Map<String, Field>>();
	//映射关系
	public static Map<Class, Map<Class, List<Field[]>>> cml = new HashMap<Class, Map<Class, List<Field[]>>>();

	/**
	 * 拷贝对象属性
	 * @param sourceObj
	 * @param targetObj
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void copyProperties(Object sourceObj, Object targetObj) throws IllegalArgumentException, IllegalAccessException {
		copyProperties(sourceObj, targetObj, new HashMap<String, String>());
	}

	/**
	 * 
	 * @param sourceObj
	 * @param targetObj
	 * @param proNameArray 映射内容，以字符串数组方式提供 {{"源属性名","目标属性名"},{"源属性名","目标属性名"}}
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void copyProperties(Object sourceObj, Object targetObj, String[][] proNameArray) throws IllegalArgumentException, IllegalAccessException {
		Map<String, String> proNameMap = new HashMap<String, String>();
		for (int i = 0; i < proNameArray.length; i++) {
			proNameMap.put(proNameArray[i][0], proNameArray[i][1]);
		}
		copyProperties(sourceObj, targetObj, proNameMap);
	}

	/**
	 * 名称映射  
	 * @param sourceObj
	 * @param targetObj
	 * @param proNameMap 映射内容，以字符串数组方式提供  key=源对象属性名称，value=目标对象属性名称 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void copyProperties(Object sourceObj, Object targetObj, Map<String, String> proNameMap) throws IllegalArgumentException, IllegalAccessException {

		List<Field[]> fieldMapList = null;

		Map<Class, List<Field[]>> fieldM = cml.get(sourceObj.getClass());
		if (fieldM != null) {
			fieldMapList = fieldM.get(targetObj.getClass());
			if(fieldMapList !=null) {
				copyPro(fieldMapList, sourceObj, targetObj);
				return;
			}
		}else {
			fieldM = new HashMap<Class, List<Field[]>>();
			cml.put(sourceObj.getClass(), fieldM);
		}
		
		System.out.println("1");

		//获取属性列表
		Map<String, Field> sourceFieldM = getFields(sourceObj.getClass());
		Map<String, Field> targetFieldM = getFields(targetObj.getClass());

		//增加映射关系
		fieldMapList = new ArrayList<Field[]>();
		Iterator<String> it = sourceFieldM.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String targetKey = key;
			//查找映射关系
			if (proNameMap != null) {
				targetKey = proNameMap.get(key);
				if (targetKey == null)
					targetKey = key;
			}

			if (targetFieldM.get(targetKey) != null) {
				fieldMapList.add(new Field[] { sourceFieldM.get(key), targetFieldM.get(targetKey) });
			}
		}
		fieldM.put(targetObj.getClass(), fieldMapList);
		copyPro(fieldMapList, sourceObj, targetObj);
	}

	private static void copyPro(List<Field[]> fieldMapList, Object sourceObj, Object targetObj) throws IllegalArgumentException, IllegalAccessException {
		//复制属性
		for (Field[] fields : fieldMapList) {
			fields[0].setAccessible(true);
			fields[1].setAccessible(true);
			fields[1].set(targetObj, fields[0].get(sourceObj));
		}
	}

	private static Map<String, Field> getFields(Class c) {
		Map<String, Field> m = cm.get(c);
		if (m == null) {
			m = new HashMap<String, Field>();
			getFields(c, m);
			cm.put(c, m);
		}
		return m;
	}

	private static void getFields(Class c, Map<String, Field> fieldMap) {
		Field[] fields = c.getDeclaredFields();

		for (Field field : fields) {
			if (!isSwitchType(field.getType())) {
				continue;
			}
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			String key = field.getName();
			if (fieldMap.get(key) == null)
				fieldMap.put(key, field);
		}

		if (!c.getSuperclass().equals(Object.class)) {
			getFields(c.getSuperclass(), fieldMap);
		}
	}

	private static Map<Class, String> switchTypeM = new HashMap<Class, String>();
	static {
		switchTypeM.put(java.lang.String.class, "true");
		switchTypeM.put(java.lang.Integer.class, "true");
		switchTypeM.put(java.lang.Long.class, "true");
		switchTypeM.put(java.lang.Short.class, "true");
		switchTypeM.put(java.lang.Boolean.class, "true");
		switchTypeM.put(java.lang.Double.class, "true");
		switchTypeM.put(java.lang.Float.class, "true");
	}

	/**
	 * 是否转换类型
	 * @param c
	 * @return
	 */
	private static boolean isSwitchType(Class c) {
		if (switchTypeM.get(c) == null) {
			return false;
		}
		return true;
	}
}
