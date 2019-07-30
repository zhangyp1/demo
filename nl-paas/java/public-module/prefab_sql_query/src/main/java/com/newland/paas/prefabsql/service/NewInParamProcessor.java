package com.newland.paas.prefabsql.service;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.prefabsql.db.domain.DBParam;
import com.newland.paas.prefabsql.pojo.PrefabSqlInparam;
import com.newland.paas.prefabsql.util.PrefabSqlUtils;

/**
 * 新sql模板输入参数处理类
 * 
 * @author linju
 * 
 */
public class NewInParamProcessor {

	private DBParam param = new DBParam();
	//rivate static LogObj logCache = LogObjFactory.getLogObj(NewInParamProcessor.class);

	public DBParam getParam() {
		return param;
	}

	/*public static void main(String[] args) {
		String sqlStatement = "select * from {table} where {column} = :name";
		Map requestMap = new HashMap();
		requestMap.put("table", "select * from USER_INFO");
		requestMap.put("column", "name");
		for (Object key : requestMap.keySet()) {
			Object value = requestMap.get(key);
			if (value != null) {
				value = value.toString().replaceAll(" ", "");
				sqlStatement = sqlStatement.replaceAll("\\{" + key + "\\}", value.toString());
			}
		}
		String sql = "[to_date(theme_start_date,'yyyymmdd')]";
		System.out.println("result="
				+ sql.replaceAll("\\[to_date\\(theme_start_date,\\'yyyymmdd\\'\\)\\]", "theme_start_date"));
		System.out.println(sqlStatement);
		sqlStatement = "update mm_theme set theme_start_date=[to_date(:theme_start_date,'yyyymmdd')],name=[name],age=[age],create_time=[to_date(:create_time,'yyyymmdd')] where theme_id=591110021654";
		requestMap.put("name", "linju");
		requestMap.put("b", "11111111111");
		requestMap.put("create_time", "20150612");
		Pattern p = Pattern.compile("\\[.*?\\]");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(sqlStatement);
		while (m.find()) {// 遍历找到的所有大括号
			String param = m.group().replace("[", "").replace("]", "");// 去掉括号
			String fullParam = param;
			if (param.indexOf(":") != -1) {
				param = PrefabSqlUtils.getParam(param);
			}
			Object object = requestMap.get(param);
			if (object == null) {
				sqlStatement = sqlStatement.replace("[" + fullParam + "]", param);
			} else {
				if (fullParam.indexOf(":") != -1) {
					sqlStatement = sqlStatement.replace("[" + fullParam + "]", fullParam);
				} else {
					sqlStatement = sqlStatement.replace("[" + param + "]", ":" + param);
				}

			}
			// System.out.println(param);
		}
		System.out.println(sqlStatement);
		sqlStatement = "(#a.create_user_id in " + "(select b.operator_id" + "from operator b"
				+ "where b.name like :search_param) #) or";

		p = Pattern.compile("\\#.*?\\#");
		m = p.matcher(sqlStatement);
		while (m.find()) {
			String group = m.group();
			System.out.println(group);
			List<String> paramNames = PrefabSqlUtils.getParamNames(group);
			boolean emptyFlag = true;
			for (String string : paramNames) {
				System.out.println(string);
				Object object = requestMap.get(string);
				if (!PrefabSqlUtils.isNvlObj(object)) {
					emptyFlag = false;
				}
			}
			if (emptyFlag) {
				sqlStatement = sqlStatement.replaceFirst("\\#[^#^#]*\\#", "1=1");
				System.out.println(sqlStatement);
			} else {
				sqlStatement = sqlStatement.replaceAll("\\#", "");
			}
		}
		System.out.println(sqlStatement);
		String sing = "a1!@~%^*1a+-|";
		if (sing.indexOf("$") != -1) {
			String[] split = sing.split("\\$");
			sing = "";
			for (int i = 0; i < split.length; i++) {
				sing += split[i];
				if (i < split.length - 1) {
					sing += "\\$";
				}
			}
		}
		System.out.println(sing);
		sql = "select * from user where name = ?";
		sql = sql.replaceFirst("\\?", "'" + sing + "'");
		System.out.println(sql);
	}*/
	
