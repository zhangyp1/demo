package com.newland.paas.prefabsql.db.dbutils;

import com.newland.paas.prefabsql.db.dbutils.handlers.MapListHandler;
import com.newland.paas.prefabsql.pojo.PrefabSqlStatement;
import com.newland.paas.sbcommon.utils.PageParamsUtil;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;


/**
 * @author yyq
 *
 */
public class DbUtilsOpe {
	static public <T> T query( Connection conn, String sqlId, String sql, ResultSetHandler<T> rsh, Object[] param,
			PrefabSqlStatement prefabSqlStatement) throws SQLException {
		T result = null;
		try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
			int timeOut = prefabSqlStatement.getTimeout();
	        if(timeOut>0) {
	            stmt.setQueryTimeout(timeOut);
	        }

	        fillStatement(stmt, param);
            result = rsh.handle( stmt.executeQuery() );
		}

		return result;
	}

    private static void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {
        // check the parameter count, if we can
        ParameterMetaData pmd = stmt.getParameterMetaData();
        int stmtCount = pmd.getParameterCount();
        int paramsCount = params == null ? 0 : params.length;

        if (stmtCount != paramsCount) {
        	throw new SQLException("Wrong number of parameters: expected "
        			+ stmtCount + ", was given " + paramsCount);
        }

        // nothing to do here
        if (params == null ) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            }
            else {
                // VARCHAR works with many drivers regardless
                // of the actual column type. Oddly, NULL and
                // OTHER don't work with Oracle's drivers.
                int sqlType = Types.VARCHAR;
                sqlType = pmd.getParameterType(i + 1);
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

	public static ResultPageData queryFilter(Connection con, String sqlId, String sql, Object[] param, PageInfo pageInfo,
											 Map<String, Object> requestMap, PrefabSqlStatement prefabSqlStatement) throws SQLException {
		List result = null;
		if (param == null) {
			param = new Object[0];
		}

		Object[] indexs = new Object[param.length + 2];

		DatabaseMetaData dataMeta = con.getMetaData();
		String totalSql = "select count(*) as total from (" + sql + ") allcount ";
		MapListHandler totalHandler = new MapListHandler();
		List<Map<String,Object>> totalList = DbUtilsOpe.query(con, sqlId, totalSql, totalHandler, param, prefabSqlStatement);
		Map<String, Object> mapTotal = totalList.get(0);
        BigDecimal totalObject = (BigDecimal)(mapTotal.entrySet().iterator().next().getValue());
		ResultPageData resultPageData = new ResultPageData();
		pageInfo.setTotalRecord(totalObject.longValue());

		String newSql = "";
		StringBuilder newSqlTemp = new StringBuilder();
		if (dataMeta.getDriverName().indexOf("JDBC-ODBC Bridges") > -1) {
			newSql = "select row ? to ? * from (" + sql + ") ";
			System.arraycopy(param, 0, indexs, 2, param.length);
			indexs[0] = PageParamsUtil.getStartIndex(pageInfo);
			indexs[1] = PageParamsUtil.getEndIndex(pageInfo);
		} else if (dataMeta.getDriverName().toLowerCase().indexOf("postgresql") > -1) {
			indexs = new Object[param.length];
			System.arraycopy(param, 0, indexs, 0, param.length);
			newSqlTemp.append(sql);
			newSqlTemp.append(" limit ").append(pageInfo.getPageSize());
//			newSqlTemp.append(" offset ").append(PageParamsUtil.getStartIndex(pageParams) -1);
			newSql =  newSqlTemp.toString();
		}  else if (dataMeta.getDriverName().toLowerCase().indexOf("mysql") > -1) {
			indexs = new Object[param.length];
			System.arraycopy(param, 0, indexs, 0, param.length);
			newSqlTemp.append(sql);
//			newSqlTemp.append(" limit ").append(PageParamsUtil.getStartIndex(pageParams) -1);
			newSqlTemp.append(" , ").append(pageInfo.getPageSize());
			newSql =  newSqlTemp.toString();
		} else if (!"Oracle JDBC driver".equals(dataMeta.getDriverName())) {
			StringBuilder sb = new StringBuilder();
			sb.append("sql id = ");
			sb.append(sqlId);
			sb.append(" , invalid database driver name ");
			sb.append(dataMeta.getDriverName());
			throw new SQLException(sb.toString());
		} else {
			newSql = "SELECT * FROM (SELECT tt.*,ROWNUM As row_num FROM (" + sql + ") tt "
					+ "WHERE ROWNUM<=?) tab WHERE tab.row_num >=? ";
			System.arraycopy(param, 0, indexs, 0, param.length);
			indexs[indexs.length - 1] = PageParamsUtil.getStartIndex(pageInfo);
			indexs[indexs.length - 2] = PageParamsUtil.getEndIndex(pageInfo);
		}

		MapListHandler rsh = new MapListHandler();
		result  = DbUtilsOpe.query(con, sqlId, newSql, rsh, indexs, prefabSqlStatement);

		resultPageData.setPageInfo(pageInfo);
		resultPageData.setList(result);
		//logParam(indexs, logSql);
		//long end_time = System.currentTimeMillis();
		//logCache.info(Constant.LOG_CSF_DBSQL,
			//	"isNeedMetrics =" + isNeedMetrics + ",queryFilter execute:" + logMsg + "sqlId[" + sqlId
				//		+ "],执行结果码 [0],sql [" + newSql + ";page.getStartIndex()=" + page.getStartIndex()
					//	+ ",page.getEndIndex()=" + page.getEndIndex() + ",indexs=" + logSql + "] used time ("
						//+ (end_time - start_time) + "ms)");
		return resultPageData;
	}
}
