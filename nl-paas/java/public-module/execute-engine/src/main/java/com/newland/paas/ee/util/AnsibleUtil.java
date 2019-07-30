package com.newland.paas.ee.util;

import java.util.List;

public class AnsibleUtil {
	/**每个数据生成在一行， 生成格式是 $prefix$bracket$ip$bracket
	 * @param prefix 前缀, 附加在每一行的前面
	 * @param bracket 分隔符,如小括号
	 * @param datas 需要被分割在多行的数据
	 * @return 格式化的数据
	 */
	public static String getEachLineData( String prefix, String bracket, List<String> datas ) {
		StringBuilder sb = new StringBuilder();
		for ( String data : datas ) {
			sb.append(prefix);
			sb.append(bracket);
			sb.append(data);
			sb.append(bracket);
			sb.append("\n");
		}		
		return sb.toString();
	}
}
