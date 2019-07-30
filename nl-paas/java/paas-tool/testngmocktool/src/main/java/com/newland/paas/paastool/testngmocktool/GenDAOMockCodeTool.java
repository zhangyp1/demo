package com.newland.paas.paastool.testngmocktool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 生成service中的dao属性mock代码.<br>
 * 依赖odbc,fastjson.
 */
public class GenDAOMockCodeTool {
	/**
	 * 扫描目录(workspace顶层文件夹)
	 */
	private static String SCAN_FOLDER = "java";
	private static String URL;
	private static String USER;
	private static String PASSWORD;
	private static String TABLE_SCHEM;
	private static Long RESULT_COUNT;
	private static String MODEL_PACKAGE;
	private static final String[] Methods;

	private static final String TAB = "\t";
	private static final String TAB2 = TAB + TAB;
	private static final String TAB3 = TAB2 + TAB;
	private static final String TAB4 = TAB3 + TAB;
	private static final String TAB5 = TAB4 + TAB;
	private static final String NEWLINE = "\n";
	private static Map<String, List<PkInfo>> tableInfo = new HashMap<String, List<PkInfo>>();

	static {
		URL = PropertiesUtil.getValue("URL");
		USER = PropertiesUtil.getValue("USER");
		PASSWORD = PropertiesUtil.getValue("PASSWORD");
		TABLE_SCHEM = PropertiesUtil.getValue("TABLE_SCHEM");
		RESULT_COUNT = Long.parseLong(PropertiesUtil.getValue("DATACOUNT"));
		MODEL_PACKAGE = PropertiesUtil.getValue("MODEL_PACKAGE");
		Methods = new String[] { "deleteByPrimaryKey", "insert", "insertSelective", "selectByPrimaryKey",
				"updateByPrimaryKeySelective", "updateByPrimaryKey" };
	}

	public static void main(String[] args) throws Exception {
		go();
	}

	public static void go() {
		String tablesStr = PropertiesUtil.getValue("TABLES");
		if (tablesStr.trim().length() == 0) {
			System.out.println("没有指定表名.");
			return;
		}
		System.out.println("生成mock代码...start");
		String[] tables = tablesStr.split(",");
		createMockFile(tables);
		System.out.println("生成mock代码...finished");
	}

