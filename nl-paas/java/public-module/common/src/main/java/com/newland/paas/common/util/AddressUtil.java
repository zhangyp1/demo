package com.newland.paas.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 获取本机IP地址
 * 
 * @author Administrator
 *
 */
public class AddressUtil {
	
	private static final Log log = LogFactory.getLogger(AddressUtil.class);
	private static final String tag = "AddressUtil===";

	public static boolean isWindowOS() {
		boolean isWindowOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowOS = true;
		}
		return isWindowOS;
	}

	public static String getInetAddress() {
		StringBuffer strIps = new StringBuffer();
		InetAddress inetAddress = null;
		try {
			// 如果是windows操作系统
			if (isWindowOS()) {
				inetAddress = InetAddress.getLocalHost();
			} else {
				boolean bFindIP = false;
				// 定义一个内容都是NetworkInterface的枚举对象
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				// 如果枚举对象里面还有内容(NetworkInterface)
				while (netInterfaces.hasMoreElements()) {
//					if (bFindIP) {
//						break;
//					}
					// 获取下一个内容(NetworkInterface)
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有IP
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						inetAddress = (InetAddress) ips.nextElement();
						if (inetAddress.isSiteLocalAddress() // 属于本地地址
								&& !inetAddress.isLoopbackAddress() // 不是回环地址
								&& inetAddress.getHostAddress().indexOf(":") == -1) { // 地址里面没有:号
							bFindIP = true; // 找到了地址
							strIps.append(inetAddress.getHostAddress() + ",");
//							break; // 退出循环
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "getInetAddressException");
		}
		return strIps.toString();
	}

	public static String getLocalIP() {
		String ip = null;
		try {
			ip = getInetAddress();
			log.debug(LogProperty.LOGTYPE_SYS, tag + "getInetAddress().getHostAddress():" + ip);
		} catch (Exception e) {
			log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "getLocalIPException");
		}
		if(ip == null || ip.trim().length() == 0) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				ip = "UN_KNOWN_HOST";
			}
		}
		return ip;
	}

	public static void main(String[] args) {
		System.out.println(AddressUtil.getLocalIP());
	}

}
