package com.newland.paas.prefabsql.pojo;

import com.newland.paas.prefabsql.util.PrefabSqlUtils;

/**
 * sql参数定义
 * @author yyq
 *
 */
public class PrefabSqlInparam{
	
	public static final int EMPTY_FLAG = 1;
	public static final int IN_CONDITION = 9;
	private long sqlId;
	private int seq;
	private String paramName;
	private int dataType;
	private long dataLength;
	private String columnName;
	private int canEmpty;
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public PrefabSqlInparam(String sqlId,String seq, String paramName,
			String dataType, String dataLength, String columnName, String canEmpty,String groupId) {
		this.sqlId = PrefabSqlUtils.stringToLong(sqlId);
		this.seq = PrefabSqlUtils.stringToInt(seq);
		this.paramName = paramName;
		this.dataType = PrefabSqlUtils.stringToInt(dataType);
		this.dataLength = PrefabSqlUtils.stringToLong(dataLength);
		this.columnName = columnName;
		this.canEmpty = PrefabSqlUtils.stringToInt(canEmpty);
		this.groupId = groupId;
	}

	public long getSqlId() {
		return sqlId;
	}

	public void setSqlId(long sqlId) {
		this.sqlId = sqlId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getCanEmpty() {
		return canEmpty;
	}

	public void setCanEmpty(int canEmpty) {
		this.canEmpty = canEmpty;
	}

}
