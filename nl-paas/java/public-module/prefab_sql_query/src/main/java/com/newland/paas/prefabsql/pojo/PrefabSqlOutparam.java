package com.newland.paas.prefabsql.pojo;

import java.io.Serializable;

import com.newland.paas.prefabsql.util.PrefabSqlUtils;

/**
 * sql返回参数
 * @author yyq
 *
 */
public class PrefabSqlOutparam implements Serializable{
	
	public static final int EMPTY_FLAG = 1;
	public static final int NEED_REPLACE_FLAG = 1;
	private long sqlId;
	private int statementSeq;
	private int paramSeq;
	private String paramName;
	private int dataType;
	private long dataLength;
	private int canEmpty;
	private int needReplace;
	private int needOutput;

	public PrefabSqlOutparam(String sqlId,String statementSeq,String paramSeq, String paramName,
			String dataType, String dataLength, String canEmpty,String needReplace,String needOutput) {
		this.sqlId = PrefabSqlUtils.stringToLong(sqlId);
		this.statementSeq = PrefabSqlUtils.stringToInt(statementSeq);
		this.paramSeq = PrefabSqlUtils.stringToInt(paramSeq);
		this.paramName = paramName;
		this.dataType = PrefabSqlUtils.stringToInt(dataType);
		this.dataLength = PrefabSqlUtils.stringToLong(dataLength);
		this.canEmpty = PrefabSqlUtils.stringToInt(canEmpty);
		this.needReplace = PrefabSqlUtils.stringToInt(needReplace);
		this.needOutput = PrefabSqlUtils.stringToInt(needOutput);
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

	public int getParamSeq() {
		return paramSeq;
	}

	public void setParamSeq(int paramSeq) {
		this.paramSeq = paramSeq;
	}

	public int getNeedReplace() {
		return needReplace;
	}

	public void setNeedReplace(int needReplace) {
		this.needReplace = needReplace;
	}

	public int getNeedOutput() {
		return needOutput;
	}

	public void setNeedOutput(int needOutput) {
		this.needOutput = needOutput;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public long getDataLength() {
		return dataLength;
	}

	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}

	public int getCanEmpty() {
		return canEmpty;
	}

	public void setCanEmpty(int canEmpty) {
		this.canEmpty = canEmpty;
	}

}