	/**
	 * @param sqlStatement
	 * @param requestMap
	 * @param inparamMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected String processPrepareSql(String sqlStatement, Map requestMap, 
			Map<String, PrefabSqlInparam> inparamMap) throws SQLException {
		String prepareSql = "";
		long sqlId = 0;
		boolean isColonEquals = false;
		sqlStatement = sqlStatement.replaceAll("\n", " ");// 回车替换空格
		//logCache.debug(Constant.LOG_CSF_DBSQL, "原始sqlStatement=" + sqlStatement);
		
		// add by 2017-6 处理:=
		if (sqlStatement.indexOf(":=") != -1) {
			isColonEquals = true;
			sqlStatement = StringUtils.replace(sqlStatement, ":=", "^isColonEquals^");
			//logCache.debug(Constant.LOG_CSF_DBSQL, "替换:=后 语句为：" + sqlStatement);
		}
		// sqlStatement = sqlStatement.toLowerCase();
		// 替换花挂号内容
		for (Object key : requestMap.keySet()) {
			Object value = requestMap.get(key);
			if (value != null && value.toString().trim().length() != 0) {
				String replaceValue = value.toString().replaceAll(" ", "");
				sqlStatement = sqlStatement.replaceAll("\\{" + key + "\\}", replaceValue);
			}
		}
		// 设置动态更新内容
		Pattern p = Pattern.compile("\\[.*?\\]");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(sqlStatement);
		while (m.find()) {// 遍历找到的所有大括号
			String param = m.group().replace("[", "").replace("]", "");// 去掉括号
			String fullParam = param;
			if (param.indexOf(":") != -1) {
				param = PrefabSqlUtils.getParam(param);
			}
			Object object = requestMap.get(param);
			if (object == null) {
				sqlStatement = sqlStatement.replace("[" + fullParam + "]", param);
			} else {
				if (fullParam.indexOf(":") != -1) {
					sqlStatement = sqlStatement.replace("[" + fullParam + "]", fullParam);
				} else {
					sqlStatement = sqlStatement.replace("[" + param + "]", ":" + param);
				}

			}
			// System.out.println(param);
		}
		// 替换子查询动态条件##包起来的
		p = Pattern.compile("\\#.*?\\#");
		m = p.matcher(sqlStatement);
		while (m.find()) {
			String group = m.group();
			// System.out.println(group);
			List<String> paramNames = PrefabSqlUtils.getParamNames(group);
			boolean emptyFlag = true;
			for (String string : paramNames) {
				// System.out.println(string);
				Object object = requestMap.get(string);
				if (!PrefabSqlUtils.isNvlObj(object)) {
					emptyFlag = false;
				}
			}
			if (emptyFlag) {
				sqlStatement = sqlStatement.replaceFirst("\\#[^#^#]*\\#", "1=1");
				// System.out.println(sqlStatement);
			} else {
//				sqlStatement = sqlStatement.replaceAll("\\#", "");
				sqlStatement = sqlStatement.replaceFirst("\\#", "");
				sqlStatement = sqlStatement.replaceFirst("\\#", "");
			}
		}
		sqlStatement = sqlStatement.replaceAll(" WHERE ", " where ").replaceAll(" AND ", " and ")
				.replaceAll(" BETWEEN ", " between ");

		try {
			StringBuffer mainSql = new StringBuffer();
			StringBuffer betweenSql = new StringBuffer();
			String[] whereSplit = sqlStatement.split(" where ");// 按where分割
			for (int i = 0; i < whereSplit.length; i++) {
				String[] andSplit = whereSplit[i].split(" and ");// 再按and分割
				StringBuffer whereSql = new StringBuffer();
				for (int j = 0; j < andSplit.length; j++) {
					if (andSplit[j].indexOf(" between ") != -1) {// between语句暂时不处理
						betweenSql.append(andSplit[j]);
						continue;
					}
					if (betweenSql.length() != 0) {
						betweenSql.append(" and ").append(andSplit[j]);
						andSplit[j] = betweenSql.toString();// between语句要连上一句才能完整处理
						betweenSql.setLength(0);
					}
					if (andSplit[j].indexOf(":") != -1) {
						String paramName = PrefabSqlUtils.getParam(andSplit[j]);// :name这样的参数
						if (paramName.indexOf(":") == -1) {
							PrefabSqlInparam prefabSqlInparam = inparamMap.get(paramName);
							Object paramValue = requestMap.get(paramName);
							boolean replaceEmpty = false;
							if (prefabSqlInparam == null) {
								// LogCache.debug(Constant.LOG_CSF_DBSQL,"没有配置输入参数paramName="
								// + paramName);
								replaceEmpty = PrefabSqlUtils.isNvlObj(paramValue);
							} else {
								int canEmpty = prefabSqlInparam.getCanEmpty();
								replaceEmpty = canEmpty == PrefabSqlInparam.EMPTY_FLAG && PrefabSqlUtils.isNvlObj(paramValue);
							}
							if (replaceEmpty && andSplit[j].trim().startsWith("(")) {
								andSplit[j] = PrefabSqlUtils.replaceEmptyParam(andSplit[j]);
								// if(andSplit[j].indexOf("between")==-1){//不是between语句直接替换
								// andSplit[j] =
								// PrefabSqlUtils.replaceEmptyParam(andSplit[j]);
								// }else{
								// if(andSplit[j].trim().startsWith("(")){//是between语句且是用()刮起来，则替换为空
								// andSplit[j] =
								// PrefabSqlUtils.replaceEmptyParam(andSplit[j]);
								// }
								// }
							}
						}
					}
					whereSql.append(andSplit[j]);
					if (j != andSplit.length - 1) {
						whereSql.append(" and ");
					}
				}
				mainSql.append(whereSql.toString());
				if (i != whereSplit.length - 1) {
					mainSql.append(" where ");
				}
			}
			sqlStatement = mainSql.toString();
			List<String> paramNameList = PrefabSqlUtils.getParamNames(sqlStatement);
			for (String paramName : paramNameList) {
				Object paramValue = requestMap.get(paramName);
				PrefabSqlInparam prefabSqlInparam = inparamMap.get(paramName);// 数据库中配置的输入参数
//				if(paramValue == null 
//						|| ((paramValue instanceof Collection) && ((Collection) paramValue).size() == 0) ) {
//					// in表达式 替换 1=1 
//					System.out.println("=========paramName====:"+paramName);
//					System.out.println(sqlStatement);
//					
//					String isInExprSql = PrefabSqlUtils.replaceInExprSqlNullParam(sqlStatement, paramName);
//					if(!sqlStatement.equals(isInExprSql)) {
////						System.out.println("=========paramName=sql===:"+isInExprSql);
//						sqlStatement = isInExprSql;
//						continue;
//					}
//				}
				int dataType = getDataType(prefabSqlInparam, paramValue);
				if (dataType >= PrefabSqlInparam.IN_CONDITION) {// in条件处理
					
					sqlStatement = PrefabSqlUtils.replaceInParamNew(sqlStatement, paramName, paramValue);
					if ((paramValue instanceof Collection) && ((Collection) paramValue).size() > 0) {// 传入参数是集合
						Object value = ((Collection) paramValue).iterator().next();
						dataType = getDataType(prefabSqlInparam, value);
						for (Object v : (Collection) paramValue) {
							param.addParam(PrefabSqlUtils.convertValue(v.toString(), dataType),
									PrefabSqlUtils.toDataType(dataType));
						}
					} else if (paramValue instanceof String) {
						String[] valueSplit = ((String) paramValue).split(",");
						dataType = 3;
						if (prefabSqlInparam != null) {
							dataType = prefabSqlInparam.getDataType();
							String type = String.valueOf(dataType);
							if (type.length() == 2) {
								dataType = Integer.parseInt(type.substring(1));
								// System.out.println("dataType=" + dataType);
							}
						}
						for (String string : valueSplit) {
							param.addParam(PrefabSqlUtils.convertValue(string, dataType),
									PrefabSqlUtils.toDataType(dataType));
						}
					}
				} else {
					if (sqlStatement.indexOf(":" + paramName) != -1) {
						if (paramValue != null) {
							param.addParam(PrefabSqlUtils.convertValue(paramValue, dataType),
									PrefabSqlUtils.toDataType(dataType));
						} else {
							param.addParam("", DBParam.STRING_TYPE);
						}
					} else {
						//logCache.debug(Constant.LOG_CSF_DBSQL, "传进来为空的参数paramName=" + paramName);
					}
				}
			}

			prepareSql = param.prepareSql(sqlStatement);
			
			//add by 2017-6
			if (isColonEquals){
				//StringUtils.replace(prepareSql, "^isColonEquals^", ":=");
				prepareSql = StringUtils.replace(prepareSql, "^isColonEquals^", ":=");
				//logCache.debug(Constant.LOG_CSF_DBSQL, "恢复:= 语句为：" + prepareSql);
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder("SQLID = ");
			sb.append(sqlId).append(" , inner exception : ").append(e.getMessage());
			throw new SQLException(sb.toString());
		}
			return prepareSql;
	}
	
	public String processInParam(String sqlStatement, Map requestMap, Map<String, PrefabSqlInparam> inparamMap)
			throws SQLException {
		String prepareSql = "";
		long sqlId = 0;
		try {
			prepareSql = processPrepareSql(sqlStatement, requestMap,inparamMap);
			replaceParam(prepareSql);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder("SQLID = ");
			sb.append(sqlId).append(" , inner exception : ").append(e.getMessage());
			throw new SQLException(sb.toString());
		}
		return prepareSql;
	}
	
	// add by 2017-6
	protected String getReplaceParm(String prepareSql) throws Exception{
		String sql = prepareSql;
		Object[] param2 = param.getParam();
		for (Object object : param2) {
			Object[] value = (Object[]) object;
			if (value[0] instanceof String) {
				String paramValue = (String) value[0];
				StringBuffer sbParamValue = new StringBuffer((String) value[0]);
				if (paramValue.indexOf("$") != -1) {
					String[] split = paramValue.split("\\$");
					//paramValue = "";
					sbParamValue = new StringBuffer("");
					for (int i = 0; i < split.length; i++) {
						//paramValue += split[i];
						sbParamValue = sbParamValue.append(split[i]);
						if (i < split.length - 1) {
							//paramValue += "\\$";
							sbParamValue = sbParamValue.append("\\$");
						}
					}
				}
				// sql = sql.replaceFirst("\\?", "'" + paramValue + "'");
				sql = sql.replaceFirst("\\?", "'" + sbParamValue + "'");
			} else {
				sql = sql.replaceFirst("\\?", value[0].toString());
			}
		}
		return sql;
	}
	
	protected String replaceParam(String prepareSql) throws Exception{
		String sql = getReplaceParm(prepareSql);
		//logCache.info(Constant.LOG_CSF_DBSQL, "条件为空处理和in条件处理后,可执行的sql=" + sql);
		return prepareSql;
	}

	/*protected String operExecutableSql(String sqlStatement, Map requestMap, Map<String, PrefabSqlInparam> inparamMap)throws PrefabSqlException {
		String prepareSql = "";
		String executableSql = "";
		logCache.debug(Constant.LOG_CSF_DBSQL, "operExecutableSql begin.");
		long sqlId = 0;
		try {
			prepareSql = processPrepareSql(sqlStatement, requestMap,inparamMap);
			executableSql = getReplaceParm(prepareSql);			
		} catch (Exception e) {
			ErrorParam param = new ErrorParam();
			param.addParam("SQLID", String.valueOf(sqlId));
			PrefabSqlException prefabSqlException = new PrefabSqlException(
					ErrorConstant.ERROR_PREFAB_IN_PARAM_EXCEPTION, param, e);
			logCache.error(Constant.LOG_CSF_DBSQL, e.getMessage(), -1, e);
			throw prefabSqlException;
		}
		logCache.debug(Constant.LOG_CSF_DBSQL, "operExecutableSql end.");
		return executableSql;
	}*/
	
