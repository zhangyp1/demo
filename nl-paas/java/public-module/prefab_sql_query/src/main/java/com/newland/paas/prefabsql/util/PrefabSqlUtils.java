package com.newland.paas.prefabsql.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.newland.paas.prefabsql.db.domain.DBParam;

/**
 * prefabsql处理
 * @author yyq
 *
 */
public class PrefabSqlUtils {
//	private static LogObj LogCache = LogObjFactory.getLogObj(PrefabSqlUtils.class);

	
	public static boolean isNVL(String str) {
		return str == null || str.trim().equals("");
	}
	
	public static boolean isNvlObj(Object value) {
		return value == null || value.toString().trim().equals("");
	}
	
	public static long stringToLong(String str) {
		return str == null || str.trim().equals("")?0:Long.valueOf(str);
	}
	
	public static int stringToInt(String str) {
		return str == null || str.trim().equals("")?0:Integer.valueOf(str);
	}
	
	public static String replaceEmptyParam(String sql) {
		 sql = sql.trim();
		 int i = 0;
		 while(sql.startsWith("(")&&i<5){//最多5个嵌套挂号
			 sql = sql.replaceFirst("\\([^)^(]*\\)","1=1");
			 i++;
		 }
		 return sql;
	}
	
	public static String replaceText(String sql,String text) {
		 sql = sql.trim().replaceAll("\\{[^}]*\\}",text);
		 return sql;
	}
	
