package com.newland.paas.prefabsql.pojo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * sql定义
 * @author yyq
 *
 */
public class PrefabSql {
	public static final int SQL_TYPE_SELECT = 1;
	private long sqlId;
	private String sqlName;
	private int sqlType;
	private String outTemplate;
	private Map<String,PrefabSqlStatement> statmentMap = new LinkedHashMap<String,PrefabSqlStatement>();
	private List<PrefabSqlInparam> inparamList = new ArrayList<PrefabSqlInparam>();
	private List<PrefabSqlOutparam> outparamList = new ArrayList<PrefabSqlOutparam>();
	private List<PrefabSqlGroupParam> groupParamList = new ArrayList<PrefabSqlGroupParam>();

	public List<PrefabSqlGroupParam> getGroupParamList() {
		return groupParamList;
	}

	public void setGroupParamList(List<PrefabSqlGroupParam> groupParamList) {
		this.groupParamList = groupParamList;
	}

	public Map<String, PrefabSqlStatement> getStatmentMap() {
		return statmentMap;
	}
	
	public PrefabSqlStatement getStatmentBySeq(String seq){
		return statmentMap.get(sqlId+"-"+seq);
	}

	public void setStatmentMap(Map<String, PrefabSqlStatement> statmentMap) {
		this.statmentMap = statmentMap;
	}

	public List<PrefabSqlOutparam> getOutparamList() {
		return outparamList;
	}

	public void setOutparamList(List<PrefabSqlOutparam> outparamList) {
		this.outparamList = outparamList;
	}

	public List<PrefabSqlInparam> getInparamList() {
		return inparamList;
	}
	
	public Map<String,PrefabSqlInparam> getInparamMap() {
		Map<String,PrefabSqlInparam> map = new LinkedHashMap<String,PrefabSqlInparam>();
		for (PrefabSqlInparam inparam : inparamList) {
			map.put(inparam.getParamName(), inparam);
		}
		return map;
	}
	
	public Map<String,PrefabSqlInparam> getInparamSeqMap() {
		Map<String,PrefabSqlInparam> map = new LinkedHashMap<String,PrefabSqlInparam>();
		for (PrefabSqlInparam inparam : inparamList) {
			map.put(String.valueOf(inparam.getSeq()), inparam);
		}
		return map;
	}

	public void setInparamList(List<PrefabSqlInparam> inparamList) {
		this.inparamList = inparamList;
	}

	public PrefabSql() {
	}

	public PrefabSql(String sqlId, String sqlName, 
			String sqlType, String outTemplate) {
		this.sqlId = stringToLong(sqlId);
		this.sqlName = sqlName;
		this.sqlType = stringToInt(sqlType);
		this.outTemplate = outTemplate;
	}
	
	public static long stringToLong(String str) {
		return str == null || str.trim().equals("")?0:Long.valueOf(str);
	}
	
	public static int stringToInt(String str) {
		return str == null || str.trim().equals("")?0:Integer.valueOf(str);
	}

	public long getSqlId() {
		return sqlId;
	}

	public void setSqlId(long sqlId) {
		this.sqlId = sqlId;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getOutTemplate() {
		return outTemplate==null?"":outTemplate;
	}

	public void setOutTemplate(String outTemplate) {
		this.outTemplate = outTemplate;
	}
}
