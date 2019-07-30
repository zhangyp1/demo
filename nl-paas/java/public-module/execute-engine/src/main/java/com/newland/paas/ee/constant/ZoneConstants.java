
package com.newland.paas.ee.constant;
/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ZoneConstants
 * @Description: 
 * @Funtion List:ZoneConstants
 *
 */
public class ZoneConstants {

	/**
	 * 集群分区前缀
	 */
	public final static String ZONE_PREFIX="zone";
	
	/**
	 * 集群分区前缀
	 */
	public final static String ZONE_QUOTA="quota";
	/**
	 * 路由id前缀
	 */
	public final static String LOAD_BALANCE_PREFIX="a";
	/**
	 *  路由负载均衡key
	 */
	public final static String  LOAD_BALANCE_KEY= "LB_VALUE";
	public final static String  LOAD_BALANCE_KEY_INITKEY = "INITKEY";
	public final static String  LOAD_BALANCE_KEY_LBNAME = "LBNAME";
	public final static String  LOAD_BALANCE_KEY_IP= "IP";
	public final static String  LOAD_BALANCE_KEY_PORT = "PORT";
	
	/**
	 * 集群属性masterFloatIp key值
	 */
	public final static String CLUATTR_MASTER_FLOAT_IP="MASTER_FLOAT_IP";
	
	/**
	 * 集群属性apiServer key值
	 */
	public final static String CLUATTR_API_SERVER="API_SERVER";
	
	/**
	 * 集群属性K8SPrometheusAddress key值
	 */
	public final static String CLUATTR_K8S_PROMETHEUS_ADDRESS="K8S_PROMETHEUS_ADDRESS";
	
}

