package com.newland.paas.prefabsql.pojo;

import java.io.Serializable;

/**
 * 
 * @author yyq
 *
 */
public class PrefabSqlGroupParam implements Serializable {
	public static final int LIMIT_FLAG = 1;
	private String sqlId;
	private String groupId;
	private int maxEmptyNum;
	private int minUnemptyNum;
	private int groupFlag;

	public PrefabSqlGroupParam(String sqlId, String groupId, int maxEmptyNum,
			int minUnemptyNum, int groupFlag) {
		super();
		this.sqlId = sqlId;
		this.groupId = groupId;
		this.maxEmptyNum = maxEmptyNum;
		this.minUnemptyNum = minUnemptyNum;
		this.groupFlag = groupFlag;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getMaxEmptyNum() {
		return maxEmptyNum;
	}

	public void setMaxEmptyNum(int maxEmptyNum) {
		this.maxEmptyNum = maxEmptyNum;
	}

	public int getMinUnemptyNum() {
		return minUnemptyNum;
	}

	public void setMinUnemptyNum(int minUnemptyNum) {
		this.minUnemptyNum = minUnemptyNum;
	}

	public int getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(int groupFlag) {
		this.groupFlag = groupFlag;
	}
}
