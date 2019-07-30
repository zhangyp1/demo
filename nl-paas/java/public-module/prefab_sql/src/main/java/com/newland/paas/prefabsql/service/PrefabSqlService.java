package com.newland.paas.prefabsql.service;
import java.sql.SQLException;
import java.util.Map;

import com.newland.paas.prefabsql.db.dbutils.ResultSetHandler;
import com.newland.paas.sbcommon.common.PageBean;
import com.newland.paas.sbcommon.common.PageParams;

public interface PrefabSqlService {
	public <T> T executeQuery(final String sqlId, final Map<String, Object> requestMap,
			final ResultSetHandler<T> rsh) throws SQLException;
	
	public PageBean executeQueryPage(String sqlId, final Map<String, Object> requestMap, 
			PageParams pageParams) throws SQLException ;		
}
