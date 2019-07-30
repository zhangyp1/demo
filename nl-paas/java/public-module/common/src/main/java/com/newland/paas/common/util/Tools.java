package com.newland.paas.common.util;

/**
 * 工具类(暂时将小工具统一计入到此类中)
 * @author huashao
 */
public class Tools {
	
	/**
	 * @param path   路径
	 * @return 针对zk有效的路径
	 */
	public static String addSlash(String path){
		String Path = null;
		if(path == null) {
			return "/";
		} else if(path.startsWith("/")){
			if(path.endsWith("/")){
				return path.substring(0, path.length() - 1);
			} else {
				return path;
			}
		} else {
			if(path.endsWith("/")){
				Path = path.substring(0, path.length() - 1);
			}
			Path = "/" + path;
			return Path;
		}
	}
	
	/**
	 * 判断暴露服务的 服务头是否以nlzookeeper类似的  为注册中心的协议（提供zk集群的使用）
	 * 解析 nlzookeeper://  类似的服务头 成 nlzookeeper等
	 * @param str  服务头
	 * @return     解析之后的服务头
	 */
	public static String zkHeader(String str) {
		if(str.indexOf(":") != -1){
			String[] string = str.split(":");
			return string[0];
		}
		return str;
	}

	/**
	 * 这种方式获取UUID比java.util的UUID性能更好
	 * @return
	 */
//	public static String getUUID(){
//		return org.apache.commons.id.uuid.UUID.randomUUID().toString();
//	}

}
