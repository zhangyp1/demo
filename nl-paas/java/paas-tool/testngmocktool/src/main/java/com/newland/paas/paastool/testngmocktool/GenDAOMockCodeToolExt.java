package com.newland.paas.paastool.testngmocktool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.alibaba.fastjson.JSON;

/**
 * 生成service中的dao属性mock代码.<br>
 * 依赖odbc,fastjson.
 */
public class GenDAOMockCodeToolExt {
	/**
	 * 扫描目录(workspace顶层文件夹)
	 */
	private static String SCAN_FOLDER = "java";
	private static String URL;
	private static String USER;
	private static String PASSWORD;
	/**
	 * mock数据的条数
	 */
	private static Long RESULT_COUNT = 10L;

	private static final String TAB = "\t";
	private static final String TAB2 = TAB + TAB;
	private static final String TAB3 = TAB2 + TAB;
	private static final String TAB4 = TAB3 + TAB;
	private static final String TAB5 = TAB4 + TAB;
	private static final String NEWLINE = "\n";
	private static Map<String, Set<String>> tableInfo = new HashMap<String, Set<String>>();

	static {
		URL = PropertiesUtil.getValue("URL");
		USER = PropertiesUtil.getValue("USER");
		PASSWORD = PropertiesUtil.getValue("PASSWORD");
		RESULT_COUNT = Long.parseLong(PropertiesUtil.getValue("DATACOUNT"));
	}

	public static void main(String[] args) throws Exception {
		go();
	}

	public static void go() {
		// 1.用户输入类名
		String className;
		String classPath;
		System.out.println("请输入SERVICE类名(将在SCAN_FOLDER目录下扫描所有的类):");
		Scanner scanner = new Scanner(System.in);
		while ((className = scanner.nextLine()) != null) {
			break;
		}
		// 2.查询所有类名,如果只有一个直接生成,多个则需要选择确认
		List<ClassInfo> allClass = findClassWidthAllPath(className);
		if (allClass.size() == 0) {
			System.out.println("没有找到对应的类文件,请确认类名是否正确!");
			return;
		}
		if (allClass.size() > 0) {
			System.out.println("请选择具体的类:");
			for (int i = 1; i <= allClass.size(); i++) {
				ClassInfo tempClassInfo = allClass.get(i - 1);
				System.out.println(" " + i + ":" + tempClassInfo.getClassName());
			}
			String choiceNum;
			while ((choiceNum = scanner.nextLine()) != null) {
				break;
			}
			className = allClass.get(Integer.parseInt(choiceNum) - 1).getClassName();
			classPath = allClass.get(Integer.parseInt(choiceNum) - 1).getClassPath();
			Set<String> allClassPath = allClass.get(Integer.parseInt(choiceNum) - 1).getAllClassPath();
			// 3.生成文件
			createMockFile(className, classPath, allClassPath);
			System.out.println("生成文件的mock代码..." + className);
		} else {
			System.out.println("没有找到相应的类.");
		}
	}

	private static List<Map<String, String>> findClass(String className) {
		List<Map<String, String>> allList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> allClass = getClassName("", true);
		Set<String> allClassPath = new HashSet<String>();
		for (Map<String, String> clazz : allClass) {
			allClassPath.add(clazz.get("classPath"));
			if (clazz.get("className").endsWith(className)) {
				allList.add(clazz);
			}
		}
		return allList;
	}

	private static List<ClassInfo> findClassWidthAllPath(String className) {
		List<ClassInfo> allList = new ArrayList<ClassInfo>();
		List<Map<String, String>> allClass = getClassName("", true);
		Set<String> allClassPath = new HashSet<String>();
		for (Map<String, String> clazz : allClass) {
			allClassPath.add(clazz.get("classPath"));
			if (clazz.get("className").endsWith(className)) {
				ClassInfo temp = new ClassInfo();
				temp.setClassName(clazz.get("className"));
				temp.setClassPath(clazz.get("classPath"));
				temp.setAllClassPath(allClassPath);
				allList.add(temp);
			}
		}
		return allList;
	}

	/**
	 * 首字母小写
	 * 
	 * @param fieldName
	 * @return
	 */
	private static String genFieldName(String fieldName) {
		return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
	}

	private static String genFieldTypeName(String fieldType) {
		int idx = fieldType.lastIndexOf(".");
		return fieldType.substring(idx + 1);
	}

