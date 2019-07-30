package com.newland.paas.prefabsql.service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.prefabsql.pojo.PrefabSql;
import com.newland.paas.prefabsql.pojo.PrefabSqlGroupParam;
import com.newland.paas.prefabsql.pojo.PrefabSqlInparam;
import com.newland.paas.prefabsql.pojo.PrefabSqlStatement;



/**
 * 执行sql定义
 * @author yyq
 *
 */
@Component("prefabSqlManager")
public class PrefabSqlManager implements CommandLineRunner{
	private static final Log log = LogFactory.getLogger(PrefabSqlManager.class);
	
	@Autowired
	DataSource dataSource;
	private Map<String, PrefabSql> loadingCache;// 备用内存
	private Map<String, PrefabSql> cache = new HashMap<String, PrefabSql>();// 使用内存
	
	public PrefabSql getPrefabSqlBySqlId(String sqlId) {
		return (PrefabSql)cache.get(sqlId);
	}
	
	private void putLoadingCache(String key, PrefabSql element) {
		loadingCache.put(key, element);
	}
	
	private PrefabSql getLoadingCache(String sqlId) {
		return (PrefabSql)loadingCache.get(sqlId);
	}
	
	private void createLoadingCache() {
		loadingCache = new HashMap<String, PrefabSql>();
	}
	
	private void switchLoadingCacheToCache() {
		cache = loadingCache;
	}

	public PrefabSqlStatement getStatmentBySqlIdSeq(String sqlId_seq) {
		String[] split = sqlId_seq.split("-");
		String sqlId = split[0];
		PrefabSql prefabSql = getPrefabSqlBySqlId(sqlId);
		PrefabSqlStatement prefabSqlStatement = null;
		if (prefabSql != null) {
			prefabSqlStatement = prefabSql.getStatmentMap().get(sqlId_seq);
		}
		return prefabSqlStatement;
	}

