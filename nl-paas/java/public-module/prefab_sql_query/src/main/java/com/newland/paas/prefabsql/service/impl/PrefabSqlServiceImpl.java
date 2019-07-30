package com.newland.paas.prefabsql.service.impl;

import com.newland.paas.dataaccessmodule.dao.PrefabSqlMapper;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.prefabsql.db.dbutils.DbUtilsOpe;
import com.newland.paas.prefabsql.db.dbutils.ResultSetHandler;
import com.newland.paas.prefabsql.db.domain.DBParam;
import com.newland.paas.prefabsql.pojo.PrefabSql;
import com.newland.paas.prefabsql.pojo.PrefabSqlGroupParam;
import com.newland.paas.prefabsql.pojo.PrefabSqlInparam;
import com.newland.paas.prefabsql.pojo.PrefabSqlStatement;
import com.newland.paas.prefabsql.service.NewInParamProcessor;
import com.newland.paas.prefabsql.service.PrefabSqlManager;
import com.newland.paas.prefabsql.service.PrefabSqlService;
import com.newland.paas.prefabsql.util.PrefabSqlUtils;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class PrefabSqlService 数据访问统一入口
 */
@Service("prefabSqlService")
public class PrefabSqlServiceImpl implements PrefabSqlService{
	private static final Log log = LogFactory.getLogger(PrefabSqlServiceImpl.class);

	@Autowired
	PrefabSqlManager prefabSqlManager ;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PrefabSqlMapper prefabSqlMapper;

	public static void groupParamCheck(Map requestMap, PrefabSql prefabSql) throws SQLException {
		List<PrefabSqlGroupParam> groupParamList = prefabSql.getGroupParamList();
		List<PrefabSqlInparam> inparamList = prefabSql.getInparamList();
		Map<String, List<String>> inparamGroup = new HashMap<String, List<String>>();
		for (PrefabSqlInparam prefabSqlInparam : inparamList) {
			String groupId = prefabSqlInparam.getGroupId();
			if (!PrefabSqlUtils.isNVL(groupId)) {
				if (inparamGroup.containsKey(groupId)) {
					inparamGroup.get(groupId).add(prefabSqlInparam.getParamName());
				} else {
					List<String> list = new ArrayList<String>();
					list.add(prefabSqlInparam.getParamName());
					inparamGroup.put(groupId, list);
				}
			}
		}
		for (PrefabSqlGroupParam prefabSqlGroupParam : groupParamList) {
			int groupFlag = prefabSqlGroupParam.getGroupFlag();
			int maxEmptyNum = prefabSqlGroupParam.getMaxEmptyNum();
			int minUnemptyNum = prefabSqlGroupParam.getMinUnemptyNum();
			String groupId = prefabSqlGroupParam.getGroupId();
			List<String> paramList = inparamGroup.get(groupId);
			if (groupFlag == PrefabSqlGroupParam.LIMIT_FLAG && paramList != null) {
				int emptyNum = 0;
				int unemtyNum = 0;
				for (String paramName : paramList) {
					Object paramValue = requestMap.get(paramName);
					if (PrefabSqlUtils.isNvlObj(paramValue)) {
						emptyNum++;
					} else {
						unemtyNum++;
					}
				}
				log.debug( "", "groupId=" + groupId + ",maxEmptyNum=" + maxEmptyNum
                        + ",emptyNum=" + emptyNum + ",minUnemptyNum=" + minUnemptyNum + ",unemtyNum="
                        + unemtyNum);
				if ((maxEmptyNum != 0 && emptyNum > maxEmptyNum)
						|| (minUnemptyNum != 0 && unemtyNum < minUnemptyNum)) {
					StringBuilder sb = new StringBuilder("SQLID=");
					sb.append(String.valueOf(prefabSql.getSqlId()))
						.append( " , GROUPID").append(groupId).append(" , detail : ").append("maxEmptyNum=")
						.append(maxEmptyNum).append(",emptyNum=").append(emptyNum)
						.append(",minUnemptyNum=").append(minUnemptyNum).append(",unemtyNum=")
						.append(unemtyNum);
					throw new SQLException(sb.toString());
				}
			}
		}
	}