	private static String genImportString(Set<String> importStrSet) {
		String[] basicType = new String[] { "void", "int", "byte", "short", "long", "float", "double", "boolean" };
		importStrSet.add("java.util.*");
		importStrSet.add("mockit.Deencapsulation");
		importStrSet.add("mockit.Mock");
		importStrSet.add("mockit.MockUp");
		importStrSet.add("mockit.Mocked");
		importStrSet.add("org.testng.Assert");
		importStrSet.add("org.testng.annotations.AfterClass");
		importStrSet.add("org.testng.annotations.BeforeClass");
		importStrSet.add("org.testng.annotations.Test");
		importStrSet.add("com.alibaba.fastjson.JSON");
		importStrSet.add("com.alibaba.fastjson.JSONArray");
		importStrSet.add("com.alibaba.fastjson.JSONObject");
		importStrSet.add("java.lang.reflect.Field");
		importStrSet.add("java.lang.reflect.Method");
		StringBuilder importStr = new StringBuilder();
		for (Iterator it = importStrSet.iterator(); it.hasNext();) {
			String _type = it.next().toString();
			if (!Arrays.asList(basicType).contains(_type))
				importStr.append("import " + _type + ";" + NEWLINE);
		}
		return importStr.toString();
	}

	/**
	 * 生成mock的java代码,只会针对Mapper结尾的DAO属性设置mock.
	 * 
	 * @param className
	 *            service的类名
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static String genJavaContent(String className, Set<String> allClassPath) throws ClassNotFoundException {
		DClassLoader classLoader = new DClassLoader(new URL[] {}, null);

		try {
			for (String str : allClassPath) {
				File file = new File(str);
				classLoader.addClass(file.toURI().toURL());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> reModellist = new ArrayList<String>(); // services层中以Mapper结尾的dao属性集合
		Class clazz = classLoader.loadClass(className);
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder packageStr = new StringBuilder();
		packageStr.append("package " + clazz.getPackage().getName() + ";" + NEWLINE + NEWLINE);
		Set<String> importStrSet = new HashSet<String>();
		StringBuilder classStr1 = new StringBuilder(
				NEWLINE + "public class " + clazz.getSimpleName() + "Test {" + NEWLINE + NEWLINE);
		StringBuilder mockField = new StringBuilder();
		StringBuilder testClassInstnceStr = new StringBuilder(NEWLINE + TAB + "// 测试类" + NEWLINE);
		testClassInstnceStr.append(TAB + genFieldTypeName(className) + " " + genFieldName(genFieldTypeName(className))
				+ " = new " + genFieldTypeName(className) + "();" + NEWLINE);

		testClassInstnceStr.append(TAB + "// 模拟数据" + NEWLINE);
		testClassInstnceStr
				.append(TAB + "private static Map<String, List> mockData = new HashMap<String, List>();" + NEWLINE);
		testClassInstnceStr.append(TAB + "// 表主键信息" + NEWLINE);
		testClassInstnceStr
				.append(TAB + "private static Map<String, Set> tablePk = new HashMap<String, Set>();" + NEWLINE);

		// beforeClassStr
		StringBuilder beforeClassStr = new StringBuilder();
		beforeClassStr.append(NEWLINE + TAB + "@BeforeClass" + NEWLINE);
		beforeClassStr.append(TAB + "public void beforeClass() {" + NEWLINE);
		beforeClassStr.append(TAB2 + "System.out.println(\"this is before class\");" + NEWLINE + NEWLINE);
		beforeClassStr.append(TAB2 + "genMockData();" + NEWLINE + NEWLINE);

		StringBuilder mockAllDAOStr = new StringBuilder();
		for (Field field : fields) {
			String typeFullName = field.getType().getName(); // 类型全名
			String typeName = genFieldTypeName(typeFullName); // 类型简称
			String fieldName = field.getName(); // 属性名
			importStrSet.add(typeFullName);
			// 针对每个Mapper属性中所有的方法进行mock
			StringBuilder mockEachDAOStr = new StringBuilder();
			if (field.getName().endsWith("Mapper")) {

				mockField.append(TAB + "@Mocked" + NEWLINE);
				mockField.append(TAB + "private " + typeName + " " + fieldName + ";" + NEWLINE);

				mockEachDAOStr.append(TAB2 + "// " + typeName + " mock" + NEWLINE);
				mockEachDAOStr.append(TAB2 + "MockUp<" + typeName + "> mock_" + genFieldName(typeName)
						+ " = new MockUp<" + typeName + ">() {" + NEWLINE);
				// DAO中的每个方法mock
				StringBuilder mockAllDAOMethodStr = new StringBuilder();
				Class clazzDAO = classLoader.loadClass(typeFullName);
				Method[] Methods = clazzDAO.getDeclaredMethods();
				for (Method method : Methods) {
					StringBuilder mockEachDAOMethodStr = new StringBuilder();
					mockEachDAOMethodStr.append(TAB3 + "@Mock" + NEWLINE);
					mockEachDAOMethodStr.append(TAB3 + "public " + genFieldTypeName(method.getReturnType().getName())
							+ " " + method.getName() + "(");

					importStrSet.add(method.getReturnType().getName());
					StringBuilder mockEachDAOMethodParameterStr = new StringBuilder();
					Class[] paramTypes = method.getParameterTypes();
					for (int i = 0; i < paramTypes.length; i++) {
						importStrSet.add(paramTypes[i].getName());
						mockEachDAOMethodParameterStr
								.append(genFieldTypeName(paramTypes[i].getName()) + " " + "param" + (i + 1));
						if (i < paramTypes.length - 1) {
							mockEachDAOMethodParameterStr.append(", ");
						}
					}
					mockEachDAOMethodStr.append(mockEachDAOMethodParameterStr.toString());
					mockEachDAOMethodStr.append(") { " + NEWLINE);
					// 根据返回类型,随机生成返回值
					mockEachDAOMethodStr.append(genReturnData(method, reModellist));
					mockEachDAOMethodStr.append(TAB3 + "}" + NEWLINE);
					mockAllDAOMethodStr.append(mockEachDAOMethodStr);
				}
				mockEachDAOStr.append(mockAllDAOMethodStr);
				mockEachDAOStr.append(TAB2 + "};" + NEWLINE);

				mockEachDAOStr.append(
						TAB2 + fieldName + " = mock_" + genFieldName(typeName) + ".getMockInstance();" + NEWLINE);
				mockEachDAOStr.append(TAB2 + "// 设置MOCK对象" + NEWLINE);
				mockEachDAOStr.append(TAB2 + "Deencapsulation.setField(" + genFieldName(genFieldTypeName(className))
						+ ", \"" + fieldName + "\", " + fieldName + ");" + NEWLINE);
			}
			mockAllDAOStr.append(mockEachDAOStr);
		}
		mockAllDAOStr.append(TAB + "}" + NEWLINE);

		// testBodyStr
		StringBuilder testBodyStr = new StringBuilder();
		testBodyStr.append(NEWLINE + TAB + "@Test" + NEWLINE);
		testBodyStr.append(TAB + "public void doTest() {" + NEWLINE);
		testBodyStr.append(TAB2 + "//编写测试代码" + NEWLINE);
		testBodyStr.append(TAB + "}" + NEWLINE);

		// afterClassStr
		StringBuilder afterClassStr = new StringBuilder();
		afterClassStr.append(NEWLINE + TAB + "@AfterClass" + NEWLINE);
		afterClassStr.append(TAB + "public void afterClass() {" + NEWLINE);
		afterClassStr.append(TAB2 + "System.out.println(\"this is after class\");" + NEWLINE);
		afterClassStr.append(TAB + "}" + NEWLINE);

		// 根据bean的名词数组生成数据json,并且获取table的主键列名
		String jsonData = "{}";
		String tableInfo = "{}";
		try {
			jsonData = genData(allClassPath, reModellist);
			tableInfo = JSON.toJSONString(GenDAOMockCodeToolExt.tableInfo);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// jsonDataStr
		StringBuilder jsonDataStr = new StringBuilder();
		jsonDataStr.append(NEWLINE + TAB + "//将数据反序列化到mockData");
		jsonDataStr.append(NEWLINE + TAB + "private static void genMockData() {");
		jsonDataStr.append(NEWLINE + TAB2 + "String tableInfo=\"" + tableInfo.replaceAll("\"", "'") + "\";");
		jsonDataStr.append(NEWLINE + TAB2 + "String jsonData=\"" + jsonData.replaceAll("\"", "'") + "\";");
		jsonDataStr.append(NEWLINE + TAB2 + "JSONObject tableInfoObject = JSON.parseObject(tableInfo);");
		jsonDataStr.append(NEWLINE + TAB2 + "JSONObject jsonObject = JSON.parseObject(jsonData);");
		jsonDataStr.append(NEWLINE + TAB2 + "for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {");
		jsonDataStr.append(NEWLINE + TAB3 + "String className = entry.getKey();");
		jsonDataStr.append(NEWLINE + TAB3 + "Object classData = entry.getValue();");
		jsonDataStr.append(NEWLINE + TAB3 + "JSONArray tempJA = (JSONArray) classData;");
		jsonDataStr.append(NEWLINE + TAB3 + "Class cls = null;");
		jsonDataStr.append(NEWLINE + TAB3 + "try {");
		jsonDataStr.append(NEWLINE + TAB4 + "cls = Class.forName(className);");
		jsonDataStr.append(NEWLINE + TAB3 + "} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB4 + "e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB3 + "}");
		jsonDataStr.append(NEWLINE + TAB3 + "List temp = JSON.parseArray(tempJA.toJSONString(), cls);");
		jsonDataStr.append(NEWLINE + TAB3 + "mockData.put(className, temp);");
		jsonDataStr.append(NEWLINE + TAB2 + "}");

		jsonDataStr.append(NEWLINE + TAB2 + "for (Map.Entry<String, Object> entry : tableInfoObject.entrySet()) {");
		jsonDataStr.append(NEWLINE + TAB3 + "String className = entry.getKey();");
		jsonDataStr.append(NEWLINE + TAB3 + "JSONArray classPk = (JSONArray) entry.getValue();");
		jsonDataStr.append(NEWLINE + TAB3 + "Set<String> pkSet = new HashSet<String>();");
		jsonDataStr.append(NEWLINE + TAB3 + "for (Object pk : classPk) {");
		jsonDataStr.append(NEWLINE + TAB4 + "pkSet.add(pk.toString());");
		jsonDataStr.append(NEWLINE + TAB3 + "}");
		jsonDataStr.append(NEWLINE + TAB3 + "tablePk.put(className, pkSet);");
		jsonDataStr.append(NEWLINE + TAB2 + "}");
		jsonDataStr.append(NEWLINE + TAB + "}");

		jsonDataStr.append(NEWLINE + TAB + "private static List getJsonData(String beanName) {");
		jsonDataStr.append(NEWLINE + TAB2 + "return mockData.get(beanName);");
		jsonDataStr.append(NEWLINE + TAB + "}");

		jsonDataStr.append(NEWLINE + TAB + "// 新增(返回主键值)");
		jsonDataStr.append(NEWLINE + TAB + "private static String insertOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB + "	String className = obj.getClass().getName();");
		jsonDataStr.append(NEWLINE + TAB + "	// 获取类的主键,生成序列");
		jsonDataStr.append(NEWLINE + TAB + "	Set<String> pkInfo = tablePk.get(className);");
		jsonDataStr.append(NEWLINE + TAB + "	String pk = pkInfo.iterator().next();");
		jsonDataStr.append(NEWLINE + TAB + "	Class cls = null;");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		cls = Class.forName(className);");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Method[] methods = cls.getMethods();");
		jsonDataStr.append(NEWLINE + TAB + "	String typeName = \"\";");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		Field pkField = cls.getDeclaredField(pk);");
		jsonDataStr.append(NEWLINE + TAB + "		typeName = pkField.getType().getSimpleName();");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Long pkValue = System.currentTimeMillis();");
		jsonDataStr.append(NEWLINE + TAB
				+ "	String setMethodStr = \"set\" + pk.substring(0, 1).toUpperCase() + pk.substring(1);");
		jsonDataStr.append(NEWLINE + TAB + "	for (Method method : methods) {");
		jsonDataStr.append(NEWLINE + TAB + "		if (method.getName().equals(setMethodStr)) {");
		jsonDataStr.append(NEWLINE + TAB + "			if (typeName.equals(\"BigDecimal\")) {");
		jsonDataStr.append(NEWLINE + TAB + "				try {");
		jsonDataStr.append(NEWLINE + TAB + "					method.invoke(obj, new BigDecimal(pkValue));");
		jsonDataStr.append(NEWLINE + TAB + "				} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "					e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "				}");
		jsonDataStr.append(NEWLINE + TAB + "			} else {");
		jsonDataStr.append(NEWLINE + TAB + "				try {");
		jsonDataStr.append(NEWLINE + TAB + "					method.invoke(obj, pkValue + \"\");");
		jsonDataStr.append(NEWLINE + TAB + "				} catch (Exception e)  {");
		jsonDataStr.append(NEWLINE + TAB + "					e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "				}");
		jsonDataStr.append(NEWLINE + TAB + "			}");
		jsonDataStr.append(NEWLINE + TAB + "			break;");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	List list = mockData.get(className);");
		jsonDataStr.append(NEWLINE + TAB + "	if (list != null) {");
		jsonDataStr.append(NEWLINE + TAB + "		list.add(obj);");
		jsonDataStr.append(NEWLINE + TAB + "	} else {");
		jsonDataStr.append(NEWLINE + TAB + "		list = new ArrayList<Object>();");
		jsonDataStr.append(NEWLINE + TAB + "		list.add(obj);");
		jsonDataStr.append(NEWLINE + TAB + "		mockData.put(className, list);");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	return pkValue + \"\";");
		jsonDataStr.append(NEWLINE + TAB + "}");

		jsonDataStr.append(NEWLINE + TAB + "// 修改(返回数量)");
		jsonDataStr.append(NEWLINE + TAB + "private static int updateOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB + "	// 先删除再新增");
		jsonDataStr.append(NEWLINE + TAB + "	int count = 0;");
		jsonDataStr.append(NEWLINE + TAB + "	count = deleteOp(obj);");
		jsonDataStr.append(NEWLINE + TAB + "	if (count > 0) {");
		jsonDataStr.append(NEWLINE + TAB + "		insertOp(obj);");
		jsonDataStr.append(NEWLINE + TAB + "	} else {");
		jsonDataStr.append(NEWLINE + TAB + "		return 0;");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	return count;");
		jsonDataStr.append(NEWLINE + TAB + "}");

		jsonDataStr.append(NEWLINE + TAB + "// 删除(返回数量)");
		jsonDataStr.append(NEWLINE + TAB + "private static int deleteOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB + "	int count = 0;");
		jsonDataStr.append(NEWLINE + TAB + "	String className = obj.getClass().getName();");
		jsonDataStr.append(NEWLINE + TAB + "	// 获取类的主键");
		jsonDataStr.append(NEWLINE + TAB + "	Set<String> pkInfo = tablePk.get(className);");
		jsonDataStr.append(NEWLINE + TAB + "	String pk = pkInfo.iterator().next();");
		jsonDataStr.append(NEWLINE + TAB + "	Class cls = null;");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		cls = Class.forName(className);");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Method[] methods = cls.getMethods();");
		jsonDataStr.append(NEWLINE + TAB + "	String typeName = \"\";");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		Field pkField = cls.getDeclaredField(pk);");
		jsonDataStr.append(NEWLINE + TAB + "		typeName = pkField.getType().getSimpleName();");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Long pkValue = System.currentTimeMillis();");
		jsonDataStr.append(NEWLINE + TAB
				+ "	String getMethodStr = \"get\" + pk.substring(0, 1).toUpperCase() + pk.substring(1);");
		jsonDataStr.append(NEWLINE + TAB + "	Method getMethod = null;");
		jsonDataStr.append(NEWLINE + TAB + "	for (Method method : methods) {");
		jsonDataStr.append(NEWLINE + TAB + "		if (method.getName().equals(getMethodStr)) {");
		jsonDataStr.append(NEWLINE + TAB + "			getMethod = method;");
		jsonDataStr.append(NEWLINE + TAB + "			break;");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	String getMethodValue = \"\";");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		getMethodValue = getMethod.invoke(obj, null).toString();");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (Exception e)  {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	if (getMethodValue.trim().length() == 0) {");
		jsonDataStr.append(NEWLINE + TAB + "		return count;");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	List list = mockData.get(className);");
		jsonDataStr.append(NEWLINE + TAB + "	Iterator<Object> it = list.iterator();");
		jsonDataStr.append(NEWLINE + TAB + "	while (it.hasNext()) {");
		jsonDataStr.append(NEWLINE + TAB + "		Object object = it.next();");
		jsonDataStr.append(NEWLINE + TAB + "		try {");
		jsonDataStr.append(NEWLINE + TAB + "			String tempVal = getMethod.invoke(object, null).toString();");
		jsonDataStr.append(NEWLINE + TAB + "			if (tempVal.equals(getMethodValue)) { // 删除");
		jsonDataStr.append(NEWLINE + TAB + "				it.remove();");
		jsonDataStr.append(NEWLINE + TAB + "				count++;");
		jsonDataStr.append(NEWLINE + TAB + "			}");
		jsonDataStr.append(NEWLINE + TAB + "		} catch (Exception e)  {");
		jsonDataStr.append(NEWLINE + TAB + "			e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	return count;");
		jsonDataStr.append(NEWLINE + TAB + "}");

		// endStr
		StringBuilder endStr = new StringBuilder();
		endStr.append(NEWLINE + "}");

		try {
			classLoader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return packageStr.toString() +

				genImportString(importStrSet) + classStr1.toString() + mockField.toString()
				+ testClassInstnceStr.toString() + beforeClassStr.toString() + mockAllDAOStr.toString()
				+ testBodyStr.toString() + afterClassStr.toString() + jsonDataStr.toString() + endStr.toString();
	}

	/**
	 * 根据方法返回值,随机生成返回数据.<br>
	 * <b>注意List<>形式目前只支持一层嵌套.</b>
	 * 
	 * @param method
	 * @return
	 */
	private static String genReturnData(Method method, List<String> reModellist) {
		StringBuilder reStr = new StringBuilder();
		Class reType = method.getReturnType();
		String reTypeName = reType.getSimpleName();
		Type genericReturnType = method.getGenericReturnType();
		if (reTypeName.equals("List")) {
			if (genericReturnType instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
				Type actualTypeArgument = actualTypeArguments[0];
				Class argumentClass = null;
				try {
					argumentClass = (Class) actualTypeArgument;
				} catch (ClassCastException e) {
					System.out.println("List<>目前不支持集合嵌套,使用List<Object>替代.");
					argumentClass = Object.class;
				}
				String genericClassName = argumentClass.getName();
				if (!argumentClass.getSimpleName().equals("Object")) {
					reModellist.add(genericClassName);
				}
				reStr.append(TAB4 + "String beanName=\"" + genericClassName + "\";" + NEWLINE);
				reStr.append(TAB4 + "List<" + genericClassName + "> reList=" + NEWLINE);
				reStr.append(TAB4 + "getJsonData(beanName);" + NEWLINE);
				reStr.append(TAB4 + "return reList;" + NEWLINE);
			}
			return reStr.toString();
		} else if (reTypeName.equals("void")) {
			reStr.append(TAB4 + "//TODO" + NEWLINE);
		} else if (reTypeName.equals("int")) {
			reStr.append(TAB4 + "java.util.Random random=new java.util.Random();" + NEWLINE);
			reStr.append(TAB4 + "int reInt=random.nextInt(1);" + NEWLINE);
			reStr.append(TAB4 + "//TODO" + NEWLINE);
			reStr.append(TAB4 + "return reInt;" + NEWLINE);
		} else if (reType instanceof Class) {
			reModellist.add(reType.getName());
			reStr.append(TAB4 + "String beanName=\"" + reType.getName() + "\";" + NEWLINE);
			reStr.append(TAB4 + reType.getName() + " reObj= (" + reType.getName() + ")"
					+ "getJsonData(beanName).get(0);" + NEWLINE);
			reStr.append(TAB4 + "return reObj;" + NEWLINE);
		} else {
			reStr.append(TAB4 + "return null;" + NEWLINE);
		}

		return reStr.toString();
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName
	 *            包名
	 * @param isRecursion
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	public static List<Map<String, String>> getClassName(String packageName, boolean isRecursion) {
		List<Map<String, String>> classNames = null;
		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		URL url2 = GenDAOMockCodeToolExt.class.getResource("");
		if (url != null) {
			String protocol = url.getProtocol();
			if (protocol.equals("file")) {
				String filePath = url.getPath();
				File file = new File(filePath);
				while (!file.getName().equalsIgnoreCase(SCAN_FOLDER)) {
					file = file.getParentFile();
				}
				classNames = getClassNameFromDir(file.getPath(), packageName, isRecursion);
			}
		}
		return classNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * 
	 * @param filePath
	 *            文件路径
	 * @param isRecursion
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<Map<String, String>> getClassNameFromDir(String filePath, String packageName,
			boolean isRecursion) {
		if (packageName.trim().length() > 0) {
			packageName = packageName + ".";
		}
		List<Map<String, String>> className = new ArrayList<Map<String, String>>();
		File file = new File(filePath);
		File[] files = file.listFiles();
		for (File childFile : files) {
			if (childFile.isDirectory()) {
				if (isRecursion) {
					className.addAll(
							getClassNameFromDir(childFile.getPath(), packageName + childFile.getName(), isRecursion));
				}
			} else {
				String fileName = childFile.getName();
				if (fileName.endsWith(".class") && !fileName.contains("$")) {
					Map<String, String> _map = new HashMap<String, String>();
					// 获取classes所在的path
					String allPath = childFile.getPath();
					String _classPath = "";
					int _isClassPath = 100;
					String _className = "";
					String[] _split = allPath.split(java.io.File.separator);
					for (int i = 0; i < _split.length; i++) {
						if (_split[i].equals("classes")) {
							_isClassPath = i;
						}
						if (_isClassPath >= i) {
							_classPath += _split[i] + "/";
						} else {
							_className += _split[i] + ".";
						}
					}
					_className = _className.replace(".class.", "");
					_map.put("className", _className);
					_map.put("classPath", _classPath);
					className.add(_map);
				}
			}
		}
		return className;
	}

	/**
	 * @param jarEntries
	 * @param packageName
	 * @param isRecursion
	 * @return
	 */
	private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries, String packageName,
			boolean isRecursion) {
		Set<String> classNames = new HashSet<String>();

		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			if (!jarEntry.isDirectory()) {
				/*
				 * 这里是为了方便，先把"/" 转成 "." 再判断 ".class" 的做法可能会有bug (FIXME: 先把"/" 转成 "." 再判断
				 * ".class" 的做法可能会有bug)
				 */
				String entryName = jarEntry.getName().replace("/", ".");
				if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(packageName)) {
					entryName = entryName.replace(".class", "");
					if (isRecursion) {
						classNames.add(entryName);
					} else if (!entryName.replace(packageName + ".", "").contains(".")) {
						classNames.add(entryName);
					}
				}
			}
		}