	public void loadConfig() throws SQLException {
		Connection conn = null;
		
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			createLoadingCache();
			loadSql(conn);
			loadSqlStatement(conn);	
			loadSqlInParam(conn);
			loadSqlGroupParam(conn);
			switchLoadingCacheToCache();
		}finally {
			if ( conn != null ) {
				DataSourceUtils.releaseConnection(conn, dataSource);
			}
		}			
	}
	
	private void loadSql( Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement() ) {
			ResultSet rs = statement.executeQuery("select sql_id,sql_name,sql_type from prefab_sql");
			while (rs.next()) {
				int i = 1;
				String sqlId = StringUtils.stripToEmpty(rs.getString(i++)) ;
				String sqlName = StringUtils.stripToEmpty(rs.getString(i++));
				String sqlType = StringUtils.stripToEmpty(rs.getString(i++));
				String outTemplate = "";
				PrefabSql prefabSql = new PrefabSql(sqlId, sqlName, sqlType, outTemplate);
				putLoadingCache(sqlId, prefabSql);
			}				
		}
	}
	
	private void loadSqlStatement(Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement()) {
			Map<String, Map<String, PrefabSqlStatement>> map = new HashMap<>();
			ResultSet rs = statement.executeQuery("select sql_id,statement_seq,sql_statement,timeout,max_rows from prefab_sql_statement order by sql_id,statement_seq");
			while (rs.next()) {
				int i = 1;
				String sql_id = rs.getString(i++);
				String statement_seq = StringUtils.stripToEmpty(rs.getString(i++));
				String sql_statement = StringUtils.stripToEmpty(rs.getString(i++));
				String timeout = StringUtils.stripToEmpty(rs.getString(i++));
				String max_rows = StringUtils.stripToEmpty(rs.getString(i++));
				//String sql_paser_result = "json";
				PrefabSqlStatement prefabSqlStatement = new PrefabSqlStatement(sql_id, statement_seq,
						sql_statement, timeout, max_rows /*, max_file_rows, sql_jndi, old_statement_flag,
						cache_id, remote_flag,his_oper,cache_data_source_id */ );
				//prefabSqlStatement.setSqlPaserResult(sql_paser_result);
				if (map.containsKey(sql_id)) {
					map.get(sql_id).put(sql_id + "-" + statement_seq, prefabSqlStatement);
				} else {
					Map<String, PrefabSqlStatement> statmentMap = new LinkedHashMap<String, PrefabSqlStatement>();
					statmentMap.put(sql_id + "-" + statement_seq, prefabSqlStatement);
					map.put(sql_id, statmentMap);
				}
				PrefabSql prefabSql = getLoadingCache(sql_id);
				if (prefabSql != null) {
					prefabSql.setStatmentMap(map.get(sql_id));
				}			
			}
		}
	}

	private void loadSqlInParam(Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement()) {
			Map<String, List<PrefabSqlInparam>> map = new HashMap<String, List<PrefabSqlInparam>>();
			ResultSet rs = statement.executeQuery("select sql_id,seq,param_name,data_type,data_length,column_name,can_empty,group_id from prefab_sql_in_param order by sql_id,seq");
			while ( rs.next()) {
				int i = 1;
				String sql_id = StringUtils.stripToEmpty(rs.getString(i++));
				String seq = StringUtils.stripToEmpty(rs.getString(i++));
				String paramName = StringUtils.stripToEmpty(rs.getString(i++));
				String dataType = StringUtils.stripToEmpty(rs.getString(i++));
				String dataLength = StringUtils.stripToEmpty(rs.getString(i++));
				String columnName = StringUtils.stripToEmpty(rs.getString(i++));
				String canEmpty = StringUtils.stripToEmpty(rs.getString(i++));
				String groupId = StringUtils.stripToEmpty(rs.getString(i++));
				PrefabSqlInparam inparam = new PrefabSqlInparam(sql_id, seq, paramName, dataType, dataLength,
						columnName, canEmpty, groupId);
				if (map.containsKey(sql_id)) {
					map.get(sql_id).add(inparam);
				} else {
					List<PrefabSqlInparam> inparamList = new ArrayList<PrefabSqlInparam>();
					inparamList.add(inparam);
					map.put(sql_id, inparamList);
				}
				
				PrefabSql prefabSql = getLoadingCache(sql_id);
				if (prefabSql != null) {
					prefabSql.setInparamList(map.get(sql_id));
				}				
			}			
		}
	}	
	
	private void loadSqlGroupParam(Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement()) {
			Map<String, List<PrefabSqlGroupParam>> map = new HashMap<String, List<PrefabSqlGroupParam>>();
			ResultSet rs = statement.executeQuery("select group_id,sql_id,max_empty_num,min_unempty_num,group_flag from prefab_sql_group_param");
			while ( rs.next()) {
				int i = 1;
				String groupId = StringUtils.stripToEmpty(rs.getString(i++));
				String sql_id = StringUtils.stripToEmpty(rs.getString(i++));
				int maxEmptyNum = rs.getInt(i++);
				int minUnemptyNum = rs.getInt(i++);
				int groupFlag = rs.getInt(i++);
				PrefabSqlGroupParam prefabSqlGroupParam = new PrefabSqlGroupParam(sql_id, groupId,
						maxEmptyNum, minUnemptyNum, groupFlag);
				if (map.containsKey(sql_id)) {
					map.get(sql_id).add(prefabSqlGroupParam);
				} else {
					List<PrefabSqlGroupParam> groupParamList = new ArrayList<PrefabSqlGroupParam>();
					groupParamList.add(prefabSqlGroupParam);
					map.put(sql_id, groupParamList);
				}

				PrefabSql prefabSql = getLoadingCache(sql_id);
				if (prefabSql != null) {
					prefabSql.setGroupParamList(map.get(sql_id));
				}
			}
		}
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		try {
			loadConfig();
		}catch(SQLException exception) {
			log.error("", "", exception, exception.getMessage());
		}
	}
	

	@Scheduled(cron="0 0/1 * * * *")
	public void scheduled(){
		try {
			run(null);
		} catch (Exception e) {
		}
	}
}
