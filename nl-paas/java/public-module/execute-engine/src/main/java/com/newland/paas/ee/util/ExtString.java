/* Generated by Together */
package com.newland.paas.ee.util;

import java.util.Vector;

/**
 * 提供对String对象进行扩展操作的类
 */
public class ExtString{
	/**
	 * 提供替换字符串的功能,区分大小写
	 * @param StringBuffer sSrcStr 为要被处理的StringBuffer
	 * @param String sStr 为被替换的子字符串
	 * @param String sRepStr 为替换进去的子字符串
	 * @return 无
	 */
	public static void old_replace(StringBuffer sb,String sStr,String sRepStr){
		try{
			if((sb==null)||(sStr==null)||(sRepStr==null)){
				return;
			}
			if((sb.length()==0)||(sStr.length()==0)||sb.indexOf(sStr)<0){
				return;
			}
			int iStartIndex = 0;
			int iLen = sb.length();
			int iLen2 = sStr.length();
			while(iStartIndex<iLen){
				if(sb.substring(iStartIndex,iLen2+iStartIndex).equals(sStr)){
					sb.replace(iStartIndex,iLen2+iStartIndex,sRepStr);
					iLen = sb.length();
					iStartIndex = iStartIndex+sRepStr.length();
				}
				else{
					iStartIndex++;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 提供替换字符串的功能,区分大小写
	 * @param String sSrcStr 为要被处理的字符串
	 * @param String sStr 为被替换的子字符串
	 * @param String sRepStr 为替换进去的子字符串
	 * @return String 替换后的字符串,出现异常返回原字符串
	 */
	public static String old_replace(String sSrcStr,String sStr,String sRepStr){
		try{
			if((sSrcStr==null)||(sStr==null)||(sRepStr==null)){
				return sSrcStr;
			}
			if((sSrcStr.length()==0)||(sStr.length()==0)){
				return sSrcStr;
			}
			StringBuffer sb = new StringBuffer(sSrcStr);
			old_replace(sb,sStr,sRepStr);
			return new String(sb);
		}
		catch(Exception e){
			return sSrcStr;
		}
	}
	/**
	 * 优化后的字符串替换算法
	 * @param sb StringBuffer
	 * @param sStr String
	 * @param sRepStr String
	 */
	public static void replace(StringBuffer sb,String sStr,String sRepStr){
		try{
			if((sb==null)||(sStr==null)||(sRepStr==null)){
				return;
			}
			if((sb.length()==0)||(sStr.length()==0)||sb.indexOf(sStr)<0){
				return;
			}
			int iStartIndex = 0;
			int iLen = sb.length();
			int index = 0;
			int old_str_len = sStr.length();
			// int find_num=0;
			StringBuffer sbNew = new StringBuffer();
			while(iStartIndex<iLen){
				index = sb.indexOf(sStr,iStartIndex);
				if(index>=0){
					if(iStartIndex<index){
						sbNew.append(sb.substring(iStartIndex,index));
					}
					sbNew.append(sRepStr);
					iStartIndex = index+old_str_len;
					// find_num++;
				}
				else{
					sbNew.append(sb.substring(iStartIndex));
					iStartIndex = iLen;
					break;
				}
			}
			if(iStartIndex<iLen){
				sbNew.append(sb.substring(iStartIndex));
			}
			// System.out.println("find ["+sStr+"] ["+find_num+"] times");
			sb.setLength(0);
			sb.append(sbNew);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 优化好的字符串替换算法
	 * @param sSrcStr String
	 * @param sStr String
	 * @param sRepStr String
	 * @return String
	 */
	public static String replace(String sSrcStr,String sStr,String sRepStr){
		try{
			if((sSrcStr==null)||(sStr==null)||(sRepStr==null)){
				return sSrcStr;
			}
			if((sSrcStr.length()==0)||(sStr.length()==0)){
				return sSrcStr;
			}
			StringBuffer sb = new StringBuffer(sSrcStr);
			replace(sb,sStr,sRepStr);
			return new String(sb);
		}
		catch(Exception e){
			return sSrcStr;
		}
	}
	/**
	 * 采用正则表达式的替换算法
	 * @param sb StringBuffer
	 * @param sStr String
	 * @param sRepStr String
	 */
	public static void replaceNew(StringBuffer sb,String sStr,String sRepStr){
		if((sb==null)||(sStr==null)||(sRepStr==null)){
			return;
		}
		if((sb.length()==0)||(sStr.length()==0)||sb.indexOf(sStr)<0){
			return;
		}
		String str = new String(sb);
		sb.setLength(0);
		sb.append(str.replaceAll(sStr,sRepStr));
	}
	/**
	 * 采用正则表达式的替换算法
	 * @param src String
	 * @param sStr String
	 * @param sRepStr String
	 * @return String
	 */
	public static String replaceNew(String sSrcStr,String sStr,String sRepStr){
		if((sSrcStr==null)||(sStr==null)||(sRepStr==null)){
			return sSrcStr;
		}
		if((sSrcStr.length()==0)||(sStr.length()==0)){
			return sSrcStr;
		}
		StringBuffer sb = new StringBuffer(sSrcStr);
		replaceNew(sb,sStr,sRepStr);
		return new String(sb);
	}
	/**
	 * 提供字符串分割的功能,区分大小写
	 * @param String sStr 为要被处理的源字符串
	 * @param String sSplitStr 分割串或结束串
	 * @param String sEscStr 为转义字符串
	 * @param int iSplitnum 为要分割的个数
	 * @param int flag flag=1时:
	 *        当iSplitnum>0时，严格按其要求分割的个数分割，即最后被分割出来的可能还含有分割符（它们将被忽略）
	 *        当iSplitnum<=0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。
	 *        flag=0表示sSplitStr为结束符
	 *        当iSplitnum<0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。
	 *        当iSplitnum>=0时,sSplitStr被认为分割符
	 *        ，即只有sSplitStr左右边都有效。(但当sSplitStr在最后时候，右边无效)
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr,String sSplitStr,String sEscStr,int iSplitnum,int flag){
		try{
			String[] aResult = null;
			Vector vResult = null;
			if(sSplitStr==null){
				sSplitStr = "";
			}
			if(sSplitStr.length()==0){
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}
			if(iSplitnum<=0){
				iSplitnum = 0;
			}
			if(sEscStr==null){
				sEscStr = "";
			}
			if(iSplitnum>0){
				aResult = new String[iSplitnum];
			}
			else{
				vResult = new Vector();
			}
			int iNum = 0;
			int index = 0;
			int preindex = 0;
			int len = sStr.length();
			int iSplitlen = sSplitStr.length();
			int iEsclen = sEscStr.length();
			String sNosplit = sEscStr+sSplitStr;
			String sTemp = null;
			String sTemp2 = null;
			while(index<len){
				try{
					sTemp = sStr.substring(index,iSplitlen+index);
				}
				catch(Exception e){
					sTemp = "";
				}
				if(sTemp.equals(sSplitStr)){
					sTemp2 = null;
					try{
						if(iEsclen==0){
							sTemp2 = sStr.substring(preindex,index);
						}
						else{
							if(index-iEsclen<0){
								sTemp2 = sStr.substring(preindex,index);
							}
							else if(!sStr.substring(index-iEsclen,iSplitlen+index).equals(sNosplit)){
								sTemp2 = sStr.substring(preindex,index);
							}
						}
					}
					catch(Exception e){
						sTemp2 = "";
					}
					if(sTemp2!=null){
						if(iEsclen>0){
							sTemp2 = replace(sTemp2,sNosplit,sSplitStr);
						}
						if(iSplitnum>0){
							aResult[iNum] = sTemp2;
						}
						else{
							vResult.add(iNum,sTemp2);
						}
						iNum++;
						if((iNum>=iSplitnum)&&(iSplitnum>0)){
							break;
						}
						index = iSplitlen+index;
						preindex = index;
						if((flag==1)&&(iSplitnum-iNum==1)&&(index<len)){
							sTemp2 = sStr.substring(index);
							if(iEsclen>0){
								sTemp2 = replace(sTemp2,sNosplit,sSplitStr);
							}
							if(iSplitnum>0){
								aResult[iNum] = sTemp2;
							}
							else{
								vResult.add(iNum,sTemp2);
							}
							iNum++;
							break;
						}
						continue;
					}
				}
				index++;
			}
			if(((flag==0)&&(preindex<index)&&(iNum<iSplitnum))||((iSplitnum==0)&&(preindex<index))){
				sTemp2 = sStr.substring(preindex,index);
				if(iEsclen>0){
					sTemp2 = replace(sTemp2,sNosplit,sSplitStr);
				}
				if(iSplitnum>0){
					aResult[iNum] = sTemp2;
				}
				else{
					vResult.add(iNum,sTemp2);
				}
				iNum++;
			}
			if(iNum==0){
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}
			if(iNum<iSplitnum){
				for(int i = iNum;i<iSplitnum;i++){
					if(iSplitnum==0){
						vResult.add(i,"");
					}
					else{
						aResult[i] = "";
					}
				}
			}
			if(iSplitnum==0){
				aResult = new String[vResult.size()];
				for(int i = 0;i<aResult.length;i++){
					aResult[i] = (String)(vResult.elementAt(i));
				}
			}
			return aResult;
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 提供字符串分割的功能,区分大小写
	 * @param String sStr 为要被处理的源字符串
	 * @param String sSplitStr 结束字符串
	 * @param String sEscStr 为转义字符串
	 * @param int iSplitnum 为要分割的个数
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr,String sSplitStr,String sEscStr,int iSplitnum){
		try{
			return split(sStr,sSplitStr,sEscStr,iSplitnum,0);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 提供分割字符串的功能,区分大小写
	 * @param String sStr 为要被处理的源字符串
	 * @param String sSplitStr 分割串
	 * @param String sEscStr 为转义字符串
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr,String sSplitStr,String sEscStr){
		try{
			return split(sStr,sSplitStr,sEscStr,0,0);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 提供分割字符串的功能,区分大小写
	 * @param String sStr 为要被处理的源字符串
	 * @param String sFieldStr 字段结束符
	 * @param String sLineStr 行结束符
	 * @param String sEscStr 为转义字符串
	 * @param int iLinenum 行数
	 * @param int iFieldnum 列数
	 * @return 按要求分割后的二维字符数组，为null表示失败
	 */
	public static String[][] split(String sStr,String sFieldStr,String sLineStr,String sEscStr,int iLinenum,int iFieldnum){
		try{
			String[][] aaResult = new String[iLinenum][iFieldnum];
			String[] aTemp = split(sStr,sLineStr,sEscStr,iLinenum);
			if(aTemp.length!=aaResult.length){
				return null;
			}
			for(int i = 0;i<aaResult.length;i++){
				aaResult[i] = split(aTemp[i],sFieldStr,sEscStr,iFieldnum);
			}
			if(aaResult[0].length!=iFieldnum){
				return null;
			}
			return aaResult;
		}
		catch(Exception e){
			return null;
		}
	}
	
	 /**
	 * 获取剔除变量名中下划线,并转换为驼峰写法
	 * 如   表字段：activity_name 则转换为 activityName
	 * @param name
	 *            变量名
	 * @return
	 */
	public static String toFirstLetterUpperCase(String name) {
		if (null ==name || name.length() < 2) {
			return name;
		}
		String[] tmp = name.split("_");
		StringBuffer changeBuf = new StringBuffer();
		changeBuf.append(tmp[0]);
		
		for (int j = 1; j < tmp.length; j++) {
			changeBuf = changeBuf.append(tmp[j].substring(0, 1).toUpperCase() + tmp[j]
					.substring(1, tmp[j].length()));
		}
		StringBuffer firstLetter = new StringBuffer();
		firstLetter = firstLetter.append(changeBuf.substring(0, 1).toUpperCase());
		return firstLetter + changeBuf.substring(1, changeBuf.length());
	}
	
	
//	public static void main(String[] args){
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0;i<1023*3000+1;i++){
//			sb.append("a");
//		}
//		String testStr = new String(sb.insert(0,"b"));
//		System.out.println("start to test...");
//		long start = System.currentTimeMillis();
//		String result1 = "";// ExtString.old_replace(testStr,"aaa","");
//		long end = System.currentTimeMillis();
//		System.out.println("1.used time ["+(end-start)+"ms]");
//		start = System.currentTimeMillis();
//		String result2 = ExtString.replace(testStr,"a","kk");
//		end = System.currentTimeMillis();
//		System.out.println("2.used time ["+(end-start)+"ms]");
//		start = System.currentTimeMillis();
//		String result3 = ExtString.replaceNew(testStr,"a","kk");
//		end = System.currentTimeMillis();
//		System.out.println("3.used time ["+(end-start)+"ms]");
//		System.out.println("end to test...["+result2.equals(result3)+","+result1.equals(result2)+","+result1.equals(result3)+"]");
//		System.exit(0);
//	}
}