		return classNames;
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * 
	 * @param urls
	 *            URL集合
	 * @param packageName
	 *            包路径
	 * @param isRecursion
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
		Set<String> classNames = new HashSet<String>();

		for (int i = 0; i < urls.length; i++) {
			String classPath = urls[i].getPath();

			// 不必搜索classes文件夹
			if (classPath.endsWith("classes/")) {
				continue;
			}

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (jarFile != null) {
				classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
			}
		}

		return classNames;
	}

	public static void createMockFile(String classWholeName, String classPath, Set<String> allClassPath) {
		String packageName = classWholeName.substring(0, classWholeName.lastIndexOf("."));
		String className = classWholeName.substring(classWholeName.lastIndexOf(".") + 1);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", "/");
		String path = classPath.replaceAll("target/classes", "src/test/java").replaceAll("target/test-classes",
				"src/test/java") + packagePath + "/" + className;
		String content = "";
		try {
			content = genJavaContent(classWholeName, allClassPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		createNewFile(path, content);
	}

	private static boolean CreateMultilayerFile(String dir) {
		try {
			File dirPath = new File(dir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
		} catch (Exception e) {
			System.out.println("创建多层目录操作出错: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void createNewFile(String path, String content) {
		try {
			int lastLength = path.lastIndexOf(java.io.File.separator);
			// 得到文件夹目录
			String dir = path.substring(0, lastLength);
			// 先创建文件夹
			if (CreateMultilayerFile(dir) == true) {
				File filePath = new File(path + "Test.java");
				if (!filePath.exists()) {
					filePath.createNewFile();
				}
				FileWriter filterWriter = new FileWriter(filePath);
				filterWriter.write(content);
				filterWriter.close();
			}
		} catch (Exception e) {
			System.out.println("新建java文件操作出错: " + e.getMessage());
			e.printStackTrace();
		} finally {

		}
	}

	static class ClassInfo {
		String className;
		String classPath;
		Set<String> allClassPath;

		public String getClassPath() {
			return classPath;
		}

		public void setClassPath(String classPath) {
			this.classPath = classPath;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public Set<String> getAllClassPath() {
			return allClassPath;
		}

		public void setAllClassPath(Set<String> allClassPath) {
			this.allClassPath = allClassPath;
		}
	}

	static class DClassLoader extends URLClassLoader {
		public DClassLoader(URL[] urls) {
			super(urls);
		}

		public DClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public void addClass(URL url) {
			this.addURL(url);
		}
	}

	private static String genData(Set<String> allClassPath, List<String> beans)
			throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		Map<String, List> data = new HashMap<String, List>();
		for (int i = 0; i < beans.size(); i++) {
			Statement st = null;
			try {
				st = conn.createStatement();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 获取表的主键
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rsPk = dbmd.getPrimaryKeys(null, null, genTableName(beans.get(i))); // 要将表名转为大写才能正确取出主键来

			Set<String> tablePk = new HashSet<String>();
			while (rsPk.next()) {
				String pkName = rsPk.getString("COLUMN_NAME");
				tablePk.add(genFieldNameFromColumn(pkName));
			}
			tableInfo.put(beans.get(i), tablePk);

			String sql = getOraclePageList("select * from " + genTableName(beans.get(i)), 1L, RESULT_COUNT);
			ResultSet rs = null;
			try {
				rs = st.executeQuery(sql);
			} catch (SQLException e1) {
				System.out.println("无法执行:" + sql);
				try {
					rs.close();
					st.close();
				} catch (Exception e) {
				}
				continue;
			}
			List list = new ArrayList<>();
			try {
				list = tranRsToObj(allClassPath, rs, beans.get(i));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			data.put(beans.get(i), list);
			rs.close();
			st.close();
		}
		conn.close();
		String jsonData = JSON.toJSONString(data);
		return jsonData;
	}

	/**
	 * 根据resultset生成List数据
	 * 
	 * @param allClassPath
	 * @param rs
	 * @param clsName
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static List tranRsToObj(Set<String> allClassPath, ResultSet rs, String clsName)
			throws SQLException, IllegalArgumentException, IllegalAccessException {

		DClassLoader classLoader = new DClassLoader(new URL[] {}, null);

		try {
			for (String str : allClassPath) {
				File file = new File(str);
				classLoader.addClass(file.toURI().toURL());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Class reObject = null;
		try {
			reObject = classLoader.loadClass(clsName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		List<Field> fields = new LinkedList();
		Class preObject = reObject;
		while (preObject != null) {
			Field[] f = preObject.getDeclaredFields();
			for (int i = 0; i < f.length; i++) {
				fields.add(f[i]);
			}
			preObject = preObject.getSuperclass();
		}
		ResultSetMetaData rsmd = rs.getMetaData();

		int colCount = rsmd.getColumnCount();
		List list = new ArrayList();
		while (rs.next()) {
			Object bean = null;
			try {
				bean = reObject.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			for (int i = 1; i <= colCount; i++) {
				Object value = rs.getObject(i);
				for (int j = 0; j < fields.size(); j++) {
					Field f = fields.get(j);
					if (genTableName(f.getName()).equalsIgnoreCase(rsmd.getColumnName(i))) {
						boolean flag = f.isAccessible();
						f.setAccessible(true);
						if (f.getType().getSimpleName().equals("Short")) {
							f.set(bean, ((BigDecimal) value).shortValueExact());
						} else if (f.getType().getSimpleName().equals("Date")) {
							f.set(bean, value);
						} else {
							f.set(bean, value);
						}
						f.setAccessible(flag);
					}
				}
			}
			list.add(bean);
		}
		return list;
	}

	private static String getOraclePageList(String sql, Long currentpage, Long avgPageNum) {
		String newsql = "";
		if (sql != null && sql.trim().length() > 0 && currentpage != null && avgPageNum != null) {
			long pagesize = avgPageNum;
			newsql = "Select * from (Select t.*,rownum rn from (" + sql + " )t )tmp" + " where tmp.rn >=(" + currentpage
					+ "-1)* " + pagesize + "+1 and tmp.rn<=" + pagesize + "*" + currentpage;
		} else {
			newsql = sql;
		}
		return newsql;
	}

	/**
	 * 根据bean的名字生成表名:<br>
	 * 如ResOperationCode==>RES_OPERATION_CODE
	 * 
	 * @param beanName
	 * @return
	 */
	private static String genTableName(String beanName) {
		// 去除包名
		int idx = beanName.lastIndexOf(".");
		if (idx > 0) {
			beanName = beanName.substring(idx + 1);
		}
		if (beanName.trim().length() == 0) {
			return "";
		}
		char[] cc = beanName.toCharArray();
		String tableName = "" + cc[0];
		for (int i = 1; i < cc.length; i++) {
			if (Character.isUpperCase(cc[i])) {
				tableName = tableName + ("_" + cc[i]);
			} else {
				tableName = tableName + ("" + cc[i]);
			}
		}
		return tableName.toUpperCase();
	}

	/**
	 * 根据列名生成字段名:<br>
	 * 如RES_OPERATION_CODE==>resOperationCode
	 * 
	 * @param columnName
	 * @return
	 */
	private static String genFieldNameFromColumn(String columnName) {
		if (columnName == null || columnName.trim().length() == 0)
			return "";
		char[] cc = columnName.toLowerCase().toCharArray();
		String tableName = "" + cc[0];
		for (int i = 1; i < cc.length; i++) {
			if (cc[i] == '_') {
				tableName = tableName + ("" + cc[i + 1]).toUpperCase();
				i++;
			} else {
				tableName = tableName + ("" + cc[i]);
			}
		}
		return tableName;
	}
}