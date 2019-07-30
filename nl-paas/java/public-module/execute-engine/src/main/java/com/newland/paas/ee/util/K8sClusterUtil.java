package com.newland.paas.ee.util;
import com.newland.paas.common.util.StringUtils;
import java.util.List;
import java.util.Map;

public class K8sClusterUtil {
	/**
	 * 传入的k8s master的ip列表如果不为空, 则把k8s master的ip列表组成类似192.168.1.2,192.168.2.9格式的字符串，并插入jenkins参数映射表
	 * 否则抛出异常
	 * @param k8sMasterIpList  k8s master的ip列表
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddMasterIpToMap(List<String> k8sMasterIpList, Map<String, String> parameterMap) throws Exception {
		String k8sMasterIp = StringUtils.join(k8sMasterIpList.toArray(), ",");
		if (StringUtils.isEmpty(k8sMasterIp))
			throw new Exception("k8s master ip is empty");
		parameterMap.put("k8s_master_ip", k8sMasterIp);
	}

	/**
	 * 传入的k8s worker的ip列表如果不为空, 则把k8s worker的ip列表组成类似192.168.1.2,192.168.2.9格式的字符串，并插入jenkins参数映射表
	 * 	否则抛出异常
	 * @param k8sWorkerIpList  k8s worker的ip列表
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddWorkerIpToMap(List<String> k8sWorkerIpList, Map<String, String> parameterMap) throws Exception {
		String k8sWorkerIp = StringUtils.join(k8sWorkerIpList.toArray(), ",");
		if ( StringUtils.isEmpty(k8sWorkerIp))
			throw new Exception("k8s worker ip is empty");
		parameterMap.put("k8s_worker_ip", k8sWorkerIp);
	}

	/**
	 * 传入的k8s master float的ip如果不为空, 则把k8s master float的ip插入jenkins参数映射表
	 * 否则抛出异常
	 * @param floatIp  k8s master float的ip
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddFloatIpToMap(String floatIp, Map<String, String> parameterMap ) throws Exception {
		if ( StringUtils.isEmpty(floatIp))
			throw new Exception("k8s float ip is empty");
		parameterMap.put("k8s_master_float_ip", floatIp);
	}

	/**
	 * 传入的k8s的安装home如果不为空, 则把k8s的安装home插入jenkins参数映射表
	 * 否则抛出异常
	 * @param homePath  k8s的安装home
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddHomePath(String homePath, Map<String, String> parameterMap )  throws Exception {
		if ( StringUtils.isEmpty(homePath))
			throw new Exception("k8s home path is empty");
		parameterMap.put("k8s_home", homePath);
	}

	/**
	 * 传入的etcdIpList的ip列表如果不为空, 则把etcdIpList的ip列表组成类似192.168.1.2，192.168.2.9格式的字符串，并插入jenkins参数映射表
	 * 否则抛出异常
	 * @param etcdIpList  etcd的ip列表
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddEtcdIpToMap(List<String> etcdIpList, Map<String, String> parameterMap )   throws Exception {
		String etcdIp = StringUtils.join(etcdIpList.toArray(),",");
		if ( StringUtils.isEmpty(etcdIp))
			throw new Exception("k8s etcd ip is empty");
		parameterMap.put("etcd_ip", etcdIp);
	}

	/**
	 * 传入的etcd的安装home如果不为空, 则把k8s的安装home插入jenkins参数映射表
	 * 否则抛出异常
	 * @param etcdHome  etcd的安装home
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddEtcdHomeToMap(String etcdHome, Map<String, String> parameterMap) throws  Exception {
		if ( StringUtils.isEmpty(etcdHome))
			throw new Exception("k8s etcd home is empty");
		parameterMap.put("etcd_home", etcdHome);
	}

	/**
	 * 传入的ftp server的ip如果不为空, 则把ftp server的ip插入jenkins参数映射表
	 * 否则抛出异常
	 * @param pubHarborAddress  ftp server的ip
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddPubHarborAddressToMap(String pubHarborAddress, Map<String, String> parameterMap) throws Exception{
		if ( StringUtils.isEmpty(pubHarborAddress))
			throw new Exception("pub harbor address is empty");
		parameterMap.put("tenant_harbor_path",pubHarborAddress);
	}

	/**
	 * 传入的镜像中心地址如果不为空, 则把镜像中心地址插入jenkins参数映射表
	 * 否则抛出异常
	 * @param clusterImagePath  镜像中心地址
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddClusterImagePath(String clusterImagePath, Map<String, String> parameterMap) throws Exception {
		if (StringUtils.isEmpty(clusterImagePath))
			throw new Exception("cluster image path is empty");
		parameterMap.put("docker_image_reposity", clusterImagePath);
	}

	/**
	 * 传入的ftp server的用户名如果不为空, 则把ftp server的用户名插入jenkins参数映射表
	 * 否则抛出异常
	 * @param astUserName  ftp server的用户名
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddAstUserName(String astUserName, Map<String, String> parameterMap) throws Exception {
		if (StringUtils.isEmpty(astUserName))
			throw new Exception("asset user name is empty");
		parameterMap.put("ast_sshd_user", astUserName);
	}

	/**
	 * 传入的ftp server的密码如果不为空, 则把ftp server的密码插入jenkins参数映射表
	 * 否则抛出异常
	 * @param astPassword  ftp server的密码
	 * @param parameterMap jenkins参数映射表
	 */
	public static void checkAndAddAstPassword(String astPassword,  Map<String, String> parameterMap) throws Exception {
		if (StringUtils.isEmpty(astPassword))
			throw new Exception("asset password is empty");
		parameterMap.put("ast_sshd_password", astPassword);
	}
}