	private int getDataType(PrefabSqlInparam prefabSqlInparam, Object paramValue) {
		int dataType = 3;
		if (prefabSqlInparam != null) {// 数据库有配置输入参数优先使用
			dataType = prefabSqlInparam.getDataType();
		} else {
			if (paramValue instanceof Double || paramValue instanceof Float) {
				dataType = 1;
			} else if (paramValue instanceof Long || paramValue instanceof Integer) {
				dataType = 2;
			} else if (paramValue instanceof Collection) {
				dataType = 9;
			} else {
				dataType = 3;
			}
		}
		return dataType;
	}

	public String processSqlStatementForBatch(String sqlStatement) {
		String prepareSql = "";
		sqlStatement = sqlStatement.replaceAll("\n", " ");// 回车替换空格
		//logCache.debug(Constant.LOG_CSF_DBSQL, "原始sqlStatement=" + sqlStatement);
		prepareSql = param.prepareSql(sqlStatement);
		//logCache.debug(Constant.LOG_CSF_DBSQL, "prepareSql=" + sqlStatement);
		return prepareSql;
	}

	// add by 2016-3
	/*public void processInParamForBatch(String sqlStatement, Map<String, Object> requestMap)
			throws PrefabSqlException {
		// String prepareSql = "";
		long sqlId = 0;
		List<String> paramNameList = PrefabSqlUtils.getParamNames(sqlStatement);
		
		for (String paramName : paramNameList) {
			Object paramValue = requestMap.get(paramName);
			int dataType = getDataType(null, paramValue);
			if (sqlStatement.indexOf(":" + paramName) != -1) {
				if (paramValue != null) {
					try {
						param.addParam(PrefabSqlUtils.convertValue(paramValue, dataType),
								PrefabSqlUtils.toDataType(dataType));
					} catch (Exception e) {
						ErrorParam param = new ErrorParam();
						param.addParam("SQLID", String.valueOf(sqlId));
						PrefabSqlException prefabSqlException = new PrefabSqlException(
								ErrorConstant.ERROR_PREFAB_IN_PARAM_EXCEPTION, param, e);
						logCache.error(Constant.LOG_CSF_DBSQL, e.getMessage(), -1, e);
						throw prefabSqlException;
					}
				} else {
					param.addParam("", DBParam.STRING_TYPE);
				}
			} else {
				logCache.debug(Constant.LOG_CSF_DBSQL, "传进来为空的参数paramName=" + paramName);
			}
		}
		param.prepareSql(sqlStatement);
		// prepareSql = param.prepareSql(sqlStatement);
	}*/
	

	public String logExecuteSql(String sqlStatement) {
		Object[] param2 = param.getParam();
		String sql = param.prepareSql(sqlStatement);
		for (Object object : param2) {
			Object[] value = (Object[]) object;
			if (value[0] instanceof String) {
				String paramValue = (String) value[0];
				if (paramValue.indexOf("$") != -1) {
					String[] split = paramValue.split("\\$");
					paramValue = "";
					for (int i = 0; i < split.length; i++) {
						paramValue += split[i];
						if (i < split.length - 1) {
							paramValue += "\\$";
						}
					}
				}
				sql = sql.replaceFirst("\\?", "'" + paramValue + "'");
			} else {
				sql = sql.replaceFirst("\\?", value[0].toString());
			}
		}
		//logCache.info(Constant.LOG_CSF_DBSQL, "条件为空处理和in条件处理后,可执行的sql=" + sql);
		return sql;
	}

}