	@Override
	public <T> T executeQuery(final String sqlId, final Map<String, Object> requestMap,
			final ResultSetHandler<T> rsh) throws SQLException{
		logInputParam(sqlId, requestMap);// 输入日志跟踪
		final PrefabSql prefabSql = getPrefabSql(sqlId);// 获取预制查询配置
		groupParamCheck(requestMap, prefabSql);// 分组参数限制检查
		final PrefabSqlStatement prefabSqlStatement = prefabSql.getStatmentBySeq("1");

		String sqlStatement = prefabSqlStatement.getSqlStatement();
		Map<String, PrefabSqlInparam> inparamMap = prefabSql.getInparamMap();
		NewInParamProcessor inParamProcessor = new NewInParamProcessor();
		String prepareSql = inParamProcessor.processInParam(sqlStatement, requestMap, inparamMap);
		DBParam dbParam = inParamProcessor.getParam();
		Connection conn = DataSourceUtils.getConnection(dataSource);

		try {
			return DbUtilsOpe.query(conn, sqlId, prepareSql, rsh, dbParam.getParamValueObjs(), prefabSqlStatement);
		}finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	@Override
	public ResultPageData executeQueryPage(String sqlId, final Map<String, Object> requestMap, PageInfo pageParams)
			throws SQLException {
		logInputParam(sqlId, requestMap);// 输入日志跟踪
		PrefabSql prefabSql = getPrefabSql(sqlId);// 获取预制查询配置
		groupParamCheck(requestMap, prefabSql);// 分组参数限制检查
		PrefabSqlStatement prefabSqlStatement = prefabSql.getStatmentBySeq("1");
		String sqlStatement = prefabSqlStatement.getSqlStatement();
		Map<String, PrefabSqlInparam> inparamMap = prefabSql.getInparamMap();
		NewInParamProcessor inParamProcessor = new NewInParamProcessor();
		String prepareSql = inParamProcessor.processInParam(sqlStatement, requestMap, inparamMap);
		DBParam dbParam = inParamProcessor.getParam();
		Connection conn = DataSourceUtils.getConnection(dataSource);

		try {
			return DbUtilsOpe.queryFilter(conn, sqlId, prepareSql, dbParam.getParamValueObjs(), pageParams,requestMap, prefabSqlStatement);
		} finally {
				DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	@Override
	public List<com.newland.paas.dataaccessmodule.model.PrefabSql> findAll() {
		return prefabSqlMapper.findAll();
	}

	/*public ResultConnect executeQuery(String sqlId, Map<String, Object> requestMap)
			throws PrefabSqlException, DbRouteException, CsfDbException {
		logInputParam(sqlId, requestMap);// 输入日志跟踪
		PrefabSql prefabSql = getPrefabSql(sqlId);// 获取预制查询配置
		groupParamCheck(requestMap, prefabSql);// 分组参数限制检查
		PrefabSqlStatement prefabSqlStatement = prefabSql.getStatmentBySeq("1");
		String sqlStatement = prefabSqlStatement.getSqlStatement();
		Map<String, PrefabSqlInparam> inparamMap = prefabSql.getInparamMap();
		NewInParamProcessor inParamProcessor = new NewInParamProcessor();
		String prepareSql = inParamProcessor.processInParam(sqlStatement, requestMap, inparamMap);
		DBParam dbParam = inParamProcessor.getParam();
		IDbOpe dbOpe = new DbUtilsOpe();
		//LOG_CACHE.debug(Constant.LOG_CSF_DBSQL, "执行sql语句prepareSql="
           //     + prepareSql);
		ResultConnect query = dbOpe.query(sqlId, prepareSql, dbParam.getParamValueObjs(),requestMap,prefabSqlStatement);
		return query;
	}*/

	private PrefabSql getPrefabSql(String sqlId) throws SQLException {
		PrefabSql prefabSql = prefabSqlManager.getPrefabSqlBySqlId(sqlId);
		if (prefabSql == null) {
			throw new SQLException("can not find prefab_sql where sql_id=" + sqlId);
		}
		return prefabSql;
	}

	private static void logInputParam(String sqlId, Map<String, Object> requestMap) {
		StringBuilder input = new StringBuilder();
		input.append("输入参数:sqlId=").append(sqlId).append(";");
		for (String key : requestMap.keySet()) {
			Object value = requestMap.get(key);
			input.append(key).append("=").append(value).append(";");
		}
		log.info("", input.toString());
	}

	/*private static void logInputParamList(String sqlId, List<Map<String, Object>> listRequestMap) {
		StringBuilder input = new StringBuilder();
		input.append("输入:sqlId=").append(sqlId).append(";");
		input.append("输入参数:");
		for (Map<String, Object> requestMap : listRequestMap) {
			for (String key : requestMap.keySet()) {
				Object value = requestMap.get(key);
				input.append(key).append("=").append(value).append(";");
			}
			input.append("==");
		}
		log.info("", input.toString());
	}*/

	/*private static void logInputParam(String sqlId, Map<String, Object> requestMap, Page page) {
		StringBuilder input = new StringBuilder();
		input.append("输入参数:sqlId=").append(sqlId).append(";");
		for (String key : requestMap.keySet()) {
			Object value = requestMap.get(key);
			input.append(key).append("=").append(value).append(";");
		}
		input.append("startIndex=").append(page.getStartIndex()).append(";").append("endIndex=")
				.append(page.getEndIndex()).append(";").append("PageSize=").append(page.getPageSize())
				.append(";");
		//LOG_CACHE.info(Constant.LOG_CSF_DBSQL, input.toString());
	}*/
}
