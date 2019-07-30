package com.newland.paas.prefabsql.db.domain;

import java.io.Serializable;

import com.newland.paas.prefabsql.util.ClassMsg;

/**
 * <p>
 * Title:使用prepare sql时候使用参数的类定义
 * </p>
 * @author 林光
 * @version 1.0 modified by ling 2005/05/04 设置准备sql语句的时候，自动清除上一次的参数设置 modified
 *          by ling 2005/05/18 设置准备sql语句的时候，取消自动清除上一次的参数设置 modified by ling
 *          2005/06/20 修改对参数的识别，对于那些属于用''包含起来的:和?不认为它们是参数 modified by ling
 *          2007/05/18 添加可序列化接口
 */
public class DBParam implements Serializable{
	private java.util.ArrayList vt;
	private int index;
	private java.util.Hashtable paramTab;
	/**
	 * 参数时间类型
	 */
	public static final String DATE_TYPE = "Date";
	/**
	 * 参数时间TimeStamp类型
	 */
	public static final String TIMESTAMP_TYPE = "Timestamp";
	/**
	 * 整数类型
	 */
	public static final String INT_TYPE = "int";
	/**
	 * 长整型
	 */
	public static final String LONG_TYPE = "long";
	/**
	 * 浮点数类型
	 */
	public static final String DOUBLE_TYPE = "double";
	/**
	 * 浮点数类型
	 */
	public static final String FLOAT_TYPE = "float";
	/**
	 * 字符串类型
	 */
	public static final String STRING_TYPE = "String";
	/**
	 * StringBuffer类型
	 */
	public static final String STRING_BUFFER_TYPE = "java.lang.StringBuffer";
	/**
	 * ORACLE中的blob类型
	 */
	public static final String BLOB = "blob";
	/**
	 * 游标类型,只能作为参数值，同时参数类型必须是下面的OutParameter（输出参数类型）
	 */
	public static final String CURSOR_TYPE = "Cursor";
	/**
	 * 输出参数类型
	 */
	public static final String OUT_TYPE = "OutParameter";
	public DBParam(){
		vt = new java.util.ArrayList();
		paramTab = new java.util.Hashtable();
	}
	/**
	 * 清除输入的参数集合
	 */
	public void clearParam(){
		vt.clear();
		paramTab.clear();
		index = 0;
	}
	public int getParamNum(){
		return vt.size();
	}
	public DBParam cloneSelf(){
		try{
			DBParam dbParam = new DBParam();
			if(vt.size()>0){
				for(int i = 0;i<vt.size();i++){
					dbParam.vt.add(vt.get(i));
				}
			}
			if(!paramTab.isEmpty()){
				java.util.Enumeration list = paramTab.keys();
				while(list.hasMoreElements()){
					String key = (String)list.nextElement();
					dbParam.paramTab.put(key,paramTab.get(key));
				}
			}
			dbParam.index = index;
			return dbParam;
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 获取变量名称表
	 * @return Hashtable
	 */
	public java.util.Hashtable getParamTable(){
		return paramTab;
	}
	/**
	 * 获取参数及值的组合数组
	 * @return Object[] 每一个[x]都是一个一维数组,[n][0]:表示具体的值对象，[n][1]表示类型
	 */
	public Object[] getParam(){
		try{
			Object[] aResult = new Object[vt.size()];
			for(int i = 0;i<aResult.length;i++){
				aResult[i] = vt.get(i);
			}
			return aResult;
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 添加一个参数
	 * @param value Object
	 * @param type Object
	 * @return boolean
	 */
	public boolean addParam(Object value,Object type){
		try{
			if(type==null){
				return false;
			}
			Object[] aTemp = new Object[2];
			aTemp[0] = value;
			aTemp[1] = type;
			vt.add(index,aTemp);
			index++;
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	/**
	 * 在指定位置添加一个参数
	 * @param value Object
	 * @param type Object
	 * @param index int 从1开始
	 * @return boolean
	 */
	public boolean setParamByIndex(Object value,Object type,int index){
		try{
			if(type==null){
				return false;
			}
			Object[] aTemp = new Object[2];
			aTemp[0] = value;
			aTemp[1] = type;
			if(index>vt.size()){
				int size = vt.size();
				for(int i = size;i<index;i++){
					vt.add(i,null);
				}
			}
			vt.set(index-1,aTemp);
			if(index>this.index){
				this.index = index;
			}
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	/**
	 * 按参数名给指定的参数赋值,它必须和prepareSql配合使用
	 * @param value Object
	 * @param type Object
	 * @param sParamName String
	 * @return boolean
	 */
	public boolean setParamByName(Object value,Object type,String sParamName){
		try{
			Object temp = paramTab.get(sParamName);
			if(temp==null){
				return false;
			}
			int[] aIndex = (int[])temp;
			for(int i = 0;i<aIndex.length;i++){
				if(!setParamByIndex(value,type,aIndex[i])){
					return false;
				}
			}
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	/**
	 * 按batchObject对象中提供的public字段名作为输入参数名,字段类型作为参数类型,直接批量设置一批参数值
	 * @param batchObject Object
	 */
	public void setParamBatchByObject(Object batchObject){
		try{
			Object[][] aaTemp = ClassMsg.getFieldMsgs(batchObject.getClass(),batchObject,0);
			for(int i = 0;i<aaTemp.length;i++){
				String paranName = (String)aaTemp[i][2];
				Object value = aaTemp[i][3];
				String paramType = (String)aaTemp[i][1];
				setParamByName(value,paramType,paranName);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 根据obj中的字段和sWhere生成一个字段prepare 的insert sql语句,insert的字段要过滤掉filtNames中提供的
	 * @param obj Object
	 * @param tableName String 表名
	 * @param filtNames String[]过滤字段名数组
	 * @return String
	 */
	public String prepareInsert(Object obj,String tableName,String[] filtNames){
		try{
			StringBuffer sbUpdate = new StringBuffer();
			Object[][] aaTemp = ClassMsg.getFieldMsgs(obj.getClass(),obj,0);
			boolean bFirst = true;
			for(int i = 0;i<aaTemp.length;i++){
				String paranName = (String)aaTemp[i][2];
				boolean bFilt = false;
				if(filtNames!=null&&filtNames.length>0){
					for(int j = 0;j<filtNames.length;j++){
						if(paranName.equals(filtNames[j])){
							bFilt = true;
							break;
						}
					}
				}
				if(bFilt){
					continue;
				}
				if(!bFirst){
					sbUpdate.append(",");
				}
				else{
					bFirst = false;
					sbUpdate.append("(");
				}
				sbUpdate.append(paranName);
			}
			String sql = "";
			if(sbUpdate.length()>0){
				sbUpdate.append(") values(");
				bFirst = true;
				for(int i = 0;i<aaTemp.length;i++){
					String paranName = (String)aaTemp[i][2];
					boolean bFilt = false;
					if(filtNames!=null&&filtNames.length>0){
						for(int j = 0;j<filtNames.length;j++){
							if(paranName.equals(filtNames[j])){
								bFilt = true;
								break;
							}
						}
					}
					if(bFilt){
						continue;
					}
					if(!bFirst){
						sbUpdate.append(",");
					}
					else{
						bFirst = false;
					}
					sbUpdate.append(":"+paranName);
				}
				sbUpdate.append(")");
				sql = "insert into "+tableName+" "+new String(sbUpdate);
			}
			return prepareSql(sql);
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 根据obj中的字段和sWhere生成一个字段prepare 的insert sql语句
	 * @param obj Object
	 * @param tableName String
	 * @return String
	 */
	public String prepareInsert(Object obj,String tableName){
		return prepareInsert(obj,tableName,null);
	}
	/**
	 * 根据obj中的字段和sWhere生成一个字段prepare 的update sql语句,update的字段要过滤掉filtNames中提供的
	 * @param obj Object
	 * @param tableName String 表名
	 * @param sWhere String 修改条件
	 * @param filtNames String[] 过滤字段名数组
	 * @return String
	 */
	public String prepareUpdate(Object obj,String tableName,String sWhere,String[] filtNames){
		try{
			StringBuffer sbUpdate = new StringBuffer();
			Object[][] aaTemp = ClassMsg.getFieldMsgs(obj.getClass(),obj,0);
			for(int i = 0;i<aaTemp.length;i++){
				String paranName = (String)aaTemp[i][2];
				boolean bFilt = false;
				if(filtNames!=null&&filtNames.length>0){
					for(int j = 0;j<filtNames.length;j++){
						if(paranName.equals(filtNames[j])){
							bFilt = true;
							break;
						}
					}
				}
				if(bFilt){
					continue;
				}
				if(sbUpdate.length()>0){
					sbUpdate.append(",");
				}
				else{
					sbUpdate.append("set ");
				}
				sbUpdate.append(paranName+"=:"+paranName);
			}
			String sql = "";
			if(sbUpdate.length()>0){
				sql = "update "+tableName+" "+new String(sbUpdate)+" "+sWhere;
			}
			return prepareSql(sql);
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 根据obj中的字段和sWhere生成一个字段prepare 的update sql语句
	 * @param obj Object
	 * @param tableName String
	 * @param sWhere String
	 * @return String
	 */
	public String prepareUpdate(Object obj,String tableName,String sWhere){
		return prepareUpdate(obj,tableName,sWhere,null);
	}
	/**
	 * 按顺序号获取已经设置的参数值
	 * @param index int 从1开始
	 * @return Object
	 */
	public Object getParamValueByIndex(int index){
		try{
			Object[] aParamValue = getParam();
			if(aParamValue[index-1]!=null){
				return ((Object[])aParamValue[index-1])[0];
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 按顺序号获取已经设置的参数类型
	 * @param index int 从1开始
	 * @return Object
	 */
	public Object getParamTypeByIndex(int index){
		try{
			Object[] aParamValue = getParam();
			if(aParamValue[index-1]!=null){
				return ((Object[])aParamValue[index-1])[1];
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 按参数名获取已经设置的参数值
	 * @param sParamName String
	 * @return Object
	 */
	public Object getParamValueByName(String sParamName){
		try{
			Object temp = paramTab.get(sParamName);
			if(temp==null){
				return null;
			}
			int[] aIndex = (int[])temp;
			Object[] aParamValue = getParam();
			if(aParamValue[aIndex[0]-1]!=null){
				return ((Object[])aParamValue[aIndex[0]-1])[0];
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 按参数名获取已经设置的参数的类型
	 * @param sParamName String
	 * @return Object
	 */
	public Object getParamTypeByName(String sParamName){
		try{
			Object temp = paramTab.get(sParamName);
			if(temp==null){
				return null;
			}
			int[] aIndex = (int[])temp;
			Object[] aParamValue = getParam();
			if(aParamValue[aIndex[0]-1]!=null){
				return ((Object[])aParamValue[aIndex[0]-1])[1];
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * 添加一组参数
	 * @param aaParam Object[][] [n][0]:参数值 [n][1]:参数类型
	 * @return boolean
	 */
	public boolean addParams(Object[][] aaParam){
		try{
			if(aaParam==null){
				return false;
			}
			for(int i = 0;i<aaParam.length;i++){
				vt.add(index,aaParam[i]);
				index++;
			}
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	private int getNextPo(String s,int start_index){
		try{
			if(s==null){
				start_index++;
				s="";
			}
			for(int i = start_index;i<s.length();i++){
				if(s.charAt(i)=='\r'){
					return i;
				}
				if(s.charAt(i)=='\n'){
					return i;
				}
				if(s.charAt(i)==' '){
					return i;
				}
				if(s.charAt(i)=='|'){
					return i;
				}
				if(s.charAt(i)=='"'){
					return i;
				}
				if(s.charAt(i)==','){
					return i;
				}
				if(s.charAt(i)==')'){
					return i;
				}
				if(s.charAt(i)==';'){
					return i;
				}
				if(s.charAt(i)=='+'){
					return i;
				}
				if(s.charAt(i)=='-'){
					return i;
				}
				if(s.charAt(i)=='/'){
					return i;
				}
				if(s.charAt(i)=='*'){
					return i;
				}
				if(s.charAt(i)==39){
					return i;
				}
			}
			return s.length();
		}
		catch(Exception ex){
			return start_index++;
		}
	}
	/**
	 * 分析自定义的sql语法，参数 使用“:参数名”的方式，这样添加参数的时候可以按名添加, 它一定和方法setParamByName配合使用
	 * @param sql String
	 * @return String
	 */
	public String prepareSql(String sql){
		try{
			if(sql==null){
				return null;
			}
			// clearParam();
			int dead_num = 0;
			int index = 0;
			int end_index = 0;
			int start_index = sql.indexOf(":");
			int dead_index = sql.indexOf("'"); // 看看是否存在该死的单引号
			java.util.Vector vtParam = new java.util.Vector();
			while(start_index>=0&&start_index<sql.length()){
				start_index++;
				end_index = getNextPo(sql,start_index);
				// --------------------added by ling
				// 2005/06/20--------------------------
				if(dead_index>=0){ // 太不幸了，居然有单引号
					String temp = sql.substring(0,start_index);
					int temp_index = temp.indexOf("'");
					dead_num = 0;
					while(temp_index>=0&&temp_index<start_index){
						dead_num++;
						temp_index = temp.indexOf("'",temp_index+1);
					}
				}
				// ----------------------------------------------------------------------
				if(dead_num%2==0){ // 只有是配对的'才说明当前参数是有效的
					String sParam = sql.substring(start_index,end_index);
					vtParam.add(index,sParam);
					index++;
				}
				start_index = end_index;
				start_index = sql.indexOf(":",start_index);
			}
			for(int i = 0;i<vtParam.size();i++){
				int[] aTemp = new int[1];
				String sParam = (String)vtParam.elementAt(i);
				aTemp[0] = i+1;
				if(paramTab.get(sParam)==null){
					paramTab.put(sParam,aTemp);
				}
				else{
					int[] aIndex = (int[])paramTab.get(sParam);
					int[] aIdx = new int[aIndex.length+1];
					aIdx[0] = aTemp[0];
					for(int j = 1;j<aIdx.length;j++){
						aIdx[j] = aIndex[j-1];
					}
					paramTab.put(sParam,aIdx);
				}
			}
			StringBuffer sbSql = new StringBuffer(sql);
			start_index = 0;
			for(int i = 0;i<vtParam.size();i++){
				String sParaName = ":"+(String)vtParam.elementAt(i);
				start_index = sbSql.indexOf(sParaName);
				end_index = sParaName.length()+start_index;
				sbSql.replace(start_index,end_index,"?");
			}
			vtParam.clear();
			return new String(sbSql);
		}
		catch(Exception ex){
			return sql;
		}
	}
	
	/**
	 * 获取已经设置的参数值
	 * @return Object
	 */
	public Object[] getParamValueObjs(){
		try{
			Object[] aParamValue = getParam();
			Object[] valueObj = new Object[aParamValue.length];
			for(int i=0;i<aParamValue.length;i++){
				valueObj[i]=((Object[])aParamValue[i])[0];				
			}
			if (valueObj.length<1) {
				valueObj = null;
			}
			return valueObj;
		}
		catch(Exception ex){
			return null;
		}
	}
	

}
