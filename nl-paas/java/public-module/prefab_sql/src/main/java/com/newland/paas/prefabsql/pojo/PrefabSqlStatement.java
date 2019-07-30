package com.newland.paas.prefabsql.pojo;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.prefabsql.util.PrefabSqlUtils;

/**
 * 
 * @author yyq
 *
 */
public class PrefabSqlStatement{
	public static final String OLD_STATEMENT = "1";
	private long sqlId;
	private int statementSeq;
	private String sqlStatement;
	private int timeout;
	private String maxRows;
	private String maxFileRows;
	private String sourceTable;
	private String sourceTableId;
	private String cacheId ;
	private int hisOper;
	private String hisWhere;
	private int cacheDataSourceId; //缓存表ID
	
	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getSourceTableId() {
		return sourceTableId;
	}

	public void setSourceTableId(String sourceTableId) {
		this.sourceTableId = sourceTableId;
	}

	//---语句路由增加字段 begin---
	private String sqlJndi = "";
	private String sqlPaserResult = "";
	
	public void setSqlPaserResult(String sqlPaserResult) {
		this.sqlPaserResult = sqlPaserResult;
	}

	private String sqlType = "";
	private String tableNames = "";
	//---语句路由增加字段 end---

	

	public PrefabSqlStatement(String sqlId, String statementSeq,
			String sqlStatement, String timeout, String maxRows /*, 
			String maxFileRows,String sqlJndi,String oldStatementFlag,
			String cacheId,String remoteFlag,String hisOper,String cacheDataSourceId*/ ) {
		this.sqlId = PrefabSqlUtils.stringToLong(sqlId);
		this.statementSeq = PrefabSqlUtils.stringToInt(statementSeq);
		this.timeout = PrefabSqlUtils.stringToInt(timeout);
		//this.oldStatementFlag = oldStatementFlag;
		this.maxRows = maxRows;
		//this.maxFileRows = maxFileRows;
		//this.sqlJndi = sqlJndi;
		//sqlId-statementSeq 拼成sql_id
		//this.sqlStatement = "// --sql_id-- //" + this.sqlId +"-"+this.statementSeq +"// --sql_id-- //" + sqlStatement;
		this.sqlStatement = sqlStatement;
		//this.remoteFlag = PrefabSqlUtils.stringToInt(remoteFlag);
		//this.cacheId = cacheId;		
		//this.hisOper = PrefabSqlUtils.stringToInt(hisOper);
		//this.cacheDataSourceId =  PrefabSqlUtils.stringToInt(cacheDataSourceId);
	}
	
	/**
	 * 设置sqlPaserResult的值
	 * 
	 * @param sqlType
	 * @param tableNames
	 * @return
	 */
	public synchronized void setSqlPaserResultValue(String sqlType,
			String tableNames) {
		this.sqlPaserResult = "{sqltype:" + sqlType + ", tableNames:"
				+ tableNames + "}";
		this.parseSqlPaserResult();
	}
	/*public PrefabSqlStatement(){}
public static void main(String[] args) {
	PrefabSqlStatement pre = new PrefabSqlStatement();
	pre.sqlPaserResult = "{tableNames:smc_file_operation,sqltype:EDITSQL }";
	pre.parsedSqlPaserResult();
	System.out.println(pre.getTableNames()+"---"+pre.sqlType);
}*/
	
	public void parseSqlPaserResult() {
		if (StringUtils.isBlank(this.getSqlPaserResult()) ) {
			return ;
		}
		this.sqlPaserResult = this.sqlPaserResult.trim();
		int sqlTpInd = this.sqlPaserResult.indexOf("sqltype");
		if(sqlTpInd >= 0) {
			int sqlTpsplit = this.sqlPaserResult.indexOf(",", sqlTpInd);
			if(sqlTpsplit >= 0) {
				String subSqlTp = this.sqlPaserResult.substring(sqlTpInd, sqlTpsplit).trim();
				String[] sqlTpVal = subSqlTp.split(":");
				if(sqlTpVal != null && sqlTpVal.length == 2) {
					this.sqlType = sqlTpVal[1].trim();
				}
			} else {
				sqlTpsplit = this.sqlPaserResult.indexOf("}", sqlTpInd);
				String subSqlTp = "";
				if(sqlTpsplit >= 0) {
					subSqlTp = this.sqlPaserResult.substring(sqlTpInd, sqlTpsplit);
				} else {
					subSqlTp = this.sqlPaserResult.substring(sqlTpInd);
				}
				String[] sqlTpVal = subSqlTp.split(":");
				if(sqlTpVal != null && sqlTpVal.length == 2) {
					this.sqlType = sqlTpVal[1].trim();
				}
			}
		}
		//tableNames
		sqlTpInd = this.sqlPaserResult.indexOf("tableNames");
		if(sqlTpInd >= 0) {
			int sqlTpsplit = this.sqlPaserResult.indexOf("sqltype", sqlTpInd);
			if(sqlTpsplit >= 0) {
				String subSqlTp = this.sqlPaserResult.substring(sqlTpInd, sqlTpsplit).trim();
				if(subSqlTp.lastIndexOf(",") == (subSqlTp.length() -1)) {
					subSqlTp = subSqlTp.substring(0, subSqlTp.length() -1).trim();
				}
				String[] sqlTpVal = subSqlTp.split(":");
				if(sqlTpVal != null && sqlTpVal.length == 2) {
					this.tableNames = sqlTpVal[1].trim();
				}
			} else {
				sqlTpsplit = this.sqlPaserResult.indexOf("}", sqlTpInd);
				String subSqlTp = "";
				if(sqlTpsplit >= 0) {
					subSqlTp = this.sqlPaserResult.substring(sqlTpInd, sqlTpsplit);
				} else {
					subSqlTp = this.sqlPaserResult.substring(sqlTpInd);
				}
				String[] sqlTpVal = subSqlTp.split(":");
				if(sqlTpVal != null && sqlTpVal.length == 2) {
					this.tableNames = sqlTpVal[1].trim();
				}
			}
		}
	}

	public long getSqlId() {
		return sqlId;
	}

	public void setSqlId(long sqlId) {
		this.sqlId = sqlId;
	}

	public int getStatementSeq() {
		return statementSeq;
	}

	public void setStatementSeq(int statementSeq) {
		this.statementSeq = statementSeq;
	}

	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;
	}

	public String getMaxFileRows() {
		return maxFileRows;
	}

	public void setMaxFileRows(String maxFileRows) {
		this.maxFileRows = maxFileRows;
	}

	public String getSqlJndi() {
		return sqlJndi;
	}

	public String getSqlPaserResult() {
		return sqlPaserResult;
	}
	
	public String getSqlType() {
		return sqlType;
	}

	public String getTableNames() {
		return tableNames;
	}

	public String getCacheId() {
		return cacheId;
	}

	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}

	public int getHisOper() {
		return hisOper;
	}

	public void setHisOper(int hisOper) {
		this.hisOper = hisOper;
	}

	public String getHisWhere() {
		return hisWhere;
	}

	public void setHisWhere(String hisWhere) {
		this.hisWhere = hisWhere;
	}

	public int getCacheDataSourceId() {
		return cacheDataSourceId;
	}

	public void setCacheDataSourceId(int cacheDataSourceId) {
		this.cacheDataSourceId = cacheDataSourceId;
	}
}