	/**
	 * 根据表名生成mock的java代码
	 * 
	 * @param tables
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static String genJavaContent(String[] tables) {
		// 根据bean的名词数组生成数据json,并且获取table的主键列名
		String jsonData = "{}";
		String tableInfo = "{}";
		try {
			jsonData = genData(tables);
			tableInfo = JSON.toJSONString(GenDAOMockCodeTool.tableInfo);
		} catch (SQLException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		StringBuilder mockField = new StringBuilder();
		StringBuilder testClassInstnceStr = new StringBuilder("");

		testClassInstnceStr.append(TAB + "// 模拟数据" + NEWLINE);
		testClassInstnceStr
				.append(TAB + "private static Map<String, List> mockData = new HashMap<String, List>();" + NEWLINE);
		testClassInstnceStr.append(TAB + "// 表主键信息" + NEWLINE);
		testClassInstnceStr.append(TAB
				+ "private static Map<String, Set<String>> tablePk = new HashMap<String, Set<String>>();" + NEWLINE);

		testClassInstnceStr.append(NEWLINE + TAB + "// 测试类" + NEWLINE);
		testClassInstnceStr.append(TAB + "测试类名 testServiceImpl = new 测试类名();" + NEWLINE);

		// beforeClassStr
		StringBuilder beforeClassStr = new StringBuilder();
		beforeClassStr.append(NEWLINE + TAB + "@BeforeClass" + NEWLINE);
		beforeClassStr.append(TAB + "public void beforeClass() {" + NEWLINE);
		beforeClassStr.append(TAB2 + "System.out.println(\"===============this is befor class===============\");"
				+ NEWLINE + NEWLINE);
		beforeClassStr.append(TAB2 + "genMockData();" + NEWLINE);

		StringBuilder mockAllDAOStr = new StringBuilder();
		for (String table : tables) {
			String field = genFieldNameFromColumn(table);
			String fieldClassName = field.substring(0, 1).toUpperCase() + field.substring(1);
			field = field + "Mapper";
			String typeName = field.substring(0, 1).toUpperCase() + field.substring(1); // mapper的类名
			// 针对每个Mapper属性中所有的方法进行mock
			StringBuilder mockEachDAOStr = new StringBuilder();

			mockField.append(TAB + "@Mocked" + NEWLINE);
			mockField.append(TAB + "private " + typeName + " " + field + ";" + NEWLINE);

			mockEachDAOStr.append(NEWLINE + TAB2 + "// " + typeName + " mock" + NEWLINE);
			mockEachDAOStr.append(
					TAB2 + "MockUp<" + typeName + "> mock_" + field + " = new MockUp<" + typeName + ">() {" + NEWLINE);
			// DAO中的每个方法mock
			StringBuilder mockAllDAOMethodStr = new StringBuilder();

			for (String method : GenDAOMockCodeTool.Methods) {
				StringBuilder mockEachDAOMethodStr = new StringBuilder();
				String returnType = "int"; // 返回类型,除了select方法是返回对象,其他都是返回int.
				if (method.startsWith("select")) {
					returnType = fieldClassName;
				}
				mockEachDAOMethodStr.append(TAB3 + "@Mock" + NEWLINE);
				mockEachDAOMethodStr.append(TAB3 + "public " + returnType + " " + method + "(");

				// 是否根据主键,如果是selectByPrimaryKey,deleteByPrimaryKey方法则参数是主键,其他方法都是对象作为参数.
				StringBuilder mockEachDAOMethodParameterStr = new StringBuilder();
				if (method.startsWith("select") || method.startsWith("delete")) {
					List<PkInfo> pkInfoList = GenDAOMockCodeTool.tableInfo.get(genFieldNameFromColumn(table));
					if (pkInfoList != null) {
						for (int i = 0; i < pkInfoList.size(); i++) {
							mockEachDAOMethodParameterStr
									.append(pkInfoList.get(i).getColType() + " " + pkInfoList.get(i).getColName());
							if (i < pkInfoList.size() - 1) {
								mockEachDAOMethodParameterStr.append(", ");
							}
						}
					}
				} else {
					mockEachDAOMethodParameterStr.append(fieldClassName + " record");
				}

				mockEachDAOMethodStr.append(mockEachDAOMethodParameterStr.toString());
				mockEachDAOMethodStr.append(") { " + NEWLINE);
				// 根据返回类型,随机生成返回值
				// TODO
				mockEachDAOMethodStr.append(genReturnData(table, method));
				mockEachDAOMethodStr.append(TAB3 + "}" + NEWLINE);
				mockAllDAOMethodStr.append(mockEachDAOMethodStr);
			}

			mockEachDAOStr.append(mockAllDAOMethodStr);
			mockEachDAOStr.append(TAB2 + "};" + NEWLINE);

			mockEachDAOStr.append(TAB2 + field + " = mock_" + field + ".getMockInstance();" + NEWLINE);
			mockEachDAOStr.append(TAB2 + "// 设置MOCK对象" + NEWLINE);
			mockEachDAOStr.append(
					TAB2 + "Deencapsulation.setField(testServiceImpl, \"" + field + "\", " + field + ");" + NEWLINE);
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
		afterClassStr
				.append(TAB2 + "System.out.println(\"===============this is after class===============\");" + NEWLINE);
		afterClassStr.append(TAB + "}" + NEWLINE);

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
		jsonDataStr.append(NEWLINE + TAB4 + "String temp=" + "\"" + MODEL_PACKAGE + ".\"+"
				+ "className.substring(0, 1).toUpperCase() + className.substring(1);");
		jsonDataStr.append(NEWLINE + TAB4 + "cls = Class.forName(temp);");
		jsonDataStr.append(NEWLINE + TAB3 + "} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB4 + "e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB3 + "}");
		jsonDataStr.append(NEWLINE + TAB3 + "List temp = JSON.parseArray(tempJA.toJSONString(), cls);");
		jsonDataStr.append(NEWLINE + TAB3 + "mockData.put(className.toUpperCase(), temp);");
		jsonDataStr.append(NEWLINE + TAB2 + "}");

		jsonDataStr.append(NEWLINE + TAB2 + "for (Map.Entry<String, Object> entry : tableInfoObject.entrySet()) {");
		jsonDataStr.append(NEWLINE + TAB3 + "String className = entry.getKey().toUpperCase();");
		jsonDataStr.append(NEWLINE + TAB3 + "JSONArray classPk = (JSONArray) entry.getValue();");
		jsonDataStr.append(NEWLINE + TAB3 + "Set<String> pkSet = new HashSet<String>();");
		jsonDataStr.append(NEWLINE + TAB3 + "for (Object pk : classPk) {");
		jsonDataStr.append(NEWLINE + TAB4 + "pkSet.add(((JSONObject) pk).get(\"colName\").toString());");
		jsonDataStr.append(NEWLINE + TAB3 + "}");
		jsonDataStr.append(NEWLINE + TAB3 + "tablePk.put(className, pkSet);");
		jsonDataStr.append(NEWLINE + TAB2 + "}");
		jsonDataStr.append(NEWLINE + TAB + "}");

		jsonDataStr.append(NEWLINE + TAB + "private static Object selectOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB2 + "String className = obj.getClass().getSimpleName().toUpperCase();");
		jsonDataStr.append(NEWLINE + TAB2 + "return mockData.get(className).get(0);");
		jsonDataStr.append(NEWLINE + TAB + "}");
		jsonDataStr.append(NEWLINE);
		jsonDataStr.append(NEWLINE + TAB + "// 新增(返回主键值)");
		jsonDataStr.append(NEWLINE + TAB + "private static int insertOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB + "	String className = obj.getClass().getName();");
		jsonDataStr.append(NEWLINE + TAB + "	String tempClzName = obj.getClass().getSimpleName().toUpperCase();");
		jsonDataStr.append(NEWLINE + TAB + "	Class cls = null;");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		cls = Class.forName(className);");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Method[] methods = cls.getMethods();");
		jsonDataStr.append(NEWLINE + TAB + "	// 获取类的主键,生成序列");
		jsonDataStr.append(NEWLINE + TAB + "	Set<String> pkInfo = tablePk.get(tempClzName);");
		jsonDataStr.append(NEWLINE + TAB + "	int pkValue = new Random().nextInt(99999);");
		jsonDataStr.append(NEWLINE + TAB + "	// 遍历所有的主键");
		jsonDataStr.append(NEWLINE + TAB + "	for (String pk : pkInfo) {");
		jsonDataStr.append(NEWLINE + TAB + "		String typeName = \"\";");
		jsonDataStr.append(NEWLINE + TAB + "		try {");
		jsonDataStr.append(NEWLINE + TAB + "			Field pkField = cls.getDeclaredField(pk);");
		jsonDataStr.append(NEWLINE + TAB + "			typeName = pkField.getType().getSimpleName();");
		jsonDataStr.append(NEWLINE + TAB + "		} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "			e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB
				+ "		String setMethodStr = \"set\" + pk.substring(0, 1).toUpperCase() + pk.substring(1);");
		jsonDataStr.append(NEWLINE + TAB + "		for (Method method : methods) {");
		jsonDataStr.append(NEWLINE + TAB + "			if (method.getName().equals(setMethodStr)) {");
		jsonDataStr.append(NEWLINE + TAB + "				if (typeName.equals(\"BigDecimal\")) {");
		jsonDataStr.append(NEWLINE + TAB + "					try {");
		jsonDataStr.append(NEWLINE + TAB + "						method.invoke(obj, new BigDecimal(pkValue));");
		jsonDataStr.append(NEWLINE + TAB + "					} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "						e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "					}");
		jsonDataStr.append(NEWLINE + TAB + "				} else if (typeName.equals(\"Short\")) {");
		jsonDataStr.append(NEWLINE + TAB + "					try {");
		jsonDataStr.append(NEWLINE + TAB + "						pkValue = (short) pkValue;");
		jsonDataStr.append(NEWLINE + TAB + "						method.invoke(obj, pkValue);");
		jsonDataStr.append(NEWLINE + TAB + "					} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "						e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "					}");
		jsonDataStr.append(NEWLINE + TAB + "				} else if (typeName.equals(\"Integer\")) {");
		jsonDataStr.append(NEWLINE + TAB + "					try {");
		jsonDataStr.append(NEWLINE + TAB + "						method.invoke(obj, pkValue);");
		jsonDataStr.append(NEWLINE + TAB + "					} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "						e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "					}");
		jsonDataStr.append(NEWLINE + TAB + "				} else if (typeName.equals(\"Long\")) {");
		jsonDataStr.append(NEWLINE + TAB + "					try {");
		jsonDataStr.append(NEWLINE + TAB + "						method.invoke(obj, new Long(pkValue + \"\"));");
		jsonDataStr.append(NEWLINE + TAB + "					} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "						e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "					}");
		jsonDataStr.append(NEWLINE + TAB + "				} else {");
		jsonDataStr.append(NEWLINE + TAB + "					try {");
		jsonDataStr.append(NEWLINE + TAB + "						method.invoke(obj, pkValue + \"\");");
		jsonDataStr.append(NEWLINE + TAB + "					} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "						e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "					}");
		jsonDataStr.append(NEWLINE + TAB + "				}");
		jsonDataStr.append(NEWLINE + TAB + "				break;");
		jsonDataStr.append(NEWLINE + TAB + "			}");
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
		jsonDataStr.append(NEWLINE + TAB + "	return pkValue;");
		jsonDataStr.append(NEWLINE + TAB + "}");
		jsonDataStr.append(NEWLINE);
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
		jsonDataStr.append(NEWLINE);
		jsonDataStr.append(NEWLINE + TAB + "// 删除(返回数量)");
		jsonDataStr.append(NEWLINE + TAB + "private static int deleteOp(Object obj) {");
		jsonDataStr.append(NEWLINE + TAB + "	int count = 0;");
		jsonDataStr.append(NEWLINE + TAB + "	String className = obj.getClass().getName();");
		jsonDataStr.append(NEWLINE + TAB + "	String tempClzName = obj.getClass().getSimpleName().toUpperCase();");
		jsonDataStr.append(NEWLINE + TAB + "	Class cls = null;");
		jsonDataStr.append(NEWLINE + TAB + "	try {");
		jsonDataStr.append(NEWLINE + TAB + "		cls = Class.forName(className);");
		jsonDataStr.append(NEWLINE + TAB + "	} catch (ClassNotFoundException e) {");
		jsonDataStr.append(NEWLINE + TAB + "		e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	Method[] methods = cls.getMethods();");
		jsonDataStr.append(NEWLINE + TAB + "	// 获取类的主键");
		jsonDataStr.append(NEWLINE + TAB + "	Set<String> pkInfo = tablePk.get(tempClzName);");
		jsonDataStr.append(NEWLINE + TAB + "	Map<Method, String> pkGetMethod = new HashMap<Method, String>();");
		jsonDataStr.append(NEWLINE + TAB + "	for (String pk : pkInfo) {");
		jsonDataStr.append(NEWLINE + TAB
				+ "		String getMethodStr = \"get\" + pk.substring(0, 1).toUpperCase() + pk.substring(1);");
		jsonDataStr.append(NEWLINE + TAB + "		Method getMethod = null;");
		jsonDataStr.append(NEWLINE + TAB + "		for (Method method : methods) {");
		jsonDataStr.append(NEWLINE + TAB + "			if (method.getName().equals(getMethodStr)) {");
		jsonDataStr.append(NEWLINE + TAB + "				getMethod = method;");
		jsonDataStr.append(NEWLINE + TAB + "			}");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "		String getMethodValue = \"\";");
		jsonDataStr.append(NEWLINE + TAB + "		try {");
		jsonDataStr.append(NEWLINE + TAB + "			getMethodValue = getMethod.invoke(obj, null).toString();");
		jsonDataStr.append(NEWLINE + TAB + "		} catch (Exception e) {");
		jsonDataStr.append(NEWLINE + TAB + "			e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "		pkGetMethod.put(getMethod, getMethodValue);");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	// 遍历现有数据根据主键get的值,删除数据");
		jsonDataStr.append(NEWLINE + TAB + "	List list = mockData.get(tempClzName);");
		jsonDataStr.append(NEWLINE + TAB + "	Iterator<Object> it = list.iterator();");
		jsonDataStr.append(NEWLINE + TAB + "	while (it.hasNext()) {");
		jsonDataStr.append(NEWLINE + TAB + "		Object object = it.next();");
		jsonDataStr.append(NEWLINE + TAB + "		boolean isEqual = true;");
		jsonDataStr.append(NEWLINE + TAB + "		Iterator<Method> methodit = pkGetMethod.keySet().iterator();");
		jsonDataStr.append(NEWLINE + TAB + "		while (methodit.hasNext()) {");
		jsonDataStr.append(NEWLINE + TAB + "			Method getMethod = (Method) methodit.next();");
		jsonDataStr.append(NEWLINE + TAB + "			String getMethodValue = pkGetMethod.get(getMethod);");
		jsonDataStr.append(NEWLINE + TAB + "			String tempVal = null;");
		jsonDataStr.append(NEWLINE + TAB + "			try {");
		jsonDataStr.append(NEWLINE + TAB + "				tempVal = getMethod.invoke(object, null).toString();");
		jsonDataStr.append(NEWLINE + TAB
				+ "			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {");
		jsonDataStr.append(NEWLINE + TAB + "				e.printStackTrace();");
		jsonDataStr.append(NEWLINE + TAB + "			}");
		jsonDataStr.append(NEWLINE + TAB + "			if (!tempVal.equals(getMethodValue)) { // 删除");
		jsonDataStr.append(NEWLINE + TAB + "				isEqual = false;");
		jsonDataStr.append(NEWLINE + TAB + "				break;");
		jsonDataStr.append(NEWLINE + TAB + "			}");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "		if (isEqual) {");
		jsonDataStr.append(NEWLINE + TAB + "			it.remove();");
		jsonDataStr.append(NEWLINE + TAB + "			count++;");
		jsonDataStr.append(NEWLINE + TAB + "		}");
		jsonDataStr.append(NEWLINE + TAB + "	}");
		jsonDataStr.append(NEWLINE + TAB + "	return count;");
		jsonDataStr.append(NEWLINE + TAB + "}");

		return mockField.toString() + testClassInstnceStr.toString() + beforeClassStr.toString()
				+ mockAllDAOStr.toString() + testBodyStr.toString() + afterClassStr.toString() + jsonDataStr.toString();
	}

	/**
	 * 根据方法返回值,随机生成返回数据.<br>
	 * <b>注意List<>形式目前只支持一层嵌套.</b>
	 * 
	 * @param method
	 * @return
	 */
	private static String genReturnData(String table, String methodName) {
		StringBuilder reStr = new StringBuilder();
		List<PkInfo> pk = GenDAOMockCodeTool.tableInfo.get(genFieldNameFromColumn(table));
		if (pk == null) {
			pk = new ArrayList<PkInfo>();
		}
		if (methodName.equals("deleteByPrimaryKey")) {
			reStr.append(TAB4 + genClassNameFromTable(table) + " obj= new " + genClassNameFromTable(table) + "();"
					+ NEWLINE);
			for (PkInfo pkInfo : pk) {
				String pkField = pkInfo.getColName();
				String pkType = pkInfo.getColType();
				if (pkType.equals("BigDecimal")) {
					reStr.append(TAB4 + "obj.set" + upperFirstLetter(pkField) + "(new BigDecimal(" + pkField + "));"
							+ NEWLINE);
				} else {
					reStr.append(TAB4 + "obj.set" + upperFirstLetter(pkField) + "(" + pkField + ");" + NEWLINE);
				}
			}
			reStr.append(TAB4 + "return deleteOp(obj);" + NEWLINE);
		} else if (methodName.equals("insert")) {
			reStr.append(TAB4 + "return insertOp(record);" + NEWLINE);
		} else if (methodName.equals("insertSelective")) {
			reStr.append(TAB4 + "return insertOp(record);" + NEWLINE);
		} else if (methodName.equals("selectByPrimaryKey")) {
			reStr.append(TAB4 + genClassNameFromTable(table) + " obj= new " + genClassNameFromTable(table) + "();"
					+ NEWLINE);
			for (PkInfo pkInfo : pk) {
				String pkField = pkInfo.getColName();
				String pkType = pkInfo.getColType();
				if (pkType.equals("BigDecimal")) {
					reStr.append(TAB4 + "obj.set" + upperFirstLetter(pkField) + "(new BigDecimal(" + pkField + "));"
							+ NEWLINE);
				} else {
					reStr.append(TAB4 + "obj.set" + upperFirstLetter(pkField) + "(" + pkField + ");" + NEWLINE);
				}
			}
			reStr.append(TAB4 + "return (" + genClassNameFromTable(table) + ")selectOp(obj);" + NEWLINE);
		} else if (methodName.equals("updateByPrimaryKeySelective")) {
			reStr.append(TAB4 + "return updateOp(record);" + NEWLINE);

		} else if (methodName.equals("updateByPrimaryKey")) {
			reStr.append(TAB4 + "return updateOp(record);" + NEWLINE);
		}

		return reStr.toString();
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName 包名
	 * @param isRecursion 是否遍历子包
	 * @return 类的完整名称
	 */
	public static List<Map<String, String>> getClassName(String packageName, boolean isRecursion) {
		List<Map<String, String>> classNames = null;
		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		URL url2 = GenDAOMockCodeTool.class.getResource("");
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
	 * @param filePath    文件路径
	 * @param isRecursion 是否遍历子包
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
					String[] _split = allPath.split("/");
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

	public static void createMockFile(String[] tables) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String path = loader.getResource("").getPath();
		String content = "";
		content = genJavaContent(tables);
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
		FileWriter filterWriter = null;
		try {
			int lastLength = path.lastIndexOf("/");
			// 得到文件夹目录
			String dir = path.substring(0, lastLength);
			// 先创建文件夹
			if (CreateMultilayerFile(dir) == true) {
				Long tt = System.currentTimeMillis();
				path = path + "mock-" + tt + ".java";
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.createNewFile();
				}
				filterWriter = new FileWriter(filePath);
				filterWriter.write(content);
				filterWriter.close();
				System.out.println("生成mock代码文件" + path);
			}
		} catch (Exception e) {
			System.out.println("新建java文件操作出错: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				filterWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	static class PkInfo {
		String colName;
		String colType;

		public String getColName() {
			return colName;
		}

		public void setColName(String colName) {
			this.colName = colName;
		}

		public String getColType() {
			return colType;
		}

		public void setColType(String colType) {
			this.colType = colType;
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

	private static String genData(String[] tables) throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		Map<String, JSONArray> data = new HashMap<String, JSONArray>();
		for (int i = 0; i < tables.length; i++) {
			Statement st = null;
			try {
				st = conn.createStatement();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 获取表的主键
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rsPk = dbmd.getPrimaryKeys(null, TABLE_SCHEM, tables[i]); // 要将表名转为大写才能正确取出主键来

			List<PkInfo> pkList = new ArrayList<PkInfo>();
			while (rsPk.next()) {
				String pkName = rsPk.getString("COLUMN_NAME");
				PkInfo pkInfo = new PkInfo();
				pkInfo.setColName(genFieldNameFromColumn(pkName));
				pkList.add(pkInfo);
			}
			rsPk.close();
			String sql = getOraclePageList("select * from " + tables[i], 1L, RESULT_COUNT);
			ResultSet rs = null;
			try {
				rs = st.executeQuery(sql);
				// 设置主键的类型
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int kk = 1; kk <= rsmd.getColumnCount(); kk++) {
					for (int j = 0; j < pkList.size(); j++) {
						if (genFieldNameFromColumn(rsmd.getColumnName(kk)).equals(pkList.get(j).getColName())) {
							int type = rsmd.getColumnType(kk);

							if (type == 2) {
								// 默认情况下的转换规则为：
								// 如果精度>0或者长度>18，就会使用java.math.BigDecimal
								// 如果精度=0并且10<=长度<=18，就会使用java.lang.Long
								// 如果精度=0并且5<=长度<=9，就会使用java.lang.Integer
								// 如果精度=0并且长度<5，就会使用java.lang.Short
								int size = rsmd.getPrecision(kk);
								int pre = rsmd.getScale(kk);
								if (pre > 0) {
									pkList.get(j).setColType("BigDecimal");
								} else {
									if (size < 5) {
										pkList.get(j).setColType("Short");
									} else if (size < 10) {
										pkList.get(j).setColType("Integer");
									} else if (size < 19) {
										pkList.get(j).setColType("Long");
									} else {
										pkList.get(j).setColType("BigDecimal");
									}
								}
							} else if (type == 12) {
								pkList.get(j).setColType("String");
							} else if (type == 93) {
								pkList.get(j).setColType("Date");
							} else {
								pkList.get(j).setColType("String");
							}
						}
					}
				}
				GenDAOMockCodeTool.tableInfo.put(genFieldNameFromColumn(tables[i]), pkList);
				try {
					JSONArray jsonAry = resultSetToJsonObject(rs);
					data.put(genFieldNameFromColumn(tables[i]), jsonAry);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				rs.close();
				st.close();
			} catch (SQLException e1) {
				System.out.println("构建模拟数据错误:");
				System.out.println(TAB + "无法执行:" + sql);
				try {
					rs.close();
				} catch (Exception e) {
				}
				try {
					st.close();
				} catch (Exception e) {
				}
				continue;
			}
		}
		conn.close();
		String jsonData = JSON.toJSONString(data);
		return jsonData;
	}

	private static JSONArray resultSetToJsonObject(ResultSet rs) throws SQLException, JSONException {
		JSONArray jsonAry = new JSONArray();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(genFieldNameFromColumn(columnName), value);
			}
			jsonAry.add(jsonObj);
		}
		return jsonAry;
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

	private static String genClassNameFromTable(String tableName) {
		String temp = genFieldNameFromColumn(tableName);
		return upperFirstLetter(temp);
	}

	private static String upperFirstLetter(String temp) {
		return temp.substring(0, 1).toUpperCase() + temp.substring(1);
	}
}