	public static List<String> getBigParamList(String sql) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\{.*?\\}");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(sql);
		while (m.find()) {// 遍历找到的所有大括号
			String param = m.group().replace("{", "")
					.replace("}", "");// 去掉括号
			list.add(param);
		}
		return list;
	}
	
	public static List<String> getMidParamList(String sql) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\[.*?\\]");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(sql);
		while (m.find()) {// 遍历找到的所有大括号
			String param = m.group().replace("[", "")
					.replace("]", "");// 去掉括号
			if(param.indexOf(":")==-1){
				list.add(param);
			}
		}
		return list;
	}
	
	
	public static String replaceInParam(String sourceSql,String column,String columnValue,String sParamName) {
		StringBuilder sb = new StringBuilder();
		String[] split = sourceSql.split(sParamName);
		//如果in条件不是在where之后，则加and
		if(split!=null&&split.length>0&&!split[0].trim().endsWith("where")){
			sb.append(" and ");
		}
		if(!isNVL(columnValue)){
			sb.append(column).append(" in (");
			String[] aValue = columnValue.split(",");
			for (int i = 0; i < aValue.length; i++) {
				sb.append("?");
				if(i!=aValue.length-1){
					sb.append(",");
				}
			}
			sb.append(")");
			sourceSql = sourceSql.replaceFirst(sParamName,sb.toString());
		}else{
			sb.append("1=1");
			sourceSql = sourceSql.replaceFirst(sParamName,sb.toString());
		}
		return sourceSql;
	}
	
	/**
	 * 值为空判断 in 条件替换为1=1
	 * @param sourceSql
	 * @param paramName
	 * @return
	 */
	public static String replaceInExprSqlNullParam(String sourceSql,String paramName) {
		String result = "";
		StringBuilder inExpr = new StringBuilder();
		
		inExpr.append("\\s+#\\s*[A-Za-z0-9_\\.]+\\s+[iI][nN]\\s+(\\()*\\s*#");
		inExpr.append(":").append(paramName);
		inExpr.append("\\s*(\\))*\\s*");
		result = sourceSql.replaceFirst(inExpr.toString(), " 1=1 ");
		
		return result;
	}
	
	public static String replaceInParamNew(String sourceSql,String paramName,Object value) {
		StringBuilder sb = new StringBuilder();
		if(value instanceof String){//传入参数值是用逗号分割的字符串
			String[] valueSplit = ((String)value).split(",");
			int i = 0;
			for (String object : valueSplit) {
				sb.append("?");
				if(i!=valueSplit.length-1){
					sb.append(",");
				}
				i++;
			}
			sourceSql = sourceSql.replaceFirst(":"+paramName,sb.toString());
		}else if(value instanceof Collection){////传入参数值是集合
			int i = 0;
			for (Object object : (Collection)value) {
				sb.append("?");
				if(i!=((Collection)value).size()-1){
					sb.append(",");
				}
				i++;
			}
			sourceSql = sourceSql.replaceFirst(":"+paramName,sb.toString());
		}
		return sourceSql;
	}
	
	// 1－number，2－long，3－varchar2，9－in条件
	public static String toDataType(int dataType) {
			String type = DBParam.STRING_TYPE;
			switch (dataType) {
			case 1:
				type = DBParam.INT_TYPE;
				break;
			case 2:
				type = DBParam.LONG_TYPE;
				break;
			case 3:
				type = DBParam.STRING_TYPE;
				break;
			default:
				type = DBParam.STRING_TYPE;
			}
			return type;
		}

		public static Object convertValue(Object value, int dataType) throws Exception {
			Object paramValue = value;
			try{
				if (!PrefabSqlUtils.isNvlObj(value)) {
					switch (dataType) {
					case 1:
						paramValue = Double.parseDouble(value.toString().trim());
						break;
					case 2:
						paramValue = Long.parseLong(value.toString().trim());
						break;
					}
				}
			}catch(NumberFormatException e){
				String message = "输入参数值无法转换成配置中的类型,配置中类型dataType="+dataType+",输入参数值value="+value;
				e.printStackTrace();
			}
			return paramValue;
		}
		
		public static String getParam(String sqlBlock){
			int start_index = sqlBlock.indexOf(":");
		     int end_index = getNextPo(sqlBlock,start_index);
		    String sParam =sqlBlock.substring(start_index+1,end_index);
		    return sParam;
		}
	
	public static void mainStart(String[] args) throws Exception {
		String sql = "select home_city,  msisdn,  provider_name from Hu_Records where hu_record_seq=? ? and hu_record_seq=?";
		String sourceSql = "select home_city,  msisdn,  provider_name from Hu_Records where provider_name=:2 and (hu_record_seq=:1) and (hu_record_seq in (:hu_record_seq)) and provider_name=:provider_name";
		List<String> paramNames = getParamNames(sourceSql);
		for (String string : paramNames) {
			System.out.println(string);
		}
     
     //sql = "select * from hu_record";
		String s = "LSLDFKDSLFK";
		s = s.toLowerCase();
		System.out.println(s);
     sql = "";
     List l = new ArrayList();
		FileInputStream fis = new FileInputStream("d:\\sql.txt"); 
		BufferedReader in = new BufferedReader(new InputStreamReader(fis)); 
		//String sql = "update cc_integral_ally_info set sts = 'PASUE' where market_id = '";
		while(true) 
		{ 
		String str = in.readLine(); 

		if(str == null) 
		{ 
		break;
		} 
		//System.out.println(str);
		sql+=str;
		} 
		in.close();
		sql = "select  ti.checker_id,"+
			       "ti.checker_name"+
			  " from dm_task_info ti,(select * from dm_task_type tt where tt.name=:9 and (tt.id=:10)) tt"+
			  "where  ti.task_type=tt.task_type "+
			  " and (task_name like '%'||:1||'%')"+
			  " and (ti.task_type=:2) "+
			  " and (task_sub_type like '%'||:3||'%')"+
			  " and trunc(insert_time) >= to_date(:4,  'yyyy-mm-dd hh24:mi:ss')"+
			  " and (to_char(trunc(insert_time)) <= to_date(:5,  'yyyy-mm-dd hh24:mi:ss'))"+
			  " and (task_id like '%'||:6||'%')"+
			  " and (ti.operator_id=:7)"+
			  " and (ti.checker_id=:8) and (task_id between :10 and :11)"+
			  " and delflag in (:9)"+
			  " and delflag=1"+
			  " order by task_id";
		sql = "select it.brand_id activity_brand_id , a.brand_desc activity_brand_name "+
  "from (select brand_id from mm_planning_brand_t where entity_id = :entity_id) it"+
  "left join (select distinct brand_id, brand_desc from dim_brand) a"+
    "on a.brand_id = it.brand_id"+
 "order by it.brand_id ";

		System.out.println(sql);
     StringBuilder mainSql = new StringBuilder();
     StringBuilder betweenSql = new StringBuilder();
     String[] whereSplit = sql.toLowerCase().split(" where ");
     for (int i = 0; i < whereSplit.length; i++) {
    	 String[] andSplit = whereSplit[i].split(" and ");
    	 StringBuilder whereSql = new StringBuilder();
 		for (int j = 0; j < andSplit.length; j++) {
 			if(andSplit[j].indexOf(" between ")!=-1){//between语句暂时不处理
 				betweenSql.append(andSplit[j]);
 				continue;
 			}
 			if(betweenSql.length()!=0){
 				betweenSql.append(" and ").append(andSplit[j]);
 				andSplit[j] = betweenSql.toString();//between语句要连上一句才能完整处理
 				System.out.println("andSplit[j]="+andSplit[j]);
 				betweenSql.setLength(0);
 			}
 			if(andSplit[j].indexOf(":")!=-1){
 			    System.out.println("sParam="+getParam(andSplit[j]));
 			    if(andSplit[j].indexOf("between")==-1){//不是between语句直接替换
 			    	andSplit[j] = replaceEmptyParam(andSplit[j]);
 			    }else{
 			    	if(andSplit[j].trim().startsWith("(")){//是between语句且是用()刮起来，则替换为空
 			    		andSplit[j] = replaceEmptyParam(andSplit[j]);
 			    	}
 			    }
 				
 			}
 			whereSql.append(andSplit[j]);
 			if(j!=andSplit.length-1){
 				whereSql.append(" and ");
 			}
		}
 		mainSql.append(whereSql.toString());
 		if(i!=whereSplit.length-1){
 			mainSql.append(" where ");
 		}
     }
     System.out.println(mainSql);
//		System.out.println(replaceEmptyParam(sql, "hu_record_seq"));
        List list = new ArrayList();
        list.add("12534547231");
        list.add("22");
        String str = "12534547231,22";
        sql = "select hu_record_seq,  msisdn,  provider_name from Hu_Records where (msisdn in (:msisdn)) and (hu_record_seq=:hu_record_seq) order by hu_record_seq";
        System.out.println(replaceInParamNew(sql,"msisdn",str));
        sourceSql = "select * from (select * from {Test})";
        System.out.println(replaceText(sourceSql,"USER_INFO"));
        String string = "dfas {d} dasf {fadf} dsafasdf";
        System.out.println(string.replaceAll("\\{[^}]*\\}","CHEMBO"));
        
        String sqlStatement = "update mm_theme set theme_start_date=[to_date(:theme_start_date,'yyyymmdd')],name=[name],age=[age],create_time=[to_date(:create_time,'yyyymmdd')],{flag1}=1 where theme_id=591110021654";
		List<String> paramNames1 = PrefabSqlUtils.getParamNames(sqlStatement);//提取:开头的参数
		//提取{}参数
		paramNames1.addAll(PrefabSqlUtils.getBigParamList(sqlStatement));
		//提取[]参数
		paramNames1.addAll(PrefabSqlUtils.getMidParamList(sqlStatement));
		for (String str1 : paramNames1) {
			System.out.println(str1);
		}
	}
	
	public static List<String> getParamNames(String sql){
		try{
			int dead_num = 0;
			int end_index = 0;
			int start_index = sql.indexOf(":");
			int dead_index = sql.indexOf("'"); // 看看是否存在该死的单引号
			List<String> vtParam = new ArrayList<String>();
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
					vtParam.add(sParam);
				}
				start_index = end_index;
				start_index = sql.indexOf(":",start_index);
			}
			return vtParam;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return new ArrayList<String>();
		}
	}
	
	private static int getNextPo(String s,int start_index){
		try{
			if(s==null){
				start_index++;
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
}
