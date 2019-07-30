package com.newland.paas.prefabsql.service;

import com.newland.paas.dataaccessmodule.model.PrefabSql;
import com.newland.paas.prefabsql.db.dbutils.ResultSetHandler;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PrefabSqlService {

	<T> T executeQuery(final String sqlId, final Map<String, Object> requestMap,
			final ResultSetHandler<T> rsh) throws SQLException;
	
	ResultPageData executeQueryPage(String sqlId, final Map<String, Object> requestMap,
									PageInfo pageParams) throws SQLException ;

	List<PrefabSql> findAll();

}
